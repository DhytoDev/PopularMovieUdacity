package com.dhytodev.popularmovie.data.model;

import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;

import com.dhytodev.popularmovie.data.provider.TmdbContract;
import com.google.gson.annotations.SerializedName;

/**
 * Created by izadalab on 7/8/17.
 */

public class Movie implements Parcelable {
    @SerializedName("id")
    private int id;
    @SerializedName("title")
    private String title;
    @SerializedName("popularity")
    private float popularity;
    @SerializedName("poster_path")
    private String posterPath;
    @SerializedName("backdrop_path")
    private String backdropPath;
    @SerializedName("overview")
    private String overview;
    @SerializedName("release_date")
    private String releaseDate;
    @SerializedName("vote_count")
    private int voteCount;
    @SerializedName("vote_average")
    private float voteAverage;
    private boolean isFavMovie = false ;

    private Movie(Builder builder) {
        setId(builder.id);
        setTitle(builder.title);
        setPopularity(builder.popularity);
        setPosterPath(builder.poster_path);
        setBackdropPath(builder.backdrop_path);
        setOverview(builder.overview);
        setReleaseDate(builder.release_date);
        setVoteCount(builder.vote_count);
        setVoteAverage(builder.vote_average);
        setFavMovie(builder.isFavMovie);
    }

    public boolean isFavMovie() {
        return isFavMovie;
    }

    public void setFavMovie(boolean favMovie) {
        isFavMovie = favMovie;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public float getPopularity() {
        return popularity;
    }

    public void setPopularity(float popularity) {
        this.popularity = popularity;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }

    public String getBackdropPath() {
        return backdropPath;
    }

    public void setBackdropPath(String backdropPath) {
        this.backdropPath = backdropPath;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public int getVoteCount() {
        return voteCount;
    }

    public void setVoteCount(int voteCount) {
        this.voteCount = voteCount;
    }

    public float getVoteAverage() {
        return voteAverage;
    }

    public void setVoteAverage(float voteAverage) {
        this.voteAverage = voteAverage;
    }

    public Movie() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.title);
        dest.writeFloat(this.popularity);
        dest.writeString(this.posterPath);
        dest.writeString(this.backdropPath);
        dest.writeString(this.overview);
        dest.writeString(this.releaseDate);
        dest.writeInt(this.voteCount);
        dest.writeFloat(this.voteAverage);
        dest.writeByte(this.isFavMovie ? (byte) 1 : (byte) 0);
    }

    protected Movie(Parcel in) {
        this.id = in.readInt();
        this.title = in.readString();
        this.popularity = in.readFloat();
        this.posterPath = in.readString();
        this.backdropPath = in.readString();
        this.overview = in.readString();
        this.releaseDate = in.readString();
        this.voteCount = in.readInt();
        this.voteAverage = in.readFloat();
        this.isFavMovie = in.readByte() != 0;
    }

    public static final Creator<Movie> CREATOR = new Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel source) {
            return new Movie(source);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };

    private static final class Builder {
        private int id;
        private String title;
        private float popularity;
        private String poster_path;
        private String backdrop_path;
        private String overview;
        private String release_date;
        private int vote_count;
        private float vote_average;
        private boolean isFavMovie;

        public Builder() {
        }

        public Builder id(int val) {
            id = val;
            return this;
        }

        public Builder title(String val) {
            title = val;
            return this;
        }

        public Builder popularity(float val) {
            popularity = val;
            return this;
        }

        public Builder poster_path(String val) {
            poster_path = val;
            return this;
        }

        public Builder backdrop_path(String val) {
            backdrop_path = val;
            return this;
        }

        public Builder overview(String val) {
            overview = val;
            return this;
        }

        public Builder release_date(String val) {
            release_date = val;
            return this;
        }

        public Builder vote_count(int val) {
            vote_count = val;
            return this;
        }

        public Builder vote_average(float val) {
            vote_average = val;
            return this;
        }

        public Builder isFavMovie(boolean val) {
            isFavMovie = val;
            return this;
        }

        public Movie build() {
            return new Movie(this);
        }
    }

    public static Movie fromCursor(Cursor query) {
        return new Movie.Builder()
                .id(query.getInt(query.getColumnIndex(TmdbContract.MovieEntry.COLUMN_MOVIE_ID)))
                .title(query.getString(query.getColumnIndex(TmdbContract.MovieEntry.COLUMN_MOVIE_TITLE)))
                .overview(query.getString(query.getColumnIndex(TmdbContract.MovieEntry.COLUMN_MOVIE_OVERVIEW)))
                .poster_path(query.getString(query.getColumnIndex(TmdbContract.MovieEntry.COLUMN_MOVIE_POSTER_PATH)))
                .vote_average((float) query.getDouble(query.getColumnIndex(TmdbContract.MovieEntry.COLUMN_MOVIE_VOTE_AVERAGE)))
                .vote_count(query.getInt(query.getColumnIndex(TmdbContract.MovieEntry.COLUMN_MOVIE_VOTE_COUNT)))
                .release_date(query.getString(query.getColumnIndex(TmdbContract.MovieEntry.COLUMN_MOVIE_RELEASE_DATE)))
                .backdrop_path(query.getString(query.getColumnIndex(TmdbContract.MovieEntry.COLUMN_MOVIE_BACKDROP_PATH)))
                .isFavMovie(query.getInt(query.getColumnIndex(TmdbContract.MovieEntry.COLUMN_MOVIE_FAVORED)) > 0)
                .build();
    }
}
