package com.caco3.orca.scheduleapi;

import java.util.Calendar;
import java.util.HashSet;
import java.util.Set;

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
        /**
         * There are duplicates in raw response which differ only {@link ScheduleItemInternal#dayNumber}
         * (0 - repeats first week, 1 - every second ...)
         * We don't want duplicates in result.
         * So we will merge them:
         * We will keep all 'unique' builders in this set, and when we will get another one from
         * raw response we will search it here and if found merge them
         * @see ScheduleItem.Builder#areSameExceptRepeats(ScheduleItem.Builder)
         * @see ScheduleItem.Builder#mergeByRepeats(ScheduleItem.Builder)
         *
         * Also there are duplicates which differ only by {@link RoomInternal#name} it's not fixed.
         * We take only one of these duplicates todo: fix it
         */
        Set<ScheduleItem.Builder> uniqueExceptClassroomBuilders = new HashSet<>();

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


                    boolean foundSame = false;
                    for (ScheduleItem.Builder unique : uniqueExceptClassroomBuilders) {
                        // no need duplicates
                        if (unique.areSameExceptRepeats(builder)) {
                            unique.mergeByRepeats(builder);
                            foundSame = true;
                            break;
                        }
                    }

                    if (!foundSame) {
                        uniqueExceptClassroomBuilders.add(builder);
                    }
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


        Set<ScheduleItem.Builder> uniqueBuilders = new HashSet<>();
        for (ScheduleItem.Builder b : uniqueExceptClassroomBuilders) {
            boolean foundSame = false;
            for(ScheduleItem.Builder builder : uniqueBuilders) {
                if (builder.areSameExceptClassroom(b)) {
                    foundSame = true;
                }
            }

            if (!foundSame) {
                uniqueBuilders.add(b);
            }
        }

        Set<ScheduleItem> result = new HashSet<>();

        for(ScheduleItem.Builder builder : uniqueBuilders) {
            result.add(builder.build());
        }

        return result;
    }
}
