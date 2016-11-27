package com.caco3.orca.orioks;

/**
 * Might be thrown by {@link Orioks} when provided {@link UserCredentials} are incorrect
 */
public final class LoginOrPasswordIncorrectException extends Exception {
    public LoginOrPasswordIncorrectException() {
        super();
    }

    public LoginOrPasswordIncorrectException(String message) {
        super(message);
    }

    public LoginOrPasswordIncorrectException(String message, Throwable cause) {
        super(message, cause);
    }

    public LoginOrPasswordIncorrectException(Throwable cause) {
        super(cause);
    }
}
