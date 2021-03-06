package com.caco3.orca.schedule;

import com.caco3.orca.mvp.BaseView;
import com.caco3.orca.schedule.model.DaySchedule;

import java.util.List;


/*package*/ interface ScheduleView extends BaseView<SchedulePresenter> {

    /**
     * Called when there is no saved group to show schedule for,
     * in 'no group selected' view the user must get suggestions to select group
     * @param list with all group names schedule for is available
     */
    void showNoGroupSelectedView(List<String> list);

    /**
     * Called when an error occurred while we were trying to load group names
     * schedule for is available
     */
    void showCouldNotLoadGroupsErrorView();

    /**
     * Called when we were unable to load schedule for group because of i/o error or.. parse error
     * @param groupName we couldn't load schedule for
     */
    void showCouldNotGetScheduleErrorView(String groupName);

    /**
     * Shows schedule view for group name
     * @param groupName schedule for is shown
     */
    void showScheduleView(String groupName);

    /**
     * Controls {@link android.support.v4.widget.SwipeRefreshLayout} refreshing state.
     * Must be used to indicate that we're really updating schedule for current set group
     * @param refreshing true to show, false to hide
     */
    void setRefreshing(boolean refreshing);

    /**
     * Shows schedule list from provided {@link DaySchedule} list
     * @param schedule to show
     */
    void showSchedule(List<DaySchedule> schedule);

    /**
     * Opens dialog where user is able to select a group schedule for he wants to see
     * @param groupNames list of group names schedule for is available
     */
    void openSelectGroupDialog(List<String> groupNames);

    /**
     * Notifies user about network unavailability
     */
    void sayNoNetwork();

    /**
     * Notifies user about network error
     */
    void sayNetworkErrorOccurred();

    /**
     * Notifies user about internal parse problem
     */
    void sayParseErrorOccurred();

    /**
     * Shows {@link android.widget.ProgressBar}.
     * It must be used to show that we're loading group names schedule is available for,
     * or we're loading schedule for another group
     */
    void showProgress();

    /**
     * Counterpart of {@link #showProgress()}
     */
    void hideProgress();
}
