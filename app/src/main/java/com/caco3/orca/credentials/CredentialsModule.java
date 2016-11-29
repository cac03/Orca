package com.caco3.orca.credentials;

import android.content.Context;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Custom dagger module for {@link CredentialsManager}
 */
@Module
public class CredentialsModule {

    @Provides
    @Singleton
    public CredentialsManager provideCredentialsManager(Context context) {
        return new CredentialsManagerPreferencesImpl(context);
    }
}
