package com.caco3.orca.settings.settingschanges;

import android.content.Context;

import com.caco3.orca.util.Preconditions;

/**
 * An abstract app settings change.
 * When user changes app settings subclass must be constructed and applied
 */
public abstract class SettingsChange {

    private final Context context;

    public SettingsChange(Context context) {
        this.context = Preconditions.checkNotNull(context, "context == null");
    }

    /**
     * Applies this settings change to the application
     */
    public abstract void apply();

    protected Context getContext(){
        return context;
    }
}
