package com.caco3.orca.miet;

import org.junit.Test;

import java.util.Calendar;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class MietUtilsTest {

    @Test
    public void firstSeptemberIsFirstWeekOfAutumnSemester(){
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.MONTH, Calendar.SEPTEMBER);
        calendar.set(Calendar.DAY_OF_MONTH, 1);

        assertTrue(MietUtils.belongsToAutumnSemester(calendar));
        assertEquals(1, MietUtils.getWeekOfSemester(calendar));
    }

    @Test
    public void firstDecemberIs14thWeekOfAutumnSemester(){
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.MONTH, Calendar.DECEMBER);
        calendar.set(Calendar.DAY_OF_MONTH, 1);

        assertTrue(MietUtils.belongsToAutumnSemester(calendar));
        assertEquals(14, MietUtils.getWeekOfSemester(calendar));
    }

    @Test
    public void firstOfMayBelongsToSpringSemester(){
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.MONTH, Calendar.MAY);
        calendar.set(Calendar.DAY_OF_MONTH, 1);

        assertTrue(MietUtils.belongsToSpringSemester(calendar));
    }

    @Test
    public void thirtyFirstOfDecemberBelongsToAutumnSemester(){
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.MONTH, Calendar.DECEMBER);
        calendar.set(Calendar.DAY_OF_MONTH, 31);

        assertTrue(MietUtils.belongsToAutumnSemester(calendar));
    }

    @Test
    public void firstLessonEndsAt1030am(){
        Calendar calendar = Calendar.getInstance();
        long millis = MietUtils.getLessonEndTime(calendar, 1);
        calendar.setTimeInMillis(millis);

        assertEquals(10, calendar.get(Calendar.HOUR_OF_DAY));
        assertEquals(30, calendar.get(Calendar.MINUTE));
    }

    @Test(expected = NullPointerException.class)
    public void npeThrownWhenNullProvided(){
        MietUtils.getLessonEndTime(null, 9);
    }

    @Test(expected = IllegalArgumentException.class)
    public void iaeThrownWhenNegativeNumberProvided(){
        MietUtils.getLessonEndTime(Calendar.getInstance(), -1);
    }

    @Test
    public void firstLessonStartsAt900Am(){
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(MietUtils.getLessonBeginTime(calendar, 1));

        assertEquals(9, calendar.get(Calendar.HOUR_OF_DAY));
        assertEquals(0, calendar.get(Calendar.MINUTE));
    }

    @Test
    public void eighthLessonStartsAt930Pm(){
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(MietUtils.getLessonBeginTime(calendar, 8));

        assertEquals(9 + 12, calendar.get(Calendar.HOUR_OF_DAY));
        assertEquals(30, calendar.get(Calendar.MINUTE));
    }

    @Test
    public void firstLessonEndsAt1030Am(){
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(MietUtils.getLessonEndTime(calendar, 1));

        assertEquals(10, calendar.get(Calendar.HOUR_OF_DAY));
        assertEquals(30, calendar.get(Calendar.MINUTE));
    }

    @Test
    public void eighthLessonEndsAt1100Pm(){
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(MietUtils.getLessonEndTime(calendar, 8));

        assertEquals(11 + 12, calendar.get(Calendar.HOUR_OF_DAY));
        assertEquals(0, calendar.get(Calendar.MINUTE));
    }
}
