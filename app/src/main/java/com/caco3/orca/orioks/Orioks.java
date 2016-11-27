package com.caco3.orca.orioks;

import com.caco3.orca.orioks.model.OrioksResponse;

import java.io.IOException;

public interface Orioks {

    /**
     * Returns a {@link OrioksResponse} for current semester
     * @return {@link OrioksResponse}
     * @throws LoginOrPasswordIncorrectException {@link UserCredentials} are incorrect
     * @throws IOException if any i/o error occurred
     */
    OrioksResponse getResponseForCurrentSemester()
            throws LoginOrPasswordIncorrectException, IOException;

    /**
     * Returns a {@link OrioksResponse} for specified semester
     * @return {@link OrioksResponse}
     * @param semester to get response for
     * @throws LoginOrPasswordIncorrectException {@link UserCredentials} are incorrect
     * @throws IOException if any i/o error occurred
     */
    OrioksResponse getResponse(int semester)
            throws LoginOrPasswordIncorrectException, IOException;
}
