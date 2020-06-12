package com.example.autotests.popularmoviesapp.interfaces;

import android.os.Parcelable;

import com.example.autotests.popularmoviesapp.model.Movie;

import java.util.ArrayList;
import java.util.List;

public interface RequestDataHandlerInterface {

    void onRequestDataFinished(List<Movie> movies);
    void onDataReady(String actionId, ArrayList<Parcelable> data);
}
