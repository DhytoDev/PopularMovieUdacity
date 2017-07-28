package com.dhytodev.popularmovie.data.model.response;

import com.dhytodev.popularmovie.data.model.Trailer;

import java.util.List;

/**
 * Created by izadalab on 7/27/17.
 */

public class TrailersResponse {
    private int id;
    private List<Trailer> results;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<Trailer> getResults() {
        return results;
    }

    public void setResults(List<Trailer> results) {
        this.results = results;
    }
}
