package com.example.autotests.popularmoviesapp.sync;

import com.example.autotests.popularmoviesapp.activity.MovieDetailActivity;
import com.example.autotests.popularmoviesapp.model.Movie;
import com.example.autotests.popularmoviesapp.model.MoviesApiResult;
import com.example.autotests.popularmoviesapp.model.reviews.ReviewsApiResult;
import com.example.autotests.popularmoviesapp.model.videos.TrailersApiResult;
import com.example.autotests.popularmoviesapp.utils.NetworkUtils;
import com.example.autotests.popularmoviesapp.model.reviews.Review;
import com.example.autotests.popularmoviesapp.model.videos.Trailer;
import com.google.gson.Gson;

import java.io.IOException;
import java.net.URL;
import java.util.List;

/**
 * Created by karina.bernice on 12/01/2018.
 */

public class RequestDataTasks {
    public static final String ACTION_REQUEST_TRAILERS = "request-trailers";
    public static final String ACTION_REQUEST_REVIEWS = "request-reviews";
    public static final String ACTION_REQUEST_MOVIES = "request-movies";
    public static final String ACTION_REQUESTS_FINISHED = "request-finished";
    public static final String REQUEST_ID ="request-id";
    public static final String REQUEST_PARAM ="request-param";
    public static final String MOVIE_ID = "movie-id";
    public static final String REQUEST_DATA_EXTRA = "request-data";

    static List<Trailer> mTrailersList;
    static List<Review> mReviewsList;

    public static List<Trailer> requestTrailers(String id){
        try {
            String jsonStr = NetworkUtils.getResponseFromHttpUrl(NetworkUtils.buildExtraURL(id, MovieDetailActivity.VIDEO));
            Gson gson = new Gson();
            TrailersApiResult trailers = gson.fromJson(jsonStr, TrailersApiResult.class);
            if (trailers != null){
                mTrailersList = trailers.getResults();
                return mTrailersList;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static List<Review> requestReviews(String id){
        try{
            String jsonStr = NetworkUtils.getResponseFromHttpUrl(NetworkUtils.buildExtraURL(id, MovieDetailActivity.REVIEW));
            Gson gson = new Gson();
            ReviewsApiResult reviews = gson.fromJson(jsonStr, ReviewsApiResult.class);
            if (reviews != null){
                mReviewsList = reviews.getResults();
                return mReviewsList;
            }
        } catch (IOException e){
            e.printStackTrace();
        }
        return null;
    }

    public static List<Movie> requestMovies(String queryText) {
        try {
            URL requestUrl = NetworkUtils.buildUrl(queryText);
            String resultsAsJson = NetworkUtils.getResponseFromHttpUrl(requestUrl);
            Gson gson = new Gson();
            MoviesApiResult jsonAsObject = gson.fromJson(resultsAsJson, MoviesApiResult.class);
            List<Movie> listOfMovies = jsonAsObject.getMovies();
            return listOfMovies;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static List<Trailer> getTrailersList(){
        return mTrailersList;
    }

    public static List<Review> getReviewsList(){
        return mReviewsList;
    }
}
