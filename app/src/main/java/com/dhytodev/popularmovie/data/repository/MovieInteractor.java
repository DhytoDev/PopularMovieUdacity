package com.dhytodev.popularmovie.data.repository;

import com.dhytodev.popularmovie.data.model.Movie;

import java.util.List;

import io.reactivex.Observable;

/**
 * Created by izadalab on 7/8/17.
 */

public interface MovieInteractor {
    Observable<List<Movie>> fetchPopularMovies();

    Observable<List<Movie>> fetchTopRatedMovies();
}
