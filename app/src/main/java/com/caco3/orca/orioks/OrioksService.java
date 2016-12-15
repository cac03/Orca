package com.caco3.orca.orioks;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Retrofit interface for orioks api.
 */
/*package*/ interface OrioksService {

    /**
     * Sends get request to the server and returns {@link OrioksResponseJson} for current semester.
     * May return response where {@link OrioksResponseJson#error} is String which contains
     * error description.
     * @return {@link OrioksResponseJson}
     */
    @GET("ORIOKSLive/?AUTH_FORM=Y&TYPE=AUTH&backurl=index.php")
    Call<OrioksResponseJson> getOrioksResponse(@Query("USER_LOGIN") String login, @Query("USER_PASSWORD") String password);

    /**
     * Same as {@link #getOrioksResponse(String, String)} but returns response for specified semester
     *
     * @param semester to get response for
     * @return {@link OrioksResponseJson}
     */
    @GET("ORIOKSLive/?AUTH_FORM=Y&TYPE=AUTH&backurl=index.php")
    Call<OrioksResponseJson> getOrioksResponse(@Query("USER_LOGIN") String login, @Query("USER_PASSWORD") String password, @Query("sem") int semester);
}
