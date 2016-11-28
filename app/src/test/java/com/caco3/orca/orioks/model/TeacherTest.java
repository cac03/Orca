package com.caco3.orca.orioks.model;


import org.junit.Assert;
import org.junit.Test;

public class TeacherTest {
    private static final String DUMMY_FIRST_NAME = "DummyFirstName";
    private static final String DUMMY_LAST_NAME = "DummyLastName";
    private static final String DUMMY_PATRONYMIC = "DummyPatronymic";

    @Test
    public void splittingFullNameWithoutPatronymicIsCorrect(){
        String fullName = DUMMY_LAST_NAME + " " + DUMMY_FIRST_NAME;
        Teacher teacher = new Teacher(fullName);

        Assert.assertEquals(DUMMY_FIRST_NAME, teacher.getFirstName());
        Assert.assertEquals(DUMMY_LAST_NAME, teacher.getLastName());
        Assert.assertEquals(null, teacher.getPatronymic());
    }

    @Test
    public void splittingFullNameWithPatronymicIsCorrect(){
        String fullName = DUMMY_LAST_NAME + " " + DUMMY_FIRST_NAME + " " + DUMMY_PATRONYMIC;
        Teacher teacher = new Teacher(fullName);

        Assert.assertEquals(DUMMY_FIRST_NAME, teacher.getFirstName());
        Assert.assertEquals(DUMMY_LAST_NAME, teacher.getLastName());
        Assert.assertEquals(DUMMY_PATRONYMIC, teacher.getPatronymic());
    }

    @Test(expected = NullPointerException.class)
    public void npeThrownWhenNullProvided(){
        Teacher teacher = new Teacher(null, null, null);
    }
}
