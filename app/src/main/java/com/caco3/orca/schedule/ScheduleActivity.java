package com.caco3.orca.schedule;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.caco3.orca.R;
import com.caco3.orca.ui.BaseActivity;

public class ScheduleActivity extends BaseActivity {
    private static final String FRAGMENT_TAG = "schedule_frag";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);

        Fragment fragment = getSupportFragmentManager().findFragmentByTag(FRAGMENT_TAG);
        if (fragment == null) {
            fragment = new ScheduleFragment();
            fragment.setRetainInstance(true);
        }

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, fragment, FRAGMENT_TAG)
                .commitNow();

        setTitle(R.string.nav_schedule_title);
    }

    @Override
    protected int getNavDrawerItemId() {
        return R.id.nav_schedule;
    }
}
