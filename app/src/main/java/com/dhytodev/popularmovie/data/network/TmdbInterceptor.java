package com.dhytodev.popularmovie.data.network;

import com.dhytodev.popularmovie.BuildConfig;

import java.io.IOException;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by izadalab on 7/8/17.
 */

public class TmdbInterceptor implements Interceptor {
    @Override
    public Response intercept(Chain chain) throws IOException {
        final HttpUrl url = chain.request()
                .url()
                .newBuilder()
                .addQueryParameter("api_key", BuildConfig.TMDB_API_KEY)
                .build();
        final Request request = chain.request().newBuilder().url(url).build();
        return chain.proceed(request);
    }
}
