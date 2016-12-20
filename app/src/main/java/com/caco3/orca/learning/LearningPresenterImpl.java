package com.caco3.orca.learning;


import com.caco3.orca.credentials.CredentialsManager;
import com.caco3.orca.data.orioks.OrioksRepository;
import com.caco3.orca.learning.preferences.LearningPreferences;
import com.caco3.orca.orioks.Orioks;
import com.caco3.orca.orioks.OrioksException;
import com.caco3.orca.orioks.UserCredentials;
import com.caco3.orca.orioks.model.Discipline;
import com.caco3.orca.orioks.model.OrioksResponse;
import com.caco3.orca.settings.Settings;
import com.caco3.orca.util.Preconditions;

import java.io.IOException;
import java.util.List;

import javax.inject.Inject;

import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import timber.log.Timber;

/**
 * Concrete {@link LearningPresenter} implementation
 */
/*package*/ class LearningPresenterImpl implements LearningPresenter {

    /**
     * View associated with this presenter.
     * <code>null</code>, when it's detached via {@link #onViewDetached()}.
     * And not <code>null</code> when attached via {@link #onViewAttached(LearningView)}
     */
    private LearningView view;

    // not null, injected in c-tor
    private Orioks orioks;

    // not null, injected in c-tor
    private CredentialsManager credentialsManager;

    // not null, injected in c-tor
    private Settings settings;

    // not null, injected in c-tor
    private OrioksRepository orioksRepository;

    // not null, injected in c-tor
    private LearningPreferences preferences;

    private Subscription orioksResponseSubscription = null;

    @Inject
    /*package*/ LearningPresenterImpl(Orioks orioks,
                                      CredentialsManager credentialsManager,
                                      Settings settings,
                                      OrioksRepository orioksRepository,
                                      LearningPreferences preferences) {
        Timber.d("new instance");
        this.orioks = orioks;
        this.credentialsManager = credentialsManager;
        this.settings = settings;
        this.orioksRepository = orioksRepository;
        this.preferences = preferences;
    }

    @Override
    public void onViewAttached(LearningView view) {
        this.view = view;
        this.view.setRefreshing(orioksResponseSubscription != null);
        initView();
    }

    @Override
    public void onViewDetached() {
        view = null;
    }

    @Override
    public void onDisciplineClicked(Discipline discipline) {
        if (view != null) {
            view.navigateToDisciplineDetailsActivity(discipline);
        }
    }

    @Override
    public void onRefreshRequest() {
        UserCredentials credentials = credentialsManager.getActive();
        if (credentials == null) {
            throw new IllegalStateException(
                    "Attempt to get response with credentials that were not saved.");
        }

        orioksResponseSubscription
                = orioks.getResponseForCurrentSemester(credentials)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ResponseForCurrentSemesterLoadedSubscriber(credentials));
    }

    @Override
    public void onReLogInRequest() {
        UserCredentials credentials = credentialsManager.getActive();
        credentialsManager.removeActiveCredentials();
        if (view != null) {
            view.navigateToLoginActivity(credentials);
        }
    }

    private void initView(){
        UserCredentials activeCredentials = credentialsManager.getActive();
        if (activeCredentials == null) {
            // no user signed in
            if (settings.isFirstRun()) {
                // force navigate to login activity
                if (view != null) {
                    view.navigateToLoginActivity();
                }

                // it's not first run already
                settings.setFirstRun(false);
            } else {
                if (view != null) {
                    view.showNoUserSignedInErrorView();
                }
            }
        } else {
            int semester = preferences.getSemesterToShowDisciplines(activeCredentials);
            if (semester == LearningPreferences.SHOW_DISCIPLINES_FOR_CURRENT_SEMESTER) {
                int currentSemester = orioksRepository.getCurrentSemester(activeCredentials);
                if (currentSemester == -1) {
                    // load
                    Timber.i("No current semester value for login %s", activeCredentials.getLogin());
                } else {
                    List<Discipline> disciplines = orioksRepository
                            .getDisciplines(activeCredentials, currentSemester);
                    if (view != null) {
                        view.setItems(disciplines);
                    }
                }
            } else {
                List<Discipline> disciplines = orioksRepository
                        .getDisciplines(activeCredentials, semester);
                if (view != null) {
                    view.setItems(disciplines);
                }
            }
        }
    }

    private class ResponseForCurrentSemesterLoadedSubscriber extends Subscriber<OrioksResponse> {

        private UserCredentials credentials;

        private ResponseForCurrentSemesterLoadedSubscriber(UserCredentials credentials) {
            this.credentials = Preconditions.checkNotNull(credentials, "credentials == null");
        }

        @Override
        public void onCompleted() {
        }

        @Override
        public void onError(Throwable e) {
            Timber.e(e, "Unable to load current semester response for credentials(login: %s)",
                    credentials.getLogin());
            if (view != null) {
                view.setRefreshing(false);
            }

            if (e instanceof OrioksException) {
                // login incorrect ?..
                Timber.w("Orioks returned error: %s", ((OrioksException)e).getOrioksErrorMessage());
                if (view != null) {
                    view.sayIncorrectLoginOrPassword();
                }
            } else if (e instanceof IOException) {
                Timber.w("i/o error occurred");
                if (view != null) {
                    view.sayNetworkErrorOccurred();
                }
            } else {
                // unexpected exception caught
                throw new RuntimeException(e);
            }
        }

        @Override
        public void onNext(OrioksResponse response) {
            Timber.i("Successfully received orioks response for current semester. Saving it");
            orioksRepository.setCurrentSemester(credentials, response.getCurrentSemester());
            orioksRepository.saveStudent(credentials, response.getStudent());
            orioksRepository.saveDisciplines(credentials, response.getDisciplines(),
                    response.getCurrentSemester());
            Timber.i("Response was successfully saved");

            /**
             * If user has changed active account we won't update view.
             */
            if (credentials.getLogin().equals(credentialsManager.getActive().getLogin())) {
                if (view != null) {
                    view.setRefreshing(false);
                    view.setItems(response.getDisciplines());
                }
            }

            orioksResponseSubscription = null;
        }
    }

    @Override
    public void onLogInClicked() {
        if (view != null) {
            view.navigateToLoginActivity();
        }
    }
}
