package com.caco3.orca.scheduleapi;


import com.google.gson.annotations.SerializedName;

/*package*/ class ScheduleItemInternal {

    @SerializedName("Class")
    ClassInternal classInternal;

    @SerializedName("Day")
    int day;

    @SerializedName("DayNumber")
    int dayNumber;

    @SerializedName("Group")
    GroupInternal group;

    @SerializedName("Room")
    RoomInternal room;

    @SerializedName("Time")
    TimeInternal time;
}
