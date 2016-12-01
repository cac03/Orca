package com.caco3.orca.scheduleapi;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Callable;

import retrofit2.Call;
import retrofit2.Response;
import rx.Observable;

/**
 * Controls instantiations for {@link ScheduleApi}
 */
public class ScheduleApiFactory {

    private static final ScheduleApi instance = new ScheduleApi() {
        private ScheduleService scheduleService
                = ScheduleApiServiceGenerator.createService(ScheduleService.class);

        @Override
        public Observable<Set<ScheduleItem>> getSchedule(final String groupName) {

            final Group group = new Group();
            group.group = groupName;

            return Observable.fromCallable(new Callable<Set<ScheduleItem>>() {
                @Override
                public Set<ScheduleItem> call() throws Exception {
                    Call<ScheduleApiResponseInternal> call
                            = scheduleService.getScheduleResponse(groupName);
                    System.out.println(
                            "Going to get schedule response for group name: " + groupName);
                    Response<ScheduleApiResponseInternal> response = call.execute();
                    System.out.println("Executed response: " + response);
                    if (response.isSuccessful()) {
                        System.out.println("Response successful");
                        return ResponseAdapter.adapt(response.body());
                    } else {
                        System.out.println("Response is not successful");
                        return null;
                    }

                }
            });
        }

        @Override
        public Observable<Set<String>> getGroups() {
            return Observable.fromCallable(new Callable<Set<String>>() {
                @Override
                public Set<String> call() throws Exception {
                    Call<List<String>> call
                            = scheduleService.getGroupNames();
                    System.out.println(
                            "Going to get group names");
                    Response<List<String>> response = call.execute();
                    System.out.println("Executed response: " + response);
                    if (response.isSuccessful()) {
                        System.out.println("Response successful");
                        return new HashSet<>(response.body());
                    } else {
                        System.out.println("Response is not successful");
                        return null;
                    }
                }
            });
        }
    };

    public static ScheduleApi getScheduleApi(){
        return instance;
    }
}
