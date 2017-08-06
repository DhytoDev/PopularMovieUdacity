package com.dhytodev.popularmovie.ui.detail;

import android.util.Log;
import android.util.Pair;

import com.dhytodev.popularmovie.data.model.Movie;
import com.dhytodev.popularmovie.data.repository.MovieInteractor;
import com.dhytodev.popularmovie.ui.BasePresenter;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by izadalab on 7/27/17.
 */

public class MovieDetailPresenter extends BasePresenter {

    private static final String TAG = MovieDetailPresenter.class.getSimpleName();
    private MovieInteractor movieInteractor ;
    private MovieDetailView mView ;

    public MovieDetailPresenter(MovieInteractor movieInteractor, MovieDetailView mView) {
        this.movieInteractor = movieInteractor;
        this.mView = mView;
    }

    public void getMovieTrailersAndReviews(int movieId) {

        compositeDisposable.add(Observable.zip(movieInteractor.fetchMovieTrailers(movieId), movieInteractor.fetchMovieReviews(movieId, 1),
                (trailers, reviews) -> new Pair<>(trailers, reviews))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(trailersreviewsPair -> {
                    mView.fetchTrailers(trailersreviewsPair.first);
                    mView.fetchReviews(trailersreviewsPair.second);
                }, throwable -> {
                    mView.showError(throwable.getLocalizedMessage());
                }));
    }

    public void saveMovieToFavorite(Movie movie) {
        compositeDisposable.add(movieInteractor.saveMovie(movie)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(() -> mView.saveToFavorited(),
                        throwable -> mView.showError(throwable.getLocalizedMessage())));
    }

    public void isMovieFavorited(Movie movie) {
        compositeDisposable.add(movieInteractor.getMovie(movie)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(result -> mView.isMovieFavorited(), error -> {
                    Log.e(TAG, "isMovieFavorited: " + error.getLocalizedMessage());
                }));
    }

    public void deleteFavorite(Movie movie) {
        compositeDisposable.add(movieInteractor.deleteMovie(movie)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(() -> {
                    Log.d(TAG, "deleteFavorite: success");
                }, throwable -> {
                    Log.e(TAG, "deleteFavorite: error");
                }));
    }


    @Override
    protected void attachView() {
        super.attachView();
    }

    @Override
    protected void detachView() {
        super.detachView();
    }
}
