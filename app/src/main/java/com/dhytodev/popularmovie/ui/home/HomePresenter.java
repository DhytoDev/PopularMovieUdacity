package com.dhytodev.popularmovie.ui.home;

import android.util.Log;

import com.dhytodev.popularmovie.data.model.Movie;
import com.dhytodev.popularmovie.data.repository.MovieRepository;
import com.dhytodev.popularmovie.ui.BasePresenter;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by izadalab on 7/8/17.
 */

public class HomePresenter extends BasePresenter {

    private MovieRepository movieRepository;
    private HomeView mView ;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    public HomePresenter(MovieRepository movieRepository, HomeView mView) {
        this.movieRepository = movieRepository;
        this.mView = mView;
    }

    public void fetchMovies(int sort) {
        mView.showLoading(true);

        Observable<List<Movie>> observableMovies;

        if (sort == HomeFragment.POPULAR) {
            observableMovies = movieRepository.fetchPopularMovies();
        } else {
            observableMovies = movieRepository.fetchTopRatedMovies();
        }

        compositeDisposable.add(observableMovies.
                subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(movies -> {
                    mView.showLoading(false);
                    if (movies != null && movies.size() > 0) {
                        mView.fetchMovies(movies);
                        Log.e("movies", movies.get(0).getPosterPath());
                    }
                }, throwable -> {
                    mView.showLoading(false);
                    mView.showError(throwable.getLocalizedMessage());
                }));
    }

    public void getFavoriteMovie() {
        compositeDisposable.add(movieRepository.fetchFavoriteMovies()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(movies -> mView.fetchMovies(movies),
                        throwable -> mView.showError(throwable.getLocalizedMessage())));
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
