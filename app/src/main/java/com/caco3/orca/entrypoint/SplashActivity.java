package com.caco3.orca.entrypoint;

import android.content.Intent;
import android.os.Bundle;

import com.caco3.orca.OrcaApp;
import com.caco3.orca.learning.LearningActivity;
import com.caco3.orca.login.LoginActivity;
import com.caco3.orca.schedule.ScheduleActivity;
import com.caco3.orca.ui.BaseActivity;

import javax.inject.Inject;

/**
 * Launcher activity of this app.
 * Shows splash screen and then starts another activity
 * @see <a href="https://material.google.com/patterns/launch-screens.html">material.google.com</a>
 */
public class SplashActivity extends BaseActivity implements EntryPointActivity {

    /**
     * Will do some bootstrap work and will decide which activity to run next
     */
    @Inject // injected in onCreate()
    EntryPointManager entryPointManager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        DaggerEntryPointComponent
                .builder()
                .applicationComponent(OrcaApp.get(this).getApplicationComponent())
                .build()
                .inject(this);

        entryPointManager.doWork(this);
    }

    @Override
    public void startActivity(Class<? extends BaseActivity> activityClass) {
        startActivity(new Intent(this, activityClass));
        finish();
    }
}
