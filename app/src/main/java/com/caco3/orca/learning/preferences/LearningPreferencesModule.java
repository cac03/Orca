package com.caco3.orca.learning.preferences;

import android.content.Context;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class LearningPreferencesModule {

    @Provides
    @Singleton
    public LearningPreferences provideLearningPreferences(Context context) {
        return new LearningPreferencesImpl(context);
    }
}
