package com.example.autotests.popularmoviesapp.sync;

import android.os.Build;

import androidx.annotation.RequiresApi;

import com.example.autotests.popularmoviesapp.model.Movie;
import com.example.autotests.popularmoviesapp.request.MoviesRequest;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public class GetMoviesTask extends BaseTask{
    private String listType;

    public void setListType(String listType) {
        this.listType = listType;
    }

    @Override
    @RequiresApi(api = Build.VERSION_CODES.N)
    public CompletableFuture<List<Movie>> execute() {
        MoviesRequest request = new MoviesRequest();
        CompletableFuture<List<Movie>> cf = CompletableFuture.supplyAsync(() -> request.getMoviesList(listType));
        return cf;
    }
}
