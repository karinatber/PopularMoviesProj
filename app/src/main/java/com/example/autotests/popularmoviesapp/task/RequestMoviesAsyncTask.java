package com.example.autotests.popularmoviesapp.task;

import android.content.Context;
import android.os.AsyncTask;

import com.example.autotests.popularmoviesapp.interfaces.RequestDataHandlerInterface;
import com.example.autotests.popularmoviesapp.model.Movie;
import com.example.autotests.popularmoviesapp.model.MoviesApiResult;
import com.example.autotests.popularmoviesapp.utils.NetworkUtils;
import com.google.gson.Gson;

import java.net.URL;
import java.util.List;

public class RequestMoviesAsyncTask extends AsyncTask<String, Void, List<Movie>> {

    private RequestDataHandlerInterface requestDataHandlerInterface;

    public RequestMoviesAsyncTask(Context context){
        requestDataHandlerInterface = (RequestDataHandlerInterface) context;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected List<Movie> doInBackground(String... strings) {
        String queryText = strings[0];
        String resultsAsJson;
        try {
            URL requestUrl = NetworkUtils.buildUrl(queryText);
            resultsAsJson = NetworkUtils.getResponseFromHttpUrl(requestUrl);
            Gson gson = new Gson();
            MoviesApiResult jsonAsObject = gson.fromJson(resultsAsJson, MoviesApiResult.class);
            List<Movie> listOfMovies = jsonAsObject.getMovies();
            return listOfMovies;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    protected void onPostExecute(List<Movie> movies) {
        super.onPostExecute(movies);
        requestDataHandlerInterface.onRequestDataFinished(movies);
    }
}
