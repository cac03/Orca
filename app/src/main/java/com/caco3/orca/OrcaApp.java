package com.caco3.orca;

import android.app.Application;
import android.content.Context;
import android.support.annotation.NonNull;

import com.caco3.orca.credentials.CredentialsModule;
import com.caco3.orca.orioks.OrioksModule;

import timber.log.Timber;


public class OrcaApp extends Application {

    @NonNull // initialized in onCreate()
    private ApplicationComponent applicationComponent;

    @Override
    public void onCreate(){
        Timber.plant(new Timber.DebugTree());
        applicationComponent = prepareApplicationComponent().build();
    }
    /**
     * Static method returns {@link OrcaApp} instance from context
     * @param context to get {@link OrcaApp}
     * @return {@link OrcaApp} instance
     */
    public static OrcaApp get(Context context) {
        if (context == null) {
            throw new NullPointerException("context == null");
        }
        return (OrcaApp) context.getApplicationContext();
    }

    /**
     * Integration test mocked Application will override it
     * to provide mocked dependencies
     * @return {@link com.caco3.orca.DaggerApplicationComponent.Builder} with all needed modules set
     */
    protected DaggerApplicationComponent.Builder prepareApplicationComponent(){
        return DaggerApplicationComponent.builder()
                .orioksModule(new OrioksModule())
                .credentialsModule(new CredentialsModule());
    }

    @NonNull
    public ApplicationComponent getApplicationComponent(){
        return applicationComponent;
    }
}
