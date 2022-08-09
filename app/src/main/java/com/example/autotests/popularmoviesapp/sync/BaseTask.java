package com.example.autotests.popularmoviesapp.sync;

import android.os.Build;

import androidx.annotation.RequiresApi;

import java.util.concurrent.CompletableFuture;

public abstract class BaseTask {
    public abstract <T> CompletableFuture<T> execute();
}
