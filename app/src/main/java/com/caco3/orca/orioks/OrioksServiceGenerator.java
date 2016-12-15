package com.caco3.orca.orioks;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Util class used to generate {@link OrioksService}
 */
/*package*/ class OrioksServiceGenerator {
    private static final String ORIOKS_BASE_URL = "http://orioks.miet.ru/";
    private static final OkHttpClient.Builder httpClient = new OkHttpClient.Builder();

    private static Retrofit.Builder retrofit
            = new Retrofit.Builder()
            .baseUrl(ORIOKS_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create());

    /*package*/ static <S> S createService(Class<S> serviceClass) {
        return retrofit.client(httpClient.build())
                .build()
                .create(serviceClass);
    }

}
