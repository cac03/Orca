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
     * Adapts raw {@link ScheduleApiResponseInternal} to set of {@link ScheduleItem}
     * @param responseInternal to adapt to
     * @return set of schedule items
     */
    public static Set<ScheduleItem> adapt(ScheduleApiResponseInternal responseInternal) {
        // Create builder for each scheduleItems from raw item and collect them into set
        Set<ScheduleItem.Builder> asScheduleItemBuilders = new HashSet<>();
        if (responseInternal.scheduleItems != null) { // ANYTHING can happen

            for (ScheduleItemInternal scheduleItemInternal : responseInternal.scheduleItems) {
                if (scheduleItemInternal != null) {
                    ScheduleItem.Builder builder = builderFromScheduleItemInternal(scheduleItemInternal);
                    if (builder != null) {
                        asScheduleItemBuilders.add(builder);
                    } // no log in else clause since there is logging in builderFromScheduleItemInternal method
                } else {
                    Timber.e("Couldn't parse scheduleItemInternal. scheduleItemInternal == null.");
                }
            }
        } else {
            // responseInternal.scheduleItems == null
            Timber.e("responseInternal.scheduleItems == null");
        }

        /**
         * There is possible situation when two different schedule items take place at same time.
         * It's not permissible for us to return them in set. So we have to merge them and create one
         * item where their properties are combined.
         *
         * Also there is possible situation when two different schedule items are exactly same except
         * classroom name. It's also not permissible for us. We will merge them to.
         *
         * We will collect all items from response and merged items in the next set
         */
        Set<ScheduleItem.Builder> mergedByDisciplineAndClassroom = new HashSet<>(asScheduleItemBuilders);
        for (ScheduleItem.Builder outer : asScheduleItemBuilders) {
            for (ScheduleItem.Builder inner : asScheduleItemBuilders) {
                if (outer != inner) {
                    if (outer.takePlaceAtSameTime(inner)) {
                        // they take place at the same time
                        if (outer.getDisciplineName().equals(inner.getDisciplineName())) {
                            // two classrooms for same discipline name
                            // merge classroom string
                            mergedByDisciplineAndClassroom.add(mergeByClassroom(inner, outer, "/"));
                        } else {
                            // different disciplines at same time
                            mergedByDisciplineAndClassroom
                                    .add(mergeByDisciplinesClassroomAndTeacher(inner, outer, "/"));
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
                        // merging outer with inner
                        // and inner with outer
                        // because we need to get true by calling equals on them
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
     * Returns a new {@link com.caco3.orca.scheduleapi.ScheduleItem.Builder} instance where
     * {@link com.caco3.orca.scheduleapi.ScheduleItem.Builder#classroom(String)}
     * set using <code>first.getClassroom() + separator + second.classroom();</code>
     *
     * Note that order of combining classrooms is natural.
     * In other words suppose that string of <code>first</code>'s classroom is lexicographically
     * less than <code>second</code>'s
     * then in returned Builder classroom string will be set as result of expression:
     * <code>first.getClassroom() + separator + second.classroom();</code>.
     * And vice versa.
     *
     * This method is commutative. Formally
     * <code>mergeByClassroom(a, b, separator).equals(mergeByClassroom(b, a, separator)); </code>
     * returns true. This ensures that when this method called twice for same builders with different
     * order, their <code>equals()</code> will return true
     *
     * @param first to merge
     * @param second to merge
     * @param separator used to combine strings
     * @return Returns a new {@link com.caco3.orca.scheduleapi.ScheduleItem.Builder}
     *
     * @throws NullPointerException if <code>first == null || second == null</code>
     */
    private static ScheduleItem.Builder mergeByClassroom(ScheduleItem.Builder first, ScheduleItem.Builder second, String separator) {
        Preconditions.checkNotNull(first, "first == null");
        Preconditions.checkNotNull(second, "second == null");

        ScheduleItem.Builder merged = first.clone();
        /**
         * We must to compare them to ensure commutative property for this method to prevent case
         * when this method called twice for same builders and their <code>equals()</code>
         * returned false
         */
        if (first.getClassroom().compareTo(second.getClassroom()) < 0) {
            merged.classroom(first.getClassroom() + separator + second.getClassroom());
        } else {
            merged.classroom(second.getClassroom() + separator + first.getClassroom());
        }

        return merged;
    }

    /**
     * Returns a new {@link com.caco3.orca.scheduleapi.ScheduleItem.Builder} instance where
     * {@link com.caco3.orca.scheduleapi.ScheduleItem.Builder#teacherName(String)}
     * set using <code>first.getTeacherName() + separator + second.getTeacherName();</code>,
     * {@link com.caco3.orca.scheduleapi.ScheduleItem.Builder#classroom(String)}
     * set using <code>first.getClassroom() + separator + second.getClassroom();</code>,
     * {@link com.caco3.orca.scheduleapi.ScheduleItem.Builder#disciplineName(String)}}
     * set using <code>first.getDisciplineName() + separator + second.getDisciplineName();</code>
     *
     * Note that order of combining classrooms is natural.
     * In other words suppose that string of <code>first</code>'s disciplineName is lexicographically
     * less than <code>second</code>'s
     * then in returned Builder classroom string will be set as result of expression:
     * <code>first.getDisciplineName() + separator + second.getDisciplineName();</code>.
     * And vice versa.
     *
     * This method is commutative. Formally
     * <code>mergeByDisciplinesClassroomAndTeacher(a, b, separator)
     * .equals(mergeByDisciplinesClassroomAndTeacher(b, a, separator)); </code>
     * returns true. This ensures that when this method called twice for same builders with different
     * order, their <code>equals()</code> will return true
     *
     * @param first to merge
     * @param second to merge
     * @param separator used to combine strings
     * @return Returns a new {@link com.caco3.orca.scheduleapi.ScheduleItem.Builder}
     *
     * @throws NullPointerException if <code>first == null || second == null</code>
     */
    private static ScheduleItem.Builder mergeByDisciplinesClassroomAndTeacher(ScheduleItem.Builder first, ScheduleItem.Builder second, String separator) {
        Preconditions.checkNotNull(first, "first == null");
        Preconditions.checkNotNull(second, "second == null");

        ScheduleItem.Builder merged = first.clone();
        if (first.getDisciplineName().compareTo(second.getDisciplineName()) < 0) {
            merged.classroom(first.getClassroom() + separator + second.getClassroom())
                    .disciplineName(first.getDisciplineName() + separator + second.getDisciplineName())
                    .teacherName(first.getTeacherName() + separator + second.getTeacherName());
        } else {
            merged.classroom(second.getClassroom() + separator + first.getClassroom())
                    .disciplineName(second.getDisciplineName() + separator + first.getDisciplineName())
                    .teacherName(second.getTeacherName() + separator + first.getTeacherName());

        }

        return merged;
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
}
