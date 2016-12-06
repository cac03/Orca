package com.caco3.orca.scheduleapi;

import com.google.gson.annotations.SerializedName;

import java.util.Arrays;

/**
 * Model object for schedule response returned by {@link ScheduleService}.
 *
 * It's internal class and shall not go out of this package
 */
/*package*/ class ScheduleApiResponseInternal {
    @SerializedName("Data")
    ScheduleItemInternal[] scheduleItems;

    @SerializedName("Semestr")
    String semesterDescription;

    @SerializedName("Times")
    TimeInternal[] times;

    @Override
    public String toString() {
        return "ScheduleApiResponseInternal{" +
                "scheduleItems=" + Arrays.toString(scheduleItems) +
                ", semesterDescription='" + semesterDescription + '\'' +
                ", times=" + Arrays.toString(times) +
                '}';
    }
}
