package com.caco3.orca.settings;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceFragment;

import com.caco3.orca.OrcaApp;
import com.caco3.orca.R;

import javax.inject.Inject;

public class SettingsFragment extends PreferenceFragment
        implements SharedPreferences.OnSharedPreferenceChangeListener {

    /**
     * Callback for the default shared preferences changes
     */
    /*package*/interface SharedPreferencesChangedListener {
        /**
         * Called when value stored by <code>key</code> is changed
         * @param key value where was changed
         */
        void onPreferenceChanged(String key);
    }

    @Inject
    SharedPreferencesChangedListener sharedPreferencesChangedListener;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        injectFields();

        addPreferencesFromResource(R.xml.preferences);
    }

    private void injectFields(){
        DaggerSettingsComponent
                .builder()
                .applicationComponent(OrcaApp.get(getActivity()).getApplicationComponent())
                .settingsModule(new SettingsModule())
                .build()
                .inject(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        getPreferenceScreen()
                .getSharedPreferences()
                .registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onPause(){
        super.onPause();
        getPreferenceScreen()
                .getSharedPreferences()
                .unregisterOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        sharedPreferencesChangedListener.onPreferenceChanged(key);
    }
}
