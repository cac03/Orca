package com.caco3.orca.schedule;

import com.caco3.orca.mvp.BasePresenter;

/*package*/ interface SchedulePresenter extends BasePresenter<ScheduleView> {

    /**
     * Called when user triggers manual refresh using {@link android.support.v4.widget.SwipeRefreshLayout}
     */
    void onRefreshRequest();

    /**
     * Called when user selects a group name to show schedule for from suggested
     * @param groupName group name selected by user
     */
    void onGroupToShowScheduleForSelected(String groupName);
}
