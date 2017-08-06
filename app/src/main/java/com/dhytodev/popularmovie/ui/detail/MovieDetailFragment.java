package com.dhytodev.popularmovie.ui.detail;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.dhytodev.popularmovie.R;
import com.dhytodev.popularmovie.data.model.Movie;
import com.dhytodev.popularmovie.data.model.Review;
import com.dhytodev.popularmovie.data.model.Trailer;
import com.dhytodev.popularmovie.data.network.TmdbServices;
import com.dhytodev.popularmovie.data.repository.MovieInteractor;
import com.dhytodev.popularmovie.data.repository.MovieInteractorImpl;
import com.dhytodev.popularmovie.ui.Constants;
import com.squareup.picasso.Picasso;


import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


/**
 * A simple {@link Fragment} subclass.
 */
public class MovieDetailFragment extends Fragment implements MovieDetailView, View.OnClickListener {

    @BindView(R.id.movie_backdrop)
    ImageView backdrop;
    @BindView(R.id.collapsing_toolbar)
    CollapsingToolbarLayout collapsingToolbar;
    @BindView(R.id.movie_poster)
    ImageView poster ;
    @BindView(R.id.movie_title)
    TextView title;
    @BindView(R.id.movie_release_date)
    TextView releaseDate;
    @BindView(R.id.movie_rating)
    TextView rating;
    @BindView(R.id.movie_description)
    TextView overview;
    @BindView(R.id.trailers_label)
    TextView trailersLabel;
    @BindView(R.id.trailers)
    LinearLayout trailers;
    @BindView(R.id.trailers_container)
    HorizontalScrollView trailersContainer ;
    @BindView(R.id.reviews_label)
    TextView reviewsLabel;
    @BindView(R.id.reviews)
    LinearLayout reviewsContainer;
    @BindView(R.id.favorite)
    FloatingActionButton buttonFavorite ;
    @Nullable @BindView(R.id.toolbar)
    Toolbar toolbar;

    private static final String TAG = MovieDetailFragment.class.getSimpleName() ;

    private TmdbServices services ;
    private MovieInteractor movieInteractor ;
    private MovieDetailPresenter presenter ;

    private Movie movie ;

    private boolean isFavorited = false ;


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

        services = TmdbServices.ServiceGenerator.instance();
        movieInteractor = new MovieInteractorImpl(services, getContext().getContentResolver());
        presenter = new MovieDetailPresenter(movieInteractor, this);

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

                presenter.getMovieTrailersAndReviews(movie.getId());
                presenter.isMovieFavorited(movie);
            }
        }
    }

    private void showDetails(Movie movie) {
        Glide.with(getContext()).load(Constants.API_BACKDROP_PATH + movie.getBackdropPath()).into(backdrop);
        Glide.with(getContext()).load(Constants.API_POSTER_PATH + movie.getPosterPath()).into(poster);
        title.setText(movie.getTitle());
        releaseDate.setText(String.format(getString(R.string.release_date), movie.getReleaseDate()));
        rating.setText(String.format(getString(R.string.rating), String.valueOf(movie.getVoteAverage())));
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

        }
    }

    @Override
    public void fetchTrailers(List<Trailer> trailers) {
        if (trailers.isEmpty()) {
            this.trailers.setVisibility(View.GONE);
            trailersLabel.setVisibility(View.GONE);
            trailersContainer.setVisibility(View.GONE);
        } else {
            this.trailers.setVisibility(View.VISIBLE);
            trailersLabel.setVisibility(View.VISIBLE);
            trailersContainer.setVisibility(View.VISIBLE);

            this.trailers.removeAllViews();

            for(Trailer trailer : trailers) {
                View thumbContainer = LayoutInflater.from(getContext()).inflate(R.layout.video, this.trailers, false);
                ImageView thumbView = ButterKnife.findById(thumbContainer, R.id.video_thumb);
                thumbView.setTag(trailer.getKey());
                thumbView.requestLayout();
                thumbView.setOnClickListener(this);
                Picasso.with(getContext())
                        .load(trailer.getImageVideoUrl())
                        .centerCrop()
                        .placeholder(R.color.colorPrimary)
                        .resizeDimen(R.dimen.video_width, R.dimen.video_height)
                        .into(thumbView);
                this.trailers.addView(thumbContainer);
            }
        }
    }

    @Override
    public void fetchReviews(List<Review> reviews) {
        if (reviews.isEmpty()) {
            reviewsLabel.setVisibility(View.GONE);
            reviewsContainer.setVisibility(View.GONE);
        } else {
            reviewsLabel.setVisibility(View.VISIBLE);
            reviewsContainer.setVisibility(View.VISIBLE);

            reviewsContainer.removeAllViews();

            for (Review review : reviews) {
                View reviewContainer = LayoutInflater.from(getContext()).inflate(R.layout.review, reviewsContainer, false);
                TextView reviewAuthor = ButterKnife.findById(reviewContainer, R.id.review_author);
                TextView reviewContent = ButterKnife.findById(reviewContainer, R.id.review_content);
                reviewAuthor.setText(review.getAuthor());
                reviewContent.setText(review.getContent());
                reviewContent.setOnClickListener(this);
                reviewsContainer.addView(reviewContainer);
            }
        }
    }

    @Override
    public void showError(String message) {
        //Toast.makeText(getContext(), "Err :" + message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void saveToFavorited() {
        isFavorited = true ;
        buttonFavorite.setImageResource(R.drawable.ic_favorite_white_24dp);
    }

    @Override
    public void isMovieFavorited() {
        isFavorited = true ;
        buttonFavorite.setImageResource(R.drawable.ic_favorite_white_24dp);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.video_thumb:
                onThumbnailClick(v);
                break;
            case R.id.review_content:
                onReviewClick((TextView) v);
                break;
        }
    }

    @OnClick(R.id.favorite)
    void onFabClick(){
        if (isFavorited) {
            presenter.deleteFavorite(movie);
            buttonFavorite.setImageResource(R.drawable.ic_favorite_border_white_24dp);
        } else  {
            presenter.saveMovieToFavorite(movie);
        }
    }

    private void onThumbnailClick(View view) {
        String key = view.getTag().toString();
        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.youtube.com/watch?v=" + key)));
    }

    private void onReviewClick(TextView view) {
        if (view.getMaxLines() == 5)
            view.setMaxLines(500);
        else
            view.setMaxLines(5);

    }
}
