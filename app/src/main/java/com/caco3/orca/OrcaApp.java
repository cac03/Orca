package com.caco3.orca;

import android.app.Application;
import android.content.Context;

import java.util.Objects;


public class OrcaApp extends Application {

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
