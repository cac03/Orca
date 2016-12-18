package com.caco3.orca.login;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.caco3.orca.R;
import com.caco3.orca.ui.BaseActivity;
import com.caco3.orca.util.Preconditions;

/**
 * Hosts {@link LoginFragment}
 * @see LoginFragment
 */
public class LoginActivity extends BaseActivity {
    private static final String FRAGMENT_TAG = "login_fragment";

    /**
     * A key for intent used to start this activity with login that must be set into login view
     * {@link #getIntent()} will contain value mapped by this key if this activity was
     * started via {@link #startAndSetLogin(Context, String)}
     *
     * @see #startAndSetLogin(Context, String)
     * @see LoginFragment#createAndSetLogin(String)
     */
    private static final String LOGIN_EXTRA = "login";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);

        LoginFragment fragment = (LoginFragment)getSupportFragmentManager()
                .findFragmentByTag(FRAGMENT_TAG);

        if (fragment == null) {
            if (getIntent().hasExtra(LOGIN_EXTRA)) {
                fragment = LoginFragment.createAndSetLogin(getIntent().getStringExtra(LOGIN_EXTRA));
            } else {
                fragment = new LoginFragment();
            }
        }

        fragment.setRetainInstance(true);

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.login_fragment_container, fragment, FRAGMENT_TAG)
                .commitNow();
    }

    /**
     * Starts {@link LoginActivity} activity. And sets provided <code>login</code>
     * to the login {@link android.widget.EditText}
     * @see LoginFragment#createAndSetLogin(String)
     * @param context to start activity
     * @param login to set into login field
     */
    public static void startAndSetLogin(Context context, String login){
        Preconditions.checkNotNull(context, "context == null");
        context.startActivity(new Intent(context, LoginActivity.class)
                .putExtra(LOGIN_EXTRA, login));
    }
}
