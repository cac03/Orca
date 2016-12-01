package com.caco3.orca.scheduleapi;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


/*package*/ class ScheduleApiServiceGenerator {

    private static OkHttpClient.Builder httpClient = new OkHttpClient.Builder();

    private static Retrofit.Builder retrofitBuilder
            = new Retrofit.Builder()
            .baseUrl("https://miet.ru/schedule/")
            .addConverterFactory(GsonConverterFactory.create());

    /*package*/ static <S> S createService(Class<S> serviceClass){
        Retrofit retrofit = retrofitBuilder.client(httpClient.build()).build();
        return retrofit.create(serviceClass);
    }


}
