package com.dhytodev.popularmovie.data.repository;

import com.dhytodev.popularmovie.data.model.Movie;
import com.dhytodev.popularmovie.data.model.Review;
import com.dhytodev.popularmovie.data.model.Trailer;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.Single;

/**
 * Created by izadalab on 7/8/17.
 */

public interface MovieInteractor {
    Observable<List<Movie>> fetchPopularMovies();

    Observable<List<Movie>> fetchTopRatedMovies();

    Observable<List<Trailer>> fetchMovieTrailers(int movieId);

    Observable<List<Review>> fetchMovieReviews(int movieId, int page);

    Completable saveMovie(Movie movie);

    Completable deleteMovie(Movie movie);

    Single<Movie> getMovie(Movie movie);

    Observable<List<Movie>> fetchFavoriteMovies();
}
