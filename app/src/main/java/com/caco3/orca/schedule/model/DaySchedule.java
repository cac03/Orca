package com.caco3.orca.schedule.model;


import com.caco3.orca.util.Preconditions;

import java.util.List;

/**
 * Used in views in {@link com.caco3.orca.schedule.ScheduleActivity}.
 */
public class DaySchedule {
    /**
     * Millis since epoch when this {@link DaySchedule} takes a place.
     * It used to determine only day when this {@link DaySchedule} takes a place.
     */
    private long timeMillis;

    /**
     * Lessons associated with this Day
     * @see Lesson
     */
    private List<Lesson> lessons;

    /**
     * Constructs new {@link DaySchedule instance}
     * @param timeMillis with correct day set
     * @param lessons associated with this day
     * @throws NullPointerException if <code>lessons == null</code>
     */
    /*public*/ DaySchedule(long timeMillis, List<Lesson> lessons) {
        this.timeMillis = timeMillis;
        this.lessons = Preconditions.checkNotNull(lessons, "lessons == null");
    }

    /**
     * Returns millis since epoch when this {@link DaySchedule} takes a place.
     * @see #timeMillis
     * @return millis since epoch when this {@link DaySchedule} takes a place.
     */
    public long getTimeMillis() {
        return timeMillis;
    }

    /**
     * List of {@link Lesson} associated with this day
     * @return list
     */
    public List<Lesson> getLessons() {
        return lessons;
    }
}
