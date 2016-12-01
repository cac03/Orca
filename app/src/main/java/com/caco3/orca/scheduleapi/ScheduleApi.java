package com.caco3.orca.scheduleapi;

import java.util.Set;

import rx.Observable;

/**
 * An interface for schedule api
 */
public interface ScheduleApi {
    /**
     * Returns Observable Set of {@link ScheduleItem} for provided groupName
     * @param groupName to get schedule items
     * @return observable
     */
    Observable<Set<ScheduleItem>> getSchedule(String groupName);

    /**
     * Returns Observable of set of string with group names schedule for is available
     * @return observable
     */
    Observable<Set<String>> getGroups();
}
