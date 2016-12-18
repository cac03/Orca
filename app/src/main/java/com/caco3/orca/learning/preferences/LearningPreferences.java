package com.caco3.orca.learning.preferences;

import com.caco3.orca.orioks.UserCredentials;

/**
 * An interface for {@link android.content.SharedPreferences} for {@link com.caco3.orca.learning.LearningActivity}.
 * Stores key-value pairs for user preferences
 */
public interface LearningPreferences {
    /**
     * Constant indicating that we must show disciplines for current semester, returned by
     * {@link com.caco3.orca.orioks.Orioks#getResponseForCurrentSemester(UserCredentials)}
     */
    int SHOW_DISCIPLINES_FOR_CURRENT_SEMESTER = -1;

    /**
     * Returns int corresponding to the semester that was selected by user to show disciplines for.
     * Will return {@link #SHOW_DISCIPLINES_FOR_CURRENT_SEMESTER} if no value was specified previously
     * via {@link #setSemesterToShowDisciplines(UserCredentials, int)}
     * @param credentials active credentials. We can show different semesters for different accounts
     * @return int
     */
    int getSemesterToShowDisciplines(UserCredentials credentials);

    /**
     * Saves current semester value to show disciplines in {@link com.caco3.orca.learning.LearningActivity}
     * @param credentials active credentials
     * @param semester to save
     */
    void setSemesterToShowDisciplines(UserCredentials credentials, int semester);
}
