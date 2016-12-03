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
    public void sizesOfLessonBeginEndTimeListsAreEqual(){
        int size = MietUtils.BEGIN_LESSON_HOURS_OF_DAY.size();
        assertEquals(MietUtils.BEGIN_LESSON_MINUTES_OF_HOUR.size(), size);
        assertEquals(MietUtils.END_LESSON_HOURS_OF_DAY.size(), size);
        assertEquals(MietUtils.END_LESSON_MINUTES_OF_HOUR.size(), size);
    }

    @Test
    public void firstLessonStartAt900Am() {
        Calendar firstLesson = Calendar.getInstance();
        firstLesson.set(Calendar.HOUR_OF_DAY, MietUtils.BEGIN_LESSON_HOURS_OF_DAY.get(0));
        firstLesson.set(Calendar.MINUTE, MietUtils.BEGIN_LESSON_MINUTES_OF_HOUR.get(0));

        assertEquals(9, firstLesson.get(Calendar.HOUR_OF_DAY));
        assertEquals(0, firstLesson.get(Calendar.MINUTE));
    }

    @Test
    public void fourthLessonEndsAt350Pm(){
        Calendar fourthLesson = Calendar.getInstance();
        fourthLesson.set(Calendar.HOUR_OF_DAY, MietUtils.END_LESSON_HOURS_OF_DAY.get(3));
        fourthLesson.set(Calendar.MINUTE, MietUtils.END_LESSON_MINUTES_OF_HOUR.get(3));

        assertEquals(15, fourthLesson.get(Calendar.HOUR_OF_DAY));
        assertEquals(50, fourthLesson.get(Calendar.MINUTE));
    }
}
