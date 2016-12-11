package com.caco3.orca.data.schedule;

import com.caco3.orca.schedule.model.Lesson;
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

    /**
     * Saves schedule into repository. So it can be retrieved later
     * @param lessons schedule to save
     * @param groupName associated with this schedule
     */
    void saveSchedule(List<Lesson> lessons, String groupName);

    /**
     * Returns the whole schedule
     * @param groupName to retrieve schedule for
     * @return list of lessons, not null. Will return empty list if no schedule was saved
     */
    List<Lesson> getSchedule(String groupName);

    /**
     * Returns saved schedule, where first lesson will take place later than <code>from</code>
     * @param groupName to get schedule for
     * @param from starting with time
     * @return list of lessons, not null. Will return empty list if no schedule was saved
     */
    List<Lesson> getSchedule(String groupName, long from);

    /**
     * Returns saved schedule, where first lesson will take place later than <code>from</code>, and the first
     * earlier <code>to</code>
     * @param groupName to get schedule for
     * @param from millis since epoch
     * @param to millis since epoch
     * @return list of lessons, not null. Will return empty list if no schedule was saved
     */
    List<Lesson> getSchedule(String groupName, long from, long to);

}
