package com.example.autotests.popularmoviesapp.request;

import android.os.Build;

import androidx.annotation.RequiresApi;

import com.example.autotests.popularmoviesapp.model.Movie;
import com.example.autotests.popularmoviesapp.model.MoviesApiResult;
import com.example.autotests.popularmoviesapp.utils.HttpRequest;
import com.example.autotests.popularmoviesapp.utils.NetworkUtils;

import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;

public class MoviesRequest extends HttpRequest {

    @RequiresApi(api = Build.VERSION_CODES.N)
    public List<Movie> getMoviesList(String listType) { //listType can be "favorites" or "top_rated"
        URL url = NetworkUtils.buildUrl(listType);
        return Optional.of((new HttpRequest()).get(url, MoviesApiResult.class)).map(moviesApiResult -> moviesApiResult.getMovies()).orElse(null);
    }
}
