package com.caco3.orca.miet;

import com.caco3.orca.util.Preconditions;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Contains some useful constants and methods which help to work with dates
 */
public class MietUtils {

    private static final long MILLIS_PER_WEEK = TimeUnit.DAYS.toMillis(7);
    /**
     * Weeks per each semester
     */
    private static final int WEEKS_PER_SEMESTER = 18;

    /**
     * Length of one lesson in minutes
     */
    private static final int LESSON_LENGTH_MINUTES = 90;

    /**
     * Length of break between lessons in minutes
     */
    private static final int BREAK_LENGTH_MINUTES = 10;

    /**
     * Calendar with date set to the start of autumn semester
     */
    private static final Calendar startOfAutumnSemester;
    /**
     * Calendar with date set to the end of autumn semester
     */
    private static final Calendar endOfAutumnSemester;
    /**
     * Calendar with date set to the start of spring semester
     */
    private static final Calendar startOfSpringSemester;
    /**
     * Calendar with date set to the end of spring semester
     */
    private static final Calendar endOfSpringSemester;

    /**
     * Begin, end lesson time lists declaration.
     *
     * @see #BEGIN_LESSON_HOURS_OF_DAY
     * @see #BEGIN_LESSON_MINUTES_OF_HOUR
     * @see #END_LESSON_HOURS_OF_DAY
     * @see #END_LESSON_MINUTES_OF_HOUR
     * Combining all of them we can get begin and (hour, minute) time of any lesson in range
     * 1 - 7, but since arrays are zero-based, to get begin hour of first lesson we must use 0 index
     */
    /**
     * Hours of lessons begin time in order. First lesson starts at 9'th hour of day
     */
    private static final List<Integer> BEGIN_LESSON_HOURS_OF_DAY
            = Collections.unmodifiableList(Arrays.asList(9, 10, 12, 14, 16, 18, 19));

    /**
     * Minutes of lesson begin time in order. First lesson starts at 0 minute of hour.
     */
    private static final List<Integer> BEGIN_LESSON_MINUTES_OF_HOUR
            = Collections.unmodifiableList(Arrays.asList(0, 40, 20, 20, 0, 10, 50));

    /**
     * Hours of lessons end time in order. First lesson ends at 10'th hour of day
     */
    private static final List<Integer> END_LESSON_HOURS_OF_DAY
            = Collections.unmodifiableList(Arrays.asList(10, 12, 13, 15, 17, 19, 21));

    /**
     * Minutes of lesson end time in order. First lesson ends at 30'th minute of hour
     */
    private static final List<Integer> END_LESSON_MINUTES_OF_HOUR
            = Collections.unmodifiableList(Arrays.asList(30, 10, 50, 50, 30, 40, 20));


    static {
        startOfAutumnSemester = Calendar.getInstance();
        startOfAutumnSemester.set(Calendar.MONTH, Calendar.SEPTEMBER);
        startOfAutumnSemester.set(Calendar.DAY_OF_MONTH, 1);
        startOfAutumnSemester.set(Calendar.HOUR_OF_DAY, 0);

        endOfAutumnSemester = (Calendar)startOfAutumnSemester.clone();
        endOfAutumnSemester.add(Calendar.WEEK_OF_YEAR, WEEKS_PER_SEMESTER);
        endOfAutumnSemester.set(Calendar.HOUR_OF_DAY, 24);
        Calendar newYear = Calendar.getInstance();
        newYear.set(newYear.get(Calendar.YEAR), Calendar.DECEMBER, 31, 24, 59);
        if (endOfAutumnSemester.compareTo(newYear) > 0) {
            endOfAutumnSemester.setTimeInMillis(newYear.getTimeInMillis());
        }

        startOfSpringSemester = Calendar.getInstance();
        startOfSpringSemester.set(Calendar.MONTH, Calendar.FEBRUARY);
        startOfSpringSemester.set(Calendar.DAY_OF_MONTH, 13);
        startOfSpringSemester.set(Calendar.HOUR_OF_DAY, 0);

        endOfSpringSemester = (Calendar) startOfSpringSemester.clone();
        endOfSpringSemester.add(Calendar.WEEK_OF_YEAR, WEEKS_PER_SEMESTER);
        endOfSpringSemester.set(Calendar.HOUR_OF_DAY, 24);
    }

    /**
     * Returns true if time in provided calendar is between {@link #startOfAutumnSemester}
     * and {@link #endOfAutumnSemester}, false otherwise
     * @param calendar to test to
     * @return boolean
     */
    public static boolean belongsToAutumnSemester(Calendar calendar) {
        return calendar.compareTo(startOfAutumnSemester) >= 0
                && calendar.compareTo(endOfAutumnSemester) <= 0;
    }

    /**
     * Returns true if time in provided calendar is between {@link #startOfSpringSemester}
     * and {@link #endOfSpringSemester}, false otherwise
     * @param calendar to test to
     * @return boolean
     */
    public static boolean belongsToSpringSemester(Calendar calendar) {
        return calendar.compareTo(startOfSpringSemester) >= 0
                && calendar.compareTo(endOfSpringSemester) <= 0;
    }

    /**
     * Number of week relative to semester where date in provided calendar belongs
     * First week is 1.
     *
     * If calendar time {@link #belongsToAutumnSemester(Calendar)} it will calculate week relative
     * to {@link #startOfAutumnSemester}
     *
     * If calendar time {@link #belongsToSpringSemester(Calendar)} it will calculate week relative
     * to {@link #startOfSpringSemester}
     *
     * @param calendar to get number of week for
     * @return number of week relative to semester where date in provided calendar belongs
     * to, or -1 if this date doesn't belong to any semester
     */
    public static int getWeekOfSemester(Calendar calendar){
        if (belongsToAutumnSemester(calendar)) {
            long millisDiff = calendar.getTimeInMillis() - startOfAutumnSemester.getTimeInMillis();
            return (int)(millisDiff / MILLIS_PER_WEEK + 1);
        } else if (belongsToSpringSemester(calendar)) {
            long millisDiff = calendar.getTimeInMillis() - startOfSpringSemester.getTimeInMillis();
            return (int)(millisDiff / MILLIS_PER_WEEK + 1);
        }

        return -1;
    }

    /**
     * Suppress instantiation
     */
    private MietUtils(){
        throw new AssertionError("No instances");
    }

    /**
     * Returns millis since epoch when a lesson with provided ordinal number begins,
     * it just takes year, and day of year from provided calendar and then sets hour
     * and minutes of lesson beginning for provided lesson's ordinal number
     * @param calendar with set day and year
     * @param ordinal ordinal number of lesson. 1 based
     * @return millis since epoch with set hour and minute for lesson with provided ordinal number
     *
     * @throws NullPointerException if <code>calendar == null</code>
     * @throws IllegalArgumentException if <code>ordinal <= 0</code>
     */
    public static long getLessonBeginTime(Calendar calendar, int ordinal) {
        Preconditions.checkNotNull(calendar);
        /**
         * We don't want to modify arguments
         */
        calendar = (Calendar)calendar.clone();
        if (ordinal <= 0){
            throw new IllegalArgumentException("ordinal < 0. ordinal = " + ordinal);
        }

        if (ordinal - 1 < BEGIN_LESSON_HOURS_OF_DAY.size()) {
            // just set pre calculated values
            calendar.set(Calendar.HOUR_OF_DAY, BEGIN_LESSON_HOURS_OF_DAY.get(ordinal - 1));
            calendar.set(Calendar.MINUTE, BEGIN_LESSON_MINUTES_OF_HOUR.get(ordinal - 1));
        } else {
            calendar.set(Calendar.HOUR_OF_DAY, BEGIN_LESSON_HOURS_OF_DAY.get(BEGIN_LESSON_HOURS_OF_DAY.size() - 1));
            calendar.set(Calendar.MINUTE, BEGIN_LESSON_MINUTES_OF_HOUR.get(BEGIN_LESSON_HOURS_OF_DAY.size() - 1));
            int toAdd = (ordinal - BEGIN_LESSON_HOURS_OF_DAY.size())
                    * (LESSON_LENGTH_MINUTES + BREAK_LENGTH_MINUTES);

            calendar.add(Calendar.MINUTE, toAdd);
        }

        return calendar.getTimeInMillis();
    }

    /**
     * Same as {@link #getLessonBeginTime(Calendar, int)}, but instead of {@link Calendar} accepts long
     * millis since epoch
     */
    public static long getLessonBeginTime(long millis, int ordinal) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(millis);
        return getLessonBeginTime(calendar, ordinal);
    }

    /**
     * Returns time in millis since epoch when lesson ends, year and date values are taken from provided
     * <code>calendar</code>, hour and minutes are calculated.
     * @param calendar calendar with set year and day, not null
     * @param ordinal ordinal number of lesson in day. 1-based
     * @return time in millis since epoch
     *
     * @throws NullPointerException if <code>calendar == null</code>
     * @throws IllegalArgumentException if <code>ordinal <= 0</code>
     */
    public static long getLessonEndTime(Calendar calendar, int ordinal) {
        Preconditions.checkNotNull(calendar, "calendar == null");
        if (ordinal <= 0) {
            throw new IllegalArgumentException("ordinal <= 0, ordinal = " + ordinal);
        }
        /**
         * Don't modify arguments
         */
        calendar = (Calendar) calendar.clone();

        if (ordinal - 1 < END_LESSON_HOURS_OF_DAY.size()) {
            // just return pre calculated values
            calendar.set(Calendar.HOUR_OF_DAY, END_LESSON_HOURS_OF_DAY.get(ordinal - 1));
            calendar.set(Calendar.MINUTE, END_LESSON_MINUTES_OF_HOUR.get(ordinal - 1));
        } else {
            // need to calculate
            calendar.set(Calendar.HOUR_OF_DAY, END_LESSON_HOURS_OF_DAY.get(BEGIN_LESSON_HOURS_OF_DAY.size() - 1));
            calendar.set(Calendar.MINUTE, END_LESSON_MINUTES_OF_HOUR.get(BEGIN_LESSON_HOURS_OF_DAY.size() - 1));
            int toAdd = (ordinal - BEGIN_LESSON_HOURS_OF_DAY.size())
                    * (LESSON_LENGTH_MINUTES + BREAK_LENGTH_MINUTES);

            calendar.add(Calendar.MINUTE, toAdd);
        }

        return calendar.getTimeInMillis();
    }

    /**
     * Same as {@link #getLessonEndTime(Calendar, int)}, but instead of Calendar accepts millis since epoch
     */
    public static long getLessonEndTime(long millis, int ordinal) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(millis);
        return getLessonEndTime(calendar, ordinal);
    }

    /**
     * Returns millis since epoch when current semester begins.
     * If current time doesn't belong to autumn semester nor spring semester, returns -1
     * @return millis since epoch when current semester begins.
     */
    public static long getCurrentSemesterBeginTime(){
        Calendar calendar = Calendar.getInstance();
        if (belongsToAutumnSemester(calendar)) {
            return startOfAutumnSemester.getTimeInMillis();
        } else if (belongsToSpringSemester(calendar)) {
            return startOfSpringSemester.getTimeInMillis();
        } else {
            return -1;
        }
    }

    /**
     * Returns millis since epoch when current semester ends.
     * If current time doesn't belong to autumn semester nor spring semester, returns -1
     * @return millis since epoch when current semester ends.
     */
    public static long getCurrentSemesterEndTime(){
        Calendar calendar = Calendar.getInstance();
        if (belongsToAutumnSemester(calendar)) {
            return endOfAutumnSemester.getTimeInMillis();
        } else if (belongsToSpringSemester(calendar)) {
            return endOfSpringSemester.getTimeInMillis();
        } else {
            return -1;
        }
    }
}
