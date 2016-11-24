package com.caco3.orca;

import android.content.Context;
import android.support.v4.content.PermissionChecker;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Dagger application module.
 * Provides {@link android.content.Context}
 */
@Module
public final class ApplicationModule {
    private final Context context;

    public ApplicationModule(Context context) {
        this.context = context;
    }

    @Provides
    @Singleton
    public Context provideContext(){
        return context;
    }
}
