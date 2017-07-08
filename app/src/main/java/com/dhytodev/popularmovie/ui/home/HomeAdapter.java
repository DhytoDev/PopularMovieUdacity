package com.dhytodev.popularmovie.ui.home;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.dhytodev.popularmovie.R;
import com.dhytodev.popularmovie.data.model.Movie;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by izadalab on 7/9/17.
 */

public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.MovieViewHolder> {

    private List<Movie> movies ;
    private Context context;
    private HomeView view ;

    public HomeAdapter(List<Movie> movies, HomeView view) {
        this.movies = movies;
        this.view = view;
    }

    @Override
    public MovieViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        View rootView = LayoutInflater.from(context).inflate(R.layout.movie_grid_item, parent, false);

        return new MovieViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(MovieViewHolder holder, int position) {
        holder.itemView.setOnClickListener(holder);
        holder.movie = movies.get(position);
        holder.title.setText(holder.movie.getTitle());
        Glide.with(context).load("http://image.tmdb.org/t/p/w342/" + holder.movie
                .getPoster_path()).asBitmap()
                .diskCacheStrategy(DiskCacheStrategy.RESULT)
                .into(new BitmapImageViewTarget(holder.poster) {
                    @Override
                    public void onResourceReady(Bitmap bitmap, GlideAnimation anim) {
                        super.onResourceReady(bitmap, anim);

                        Palette.from(bitmap).generate(new Palette.PaletteAsyncListener() {
                            @Override
                            public void onGenerated(Palette palette) {
                                holder.titleBackground.setBackgroundColor(palette.getVibrantColor(context
                                        .getResources().getColor(R.color.black_translucent_60)));
                            }
                        });
                    }
                });
    }

    @Override
    public int getItemCount() {
        return movies.size();
    }

    public class MovieViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.movie_poster)
        ImageView poster;
        @BindView(R.id.title_background)
        View titleBackground;
        @BindView(R.id.movie_title)
        TextView title;

        public Movie movie;

        public MovieViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @Override
        public void onClick(View v) {
            HomeAdapter.this.view.onMovieClicked(movie);
        }
    }
}
