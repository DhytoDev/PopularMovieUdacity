package com.dhytodev.popularmovie.data.repository;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;
import android.util.Log;

import com.dhytodev.popularmovie.data.model.Movie;
import com.dhytodev.popularmovie.data.model.Review;
import com.dhytodev.popularmovie.data.model.Trailer;
import com.dhytodev.popularmovie.data.network.TmdbServices;
import com.dhytodev.popularmovie.data.provider.TmdbContract;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.Single;

import static com.dhytodev.popularmovie.data.provider.TmdbContract.MovieEntry.COLUMN_MOVIE_BACKDROP_PATH;
import static com.dhytodev.popularmovie.data.provider.TmdbContract.MovieEntry.COLUMN_MOVIE_FAVORED;
import static com.dhytodev.popularmovie.data.provider.TmdbContract.MovieEntry.COLUMN_MOVIE_ID;
import static com.dhytodev.popularmovie.data.provider.TmdbContract.MovieEntry.COLUMN_MOVIE_OVERVIEW;
import static com.dhytodev.popularmovie.data.provider.TmdbContract.MovieEntry.COLUMN_MOVIE_POSTER_PATH;
import static com.dhytodev.popularmovie.data.provider.TmdbContract.MovieEntry.COLUMN_MOVIE_RELEASE_DATE;
import static com.dhytodev.popularmovie.data.provider.TmdbContract.MovieEntry.COLUMN_MOVIE_TITLE;
import static com.dhytodev.popularmovie.data.provider.TmdbContract.MovieEntry.COLUMN_MOVIE_VOTE_AVERAGE;
import static com.dhytodev.popularmovie.data.provider.TmdbContract.MovieEntry.COLUMN_MOVIE_VOTE_COUNT;
import static com.dhytodev.popularmovie.data.provider.TmdbContract.MovieEntry.CONTENT_URI;

/**
 * Created by izadalab on 7/8/17.
 */

public class MovieRepositoryImpl implements MovieRepository {

    private static final String TAG = MovieRepositoryImpl.class.getSimpleName() ;
    private TmdbServices services ;
    private ContentResolver contentResolver ;

    private final String[] movieProjection = new String[]{
            TmdbContract.MovieEntry.COLUMN_MOVIE_ID,
            TmdbContract.MovieEntry.COLUMN_MOVIE_TITLE,
            TmdbContract.MovieEntry.COLUMN_MOVIE_OVERVIEW,
            TmdbContract.MovieEntry.COLUMN_MOVIE_VOTE_COUNT,
            TmdbContract.MovieEntry.COLUMN_MOVIE_VOTE_AVERAGE,
            TmdbContract.MovieEntry.COLUMN_MOVIE_RELEASE_DATE,
            TmdbContract.MovieEntry.COLUMN_MOVIE_FAVORED,
            TmdbContract.MovieEntry.COLUMN_MOVIE_POSTER_PATH,
            TmdbContract.MovieEntry.COLUMN_MOVIE_BACKDROP_PATH,
    };

    public MovieRepositoryImpl(TmdbServices services, ContentResolver contentResolver) {
        this.services = services;
        this.contentResolver = contentResolver ;
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

    @Override
    public Completable saveMovie(Movie movie) {
        Log.d(TAG, "saveMovie: " + movie.getTitle());
        return Completable.create(e -> {
            final ContentValues movieValues = movieContentValues(movie);
            contentResolver.insert(CONTENT_URI, movieValues);
            e.onComplete();
        });
    }

    @Override
    public Completable deleteMovie(Movie movie) {
        Log.d(TAG, "deleteMovie: " + movie.getTitle());
        return Completable.create(e -> {
            final String where = String.format("%s=?", COLUMN_MOVIE_ID);
            final String[] args = new String[]{String.valueOf(movie.getId())};
            contentResolver.delete(CONTENT_URI, where, args);
            e.onComplete();
        });
    }

    @Override
    public Single<Movie> getMovie(Movie movie) {
        return Single.create(e -> {
            final String where = String.format("%s=?", COLUMN_MOVIE_ID);
            final String[] args = new String[]{String.valueOf(movie.getId())};
            final Cursor cursor = contentResolver.query(CONTENT_URI, movieProjection, where, args, null);
            Log.d(TAG, "getMovie: " + movie.getTitle());
            Log.d(TAG, "getMovie: cursor count " + cursor.getCount());
            if (cursor.getCount() >= 1) {
                cursor.moveToFirst();
                final Movie resultMovie = Movie.fromCursor(cursor);
                e.onSuccess(resultMovie);
            } else {
                e.onError(new Throwable("No movies"));
            }
        });
    }

    @Override
    public Observable<List<Movie>> fetchFavoriteMovies() {
        return Observable.create(e -> {
            List<Movie> movies = new ArrayList<>();
            final Cursor query = contentResolver.query(CONTENT_URI, movieProjection, null, null, null);
            if (query.moveToFirst()) {
                do {
                    movies.add(Movie.fromCursor(query));
                } while (query.moveToNext());
            }
            Log.d(TAG, "getFavoriteMovies: " + movies.size());
            e.onNext(movies);
        });
    }

    private static ContentValues movieContentValues(Movie movie) {
        final ContentValues values = new ContentValues();
        values.put(COLUMN_MOVIE_ID, movie.getId());
        values.put(COLUMN_MOVIE_TITLE, movie.getTitle());
        values.put(COLUMN_MOVIE_OVERVIEW, movie.getOverview());
        values.put(COLUMN_MOVIE_VOTE_COUNT, movie.getVoteCount());
        values.put(COLUMN_MOVIE_VOTE_AVERAGE, movie.getVoteAverage());
        values.put(COLUMN_MOVIE_RELEASE_DATE, movie.getReleaseDate());
        values.put(COLUMN_MOVIE_FAVORED, movie.isFavMovie());
        values.put(COLUMN_MOVIE_POSTER_PATH, movie.getPosterPath());
        values.put(COLUMN_MOVIE_BACKDROP_PATH, movie.getBackdropPath());
        return values;
    }
}
