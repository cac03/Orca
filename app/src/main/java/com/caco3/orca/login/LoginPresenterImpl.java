package com.caco3.orca.login;


import com.caco3.orca.credentials.CredentialsManager;
import com.caco3.orca.data.orioks.OrioksRepository;
import com.caco3.orca.orioks.Orioks;
import com.caco3.orca.orioks.OrioksException;
import com.caco3.orca.orioks.UserCredentials;
import com.caco3.orca.orioks.model.OrioksResponse;
import com.caco3.orca.util.Preconditions;

import java.io.IOException;

import javax.inject.Inject;

import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import timber.log.Timber;

/**
 * {@link LoginPresenter} implementation
 */
/*package*/ class LoginPresenterImpl implements LoginPresenter {
    /**
     * View associated with this presenter.
     * Null when view is detached, so all code here must to check this reference for null
     * before performing any actions with it
     */
    private LoginView view;

    /**
     * Orioks api.
     * Will be injected in c-tor
     */
    private Orioks orioks;

    // not null, will be injected in c-tor
    private CredentialsManager credentialsManager;

    // not null. Injected in c-tor
    private OrioksRepository orioksRepository;

    /**
     * Keep reference to subscription so we are able to tell whether we're performing login operation
     */
    private Subscription loginSubscription;

    @Inject
    /*package*/ LoginPresenterImpl(Orioks orioks, CredentialsManager credentialsManager, OrioksRepository orioksRepository){
        Timber.d("New instance");
        this.orioks = Preconditions.checkNotNull(orioks);
        this.credentialsManager = Preconditions.checkNotNull(credentialsManager);
        this.orioksRepository = Preconditions.checkNotNull(orioksRepository);
    }

    @Override
    public void onViewAttached(LoginView view) {
        Timber.d("onViewAttached()");
        this.view = view;

        String initialLogin = view.getInitialLogin();
        if (initialLogin != null) {
            view.setLogin(initialLogin);
        }

        /**
         * We were performing signing in. So we have to show progress
         */
        if (loginSubscription != null) {
            view.showProgress();
        }
    }

    @Override
    public void onViewDetached() {
        Timber.d("onViewDetached()");
        this.view = null;
    }

    @Override
    public void attemptToLogIn(String login, String password) {
        Timber.i("Attempt to log in with %s login", login);

        boolean cancel = false;
        if (password.isEmpty()) {
            if (view != null) {
                view.setPasswordIsEmptyStringError();
            }

            cancel = true;
        }

        if (login.isEmpty()) {
            if (view != null) {
                view.setLoginIsEmptyStringError();
            }

            cancel = true;
        }

        if (!cancel) {
            if (view != null) {
                view.showProgress();
            }

            final UserCredentials credentials = new UserCredentials(login, password);
            /**
             * There is only one way to determine whether provided {@link UserCredentials}
             * are valid: try to call {@link Orioks#getResponseForCurrentSemester(UserCredentials)}
             * and see whether the {@link com.caco3.orca.orioks.OrioksException} is thrown.
             * If it's thrown we assume that provided credentials are invalid. But it's not 100%
             * guarantee... Api doesn't provide another way to determine it...
             */
            loginSubscription = orioks.getResponseForCurrentSemester(credentials)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribe(new Subscriber<OrioksResponse>() {
                        @Override
                        public void onCompleted() {
                        }

                        @Override
                        public void onError(Throwable e) {
                            Timber.i("An error occurred while retrieving orioks response for %s login",
                                    credentials.getLogin());
                            Timber.e(e);
                            if (e instanceof OrioksException) {
                                Timber.i("Orioks returned error: %s. " +
                                        "Assuming that login or password is incorrect",
                                        ((OrioksException)e).getOrioksErrorMessage());
                                if (view != null) {
                                    view.sayLoginOrPasswordIsIncorrect();
                                }
                            } else if (e instanceof IOException) {
                                Timber.i("An i/o error occurred during signing in. No network?");
                                if (view != null) {
                                    view.sayNetworkErrorOccurred();
                                }
                            } else {
                                Timber.wtf(e, "Unexpected exception caught");
                                throw new RuntimeException(e);
                            }
                            if (view != null) {
                                view.hideProgress();
                            }

                            loginSubscription = null;
                        }

                        @Override
                        public void onNext(OrioksResponse orioksResponse) {
                            Timber.i("Successfully receiver orioks response. Processing");

                            // save credentials
                            credentialsManager.saveAndSetAsActive(credentials);

                            /**
                             * Save response so the {@link com.caco3.orca.learning.LearningActivity}
                             * will be able to get it
                             */
                            orioksRepository.saveStudent(credentials, orioksResponse.getStudent());
                            orioksRepository.saveDisciplines(credentials,
                                    orioksResponse.getDisciplines(),
                                    orioksResponse.getCurrentSemester());
                            orioksRepository.setCurrentSemester(credentials,
                                    orioksResponse.getCurrentSemester());
                            if (view != null) {
                                view.hideProgress();
                                view.navigateToLearningActivity();
                            }

                            loginSubscription = null;
                        }
                    });
        }
    }
}
