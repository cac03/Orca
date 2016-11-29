package com.caco3.orca.learning;


import com.caco3.orca.credentials.CredentialsManager;
import com.caco3.orca.orioks.LoginOrPasswordIncorrectException;
import com.caco3.orca.orioks.Orioks;
import com.caco3.orca.orioks.UserCredentials;
import com.caco3.orca.orioks.model.Discipline;
import com.caco3.orca.orioks.model.OrioksResponse;

import java.io.IOException;

import javax.inject.Inject;

import rx.Subscriber;
import rx.Subscription;
import timber.log.Timber;

/*package*/ class LearningPresenterImpl implements LearningPresenter {

    private LearningView view;

    // not null, injected in c-tor
    private Orioks orioks;

    // not null, injected in c-tor
    private CredentialsManager credentialsManager;

    private Subscription orioksResponseSubscription = null;

    @Inject
    /*package*/ LearningPresenterImpl(Orioks orioks, CredentialsManager credentialsManager) {
        Timber.d("new instance");
        this.orioks = orioks;
        this.credentialsManager = credentialsManager;
    }

    @Override
    public void onViewAttached(LearningView view) {
        this.view = view;
        this.view.setRefreshing(orioksResponseSubscription != null);
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

        UserCredentials credentials = credentialsManager.getCurrentCredentials();
        if (credentials == null) {
            throw new IllegalStateException(
                    "Attempt to get response with credentials that were not saved.");
        }

        orioksResponseSubscription = orioks.getResponseForCurrentSemester(credentials)
                .subscribe(new Subscriber<OrioksResponse>() {
                    @Override
                    public void onCompleted() {
                        orioksResponseSubscription = null;
                    }

                    @Override
                    public void onError(Throwable e) {
                        if (e instanceof IOException) {
                            if (view != null) {
                                view.sayNetworkErrorOccurred();
                            }
                        }

                        if (e instanceof LoginOrPasswordIncorrectException) {
                            if (view != null) {
                                view.sayIncorrectLoginOrPassword();
                            }
                        }

                        if (view != null) {
                            view.setRefreshing(false);
                        }

                        orioksResponseSubscription = null;
                    }

                    @Override
                    public void onNext(OrioksResponse orioksResponse) {
                        if (view != null) {
                            view.setItems(orioksResponse.getDisciplines());
                            view.setRefreshing(false);
                        }
                    }
                });
    }

    @Override
    public void onReLogInRequest() {
        credentialsManager.removeCurrentCredentials();
        if (view != null) {
            view.navigateToLoginActivity();
        }
    }
}
