package com.caco3.orca.credentials;

import com.caco3.orca.orioks.UserCredentials;

/**
 * Manages credentials at persistence layer.
 * Able to return current credentials and save credentials as current
 */
public interface CredentialsManager {

    /**
     * Saves provided credentials in persistent storage.
     * After credentials saved, they will be returned in {@link #getCurrentCredentials()}
     *
     * @param credentials to save
     */
    void setCurrentCredentials(UserCredentials credentials);

    /**
     * Reads saved current credentials in persistent storage and returns them
     * @return saved current credentials or <code>null</code> if no credentials saved
     */
    UserCredentials getCurrentCredentials();
}
