package com.caco3.orca.learning.preferences;

import android.content.Context;
import android.support.annotation.NonNull;

import com.caco3.orca.orioks.UserCredentials;

import javax.inject.Inject;

/**
 * Concrete {@link LearningPreferences} implementation
 */
/*package*/ class LearningPreferencesImpl implements LearningPreferences {
    private static final String FILENAME = "learning";

    /**
     * A prefix for key where stored value for a semester that {@link com.caco3.orca.learning.LearningActivity}
     * must to show disciplines for
     */
    private static final String SEMESTER_TO_SHOW_DISCIPLINES_PREFIX = "disc_to_show_";

    @NonNull // injected in c-tor
    private Context context;

    @Inject
    /*package*/ LearningPreferencesImpl(@NonNull Context context) {
        this.context = context;
    }

    @Override
    public int getSemesterToShowDisciplines(UserCredentials credentials) {
        return context.getSharedPreferences(FILENAME, Context.MODE_PRIVATE)
                .getInt(SEMESTER_TO_SHOW_DISCIPLINES_PREFIX + credentials.getLogin(),
                        SHOW_DISCIPLINES_FOR_CURRENT_SEMESTER);
    }

    @Override
    public void setSemesterToShowDisciplines(UserCredentials credentials, int semester) {
        context.getSharedPreferences(FILENAME, Context.MODE_PRIVATE)
                .edit()
                .putInt(SEMESTER_TO_SHOW_DISCIPLINES_PREFIX + credentials.getLogin(), semester)
                .apply();
    }
}
