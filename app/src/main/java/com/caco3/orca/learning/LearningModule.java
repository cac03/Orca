package com.caco3.orca.learning;

import com.caco3.orca.credentials.CredentialsManager;
import com.caco3.orca.data.orioks.OrioksRepository;
import com.caco3.orca.learning.preferences.LearningPreferences;
import com.caco3.orca.orioks.Orioks;
import com.caco3.orca.settings.Settings;


import dagger.Module;
import dagger.Provides;

@Module
public class LearningModule {

    @Provides
    @LearningScope
    public LearningPresenter provideLearningPresenter(Orioks orioks,
                                                      CredentialsManager credentialsManager,
                                                      Settings settings,
                                                      OrioksRepository orioksRepository,
                                                      LearningPreferences preferences) {
        return new LearningPresenterImpl(orioks,
                credentialsManager,
                settings,
                orioksRepository,
                preferences);
    }
}
