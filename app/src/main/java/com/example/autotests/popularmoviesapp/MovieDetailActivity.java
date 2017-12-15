package com.example.autotests.popularmoviesapp;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.autotests.popularmoviesapp.data.FavoriteMoviesContract;
import com.example.autotests.popularmoviesapp.data.FavoriteMoviesDbHelper;
import com.example.autotests.popularmoviesapp.utils.NetworkUtils;
import com.example.autotests.popularmoviesapp.utils.ResultsItem;
import com.squareup.picasso.Picasso;

import java.net.URL;

public class MovieDetailActivity extends AppCompatActivity implements View.OnClickListener{
    public static final String TAG = MovieDetailActivity.class.getSimpleName();
    public ImageView mMoviePoster;
    public TextView mMovieTitle;
    public TextView mMovieOverview;
    public TextView mReleaseDate;
    public RatingBar mRatingMovie;
    public Button mBtnAddFav;
    SQLiteDatabase mDb;
    ResultsItem mMovieDetails;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);

        mMoviePoster = (ImageView) findViewById(R.id.iv_movie_detail_poster);
        mMovieTitle = (TextView) findViewById(R.id.tv_movies_detail_title);
        mMovieOverview = (TextView) findViewById(R.id.tv_movie_detail_overview);
        mReleaseDate = (TextView)findViewById(R.id.tv_release_date);
        mRatingMovie = (RatingBar)findViewById(R.id.rtb_movie_rating);
        mBtnAddFav = (Button) findViewById(R.id.bt_movie_details_add_fav);

        Intent intent = getIntent();
        mMovieDetails = intent.getParcelableExtra(MainActivity.EXTRA_MOVIE);
        mMovieTitle.setText(mMovieDetails.getTitle());
        mMovieOverview.setText(mMovieDetails.getOverview());
        String editedDate = formatDate(mMovieDetails.getReleaseDate());
        mReleaseDate.setText(editedDate);
        mRatingMovie.setRating(mMovieDetails.getVoteAverage()/2);
        mBtnAddFav.setOnClickListener(this);

        URL posterUrl = NetworkUtils.buildImageUrl(mMovieDetails.getPosterPath());
        Picasso.with(this).load(posterUrl.toString()).fit().into(mMoviePoster);
    }

    public String formatDate(String originalDate){
        String[] list = originalDate.split("-");
        String editedDate="";
        editedDate = list[1]+"/"+list[2]+"/"+list[0];
        return editedDate;
    }

    @Override
    public void onClick(View view) {
        FavoriteMoviesDbHelper dbHelper = new FavoriteMoviesDbHelper(this);
        mDb = dbHelper.getWritableDatabase();
        int id = view.getId();
        switch (id){
            case R.id.bt_movie_details_add_fav:
                addNewFavorite();
                break;
        }
    }
    private void addNewFavorite() {
        ContentValues contentValues = new ContentValues();
        contentValues.put(FavoriteMoviesContract.FavoritesEntry.COLUMN_TITLE, mMovieDetails.toString());
        contentValues.put(FavoriteMoviesContract.FavoritesEntry.COLUMN_DATE, mMovieDetails.getReleaseDate());
        Uri uri = getContentResolver().insert(FavoriteMoviesContract.FavoritesEntry.CONTENT_URI, contentValues);
        if (uri != null) {
            Log.v(MainActivity.class.getSimpleName(), "Favorite added to database, Uri: " + uri);
            Toast.makeText(this, mMovieDetails.getTitle()+"added to favorites", Toast.LENGTH_LONG).show();
        }
    }
    public void isMovieFavorite(){

    }
}
