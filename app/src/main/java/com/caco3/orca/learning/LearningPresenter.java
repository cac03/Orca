package com.caco3.orca.learning;


import com.caco3.orca.mvp.BasePresenter;
import com.caco3.orca.orioks.model.Discipline;

/*package*/ interface LearningPresenter extends BasePresenter<LearningView> {

    /**
     * Called when discipline item in view associated with this presenter was clicked
     * @param discipline item associated with view that was clicked
     */
    void onDisciplineClicked(Discipline discipline);

    /**
     * Called when user attempts to manually refresh data via {@link android.support.v4.widget.SwipeRefreshLayout}
     */
    void onRefreshRequest();

    /**
     * Called when user was notified about incorrect credentials
     * and he clicked 're enter login and password button'
     */
    void onReLogInRequest();
}
