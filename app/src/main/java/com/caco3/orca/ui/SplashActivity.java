package com.caco3.orca.ui;

import android.content.Intent;
import android.os.Bundle;

import com.caco3.orca.login.LoginActivity;

/**
 * Launcher activity of this app.
 * Shows splash screen and then starts another activity
 * @see #NEXT_ACTIVITY
 * @see <a href="https://material.google.com/patterns/launch-screens.html">material.google.com</a>
 */
public class SplashActivity extends BaseActivity {
    private static final Class<? extends BaseActivity> NEXT_ACTIVITY
            = LoginActivity.class; // TODO: 11/25/16 Specify next activity

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        startActivity(new Intent(this, NEXT_ACTIVITY));
        finish();
    }


}
