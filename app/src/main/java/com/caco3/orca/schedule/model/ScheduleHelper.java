package com.caco3.orca.schedule.model;

import com.caco3.orca.miet.MietUtils;
import com.caco3.orca.scheduleapi.ScheduleItem;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Helps to work with schedule returned by {@link com.caco3.orca.scheduleapi.ScheduleApi}
 */
public class ScheduleHelper {

    /**
     * Builds schedule from {@link ScheduleItem}.
     * Returns list of {@link Lesson} with real time of its start/end
     *
     * @param schedule collection of {@link ScheduleItem} to build from
     * @param from     millis since epoch when the built schedule should start
     * @param to       millis since epoch built schedule should end
     * @return list of lesson with where each {@link Lesson} has concrete time when it begins/ends
     */
    public static List<Lesson> buildSchedule(Collection<ScheduleItem> schedule, long from, long to) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(from);

        List<Lesson> result = new ArrayList<>(500);

        while (calendar.getTimeInMillis() <= to) {
            for (ScheduleItem item : schedule) {
                if (item.takesPlaceAt(calendar)) {

                    long begin = MietUtils.getLessonBeginTime(calendar, item.getOrderInDay());
                    long end = MietUtils.getLessonEndTime(calendar, item.getOrderInDay());

                    Lesson lesson
                            = Lesson.builder()
                            .teacher(item.getTeacherName())
                            .classroom(item.getClassroom())
                            .beginAt(begin)
                            .endAt(end)
                            .militaryLesson(item.isMilitaryLesson())
                            .lecture(item.isLecture())
                            .seminar(item.isSeminar())
                            .laboratoryWork(item.isLaboratoryWork())
                            .physicalEducation(item.isPhysicalEducation())
                            .militaryLesson(item.isMilitaryLesson())
                            .discipline(item.getDisciplineName())
                            .build();

                    result.add(lesson);
                }
            }

            calendar.add(Calendar.DAY_OF_YEAR, 1);
        }

        Collections.sort(result, new Comparator<Lesson>() {
            @Override
            public int compare(Lesson o1, Lesson o2) {
                if (o1.getBeginMillis() - o2.getBeginMillis() < 0) {
                    return -1;
                } else if (o1.getBeginMillis() - o2.getBeginMillis() == 0) {
                    return 0;
                } else {
                    return 1;
                }
            }
        });

        return result;
    }


    /**
     * Builds daily schedule from {@link Lesson}s, which is ready to be used in views
     * It inserts {@link Lesson#noLesson(int, long, long)} into one item if there is no first lesson
     * or there is no lesson between two any in one day.
     * Merges the same {@link Lesson} if they take place at the same time
     *
     * @param lessons   List of lessons where each of them has real start/end time
     * @param from      millis since epoch to start building daily schedule from
     * @param to        millis since epoch to end building daily schedule at
     * @param separator used to separate {@link Lesson#getClassroom()}
     *                  or any other string if two items take place at the same time
     * @return list of {@link DaySchedule} which is ready to be used in views
     */
    public static List<DaySchedule> buildDailySchedule(List<Lesson> lessons, long from, long to, String separator) {
        List<DaySchedule> result = new ArrayList<>();

        boolean nextDay = true;
        Lesson prevLesson = null;
        List<Lesson> buf = null;
        for (int i = 0; i < lessons.size(); i++) {
            Lesson lesson = lessons.get(i);
            if (lesson.getBeginMillis() < from) {
                continue;
            }

            if (lesson.getEndMillis() > to) {
                break;
            }

            if (prevLesson != null) {
                Calendar prev = Calendar.getInstance();
                prev.setTimeInMillis(prevLesson.getBeginMillis());

                Calendar cur = Calendar.getInstance();
                cur.setTimeInMillis(lesson.getBeginMillis());

                nextDay = cur.get(Calendar.DAY_OF_YEAR) - prev.get(Calendar.DAY_OF_YEAR) != 0;
            }

            if (nextDay || i == lessons.size() - 1) {
                if (prevLesson != null) {

                    if (!buf.isEmpty()) {
                        Calendar calendar = Calendar.getInstance();
                        calendar.setTimeInMillis(buf.get(0).getBeginMillis());
                        calendar.setTimeInMillis(MietUtils.getLessonBeginTime(calendar, 1));
                        for (int j = 0; j < buf.size(); j++) {
                            if (buf.get(j).getBeginMillis() != MietUtils.getLessonBeginTime(calendar, j + 1)) {
                                buf.add(j, Lesson.noLesson(j + 1,
                                        MietUtils.getLessonBeginTime(calendar, j + 1),
                                        MietUtils.getLessonEndTime(calendar, j + 1)));
                            }
                        }
                    }

                    DaySchedule daySchedule = new DaySchedule(prevLesson.getBeginMillis(), buf);
                    result.add(daySchedule);
                }


                buf = new ArrayList<>();
            }

            if (prevLesson != null && !nextDay && prevLesson.getBeginMillis() == lesson.getBeginMillis()) {
                String teacher;
                if (prevLesson.getTeacherName().equals(lesson.getTeacherName())) {
                    teacher = prevLesson.getTeacherName();
                } else {
                    teacher = prevLesson.getTeacherName() + separator + lesson.getTeacherName();
                }

                String classroom;
                if (prevLesson.getClassroom().equals(lesson.getClassroom())) {
                    classroom = prevLesson.getClassroom();
                } else {
                    classroom = prevLesson.getClassroom() + separator + lesson.getClassroom();
                }

                String discipline;
                if (prevLesson.getDisciplineName().equals(lesson.getDisciplineName())) {
                    discipline = prevLesson.getDisciplineName();
                } else {
                    discipline = prevLesson.getDisciplineName() + separator + lesson.getDisciplineName();
                }

                lesson = Lesson.builder()
                        .teacher(teacher)
                        .classroom(classroom)
                        .beginAt(prevLesson.getBeginMillis())
                        .endAt(prevLesson.getEndMillis())
                        .militaryLesson(prevLesson.isMilitaryLesson())
                        .lecture(prevLesson.isLecture())
                        .seminar(prevLesson.isSeminar())
                        .laboratoryWork(prevLesson.isLaboratoryWork())
                        .physicalEducation(prevLesson.isPhysicalEducation())
                        .militaryLesson(prevLesson.isMilitaryLesson())
                        .discipline(discipline)
                        .build();

                buf.remove(prevLesson);
            }

            buf.add(lesson);
            prevLesson = lesson;
        }

        return result;
    }

    /**
     * Builds daily schedule from {@link Lesson}s, which is ready to be used in views
     * It inserts {@link Lesson#noLesson(int, long, long)} into one item if there is no first lesson
     * or there is no lesson between two any in one day.
     * Merges the same {@link Lesson} if they take place at the same time.
     *
     * Same as {@link #buildDailySchedule(List, long, long, String)}, but will build schedule
     * from all provided lessons
     *
     * @param lessons   List of lessons where each of them has real start/end time
     * @param separator used to separate {@link Lesson#getClassroom()}
     *                  or any other string if two items take place at the same time
     * @return list of {@link DaySchedule} which is ready to be used in views
     */
    public static List<DaySchedule> buildDailySchedule(List<Lesson> lessons, String separator) {
        return buildDailySchedule(lessons, Long.MIN_VALUE, Long.MAX_VALUE, separator);
    }
}
