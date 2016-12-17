package com.caco3.orca.settings;

import com.caco3.orca.learning.LearningActivity;
import com.caco3.orca.ui.BaseActivity;

/**
 * An interface for app settings storage.
 */
public interface Settings {

    /**
     * Returns true if app launched for the first time
     * @return boolean
     */
    boolean isFirstRun();

    /**
     * Sets value for 'first-run' key so the next {@link #isFirstRun()} call will return provided value
     * @param value to set
     */
    void setFirstRun(boolean value);

    /**
     * Returns {@link Class} object of activity that must be run when the app started
     * Default value - {@link LearningActivity#getClass()}
     * @return class
     */
    Class<? extends BaseActivity> getStartActivity();

    /**
     * Changes a value returned by {@link #getStartActivity()}.
     * @param activityClass to set as start activity
     */
    void setStartActivity(Class<? extends BaseActivity> activityClass);
}
