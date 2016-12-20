package com.caco3.orca.disciplinedetails;

import android.widget.TextView;

import com.caco3.orca.mvp.BaseView;
import com.caco3.orca.orioks.model.ControlEvent;
import com.caco3.orca.orioks.model.Discipline;
import com.caco3.orca.orioks.model.Teacher;

import java.util.List;


/*package*/interface DisciplineDetailsView extends BaseView<DisciplineDetailsPresenter> {

    /**
     * Sets title string to the view
     * @param title to set
     */
    void setTitle(String title);

    /**
     * Returns discipline this view shows details for
     * @return {@link Discipline}
     */
    Discipline getDiscipline();

    /**
     * Sets {@link ControlEvent}s list to show
     * @param controlEvents to show
     */
    void setControlEvents(List<ControlEvent> controlEvents);

    /**
     * Sets {@link Teacher}s list to show
     * @param teachers to show
     */
    void setTeachers(List<Teacher> teachers);

    /**
     * Shows department string
     * @param department to show as department value
     */
    void setDepartment(String department);

    /**
     * Shows provided string as assessment type value
     * @param assessmentType to show
     */
    void setAssessmentType(String assessmentType);

    /**
     * Shows achieved points value in view
     * @param achievedPoints to show
     */
    void setAchievedPoints(float achievedPoints);

    /**
     * Shows available points value
     * @param availablePoints to show
     */
    void setAvailablePoints(float availablePoints);
}
