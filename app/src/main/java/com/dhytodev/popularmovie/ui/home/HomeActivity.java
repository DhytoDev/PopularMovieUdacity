package com.dhytodev.popularmovie.ui.home;

import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.dhytodev.popularmovie.R;
import com.dhytodev.popularmovie.data.model.Movie;
import com.dhytodev.popularmovie.data.network.TmdbServices;
import com.dhytodev.popularmovie.data.repository.MovieInteractor;
import com.dhytodev.popularmovie.data.repository.MovieInteractorImpl;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HomeActivity extends AppCompatActivity implements HomeFragment.Callback {

    @BindView(R.id.toolbar)
    Toolbar mToolbar ;

    private boolean twoPaneMode ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ButterKnife.bind(this);

        setToolbar();

        if (findViewById(R.id.movie_details_container) != null) {
            twoPaneMode = true ;
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

        } else {

        }
    }

    @Override
    public void onMovieClicked(Movie movie) {
        if (twoPaneMode) {

        } else {
            Toast.makeText(this, movie.getTitle(), Toast.LENGTH_SHORT).show();
        }
    }
}
