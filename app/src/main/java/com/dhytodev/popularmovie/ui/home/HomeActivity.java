package com.dhytodev.popularmovie.ui.home;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;

import com.dhytodev.popularmovie.R;
import com.dhytodev.popularmovie.data.model.Movie;
import com.dhytodev.popularmovie.ui.Constants;
import com.dhytodev.popularmovie.ui.detail.MovieDetailActivity;
import com.dhytodev.popularmovie.ui.detail.MovieDetailFragment;



import butterknife.BindView;
import butterknife.ButterKnife;

public class HomeActivity extends AppCompatActivity implements HomeFragment.Callback {

    @BindView(R.id.toolbar)
    Toolbar mToolbar ;

    private boolean twoPaneMode ;

    public static final String DETAILS_FRAGMENT = "DetailsFragment";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ButterKnife.bind(this);

        setToolbar();

        if (findViewById(R.id.movie_details_container) != null) {
            twoPaneMode = true ;

            if (savedInstanceState == null) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.movie_details_container, new MovieDetailFragment())
                        .commit();
            }
        } else {
            twoPaneMode = false ;
        }
    }

   private void setToolbar() {
       setSupportActionBar(mToolbar);

       if (getSupportActionBar() != null) {
           getSupportActionBar().setTitle(R.string.app_name);
           getSupportActionBar().setDisplayShowTitleEnabled(true);
       }
   }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_home, menu);
        return true;
    }

    @Override
    public void onMoviesLoaded(Movie movie) {
        if (twoPaneMode) {
            loadMovieFragment(movie);
        } else {

        }
    }

    @Override
    public void onMovieClicked(Movie movie) {
        if (twoPaneMode) {
            loadMovieFragment(movie);
        } else {
            startMovieActivity(movie);
        }
    }

    private void startMovieActivity(Movie movie) {
        Intent intent = new Intent(this, MovieDetailActivity.class);
        Bundle extras = new Bundle();
        extras.putParcelable(Constants.MOVIE_DETAIL, movie);
        intent.putExtras(extras);
        startActivity(intent);
    }

    private void loadMovieFragment(Movie movie) {
        MovieDetailFragment movieDetailsFragment = MovieDetailFragment.getInstance(movie);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.movie_details_container, movieDetailsFragment, DETAILS_FRAGMENT)
                .commit();
    }
}
