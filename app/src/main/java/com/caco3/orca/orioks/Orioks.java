package com.caco3.orca.orioks;

import com.caco3.orca.orioks.model.OrioksResponse;

import java.io.IOException;

import rx.Observable;

public interface Orioks {

    /**
     * Returns a {@link OrioksResponse} for current semester
     * @param credentials to get response with
     * @return {@link OrioksResponse}'s{@link Observable}
     */
    Observable<OrioksResponse> getResponseForCurrentSemester(UserCredentials credentials);

    /**
     * Returns a {@link OrioksResponse} for specified semester
     * @return {@link OrioksResponse}'s {@link Observable}
     * @param credentials to get response with
     * @param semester to get response for
     */
    Observable<OrioksResponse> getResponse(UserCredentials credentials, int semester);
}
