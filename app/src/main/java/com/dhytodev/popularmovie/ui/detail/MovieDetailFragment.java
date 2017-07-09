package com.dhytodev.popularmovie.ui.detail;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.dhytodev.popularmovie.R;
import com.dhytodev.popularmovie.data.model.Movie;
import com.dhytodev.popularmovie.ui.Constants;


import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * A simple {@link Fragment} subclass.
 */
public class MovieDetailFragment extends Fragment {

    @BindView(R.id.movie_poster)
    ImageView poster;
    @BindView(R.id.collapsing_toolbar)
    CollapsingToolbarLayout collapsingToolbar;
    @BindView(R.id.movie_name)
    TextView title;
    @BindView(R.id.movie_year)
    TextView releaseDate;
    @BindView(R.id.movie_rating)
    TextView rating;
    @BindView(R.id.movie_description)
    TextView overview;
    @Nullable @BindView(R.id.toolbar)
    Toolbar toolbar;

    private Movie movie ;


    public MovieDetailFragment() {
        // Required empty public constructor
    }

    public static MovieDetailFragment getInstance(@NonNull Movie movie) {
        Bundle args = new Bundle();
        args.putParcelable(Constants.MOVIE_DETAIL, movie);
        MovieDetailFragment movieDetailsFragment = new MovieDetailFragment();
        movieDetailsFragment.setArguments(args);
        return movieDetailsFragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_movie_details, container, false);
        ButterKnife.bind(this, rootView);
        setToolbar();
        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (getArguments() != null) {
            Movie movie = (Movie) getArguments().get(Constants.MOVIE_DETAIL);
            if (movie != null) {
                this.movie = movie;
                showDetails(movie);
            }
        }
    }

    public void showDetails(Movie movie) {
        Glide.with(getContext()).load(Constants.API_BACKDROP_PATH + movie.getBackdrop_path()).into(poster);
        title.setText(movie.getTitle());
        releaseDate.setText(String.format(getString(R.string.release_date), movie.getRelease_date()));
        rating.setText(String.format(getString(R.string.rating), String.valueOf(movie.getVote_average())));
        overview.setText(movie.getOverview());
    }

    private void setToolbar() {
        collapsingToolbar.setContentScrimColor(ContextCompat.getColor(getContext(), R.color.colorPrimary));
        collapsingToolbar.setTitle(getString(R.string.movie_details));
        collapsingToolbar.setCollapsedTitleTextAppearance(R.style.CollapsedToolbar);
        collapsingToolbar.setExpandedTitleTextAppearance(R.style.ExpandedToolbar);
        collapsingToolbar.setTitleEnabled(true);

        if (toolbar != null) {
            ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);

            ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
            if (actionBar != null) {
                actionBar.setDisplayHomeAsUpEnabled(true);
            }
        } else {
            // Don't inflate. Tablet is in landscape mode.
        }
    }

}
