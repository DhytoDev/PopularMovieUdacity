package com.dhytodev.popularmovie.ui.home;

import com.dhytodev.popularmovie.data.model.Movie;

import java.util.List;

/**
 * Created by izadalab on 7/8/17.
 */

public interface HomeView {
    void showLoading(boolean showLoading);

    void fetchMovies(List<Movie> movies);

    void showError(String message);

    void onMovieClicked(Movie movie);
}
