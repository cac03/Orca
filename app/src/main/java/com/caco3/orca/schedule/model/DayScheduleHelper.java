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
                    Lesson lesson = new Lesson(scheduleItem.getDisciplineName(),
                            MietUtils.getLessonBeginTime(iterableCalendar, scheduleItem.getOrderInDay()),
                            MietUtils.getLessonEndTime(iterableCalendar, scheduleItem.getOrderInDay()),
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
                    lessons.add(j, Lesson.noLesson(j + 1,
                            MietUtils.getLessonBeginTime(iterableCalendar, j + 1),
                            MietUtils.getLessonEndTime(iterableCalendar, j + 1)));
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
