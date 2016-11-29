package com.caco3.orca.credentials;

import android.content.Context;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Custom dagger module for {@link CredentialsManager}
 */
@Module
public final class CredentialsModule {

    @Provides
    @Singleton
    /*package*/ CredentialsManager provideCredentialsManager(Context context) {
        return new CredentialsManagerPreferencesImpl(context);
    }
}
