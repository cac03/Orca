package com.caco3.orca;

import android.app.Application;
import android.content.Context;

import java.util.Objects;

import timber.log.Timber;


public class OrcaApp extends Application {

    @Override
    public void onCreate(){
        Timber.plant(new Timber.DebugTree());
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
}
