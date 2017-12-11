package com.example.autotests.popularmoviesapp.data;

import android.provider.BaseColumns;

/**
 * Created by katanbern on 06/12/2017.
 */

public class FavoriteMoviesContract  {
    public static final class FavoritesEntry implements BaseColumns {
        public static final String TABLE_NAME = "favorites";
        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_DATE = "date";
        public static final String COLUMN_OVERVIEW = "overview";
        public static final String COLUMN_RATING = "rating";
        public static final String COLUMN_POSTER = "poster";
    }
}
