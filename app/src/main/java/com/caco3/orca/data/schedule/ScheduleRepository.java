package com.caco3.orca.data.schedule;

import com.caco3.orca.scheduleapi.ScheduleItem;

import java.util.List;
import java.util.Set;

/**
 * An interface for repository which stores schedule
 */
public interface ScheduleRepository {

    /**
     * Saves schedule and associates it in repository with provided groupName
     * @param groupName associated with schedule
     * @param scheduleItems schedule to save
     */
    void saveSchedule(String groupName, Set<ScheduleItem> scheduleItems);

    /**
     * Retrieves schedule associated with provided groupName.
     * It returns {@link ScheduleItem} already sorted by {@link ScheduleItem#dayOfWeek}
     * and then {@link ScheduleItem#orderInDay}
     *
     * @param groupName to get schedule for
     * @return list of schedule items sorted by dayOfWeek and then by orderInDay
     */
    List<ScheduleItem> getSchedule(String groupName);

    /**
     * Removes saved schedule from repository
     * @param groupName to remove repository for
     */
    void removeSchedule(String groupName);
}
