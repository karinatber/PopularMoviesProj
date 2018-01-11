package com.example.autotests.popularmoviesapp;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.autotests.popularmoviesapp.adapter.TrailersAdapter;
import com.example.autotests.popularmoviesapp.data.FavoriteMoviesContract;
import com.example.autotests.popularmoviesapp.data.FavoriteMoviesDbHelper;
import com.example.autotests.popularmoviesapp.utils.NetworkUtils;
import com.example.autotests.popularmoviesapp.utils.ResultsItem;
import com.example.autotests.popularmoviesapp.utils.reviews.ReviewsResultsItem;
import com.example.autotests.popularmoviesapp.utils.videos.TrailersJson;
import com.example.autotests.popularmoviesapp.utils.videos.VideoResultsItem;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.net.URL;
import java.util.HashMap;
import java.util.List;

public class MovieDetailActivity extends AppCompatActivity implements View.OnClickListener, TrailersAdapter.TrailerClickHandler{
    public static final String TAG = MovieDetailActivity.class.getSimpleName();
    public static final String VIDEO = "videos";
    public static final String REVIEW = "review";

    public ImageView mMoviePoster;
    public TextView mMovieTitle;
    public TextView mMovieOverview;
    public TextView mReleaseDate;
    public RatingBar mRatingMovie;
    public Button mBtnAddFav;
    public RecyclerView mTrailerRecyclerView;

    SQLiteDatabase mDb;
    ResultsItem mMovieDetails;
    VideoResultsItem mTrailersList;
    ReviewsResultsItem mReviewsList;
    private boolean isFavorite;
    TrailersAdapter mTrailersAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);

        Intent intent = getIntent();
        mMovieDetails = intent.getParcelableExtra(MainActivity.EXTRA_MOVIE);

        mMoviePoster = (ImageView) findViewById(R.id.iv_movie_detail_poster);
        mMovieTitle = (TextView) findViewById(R.id.tv_movies_detail_title);
        mMovieOverview = (TextView) findViewById(R.id.tv_movie_detail_overview);
        mReleaseDate = (TextView)findViewById(R.id.tv_release_date);
        mRatingMovie = (RatingBar)findViewById(R.id.rtb_movie_rating);
        mBtnAddFav = (Button) findViewById(R.id.bt_movie_details_add_fav);
        mTrailerRecyclerView = (RecyclerView)findViewById(R.id.rv_trailer_list);
        mTrailersAdapter = new TrailersAdapter(this);

        isFavorite = false;
        isMovieFavorite();

        mMovieTitle.setText(mMovieDetails.getTitle());
        mMovieOverview.setText(mMovieDetails.getOverview());
        String editedDate = formatDate(mMovieDetails.getReleaseDate());
        mReleaseDate.setText(editedDate);
        mRatingMovie.setRating(mMovieDetails.getVoteAverage()/2);
        if(isFavorite){
            mBtnAddFav.setText(getString(R.string.remove_favorite));
            mBtnAddFav.setPressed(true);
        }
        mBtnAddFav.setOnClickListener(this);

        LinearLayoutManager manager = new LinearLayoutManager(this);

        URL posterUrl = NetworkUtils.buildImageUrl(mMovieDetails.getPosterPath());
        Picasso.with(this).load(posterUrl.toString()).fit().into(mMoviePoster);

        mTrailerRecyclerView.setLayoutManager(manager);
        mTrailerRecyclerView.setAdapter(mTrailersAdapter);
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
                if (isFavorite) {
                    removeFavorite();
                    isFavorite = false;
                    mBtnAddFav.setPressed(false);
                    mBtnAddFav.setText(getString(R.string.add_to_favorite));
                }
                else {
                    addNewFavorite();
                    isFavorite = true;
                    mBtnAddFav.setPressed(true);
                    mBtnAddFav.setText(getString(R.string.remove_favorite));
                }
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
            Toast.makeText(this, mMovieDetails.getTitle()+" added to favorites", Toast.LENGTH_LONG).show();
        }
    }
    private void removeFavorite(){
        Uri queryUri = FavoriteMoviesContract.FavoritesEntry.CONTENT_URI;
        String where = FavoriteMoviesContract.FavoritesEntry.COLUMN_TITLE+"=?";
        String[] selectionArgs = new String[]{mMovieDetails.toString()};
        getContentResolver().delete(queryUri, where, selectionArgs);
    }
    public void isMovieFavorite(){
        Uri queryUri = FavoriteMoviesContract.FavoritesEntry.CONTENT_URI;
        String selection = FavoriteMoviesContract.FavoritesEntry.COLUMN_TITLE+"=?";
        String[] selesctionArgs = new String[]{mMovieDetails.toString()};
        Cursor cursor = getContentResolver().query(queryUri, null, selection, selesctionArgs, null);
        if ((cursor != null)&& (cursor.moveToFirst())){
            isFavorite = true;
        }
    }

    /**TrailerClickHandler onClick method**/
    @Override
    public void onClick(VideoResultsItem trailer) {
        String youtubeAuthority = "com.google.android.youtube";
    }

    public class RequestVideoAndReviewAsyncTask extends AsyncTask<String, Void, HashMap<String,List<Object>>>{
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected HashMap<String, List<Object>> doInBackground(String... strings) {
            HashMap<String, List<Object>> results = new HashMap<>();
            try{
                int id = mMovieDetails.getId();
                Gson gson = new Gson();
                String videoJson = NetworkUtils.getResponseFromHttpUrl(NetworkUtils.buildExtraURL(id, VIDEO));

                List<VideoResultsItem> trailers = gson.fromJson(videoJson, TrailersJson.class).getResults();

                return results;
            } catch (Exception e){
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(HashMap<String, List<Object>> videosAndReviews) {
            super.onPostExecute(videosAndReviews);
        }
    }
}
