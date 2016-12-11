package com.caco3.orca.schedule;

/**
 * An interface for preferences saved for {@link ScheduleActivity}
 */
public interface SchedulePreferences {

    /**
     * Returns group name to show schedule for
     * @return String or null, if this group name was not previously set
     */
    String getGroupToShowScheduleFor();

    /**
     * Sets provided group name to show schedule for
     * @param groupNameToShowScheduleFor group name to show schedule for
     */
    void setGroupToShowScheduleFor(String groupNameToShowScheduleFor);
}
