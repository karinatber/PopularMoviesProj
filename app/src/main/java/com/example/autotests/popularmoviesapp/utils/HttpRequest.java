package com.example.autotests.popularmoviesapp.utils;

import com.example.autotests.popularmoviesapp.model.MoviesApiResult;
import com.google.gson.Gson;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

public class HttpRequest {

    public <T> T get(URL url, Class<T> responseType) {
        try {
            String response = NetworkUtils.getResponseFromHttpUrl(url);
            return parseStringAsObject(response, responseType);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private <T> T parseStringAsObject(String string, Class<T> objectType) {
        Gson gson = new Gson();
        return gson.fromJson(string, objectType);
    }
}
