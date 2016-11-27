package com.caco3.orca.orioks;

import com.caco3.orca.util.Preconditions;


public class UserCredentials {

    private final String login;
    private final String password;

    /**
     * Constructs a new {@link UserCredentials} with provided login and password
     * @param login to use
     * @param password to use
     * @throws NullPointerException if login or password is null
     */
    public UserCredentials(String login, String password) {
        this.login = Preconditions.checkNotNull(login);
        this.password = Preconditions.checkNotNull(password);
    }

    public String getPassword() {
        return password;
    }

    public String getLogin() {
        return login;
    }


}
