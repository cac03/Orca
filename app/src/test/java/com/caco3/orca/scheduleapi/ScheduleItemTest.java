package com.caco3.orca.scheduleapi;

import org.junit.Test;

import java.util.Calendar;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class ScheduleItemTest {

    private static final ScheduleItem thursdaySecondWeek
            = ScheduleItem.builder()
            .classroom("3304")
            .repeatsEverySecondWeekOfMonth(true)
            .dayOfWeek(Calendar.THURSDAY)
            .disciplineName("Фантазирование")
            .lecture(true)
            .setOrderInDay(5)
            .teacherName("Ivanov")
            .build();

    private static final ScheduleItem mondayAllWeeks
            = ScheduleItem.builder()
            .classroom("3304")
            .repeatsEveryFirstWeekOfMonth(true)
            .repeatsEverySecondWeekOfMonth(true)
            .repeatsEveryThirdWeekOfMonth(true)
            .repeatsEveryFourthWeekOfMonth(true)
            .dayOfWeek(Calendar.THURSDAY)
            .disciplineName("Фантазирование")
            .lecture(true)
            .setOrderInDay(5)
            .teacherName("Ivanov")
            .build();

    @Test
    public void thursdaySecondWeekItemTakesPlaceOnSecondThursday(){
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.MONTH, Calendar.SEPTEMBER);
        calendar.add(Calendar.WEEK_OF_YEAR, 1);

        assertTrue(thursdaySecondWeek.takesPlaceAt(calendar));
    }

    @Test
    public void thursdaySecondWeekItemDoesNotTakePlaceAtJuly(){
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.MONTH, Calendar.JULY);

        assertFalse(thursdaySecondWeek.takesPlaceAt(calendar));
    }

    @Test
    public void mondayAllWeeksItemTakesPlaceAtFirstMonday(){
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.MONTH, Calendar.SEPTEMBER);
        calendar.set(Calendar.DAY_OF_MONTH, 1);

        assertTrue(mondayAllWeeks.takesPlaceAt(calendar));
    }

    @Test
    public void mondayAllWeeksItemTakesPlaceAtSecondMonday(){
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.MONTH, Calendar.SEPTEMBER);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        calendar.add(Calendar.WEEK_OF_YEAR, 1);

        assertTrue(mondayAllWeeks.takesPlaceAt(calendar));
    }

    @Test
    public void mondayAllWeeksItemTakesPlaceAtThirdMonday(){
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.MONTH, Calendar.SEPTEMBER);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        calendar.add(Calendar.WEEK_OF_YEAR, 2);

        assertTrue(mondayAllWeeks.takesPlaceAt(calendar));
    }

    @Test
    public void mondayAllWeeksItemTakesPlaceAtFourthMonday(){
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.MONTH, Calendar.SEPTEMBER);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        calendar.add(Calendar.WEEK_OF_YEAR, 3);

        assertTrue(mondayAllWeeks.takesPlaceAt(calendar));
    }
}
