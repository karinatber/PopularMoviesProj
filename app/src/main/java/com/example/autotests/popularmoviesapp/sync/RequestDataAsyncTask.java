package com.example.autotests.popularmoviesapp.sync;

import android.os.AsyncTask;
import android.view.View;
import android.widget.ProgressBar;

import com.example.autotests.popularmoviesapp.R;
import com.example.autotests.popularmoviesapp.model.Movie;
import com.example.autotests.popularmoviesapp.model.MoviesApiResult;
import com.example.autotests.popularmoviesapp.utils.HttpRequest;
import com.example.autotests.popularmoviesapp.utils.NetworkUtils;

import java.net.URL;
import java.util.List;

public class RequestDataAsyncTask extends AsyncTask<String, Void, List<Movie>> {

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
//        mLoadIndicator = (ProgressBar) findViewById(R.id.pb_loading_indicator);
//        mLoadIndicator.setVisibility(View.VISIBLE);
    }

    @Override
    protected List<Movie> doInBackground(String... strings) {
        String queryText = strings[0];
        String resultsAsJson;
        try {
            URL requestUrl = NetworkUtils.buildUrl(queryText);
            HttpRequest client = new HttpRequest();
            MoviesApiResult jsonAsObject = client.get(requestUrl, MoviesApiResult.class);

            List<Movie> listOfMovies = jsonAsObject.getMovies();
            return listOfMovies;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    protected void onPostExecute(List<Movie> listOfMovies) {
        super.onPostExecute(listOfMovies);
//        mLoadIndicator.setVisibility(View.INVISIBLE);
//        if (listOfMovies == null) {
//            showErrorMsg();
//        } else {
//            showMoviesData();
//            mAdapter.setMovieData(listOfMovies);
//            mMoviesList = listOfMovies;
//        }
    }

}
