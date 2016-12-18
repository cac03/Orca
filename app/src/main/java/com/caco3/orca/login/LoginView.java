package com.caco3.orca.login;

import com.caco3.orca.mvp.BaseView;

/**
 * An interface for login view (mvp)
 * @see LoginFragment
 * @see LoginPresenter
 * @see LoginPresenterImpl
 */
/*package*/ interface LoginView extends BaseView<LoginPresenter>{
    /**
     * Shows progress in view
     * @throws IllegalStateException if the progress is already shown
     */
    void showProgress();

    /**
     * Hides progress in view
     * @throws IllegalStateException if no progress was shown
     */
    void hideProgress();

    /**
     * Sets 'Login can not be empty string' error at login view
     */
    void setLoginIsEmptyStringError();

    /**
     * Sets 'Password can not be empty string' error at login view
     */
    void setPasswordIsEmptyStringError();

    /**
     * Notifies user about incorrect login or password
     */
    void sayLoginOrPasswordIsIncorrect();

    /**
     * Navigates to 'learning activity'
     */
    void navigateToLearningActivity();

    /**
     * Notifies user about troubles with network
     */
    void sayNetworkErrorOccurred();


    /**
     * Returns login string that must be set to login field.
     * It's useful when the user needs to re-enter his credentials and we don't want
     * him to enter login again
     * @return String with login that must be set to login field,
     *          or <code>null</code> if login was not provided.
     */
    String getInitialLogin();

    /**
     * Sets provided string to the login field in view
     * @param login to set
     */
    void setLogin(String login);

    /**
     * Sets provided string to the password field in view
     * @param password to set
     */
    void setPassword(String password);
}
