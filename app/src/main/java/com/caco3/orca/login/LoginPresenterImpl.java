package com.caco3.orca.login;


import com.caco3.orca.credentials.CredentialsManager;
import com.caco3.orca.orioks.LoginOrPasswordIncorrectException;
import com.caco3.orca.orioks.Orioks;
import com.caco3.orca.orioks.UserCredentials;
import com.caco3.orca.orioks.model.OrioksResponse;
import com.caco3.orca.util.Preconditions;

import java.io.IOException;

import javax.inject.Inject;

import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.exceptions.Exceptions;
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

    @Inject
    /*package*/ LoginPresenterImpl(Orioks orioks, CredentialsManager credentialsManager){
        Timber.d("New instance");
        this.orioks = Preconditions.checkNotNull(orioks);
        this.credentialsManager = Preconditions.checkNotNull(credentialsManager);
    }

    @Override
    public void onViewAttached(LoginView view) {
        Timber.d("onViewAttached()");
        this.view = view;
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
            orioks.getResponseForCurrentSemester(credentials)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribe(new Subscriber<OrioksResponse>() {
                        @Override
                        public void onCompleted() {
                        }

                        @Override
                        public void onError(Throwable e) {
                            if (e instanceof LoginOrPasswordIncorrectException) {
                                Timber.i(e, "Login or password incorrect.");
                                if (view != null) {
                                    view.sayLoginOrPasswordIsIncorrect();
                                }
                            } else if (e instanceof IOException) {
                                Timber.i("An i/o error occurred during signing in");
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
                        }

                        @Override
                        public void onNext(OrioksResponse orioksResponse) {
                            Timber.i("Successfully receiver orioks response. Processing");

                            // save credentials
                            credentialsManager.setCurrentCredentials(credentials);

                            // TODO: 11/28/16 process response
                            if (view != null) {
                                view.hideProgress();
                                view.navigateToLearningActivity();
                            }
                        }
                    });
        }
    }
}
