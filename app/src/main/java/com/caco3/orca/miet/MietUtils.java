package com.caco3.orca.miet;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

/**
 * Contains some useful constants and methods which help to work with dates
 */
public class MietUtils {
    /**
     * Weeks per each semester
     */
    private static final int WEEKS_PER_SEMESTER = 18;

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
    public static final List<Integer> BEGIN_LESSON_HOURS_OF_DAY
            = Collections.unmodifiableList(Arrays.asList(9, 10, 12, 14, 16, 18, 19));

    /**
     * Minutes of lesson begin time in order. First lesson starts at 0 minute of hour.
     */
    public static final List<Integer> BEGIN_LESSON_MINUTES_OF_HOUR
            = Collections.unmodifiableList(Arrays.asList(0, 40, 20, 20, 0, 10, 50));

    /**
     * Hours of lessons end time in order. First lesson ends at 10'th hour of day
     */
    public static final List<Integer> END_LESSON_HOURS_OF_DAY
            = Collections.unmodifiableList(Arrays.asList(10, 12, 13, 15, 17, 19, 21));

    /**
     * Minutes of lesson end time in order. First lesson ends at 30'th minute of hour
     */
    public static final List<Integer> END_LESSON_MINUTES_OF_HOUR
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
        newYear.set(Calendar.MONTH, Calendar.DECEMBER);
        newYear.set(Calendar.DAY_OF_MONTH, 31);
        if (endOfAutumnSemester.compareTo(newYear) > 0) {
            endOfAutumnSemester.set(Calendar.MONTH, Calendar.DECEMBER);
            endOfAutumnSemester.set(Calendar.DAY_OF_MONTH, 31);
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
            return calendar.get(Calendar.WEEK_OF_YEAR)
                    - startOfAutumnSemester.get(Calendar.WEEK_OF_YEAR) + 1;
        } else if (belongsToSpringSemester(calendar)) {
            return calendar.get(Calendar.WEEK_OF_YEAR)
                    - startOfSpringSemester.get(Calendar.WEEK_OF_YEAR) + 1;
        }

        return -1;
    }

    /**
     * Suppress instantiation
     */
    private MietUtils(){
        throw new AssertionError("No instances");
    }
}
