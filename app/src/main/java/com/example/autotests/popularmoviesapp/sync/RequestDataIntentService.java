package com.example.autotests.popularmoviesapp.sync;

import android.app.IntentService;
import android.content.Intent;
import android.os.Parcelable;

import com.example.autotests.popularmoviesapp.model.Movie;
import com.example.autotests.popularmoviesapp.model.reviews.Review;
import com.example.autotests.popularmoviesapp.model.videos.Trailer;

import java.util.List;

/**
 * Created by karina.bernice on 12/01/2018.
 */

public class RequestDataIntentService extends IntentService {

    public RequestDataIntentService(){
        super ("RequestDataIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        sendBroadcast(generateResultIntent(intent));
    }

    private Intent generateResultIntent(Intent intent) {
        String action = intent.getAction();
        String param = intent.getStringExtra(RequestDataTasks.REQUEST_PARAM);
        Intent broadcastIntent = new Intent(RequestDataTasks.ACTION_REQUESTS_FINISHED);
        broadcastIntent.putExtra(RequestDataTasks.REQUEST_ID, action);

        if(action == RequestDataTasks.ACTION_REQUEST_REVIEWS) {
            List<Review> reviews = RequestDataTasks.requestReviews(param);
            broadcastIntent.putExtra(RequestDataTasks.REQUEST_DATA_EXTRA, (Parcelable) reviews);
        } else if(action == RequestDataTasks.ACTION_REQUEST_TRAILERS) {
            List<Trailer> trailers = RequestDataTasks.requestTrailers(param);
            broadcastIntent.putExtra(RequestDataTasks.REQUEST_DATA_EXTRA, (Parcelable) trailers);
        } else if(action == RequestDataTasks.ACTION_REQUEST_MOVIES) {
            List<Movie> movies = RequestDataTasks.requestMovies(param);
            broadcastIntent.putExtra(RequestDataTasks.REQUEST_DATA_EXTRA, (Parcelable) movies);
        }

        return broadcastIntent;
    }
}
