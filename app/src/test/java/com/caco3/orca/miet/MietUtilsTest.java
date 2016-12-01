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
}
