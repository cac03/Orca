package com.caco3.orca.orioks;


import com.caco3.orca.orioks.model.OrioksResponse;

import java.util.concurrent.Callable;

import retrofit2.Call;
import retrofit2.Response;
import rx.Observable;
import timber.log.Timber;

/**
 * Concrete {@link Orioks} implementation
 */
/*package*/ class OrioksImpl implements Orioks {
    private OrioksService orioksService = OrioksServiceGenerator.createService(OrioksService.class);

    @Override
    public Observable<OrioksResponse> getResponseForCurrentSemester(final UserCredentials credentials) {
        return Observable.fromCallable(new Callable<OrioksResponse>() {
            @Override
            public OrioksResponse call() throws Exception {
                Timber.i("Going to get orioks response for login: %s",
                        credentials.getLogin());
                Call<OrioksResponseJson> call = orioksService
                        .getOrioksResponse(credentials.getLogin(), credentials.getPassword());
                Response<OrioksResponseJson> response = call.execute();
                if (response.isSuccessful()) {
                    Timber.i("Retrofit response is successful");
                    OrioksResponseJson orioksResponse = response.body();
                    Timber.i("orioksResponse.error = %s", orioksResponse.error);
                    if (orioksResponse.error instanceof Boolean) {
                        // everything's ok
                        return OrioksResponseAdapter.convert(orioksResponse);
                    } else if (orioksResponse.error instanceof String) {
                        throw new OrioksException("orioksResponse.error", (String) orioksResponse.error);
                    } else {
                        throw new RuntimeException("orioksResponse.error is not String and is not Boolean");
                    }
                } else {
                    Timber.w("Retrofit response is not successful. Returning null");
                    return null;
                }
            }
        });
    }

    @Override
    public Observable<OrioksResponse> getResponse(final UserCredentials credentials, final int semester) {
        return Observable.fromCallable(new Callable<OrioksResponse>() {
            @Override
            public OrioksResponse call() throws Exception {
                Timber.i("Going to get orioks response for login: %s, semester: %d",
                        credentials.getLogin(), semester);
                Call<OrioksResponseJson> call = orioksService.getOrioksResponse(credentials.getLogin(), credentials.getPassword(), semester);
                Response<OrioksResponseJson> response = call.execute();
                if (response.isSuccessful()) {
                    Timber.i("Retrofit response is successful");
                    OrioksResponseJson orioksResponse = response.body();
                    Timber.i("orioksResponse.error = %s", orioksResponse.error);
                    if (orioksResponse.error instanceof Boolean) {
                        // everything's ok
                        return OrioksResponseAdapter.convert(orioksResponse);
                    } else if (orioksResponse.error instanceof String) {
                        throw new OrioksException("orioksResponse.error", (String) orioksResponse.error);
                    } else {
                        throw new RuntimeException("orioksResponse.error is not String and is not Boolean");
                    }
                } else {
                    Timber.w("Retrofit response is not successful. Returning null");
                    return null;
                }
            }
        });
    }
}
