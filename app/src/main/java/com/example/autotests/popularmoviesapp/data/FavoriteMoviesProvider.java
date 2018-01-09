package com.example.autotests.popularmoviesapp.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.Nullable;

/**
 * Created by karina.bernice on 13/12/2017.
 */

public class FavoriteMoviesProvider extends ContentProvider {
    private FavoriteMoviesDbHelper mDbHelper;

    public static final int FAVORITES = 100;
    public static final int FAVORITES_WITH_ID = 101;

    private static final UriMatcher sUriMatcher = buildUriMatcher();

    public static UriMatcher buildUriMatcher(){
        UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        //Add matches for:
        //directory
        uriMatcher.addURI(FavoriteMoviesContract.AUTHORITY, FavoriteMoviesContract.PATH_FAVORITES, FAVORITES);
        //single
        uriMatcher.addURI(FavoriteMoviesContract.AUTHORITY, FavoriteMoviesContract.PATH_FAVORITES+"/#", FAVORITES_WITH_ID);
        return uriMatcher;
    }

    @Override
    public boolean onCreate() {
        Context context = getContext();
        mDbHelper = new FavoriteMoviesDbHelper(context);
        return true;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String orderBy) {
        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        int match = sUriMatcher.match(uri);
        Cursor returnCursor;
        switch(match){
            case FAVORITES:
                returnCursor = db.query(
                        FavoriteMoviesContract.FavoritesEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        orderBy
                );
                break;
            case FAVORITES_WITH_ID:
                String id = uri.getPathSegments().get(1);
                String mSelection = "_id=?";
                String[] mSelectionArgs = new String[]{id};
                returnCursor = db.query(
                        FavoriteMoviesContract.FavoritesEntry.TABLE_NAME,
                        projection,
                        mSelection,
                        mSelectionArgs,
                        null,
                        null,
                        orderBy
                );
            default:
                throw new UnsupportedOperationException("Unknown uri: "+uri);
        }
        return returnCursor;
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues contentValues) {
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        int match = sUriMatcher.match(uri);
        Uri returnUri;
        switch(match){
            case FAVORITES:
                long id = db.insert(FavoriteMoviesContract.FavoritesEntry.TABLE_NAME, null, contentValues);
                if (id>0){
                    returnUri = ContentUris.withAppendedId(FavoriteMoviesContract.FavoritesEntry.CONTENT_URI, id);
                } else {
                    throw new android.database.SQLException("failed to insert new row into "+uri);
                }
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return returnUri;
    }

    @Override
    public int delete(Uri uri, String s, String[] strings) {
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        int match = sUriMatcher.match(uri);
        int rowsDeleted;
        switch (match){
            case FAVORITES:
                rowsDeleted = db.delete(FavoriteMoviesContract.FavoritesEntry.TABLE_NAME, s, strings);
                break;
            case FAVORITES_WITH_ID:
                String id = uri.getPathSegments().get(1);
                String mSelection = "_id=?";
                String[] mSelectionArgs = new String[]{id};
                rowsDeleted = db.delete(FavoriteMoviesContract.FavoritesEntry.TABLE_NAME, mSelection, mSelectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri in delete method: "+uri);
        }
        if (rowsDeleted>0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsDeleted;
    }

    @Override
    public int update(Uri uri, ContentValues contentValues, String s, String[] strings) {
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        int match = sUriMatcher.match(uri);
        int rowsUpdated = 0;
        switch (match){
            case FAVORITES:
                rowsUpdated = db.update(FavoriteMoviesContract.FavoritesEntry.TABLE_NAME, contentValues, s, strings);
                break;
            case FAVORITES_WITH_ID:
                String id = uri.getPathSegments().get(1);
                String mSelection = "_id=?";
                String[] mSelectionArgs = new String[]{id};
                rowsUpdated = db.update(FavoriteMoviesContract.FavoritesEntry.TABLE_NAME, contentValues, mSelection, mSelectionArgs);
                break;
        }
        return rowsUpdated;
    }

    @Override
    public int bulkInsert(Uri uri, ContentValues[] values) {
        final SQLiteDatabase db = mDbHelper.getWritableDatabase();
        int match = sUriMatcher.match(uri);
        switch(match){
            case FAVORITES:
                db.beginTransaction();
                int rowsInserted = 0;
                try{
                    for (ContentValues cv : values){
                        long _rows = db.insert(FavoriteMoviesContract.FavoritesEntry.TABLE_NAME, null, cv);
                        if (_rows != -1){
                            rowsInserted++;
                        }
                    }
                    db.setTransactionSuccessful();
                } finally{
                    db.endTransaction();
                }
                if (rowsInserted >0){
                    getContext().getContentResolver().notifyChange(uri, null);
                }
                return rowsInserted;
            default:
                return super.bulkInsert(uri, values);
        }
    }
}
