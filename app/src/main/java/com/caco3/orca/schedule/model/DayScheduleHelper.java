package com.caco3.orca.schedule.model;


import com.caco3.orca.miet.MietUtils;
import com.caco3.orca.scheduleapi.ScheduleItem;
import com.caco3.orca.util.Preconditions;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class DayScheduleHelper {

    /**
     * Compares lesson by {@link Lesson#getOrderInDay()}.
     * Used for sorting
     */
    private static final Comparator<Lesson> lessonComparatorByOrderInDay
            = new Comparator<Lesson>() {
        @Override
        public int compare(Lesson o1, Lesson o2) {
            return o1.getOrderInDay() - o2.getOrderInDay();
        }
    };

    /**
     * Returns a list of {@link DaySchedule} ready to be used in views.
     * It returns {@link DaySchedule} for each day in range <code>[from; to]</code>
     *
     * If there is no first lesson, but there is a second,
     * then {@link DaySchedule#getLessons()}.get(0) will return Lesson
     * instantiated with {@link Lesson#noLesson(int, long, long)}.
     * Same for situation where there is no lessons between any two lessons in day
     * Use {@link Lesson#isNoLesson()} to determine whether this lesson is 'no lesson'
     *
     * @see ScheduleItem
     * @see DaySchedule
     *
     * @param scheduleItems forming schedule
     * @param from millis since epoch
     * @param to millis since epoch
     * @return list of {@link DaySchedule} for each day in range
     * @throws IllegalArgumentException if <code>to > from</code>
     * @throws NullPointerException if <code>scheduleItems == null</code>
     */
    public static List<DaySchedule> dayScheduleInRange(List<ScheduleItem> scheduleItems, long from, long to) {
        Preconditions.checkNotNull(scheduleItems, "scheduleItems == null");
        /**
         * Used in loop
         */
        Calendar iterableCalendar = Calendar.getInstance();
        iterableCalendar.setTimeInMillis(from);

        List<DaySchedule> result = new ArrayList<>();

        while (iterableCalendar.getTimeInMillis() < to){
            List<Lesson> lessons = new ArrayList<>();


            for(ScheduleItem scheduleItem : scheduleItems) {
                if (scheduleItem.takesPlaceAt(iterableCalendar)) {
                    Calendar lessonBegin = (Calendar)iterableCalendar.clone();
                    lessonBegin.set(Calendar.HOUR_OF_DAY, MietUtils.BEGIN_LESSON_HOURS_OF_DAY
                            .get(scheduleItem.getOrderInDay() - 1));
                    lessonBegin.set(Calendar.MINUTE, MietUtils.BEGIN_LESSON_MINUTES_OF_HOUR
                            .get(scheduleItem.getOrderInDay() - 1));

                    Calendar lessonEnd = (Calendar)iterableCalendar.clone();
                    lessonEnd.set(Calendar.HOUR_OF_DAY, MietUtils.END_LESSON_HOURS_OF_DAY
                            .get(scheduleItem.getOrderInDay() - 1));
                    lessonEnd.set(Calendar.MINUTE, MietUtils.END_LESSON_MINUTES_OF_HOUR
                            .get(scheduleItem.getOrderInDay() - 1));

                    Lesson lesson = new Lesson(scheduleItem.getDisciplineName(),
                            lessonBegin.getTimeInMillis(),
                            lessonEnd.getTimeInMillis(),
                            scheduleItem.getClassroom(),
                            scheduleItem.getTeacherName(),
                            scheduleItem.getOrderInDay(),
                            scheduleItem.isLecture(),
                            scheduleItem.isSeminar(),
                            scheduleItem.isLaboratoryWork(),
                            scheduleItem.isPhysicalEducation(),
                            scheduleItem.isMilitaryLesson());

                    lessons.add(lesson);
                }
            }

            Collections.sort(lessons, lessonComparatorByOrderInDay);
            for(int j = 0; j < lessons.size(); j++) {
                // if there is no first lesson
                // or there is no lessons between two any
                // insert null
                if (lessons.get(j).getOrderInDay() != j + 1) {
                    Calendar lessonBegin = (Calendar)iterableCalendar.clone();
                    lessonBegin.set(Calendar.HOUR_OF_DAY, MietUtils.BEGIN_LESSON_HOURS_OF_DAY
                            .get(j));
                    lessonBegin.set(Calendar.MINUTE, MietUtils.BEGIN_LESSON_MINUTES_OF_HOUR
                            .get(j));

                    Calendar lessonEnd = (Calendar)iterableCalendar.clone();
                    lessonEnd.set(Calendar.HOUR_OF_DAY, MietUtils.END_LESSON_HOURS_OF_DAY
                            .get(j));
                    lessonEnd.set(Calendar.MINUTE, MietUtils.END_LESSON_MINUTES_OF_HOUR
                            .get(j));

                    lessons.add(j, Lesson.noLesson(j + 1,
                            lessonBegin.getTimeInMillis(),
                            lessonEnd.getTimeInMillis()));
                }
            }

            result.add(new DaySchedule(iterableCalendar.getTimeInMillis(), lessons));

            iterableCalendar.add(Calendar.DAY_OF_YEAR, 1);
        }



        return result;
    }

    // suppress instantiation
    private DayScheduleHelper(){
        throw new AssertionError("No instances");
    }
}
