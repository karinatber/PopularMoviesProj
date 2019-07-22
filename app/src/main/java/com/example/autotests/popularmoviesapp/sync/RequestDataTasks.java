package com.example.autotests.popularmoviesapp.sync;

import android.content.Context;

import com.example.autotests.popularmoviesapp.activity.MovieDetailActivity;
import com.example.autotests.popularmoviesapp.model.reviews.ReviewsApiResult;
import com.example.autotests.popularmoviesapp.model.videos.TrailersApiResult;
import com.example.autotests.popularmoviesapp.utils.NetworkUtils;
import com.example.autotests.popularmoviesapp.model.reviews.Review;
import com.example.autotests.popularmoviesapp.model.videos.Trailer;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.List;

/**
 * Created by karina.bernice on 12/01/2018.
 */

public class RequestDataTasks {
    public static final String ACTION_REQUEST_TRAILERS = "request-trailers";
    public static final String ACTION_REQUEST_REVIEWS = "request-reviews";
    public static final String ACTION_REQUESTS_FINISHED = "request-finished";
    public static final String MOVIE_ID = "movie-id";

    static List<Trailer> mTrailersList;
    static List<Review> mReviewsList;

    public static void executeTask(Context context, String action, int id){
        if (ACTION_REQUEST_TRAILERS.equals(action)){
            requestTrailers(context, id);
        } else if (ACTION_REQUEST_REVIEWS.equals(action)){
            requestReviews(context, id);
        }
    }

    private static void requestTrailers(Context context, int id){
        try {
            String jsonStr = NetworkUtils.getResponseFromHttpUrl(NetworkUtils.buildExtraURL(id, MovieDetailActivity.VIDEO));
            Gson gson = new Gson();
            TrailersApiResult trailers = gson.fromJson(jsonStr, TrailersApiResult.class);
            if (trailers != null){
                mTrailersList = trailers.getResults();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void requestReviews(Context context, int id){
        try{
            String jsonStr = NetworkUtils.getResponseFromHttpUrl(NetworkUtils.buildExtraURL(id, MovieDetailActivity.REVIEW));
            Gson gson = new Gson();
            ReviewsApiResult reviews = gson.fromJson(jsonStr, ReviewsApiResult.class);
            if (reviews != null){
                mReviewsList = reviews.getResults();
            }
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    public static List<Trailer> getTrailersList(){
        return mTrailersList;
    }

    public static List<Review> getReviewsList(){
        return mReviewsList;
    }
}
