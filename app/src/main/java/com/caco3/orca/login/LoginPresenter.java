package com.caco3.orca.login;

import com.caco3.orca.mvp.BasePresenter;

/**
 * An interface for login presenter (mvp)
 * @see LoginView
 * @see LoginFragment
 * @see LoginPresenterImpl
 */
/*package*/ interface LoginPresenter extends BasePresenter<LoginView> {

    /**
     * Called when user attempts to log in
     * @param login to log in with
     * @param password to log in with
     */
    void attemptToLogIn(String login, String password);
}
