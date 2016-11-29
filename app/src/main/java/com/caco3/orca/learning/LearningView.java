package com.caco3.orca.learning;

import com.caco3.orca.mvp.BaseView;
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
}
