package com.caco3.orca.disciplinedetails;

import com.caco3.orca.mvp.BasePresenter;
import com.caco3.orca.orioks.model.ControlEvent;

/**
 * Interface for presenter
 */
/*package*/ interface DisciplineDetailsPresenter extends BasePresenter<DisciplineDetailsView> {

    /**
     * Called when user clicks on control event item
     * @param controlEvent that was clicked
     */
    void onControlEventClicked(ControlEvent controlEvent);
}
