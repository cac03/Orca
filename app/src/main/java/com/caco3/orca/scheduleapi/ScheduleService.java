package com.caco3.orca.scheduleapi;


import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/*package*/ interface ScheduleService {

    /**
     * Returns a list of group names schedule for is available via this service
     * @return list of strings
     */
    @GET("groups")
    Call<List<String>> getGroupNames();

    @GET("data")
    Call<ScheduleApiResponseInternal> getScheduleResponse(@Query("group") String groupName);
}
