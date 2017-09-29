package com.example.autotests.popularmoviesapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.autotests.popularmoviesapp.utils.NetworkUtils;
import com.example.autotests.popularmoviesapp.utils.ResultsItem;
import com.squareup.picasso.Picasso;

import java.net.URL;

public class MovieDetailActivity extends AppCompatActivity {
    public static final String TAG = MovieDetailActivity.class.getSimpleName();
    public ImageView mMoviePoster;
    public TextView mMovieTitle;
    public TextView mMovieOverview;
    public TextView mReleaseDate;
    public RatingBar mRatingMovie;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);

        mMoviePoster = (ImageView) findViewById(R.id.iv_movie_detail_poster);
        mMovieTitle = (TextView) findViewById(R.id.tv_movies_detail_title);
        mMovieOverview = (TextView) findViewById(R.id.tv_movie_detail_overview);
        mReleaseDate = (TextView)findViewById(R.id.tv_release_date);
        mRatingMovie = (RatingBar)findViewById(R.id.rtb_movie_rating);

        Intent intent = getIntent();
        ResultsItem movieDetails = intent.getParcelableExtra(MainActivity.EXTRA_MOVIE);
        mMovieTitle.setText(movieDetails.getTitle());
        mMovieOverview.setText(movieDetails.getOverview());
        String editedDate = formatDate(movieDetails.getReleaseDate());
        mReleaseDate.setText(editedDate);
        mRatingMovie.setRating(movieDetails.getVoteAverage()/2);

        URL posterUrl = NetworkUtils.buildImageUrl(movieDetails.getPosterPath());
        Picasso.with(this).load(posterUrl.toString()).fit().into(mMoviePoster);
    }

    public String formatDate(String originalDate){
        String[] list = originalDate.split("-");
        String editedDate="";
        editedDate = list[1]+"/"+list[2]+"/"+list[0];
        return editedDate;
    }
}
