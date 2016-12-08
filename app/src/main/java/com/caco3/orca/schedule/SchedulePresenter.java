package com.caco3.orca.schedule;

import com.caco3.orca.mvp.BasePresenter;

/**
 * Presenter interface for schedule view
 */
/*package*/ interface SchedulePresenter extends BasePresenter<ScheduleView> {

    /**
     * Called when user triggers manual refresh using {@link android.support.v4.widget.SwipeRefreshLayout}
     */
    void onRefreshRequest();

    /**
     * Called when user selects a group name to show schedule for from suggested
     * @param groupName group name selected by user
     */
    void onGroupSelected(String groupName);

    /**
     * Called when user wants to change a group schedule for he wants to see
     */
    void onChangeGroupRequest();

    /**
     * When user minimizes the app view is still attached, user can resume app in long period later,
     * and if the day changed for example, some schedule items may become invalid, so we have to
     * remove invalid items if necessary when view becomes visible
     */
    void onViewVisible();

    /**
     * Called when the user clicks retry button in 'could not load groups' view
     */
    void retryLoadGroups();

    /**
     * Called when the user clicks retry button in 'could not load schedule' view
     */
    void retryToLoadSchedule();
}
