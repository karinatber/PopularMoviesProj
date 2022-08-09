package com.example.autotests.popularmoviesapp.request;

import android.os.Build;

import androidx.annotation.RequiresApi;

import com.example.autotests.popularmoviesapp.model.MoviesApiResult;
import com.example.autotests.popularmoviesapp.utils.HttpRequest;
import com.example.autotests.popularmoviesapp.utils.NetworkUtils;

import java.net.URL;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;

public class MoviesRequest extends HttpRequest {

    @RequiresApi(api = Build.VERSION_CODES.N)
    public CompletableFuture<MoviesApiResult> getMoviesList(String listType) { //listType can be "favorites" or "top_rated"
        URL url = NetworkUtils.buildUrl(listType);
        CompletableFuture<MoviesApiResult> cf = CompletableFuture.supplyAsync(
                () -> (new HttpRequest()).get(url, MoviesApiResult.class));
        return cf;
    }
}
