package com.caco3.orca.scheduleapi;

import com.caco3.orca.BuildConfig;

import java.util.Calendar;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

/**
 * This class adapts non-convenient raw {@link ScheduleApiResponseInternal} to other convenient objects
 */
public class ResponseAdapter {

    /**
     * Adapts raw {@link ScheduleApiResponseInternal} to set of {@link ScheduleItem}
     * @param responseInternal to adapt to
     * @return set of schedule items
     */
    public static Set<ScheduleItem> adapt(ScheduleApiResponseInternal responseInternal) {
        // Create builder for each scheduleItems from raw item and collect them into set
        Set<ScheduleItem.Builder> asScheduleItemBuilders = new HashSet<>();
        if (responseInternal.scheduleItems != null) { // ANYTHING can happen

            for (ScheduleItemInternal scheduleItemInternal : responseInternal.scheduleItems) {

                /**
                 * in internal response first day in week is monday
                 * and it mapped to 1. So to convert to java's mapping
                 * we need to add 1.
                 * internally 1 - monday and java's monday - 2 {@link Calendar#MONDAY}
                 * internally 7 - sunday and java's sunday - 1, so we have take a remainder by 7
                 */
                int dayOfWeek = scheduleItemInternal.day % 7 + 1;
                String classroom = scheduleItemInternal.room.name;
                String teacherName = scheduleItemInternal.classInternal.teacherName;

                String disciplineName;
                String disciplineNameRaw = scheduleItemInternal.classInternal.name;

                /**
                 * When {@link ScheduleItem.Builder#builder()} called, it may throw <code>NullPointerException</code>
                 * if any of non-primitive field is null. We must avoid it
                 */
                if (disciplineNameRaw != null
                        && classroom != null
                        && teacherName != null) {
                    boolean lecture;
                    boolean seminar;
                    boolean laboratoryWork;
                    boolean physicalEducation = false;
                    boolean militaryLesson = false;
                    {
                        /**
                         * There is String like 'Англ.яз [Лек]'.
                         * We don't want the end of it
                         */
                        int startOfRedundantInfo = disciplineNameRaw.indexOf('[');
                        if (startOfRedundantInfo != -1) {
                            disciplineName = disciplineNameRaw.substring(0, startOfRedundantInfo - 1 /*whitespace*/);
                        } else {
                            // use raw
                            disciplineName = disciplineNameRaw;
                            if (disciplineNameRaw.contains("Физическая")) {
                                // it's ok
                                physicalEducation = true;
                            } else if (disciplineNameRaw.contains("Военная")) {
                                // ok
                                militaryLesson = true;
                            } else {
                                // something went wrong... Format changed?
                                System.err.println("Unable to remove info in square brackets. Raw discipline name: "
                                        + disciplineNameRaw);
                            }
                        }

                        lecture = disciplineNameRaw.contains("[Лек]");
                        seminar = disciplineNameRaw.contains("[Пр]");
                        laboratoryWork = disciplineNameRaw.contains("[Лаб]");
                    }

                    int orderInDay = -1;
                    try {
                        int endOfNumber = scheduleItemInternal.time.time.indexOf(' ');
                        orderInDay = Integer.parseInt(scheduleItemInternal.time.time.substring(0, endOfNumber));
                    } catch (NumberFormatException e) {
                        e.printStackTrace();
                        // couldn't parse
                    }


                    ScheduleItem.Builder builder
                            = ScheduleItem.builder()
                            .dayOfWeek(dayOfWeek)
                            .disciplineName(disciplineName)
                            .classroom(classroom)
                            .lecture(lecture)
                            .seminar(seminar)
                            .laboratoryWork(laboratoryWork)
                            .setOrderInDay(orderInDay)
                            .teacherName(teacherName)
                            .repeatsEveryFirstWeekOfMonth(scheduleItemInternal.dayNumber == 0)
                            .repeatsEverySecondWeekOfMonth(scheduleItemInternal.dayNumber == 1)
                            .repeatsEveryThirdWeekOfMonth(scheduleItemInternal.dayNumber == 2)
                            .repeatsEveryFourthWeekOfMonth(scheduleItemInternal.dayNumber == 3)
                            .physicalEducation(physicalEducation)
                            .militaryLesson(militaryLesson);

                    asScheduleItemBuilders.add(builder);
                } else {
                    // this raw item is invalid. Just log
                    System.err.println("Provided scheduleItemInternal is invalid");
                    System.err.println("classroom = " + classroom);
                    System.err.println("teacherName = " + teacherName);
                }
            }
        } else {
            // responseInternal.scheduleItems == null
            System.err.println("responseInternal.scheduleItems == null");
        }

        Set<ScheduleItem.Builder> mergedByDisciplineAndClassroom = new HashSet<>(asScheduleItemBuilders);
        for (ScheduleItem.Builder outer : asScheduleItemBuilders) {
            for (ScheduleItem.Builder inner : asScheduleItemBuilders) {
                if (outer != inner) {
                    if (outer.takePlaceAtSameTime(inner)) {
                        // they take place at the same time
                        if (outer.getDisciplineName().equals(inner.getDisciplineName())) {
                            // two classrooms for same discipline name
                            // merge classroom string
                            ScheduleItem.Builder merged = outer.clone();
                            if (outer.getClassroom().compareTo(inner.getClassroom()) < 0) {
                                merged.classroom(outer.getClassroom() + "/" + inner.getClassroom());
                            } else {
                                merged.classroom(inner.getClassroom() + "/" + outer.getClassroom());
                            }

                            mergedByDisciplineAndClassroom.add(merged);
                        } else {
                            // different disciplines at same time
                            ScheduleItem.Builder merged = outer.clone();
                            if (outer.getDisciplineName().compareTo(inner.getDisciplineName()) < 0) {
                                merged.classroom(outer.getClassroom() + "/" + inner.getClassroom())
                                        .disciplineName(outer.getDisciplineName() + "/" + inner.getDisciplineName())
                                        .teacherName(outer.getTeacherName() + "/" + inner.getTeacherName());
                            } else {
                                merged.classroom(inner.getClassroom() + "/" + outer.getClassroom())
                                        .disciplineName(inner.getDisciplineName() + "/" + outer.getDisciplineName())
                                        .teacherName(inner.getTeacherName() + "/" + outer.getTeacherName());

                            }
                            mergedByDisciplineAndClassroom.add(merged);
                        }
                        mergedByDisciplineAndClassroom.remove(inner);
                        mergedByDisciplineAndClassroom.remove(outer);
                    }
                }
            }
        }

        asScheduleItemBuilders = null;
        // merge by repeats if possible
        for(ScheduleItem.Builder outer : mergedByDisciplineAndClassroom) {
            for (ScheduleItem.Builder inner : mergedByDisciplineAndClassroom) {
                if (outer != inner) {
                    if (outer.areSameExceptRepeats(inner)) {
                        outer.mergeByRepeats(inner);
                        inner.mergeByRepeats(outer);
                    }
                }
            }
        }
        Set<ScheduleItem.Builder> unique = new HashSet<>(mergedByDisciplineAndClassroom);
        for(ScheduleItem.Builder builder : mergedByDisciplineAndClassroom) {
            if (!unique.contains(builder)) {
                unique.add(builder);
            }
        }

        Set<ScheduleItem> result = new HashSet<>();
        for(ScheduleItem.Builder builder : unique) {
            result.add(builder.build());
        }

        return result;
    }
}
