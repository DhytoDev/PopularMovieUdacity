package com.dhytodev.popularmovie.data.repository;

import com.dhytodev.popularmovie.data.model.Movie;
import com.dhytodev.popularmovie.data.model.Review;
import com.dhytodev.popularmovie.data.model.Trailer;
import com.dhytodev.popularmovie.data.network.TmdbServices;

import java.util.List;

import io.reactivex.Observable;

/**
 * Created by izadalab on 7/8/17.
 */

public class MovieInteractorImpl implements MovieInteractor {

    private TmdbServices services ;

    public MovieInteractorImpl(TmdbServices services) {
        this.services = services;
    }

    @Override
    public Observable<List<Movie>> fetchPopularMovies() {
        return services.getPopularMovies().flatMap(movieResponse -> Observable.just(movieResponse.results));
    }

    @Override
    public Observable<List<Movie>> fetchTopRatedMovies() {
        return services.getTopRatedMovies().flatMap(movieResponse -> Observable.just(movieResponse.results));
    }

    @Override
    public Observable<List<Trailer>> fetchMovieTrailers(int movieId) {
        return services.getMovieTrailers(movieId).flatMap(trailersResponse -> Observable.just(trailersResponse.getResults()));
    }

    @Override
    public Observable<List<Review>> fetchMovieReviews(int movieId, int page) {
        return services.getMovieReviews(movieId, page).flatMap(reviewsResponse -> Observable.just(reviewsResponse.getResults()));
    }
}
