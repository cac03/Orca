package com.caco3.orca.schedule;

import com.caco3.orca.mvp.BaseView;
import com.caco3.orca.schedule.model.DaySchedule;

import java.util.List;


/*package*/ interface ScheduleView extends BaseView<SchedulePresenter> {

    /**
     * Shows/hides {@link android.support.v4.widget.SwipeRefreshLayout}
     * @param refreshing true to show, false to hide
     */
    void setRefreshing(boolean refreshing);

    /**
     * Sets items to show in view
     * @param items to show
     */
    void setItems(List<DaySchedule> items);

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
     * Suggests user to select group since there is no group selected
     * @param groupNames to select from
     */
    void suggestToSelectGroupToShowScheduleFor(List<String> groupNames);
}
