package com.caco3.orca.login;

import android.os.Bundle;

import com.caco3.orca.R;
import com.caco3.orca.ui.BaseActivity;

/**
 * Hosts {@link LoginFragment}
 * @see LoginFragment
 */
public class LoginActivity extends BaseActivity {
    private static final String FRAGMENT_TAG = "login_fragment";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);

        LoginFragment fragment = (LoginFragment)getSupportFragmentManager()
                .findFragmentByTag(FRAGMENT_TAG);

        if (fragment == null) {
            fragment = new LoginFragment();
        }

        fragment.setRetainInstance(true);

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.login_fragment_container, fragment, FRAGMENT_TAG)
                .commitNow();
    }
}
