package com.example.autotests.popularmoviesapp.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.autotests.popularmoviesapp.data.FavoriteMoviesContract.FavoritesEntry;
/**
 * Created by katanbern on 07/12/2017.
 */

public class FavoriteMoviesDbHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "favorites.db";
    private static final int DATABASE_VERSION = 1;
    public FavoriteMoviesDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        final String SQL_CREATE_FAVORITES_TABLE =
                "CREATE TABLE "+ FavoritesEntry.TABLE_NAME + " ("+
                        FavoritesEntry._ID + " INTEGER PRIMARY KEY AUTO INCREMENT, "+
                        FavoritesEntry.COLUMN_NAME + " TEXT NOT NULL, "+
                        FavoritesEntry.COLUMN_DATE + " INTEGER NOT NULL);";
//                        " UNIQUE (" + FavoritesEntry.COLUMN_NAME + ") ON CONFLICT REPLACE);";
        sqLiteDatabase.execSQL(SQL_CREATE_FAVORITES_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + FavoritesEntry.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
}