package com.caco3.orca.data.schedule;

import com.caco3.orca.scheduleapi.ScheduleItem;

import java.util.Collection;
import java.util.Collections;
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

    /**
     * Saves a collection of group names schedule for is available in {@link com.caco3.orca.scheduleapi.ScheduleApi}
     * @param collection string collection
     */
    void saveGroupNames(Collection<String> collection);

    /**
     * Returns list of group names already saved with {@link #saveGroupNames(Collection)} schedule
     * for is available via {@link com.caco3.orca.scheduleapi.ScheduleApi}
     * @return List of strings sorted by natural order or <code>null</code> if there
     * were no group names saved with {@link #saveGroupNames(Collection)}
     */
    List<String> getGroupNames();
}
