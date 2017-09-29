package com.example.autotests.popularmoviesapp.utils;

import android.util.Log;

import com.example.autotests.popularmoviesapp.MainActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URL;

/**
 * Created by karina.bernice on 29/08/2017.
 */

public class TMDBJsonUtils {
    public final static String TAG = TMDBJsonUtils.class.getSimpleName();

    public static String[] getSimpleMovieListFromJson(Class<MainActivity> context, String formatJsonString) throws JSONException{
        //TODO - organize query parameters
        /* title of the movie*/
        final String TMDB_TITLE = "title";
        /* This is the original movie title*/
        final String TMDB_ORIGINAL_TITLE = "original_title";
        final String TMDB_POSTER = "poster_path";
        /* Error codes*/

        /* No results found message*/
        final String notFoundMessage = "No results were found";

        if (null == formatJsonString){
            return null;
        }

        /* List that contains info of each movie*/
        String[] moviesList;

        JSONObject moviesJson = new JSONObject(formatJsonString);

        /* If there is an error with the http request*/
        if (moviesJson.has("status_code")){
            int errorCode = moviesJson.getInt("status_code");
            switch(errorCode){
                case 7:
                    /* Invalid API*/
                    return null;
                case 34:
                    /* Page not found */
                    return null;
                default:
                    return null;
            }
        }

        JSONArray results = moviesJson.getJSONArray("results");
        moviesList = new String[results.length()];

        for (int i=0; i < results.length(); i++){
            JSONObject movie = results.getJSONObject(i);
            /* Information about the movie */
            String title = movie.getString(TMDB_TITLE);
            Double popularity = movie.getDouble("popularity");
            String imgRelPath = movie.getString(TMDB_POSTER);
            URL imageUrl = NetworkUtils.buildImageUrl(imgRelPath);

            moviesList[i] = title + "\n\npopularity: " + popularity + "imageUrl: " + imageUrl;
            Log.d(TAG, "moviesList["+i+"]: "+moviesList[i]);

        }
        return moviesList;
    }

    public static String getImgUrl(String movieData){
        String imgUrl = movieData.substring(movieData.indexOf("imageUrl:")+9);
        return imgUrl;
    }
}
