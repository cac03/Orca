package com.caco3.orca.scheduleapi;

import com.caco3.orca.util.Preconditions;

import java.util.Calendar;
import java.util.HashSet;
import java.util.Set;

import timber.log.Timber;

/**
 * This class adapts non-convenient raw {@link ScheduleApiResponseInternal} to other convenient objects
 */
public class ResponseAdapter {

    /**
     * Returns a {@link com.caco3.orca.scheduleapi.ScheduleItem.Builder} where all fields are set from provided <code>internal</code>
     * or null if there any error happened during parsing {@link ScheduleItemInternal}
     * @param internal to get builder from
     * @return {@link com.caco3.orca.scheduleapi.ScheduleItem.Builder} or null if unable to get any critical value from <code>internal</code>
     * @throws NullPointerException <code>if internal == null</code>
     */
    private static ScheduleItem.Builder builderFromScheduleItemInternal(ScheduleItemInternal internal) {
        Preconditions.checkNotNull(internal, "internal == null");
        if (!allRequiredFieldsNotNull(internal)) {
            // item invalid
            return null;
        }

        /**
         * in internal response first day in week is monday
         * and it mapped to 1. So to convert to java's mapping
         * we need to add 1.
         * internally 1 - monday and java's monday - 2 {@link Calendar#MONDAY}
         * internally 7 - sunday and java's sunday - 1, so we have take a remainder by 7
         */
        int dayOfWeek = internal.day % 7 + 1;
        String classroom = internal.room.name;
        String teacherName = internal.classInternal.teacherName;
        String disciplineNameRaw = internal.classInternal.name; // it's not null


        String disciplineName;
        /**
         * There is some redundant info in square brackets. Remove it
         */
        boolean lecture = disciplineNameRaw.contains("[Лек]");
        boolean seminar = disciplineNameRaw.contains("[Пр]");
        boolean laboratoryWork = disciplineNameRaw.contains("[Лаб]");

        boolean physicalEducation = false;
        boolean militaryLesson = false;
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
                Timber.w("Unable to remove info in square brackets. Raw discipline name: %s", disciplineNameRaw);
            }
        }

        /**
         * There is a string like '1 пара'.
         * Number is ordinal number of lesson in day. Extract it
         */
        int orderInDay;
        try {
            int endOfNumber = internal.time.time.indexOf(' ');
            if (endOfNumber != -1) {
                orderInDay = Integer.parseInt(internal.time.time.substring(0, endOfNumber));
            } else {
                Timber.e("Couldn't find whitespace in string: %s while parsing ordinal number of lesson", internal.time.time);
                return null;
            }
        } catch (NumberFormatException e) {
            Timber.e(e, "Couldn't extract number from string: %s", internal.time.time);
            return null;
        }

        return ScheduleItem.builder()
                .dayOfWeek(dayOfWeek)
                .disciplineName(disciplineName)
                .classroom(classroom)
                .lecture(lecture)
                .seminar(seminar)
                .laboratoryWork(laboratoryWork)
                .setOrderInDay(orderInDay)
                .teacherName(teacherName)
                .repeatsEveryFirstWeekOfMonth(internal.dayNumber == 0)
                .repeatsEverySecondWeekOfMonth(internal.dayNumber == 1)
                .repeatsEveryThirdWeekOfMonth(internal.dayNumber == 2)
                .repeatsEveryFourthWeekOfMonth(internal.dayNumber == 3)
                .physicalEducation(physicalEducation)
                .militaryLesson(militaryLesson);
    }

    /**
     * Util methods checks whether are all required fields for converting it to {@link ScheduleItem}
     * in {@link ScheduleItemInternal} are not null
     * @param internal to check for
     * @return true if all required fields are not null, false otherwise
     */
    private static boolean allRequiredFieldsNotNull(ScheduleItemInternal internal){
        if (internal.room == null || internal.room.name == null) {
            Timber.e("Provided ScheduleItemInternal is invalid: %s, internal.room == null or internal.room.name == null",
                    internal.toString());
            return false;
        }

        if (internal.classInternal == null) {
            Timber.e("Provided ScheduleItemInternal is invalid: %s, internal.classInternal == null",
                    internal.toString());
            return false;
        }

        if (internal.classInternal.teacherName == null) {
            Timber.e("Provided ScheduleItemInternal is invalid: %s, internal.classInternal.teacherName == null",
                    internal.toString());
            return false;
        }

        if (internal.classInternal.name == null) {
            Timber.e("Provided ScheduleItemInternal is invalid: %s, internal.classInternal.name == null",
                    internal.toString());
            return false;
        }

        if (internal.time == null || internal.time.time == null) {
            Timber.e("Provided ScheduleItemInternal is invalid: %s, internal.time == null || internal.time.time == null",
                    internal.toString());
            return false;
        }

        return true;
    }

    /**
     * Converts {@link ScheduleItemInternal} from {@link ScheduleApiResponseInternal}
     * to set of {@link ScheduleItem}
     * @param responseInternal to convert from
     * @return set of schedule items
     */
    /*package*/ static Set<ScheduleItem> adapt(ScheduleApiResponseInternal responseInternal) {
        // Create builder for each scheduleItems from raw item and collect them into set
        Set<ScheduleItem> result = new HashSet<>();
        if (responseInternal.scheduleItems != null) { // ANYTHING can happen

            for (ScheduleItemInternal scheduleItemInternal : responseInternal.scheduleItems) {
                if (scheduleItemInternal != null) {
                    ScheduleItem.Builder builder = builderFromScheduleItemInternal(scheduleItemInternal);
                    if (builder != null) {
                        result.add(builder.build());
                    } // no log in else clause since there is logging in builderFromScheduleItemInternal method
                } else {
                    Timber.e("Couldn't parse scheduleItemInternal. scheduleItemInternal == null.");
                }
            }
        } else {
            // responseInternal.scheduleItems == null
            Timber.e("responseInternal.scheduleItems == null");
        }

        return result;
    }
}
