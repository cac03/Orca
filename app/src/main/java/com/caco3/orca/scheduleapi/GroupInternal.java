package com.caco3.orca.scheduleapi;

import com.google.gson.annotations.SerializedName;

/**
 * Group model returned by api
 * Part of {@link ScheduleApiResponseInternal}
 * Shall not go out of this package
 */
/*package*/ class GroupInternal {
    /**
     * Don't know
     */
    @SerializedName("Code")
    String code;

    /**
     * Group name
     */
    @SerializedName("Name")
    String name;
}
