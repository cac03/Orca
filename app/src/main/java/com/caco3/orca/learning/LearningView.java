package com.caco3.orca.learning;

import com.caco3.orca.credentials.CredentialsManager;
import com.caco3.orca.mvp.BaseView;
import com.caco3.orca.orioks.UserCredentials;
import com.caco3.orca.orioks.model.Discipline;

import java.util.List;


/*package*/ interface LearningView extends BaseView<LearningPresenter> {
    /**
     * Sets items to display
     * @param disciplineList items to display
     */
    void setItems(List<Discipline> disciplineList);

    /**
     * Notifies user about incorrect login or password in his credentials
     */
    void sayIncorrectLoginOrPassword();

    /**
     * Notifies user about network error
     */
    void sayNetworkErrorOccurred();

    /**
     * Shows/hides refreshing progress
     * @param refreshing true to show,
     *                   false to hide
     */
    void setRefreshing(boolean refreshing);

    /**
     * Navigates to discipline details activity
     * @param discipline to navigate discipline details for
     */
    void navigateToDisciplineDetailsActivity(Discipline discipline);

    /**
     * Navigates to login activity.
     */
    void navigateToLoginActivity();

    /**
     * Navigates to login activity, when user need to re-enter his credentials.
     * When {@link com.caco3.orca.login.LoginActivity} opened, login field will be set from provided
     * credentials
     * @param credentials to set login
     */
    void navigateToLoginActivity(UserCredentials credentials);

    /**
     * Shows {@link android.widget.ProgressBar} on the whole screen
     */
    void showProgress();

    /**
     * Hides progress bar, previously shown via {@link #showProgress()}
     */
    void hideProgress();

    /**
     * Shows 'no user signed in' error view.
     * It will be called by {@link LearningPresenter} when
     * {@link CredentialsManager#getCurrentCredentials()} will return <code>null</code>
     * which means no user signed in.
     * In this view user will be suggested to sign in.
     */
    void showNoUserSignedInErrorView();
}
