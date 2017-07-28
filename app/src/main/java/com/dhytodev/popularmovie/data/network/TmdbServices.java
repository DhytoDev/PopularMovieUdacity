package com.dhytodev.popularmovie.data.network;

import com.dhytodev.popularmovie.BuildConfig;
import com.dhytodev.popularmovie.data.model.response.MoviesResponse;
import com.dhytodev.popularmovie.data.model.response.ReviewsResponse;
import com.dhytodev.popularmovie.data.model.response.TrailersResponse;

import io.reactivex.Observable;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by izadalab on 7/8/17.
 */

public interface TmdbServices {

    @GET("movie/popular")
    Observable<MoviesResponse> getPopularMovies();

    @GET("movie/top_rated")
    Observable<MoviesResponse> getTopRatedMovies();

    @GET("movie/{id}/videos")
    Observable<TrailersResponse> getMovieTrailers(@Path("id") int movieId);

    @GET("movie/{id}/reviews")
    Observable<ReviewsResponse> getMovieReviews(@Path("id") int movieId, @Query("page") int page);

    class ServiceGenerator {
        public static TmdbServices instance() {
            final HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

            final TmdbInterceptor tmdbInterceptor = new TmdbInterceptor() ;

            final OkHttpClient okHttpClient = new OkHttpClient.Builder()
                    .addInterceptor(loggingInterceptor)
                    .addInterceptor(tmdbInterceptor)
                    .build();

            final Retrofit retrofit = new Retrofit.Builder()
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(okHttpClient)
                    .baseUrl(BuildConfig.TMDB_BASE_URL)
                    .build();
            return retrofit.create(TmdbServices.class);
        }
    }
}

