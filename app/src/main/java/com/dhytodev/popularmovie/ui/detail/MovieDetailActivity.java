package com.dhytodev.popularmovie.ui.detail;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.dhytodev.popularmovie.R;
import com.dhytodev.popularmovie.data.model.Movie;
import com.dhytodev.popularmovie.ui.Constants;


/**
 * Created by izadalab on 7/8/17.
 */

public class MovieDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);

        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if (extras != null && extras.containsKey(Constants.MOVIE_DETAIL)) {
                Movie movie = extras.getParcelable(Constants.MOVIE_DETAIL);
                if (movie != null) {
                    MovieDetailFragment movieDetailsFragment = MovieDetailFragment.getInstance(movie);
                    getSupportFragmentManager().beginTransaction().add(R.id.movie_details_container, movieDetailsFragment).commit();
                }
            }
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }
}
