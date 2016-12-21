package com.caco3.orca.disciplinedetails;

import com.caco3.orca.orioks.model.ControlEvent;
import com.caco3.orca.orioks.model.Discipline;

import javax.inject.Inject;

/**
 * Concrete {@link DisciplineDetailsPresenter} implementation
 */
/*package*/ class DisciplineDetailsPresenterImpl implements DisciplineDetailsPresenter {

    /**
     * That associated view shows details for
     */
    private Discipline discipline;

    /**
     * Associated with this presenter
     */
    private DisciplineDetailsView view;

    @Inject
    /*package*/ DisciplineDetailsPresenterImpl(){
    }

    @Override
    public void onViewAttached(DisciplineDetailsView view) {
        this.view = view;
        initView();
    }

    private void initView(){
        if (discipline == null && view != null) {
            discipline = view.getDiscipline();
        }

        if (view != null) {
            view.setTitle(discipline.getName());
            view.setControlEvents(discipline.getAttachedControlEvents());
            view.setTeachers(discipline.getAttachedTeachers());
            view.setAssessmentType(discipline.getAssessmentType());
            view.setAchievedPoints(discipline.getTotalAchievedPoints());
            view.setAvailablePoints(discipline.getTotalAvailablePoints());
            view.setDepartment(discipline.getDepartment());
        }
    }

    @Override
    public void onControlEventClicked(ControlEvent controlEvent) {
        // do nothing now
    }

    @Override
    public void onViewDetached() {
        view = null;
    }
}
