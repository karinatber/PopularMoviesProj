package com.example.autotests.popularmoviesapp.utils;

import android.net.Uri;
import android.util.Log;

import com.example.autotests.popularmoviesapp.BuildConfig;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;
/**
 * Created by karina.bernice on 28/08/2017.
 */

public class NetworkUtils {
    /** API Urls**/
    private static final String TAG = NetworkUtils.class.getSimpleName();
    private static final  String BASE_URL = "http://api.themoviedb.org/3/movie/";
    private static final  String BASE_IMAGE_URL = "http://image.tmdb.org/t/p/w185";
    private static String API_KEY_PARAM = "api_key";
    private static final String API_KEY = BuildConfig.API_KEY;


    public static URL buildUrl(String sortBy){
        String path = BASE_URL+sortBy;
        Uri buildUri = Uri.parse(path).buildUpon()
                    .appendQueryParameter(API_KEY_PARAM, API_KEY)
                    .build();

        URL url = null;
        try{
            url = new URL(buildUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        Log.d(TAG, "Built Url: "+ url);
        return url;
    }

    public static String getResponseFromHttpUrl(URL url) throws IOException{
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try{
            InputStream in = urlConnection.getInputStream();
            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");
            boolean hasInput = scanner.hasNext();
            if (hasInput){
                return scanner.next();
            } else{
                return null;
            }

        } finally{
            urlConnection.disconnect();
        }
    }

    public static URL buildImageUrl(String imgRelativePath){
        Uri imageUri = Uri.parse(BASE_IMAGE_URL+imgRelativePath).buildUpon().build();
        try{
            URL imageUrl = new URL(imageUri.toString());
            Log.d(TAG, "Built imageUrl: "+imageUrl);
            return imageUrl;
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return null;
        }
    }
}
