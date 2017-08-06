package com.dhytodev.popularmovie.data.provider;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import static com.dhytodev.popularmovie.data.provider.TmdbContract.BASE_CONTENT_URI;
import static com.dhytodev.popularmovie.data.provider.TmdbContract.CONTENT_AUTHORITY;

/**
 * Created by izadalab on 7/29/17.
 */

public class TmdbProvider extends ContentProvider {

    private static final int MOVIES = 100;
    private static final int MOVIES_ID = 101;
    private static final String MOVIES_PATH = "movies";
    private static final String MOVIES_ID_PATH = "movies/*";
    private static final UriMatcher URI_MATCHER = buildUriMatcher();

    private DatabaseHelper dbHelper ;

    private static UriMatcher buildUriMatcher() {
        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = CONTENT_AUTHORITY;

        matcher.addURI(authority, MOVIES_PATH, MOVIES);
        matcher.addURI(authority, MOVIES_ID_PATH, MOVIES_ID);

        return matcher;
    }

    @Override
    public boolean onCreate() {
        dbHelper = new DatabaseHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection,
                        @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        final SQLiteDatabase db = dbHelper.getWritableDatabase();
        final int match = URI_MATCHER.match(uri);

        Cursor query ;

        switch (match) {
            case MOVIES:
                query = db.query(TmdbContract.MovieEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);

                break ;
            default:
                throw new UnsupportedOperationException("Unknown insert uri " + uri);
        }

        query.setNotificationUri(getContext().getContentResolver(), uri);
        return query;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        final SQLiteDatabase db = dbHelper.getWritableDatabase();
        final int match = URI_MATCHER.match(uri);

        Uri insertUri ;

        switch (match) {
            case MOVIES:
                long id = db.insertOrThrow(TmdbContract.MovieEntry.TABLE_NAME, null, values);
                insertUri = TmdbContract.MovieEntry.buildMovieUri(String.valueOf(id));

                 break;

            default:
                throw new UnsupportedOperationException("Unknown insert uri " + uri);
        }
        notifyResolver(uri);
        return insertUri ;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {

        if (uri.equals(BASE_CONTENT_URI)) {
            dbHelper.close();
            dbHelper.deleteDatabase();
            dbHelper = new DatabaseHelper(getContext());
            return 1;
        }

        final SQLiteDatabase db = dbHelper.getWritableDatabase();
        final int match = URI_MATCHER.match(uri);

        int retrieveValue ;

        switch (match) {
            case MOVIES:
                retrieveValue = db.delete(TmdbContract.MovieEntry.TABLE_NAME, selection, selectionArgs);
                break;

            default:
                throw new UnsupportedOperationException("Unknown insert uri " + uri);
        }
        notifyResolver(uri);
        return retrieveValue ;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        final SQLiteDatabase db = dbHelper.getWritableDatabase();
        final int match = URI_MATCHER.match(uri);

        int retrieveValue ;

        switch (match) {
            case MOVIES:
                retrieveValue = db.update(TmdbContract.MovieEntry.TABLE_NAME, values, selection, selectionArgs);
                break;

            default:
                throw new UnsupportedOperationException("Unknown insert uri " + uri);
        }
        notifyResolver(uri);
        return retrieveValue ;
    }

    private void notifyResolver(Uri uri) {
        getContext().getContentResolver().notifyChange(uri, null);
    }
}
