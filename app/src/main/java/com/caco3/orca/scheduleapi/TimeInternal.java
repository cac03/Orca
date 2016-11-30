package com.caco3.orca.scheduleapi;

import com.google.gson.annotations.SerializedName;

/**
 * Part of {@link ScheduleApiResponseInternal}.
 * Shall not go out of this package.
 *
 * It associated with {@link ClassInternal}
 */
/*package*/class TimeInternal {

    /**
     * Don't know
     */
    @SerializedName("Code")
    int code;

    /**
     * There is string like '2 пара'
     */
    @SerializedName("Time")
    String time;

    /**
     * String representation of lesson time start
     * Not used
     */
    @SerializedName("TimeFrom")
    String timeFrom;

    /**
     * String representation of lesson time end.
     * Not used
     */
    @SerializedName("TimeTo")
    String timeTo;
}
