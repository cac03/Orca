package com.caco3.orca.entrypoint;

/**
 * Launcher activity must implement this interface so the
 */
/*package*/ interface EntryPointActivity {

    /**
     * Launches {@link com.caco3.orca.login.LoginActivity} and finishes current
     */
    void navigateToLoginActivity();

    /**
     * Launches {@link com.caco3.orca.learning.LearningActivity} and finishes current activity
     */
    void navigateToLearningActivity();
}
