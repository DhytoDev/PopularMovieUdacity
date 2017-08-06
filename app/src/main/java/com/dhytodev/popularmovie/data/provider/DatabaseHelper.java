package com.dhytodev.popularmovie.data.provider;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

import static com.dhytodev.popularmovie.data.provider.TmdbContract.MovieEntry.TABLE_NAME;

/**
 * Created by izadalab on 7/29/17.
 */

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "popularmovies.db";
    private static final int DB_VERSION = 1;
    
    private Context context ;

    public DatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        final String SQL_CREATE_MOVIE_TABLE = "CREATE TABLE " + TABLE_NAME + "("
                + BaseColumns._ID + " INTEGER NOT NULL PRIMARY KEY,"
                + TmdbContract.MovieEntry.COLUMN_MOVIE_ID + " TEXT NOT NULL,"
                + TmdbContract.MovieEntry.COLUMN_MOVIE_TITLE + " TEXT NOT NULL,"
                + TmdbContract.MovieEntry.COLUMN_MOVIE_OVERVIEW + " TEXT,"
                + TmdbContract.MovieEntry.COLUMN_MOVIE_VOTE_AVERAGE + " REAL,"
                + TmdbContract.MovieEntry.COLUMN_MOVIE_VOTE_COUNT + " INTEGER,"
                + TmdbContract.MovieEntry.COLUMN_MOVIE_BACKDROP_PATH + " TEXT,"
                + TmdbContract.MovieEntry.COLUMN_MOVIE_POSTER_PATH + " TEXT,"
                + TmdbContract.MovieEntry.COLUMN_MOVIE_RELEASE_DATE + " TEXT,"
                + TmdbContract.MovieEntry.COLUMN_MOVIE_FAVORED + " INTEGER NOT NULL DEFAULT 0,"
                + "UNIQUE (" + TmdbContract.MovieEntry.COLUMN_MOVIE_ID + ") ON CONFLICT REPLACE)";

        db.execSQL(SQL_CREATE_MOVIE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    void deleteDatabase() {
        context.deleteDatabase(DB_NAME);
    }
}
