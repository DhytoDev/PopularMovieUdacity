package com.dhytodev.popularmovie.ui.detail;

import com.dhytodev.popularmovie.data.model.Review;
import com.dhytodev.popularmovie.data.model.Trailer;

import java.util.List;

/**
 * Created by izadalab on 7/27/17.
 */

public interface MovieDetailView {

    void fetchTrailers(List<Trailer> trailers);
    void fetchReviews(List<Review> reviews);
    void showError(String message);
}
