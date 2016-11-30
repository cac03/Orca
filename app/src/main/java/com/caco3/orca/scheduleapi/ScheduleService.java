package com.caco3.orca.scheduleapi;


import java.util.List;

import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import rx.Observable;

public interface ScheduleService {

    /**
     * Returns a list of group names schedule for is available via this service
     * @return list of strings
     */
    @GET("https://miet.ru/schedule/groups")
    Observable<List<String>> getGroupNames();

    @POST("https://miet.ru/schedule/data")
    Observable<ScheduleApiResponseInternal> getScheduleResponse(@Body Group group);
}
