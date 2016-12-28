package com.caco3.orca.settings;

import android.app.Fragment;
import android.os.Bundle;

import com.caco3.orca.R;
import com.caco3.orca.ui.BaseActivity;


public class SettingsActivity extends BaseActivity {
    private static final String FRAGMENT_TAG = "settings_fragment";


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);

        Fragment fragment = getFragmentManager().findFragmentByTag(FRAGMENT_TAG);
        if (fragment == null) {
            fragment = new SettingsFragment();
        }

        getFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, fragment, FRAGMENT_TAG)
                .commit();

        setTitle(R.string.nav_settings_title);
    }

    @Override
    protected int getNavDrawerItemId() {
        return R.id.nav_settings;
    }
}
