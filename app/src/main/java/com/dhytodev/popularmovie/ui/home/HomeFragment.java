package com.dhytodev.popularmovie.ui.home;


import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.dhytodev.popularmovie.R;
import com.dhytodev.popularmovie.data.model.Movie;
import com.dhytodev.popularmovie.data.network.TmdbServices;
import com.dhytodev.popularmovie.data.repository.MovieRepository;
import com.dhytodev.popularmovie.data.repository.MovieRepositoryImpl;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment implements HomeView, SwipeRefreshLayout.OnRefreshListener {

    private static final String TAG = HomeFragment.class.getSimpleName();
    public static final int POPULAR = 0;
    public static final int TOP_RATED = 1;

    private TmdbServices services ;
    private MovieRepository movieRepository;
    private HomePresenter presenter ;
    private HomeAdapter adapter ;
    private List<Movie> movies = new ArrayList<>() ;

    private Callback callback ;
    private int selectedSort = 0 ;

    @BindView(R.id.rv_list_movies)
    RecyclerView listMovies ;
    @BindView(R.id.tv_error)
    TextView errorText ;
    @BindView(R.id.loading_progress)
    ProgressBar loading ;
    @BindView(R.id.refresh)
    SwipeRefreshLayout refresh ;


    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        callback = (Callback) context;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        setRetainInstance(true);

        services = TmdbServices.ServiceGenerator.instance();

        movieRepository = new MovieRepositoryImpl(services, getContext().getContentResolver());

        presenter = new HomePresenter(movieRepository, this);
    }

    @Override
    public void onResume() {
        super.onResume();
        presenter.attachView();
    }

    @Override
    public void onStop() {
        super.onStop();
        presenter.detachView();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_movies, container, false);
        ButterKnife.bind(this, rootView);

        initRecyclerView();

        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        presenter.fetchMovies(POPULAR);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_sort_by_popular:
                selectedSort = POPULAR ;
                presenter.fetchMovies(POPULAR);
                break;
            case R.id.menu_sort_by_toprated:
                selectedSort = TOP_RATED ;
                presenter.fetchMovies(TOP_RATED);
                break;
            case R.id.menu_sort_by_favorited:
                presenter.getFavoriteMovie();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void showLoading(boolean showLoading) {
        errorText.setVisibility(View.GONE);
        if (showLoading) {
            listMovies.setVisibility(View.VISIBLE);
            loading.setVisibility(View.VISIBLE);
            refresh.setVisibility(View.GONE);
        } else {
            loading.setVisibility(View.GONE);
            refresh.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void fetchMovies(List<Movie> movies) {
        this.movies.clear();
        this.movies.addAll(movies);
        listMovies.setVisibility(View.VISIBLE);
        adapter.notifyDataSetChanged();
        callback.onMoviesLoaded(movies.get(0));

    }

    @Override
    public void showError(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onMovieClicked(Movie movie) {
        callback.onMovieClicked(movie);
    }

    private void initRecyclerView() {
        listMovies.setHasFixedSize(true);

        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getContext(), numberOfColumns());
        listMovies.setLayoutManager(layoutManager);

        adapter = new HomeAdapter(movies, this);
        listMovies.setAdapter(adapter);

        refresh.setOnRefreshListener(this);
    }

    private int numberOfColumns() {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        // You can change this divider to adjust the size of the poster
        int widthDivider = 500;
        int width = displayMetrics.widthPixels;
        int nColumns = width / widthDivider;

        Log.d("columns", String.valueOf(nColumns));

        if (nColumns < 2) return 2;

        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            return 2 ;
        } else {
            return nColumns;
        }

    }

    @Override
    public void onRefresh() {
        refresh.setRefreshing(false);
        presenter.fetchMovies(selectedSort);
    }

    public interface Callback {
        void onMoviesLoaded(Movie movie);

        void onMovieClicked(Movie movie);
    }
}
