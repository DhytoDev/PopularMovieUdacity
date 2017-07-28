package com.dhytodev.popularmovie.data.model.response;

import com.dhytodev.popularmovie.data.model.Review;
import com.dhytodev.popularmovie.data.model.Trailer;

import java.util.List;

/**
 * Created by izadalab on 7/27/17.
 */

public class ReviewsResponse {

    private List<Review> results;

    public List<Review> getResults() {
        return results;
    }

    public void setResults(List<Review> results) {
        this.results = results;
    }
}
