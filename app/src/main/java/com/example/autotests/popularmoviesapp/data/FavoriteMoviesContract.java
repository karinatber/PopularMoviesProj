package com.example.autotests.popularmoviesapp.data;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by katanbern on 06/12/2017.
 */

public class FavoriteMoviesContract  {
    public static final String AUTHORITY = "com.example.autotests.popularmoviesapp";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + AUTHORITY);
    public static final String PATH_FAVORITES = "favorites";
    public static final int IS_FAVORITE = 1;
    public static final int NOT_FAVORITE = 0;
    public static final int TOP_RATED = 1;
    public static final int POPULAR = 0;

    public static final class FavoritesEntry implements BaseColumns {
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_FAVORITES).build();

        public static final String TABLE_NAME = "favorites";
        public static final String COLUMN_TITLE = "name";
        public static final String COLUMN_DATE = "date";
        public static final String COLUMN_IS_FAVORITE = "isFavorite";
        public static final String COLUMN_LABEL = "label";
    }
}
