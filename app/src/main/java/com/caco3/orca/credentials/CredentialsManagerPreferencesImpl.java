package com.caco3.orca.credentials;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;

import com.caco3.orca.orioks.UserCredentials;

import javax.inject.Inject;

/**
 * {@link CredentialsManager} implementation where saving and retrieving operations done
 * via {@link android.content.SharedPreferences}
 */
/*package*/final class CredentialsManagerPreferencesImpl implements CredentialsManager {

    /**
     * Name of preferences file
     */
    private static final String PREFERENCES_NAME = "credentials";

    /**
     * String containing login for saved credentials
     */
    private static final String KEY_LOGIN = "login";

    /**
     * String containing password for saved credentials
     */
    private static final String KEY_PASSWORD = "password";

    @NonNull // injected in c-tor
    private Context context;

    @Inject
    public CredentialsManagerPreferencesImpl(@NonNull Context context) {
        this.context = context;
    }

    @Override
    public void setCurrentCredentials(UserCredentials credentials) {
        context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE)
                .edit()
                .putString(KEY_LOGIN, credentials.getLogin())
                .putString(KEY_PASSWORD, credentials.getPassword())
                .apply();
    }

    @Override
    public UserCredentials getCurrentCredentials() {
        SharedPreferences sp
                = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE);
        String login = sp.getString(KEY_LOGIN, null);
        String password = sp.getString(KEY_PASSWORD, null);
        if (login == null || password == null) {
            return null;
        } else {
            return new UserCredentials(login, password);
        }
    }

    @Override
    public void removeCurrentCredentials() {
        context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE)
                .edit()
                .remove(KEY_LOGIN)
                .remove(KEY_PASSWORD)
                .apply();
    }
}
