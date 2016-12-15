package com.caco3.orca.header;

import com.caco3.orca.mvp.BaseView;
import com.caco3.orca.orioks.model.Student;

/**
 * Interface for (navigation drawer) header
 */
public interface HeaderView extends BaseView<HeaderPresenter> {

    /**
     * Sets text to indicate that current week is exam period week
     */
    void showExamPeriod();

    /**
     * Sets text to indicate that current week is holidays period week
     */
    void showHolidaysPeriod();

    /**
     * Sets text to show student's name and group
     */
    void showStudent(Student student);

    /**
     * Sets text to indicate that current week is learning week
     */
    void showLearningWeek(int weekNumber);
}
