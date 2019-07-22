package com.example.autotests.popularmoviesapp.activity;

import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
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

import com.example.autotests.popularmoviesapp.R;
import com.example.autotests.popularmoviesapp.adapter.ReviewsAdapter;
import com.example.autotests.popularmoviesapp.adapter.TrailersAdapter;
import com.example.autotests.popularmoviesapp.data.FavoriteMoviesContract;
import com.example.autotests.popularmoviesapp.data.FavoriteMoviesDbHelper;
import com.example.autotests.popularmoviesapp.model.Movie;
import com.example.autotests.popularmoviesapp.sync.RequestDataIntentService;
import com.example.autotests.popularmoviesapp.sync.RequestDataTasks;
import com.example.autotests.popularmoviesapp.utils.NetworkUtils;
import com.example.autotests.popularmoviesapp.model.reviews.Review;
import com.example.autotests.popularmoviesapp.model.videos.Trailer;
import com.squareup.picasso.Picasso;

import java.net.URL;
import java.util.List;

public class MovieDetailActivity extends AppCompatActivity implements View.OnClickListener, TrailersAdapter.TrailerClickHandler{
    public static final String TAG = MovieDetailActivity.class.getSimpleName();
    public static final String VIDEO = "videos";
    public static final String REVIEW = "reviews";

    public ImageView mMoviePoster;
    public TextView mMovieTitle;
    public TextView mMovieOverview;
    public TextView mReleaseDate;
    public RatingBar mRatingMovie;
    public Button mBtnAddFav;
    public RecyclerView mTrailerRecyclerView;
    public RecyclerView mReviewRecyclerView;

    SQLiteDatabase mDb;
    Movie mMovieDetails;
    private boolean isFavorite;
    TrailersAdapter mTrailersAdapter;
    ReviewsAdapter mReviewsAdapter;
    RequestDataReceiver mReceiver;
    IntentFilter mRequestFilter;

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
        mReviewRecyclerView = (RecyclerView) findViewById(R.id.rv_review_list);

        mTrailersAdapter = new TrailersAdapter(this);
        mReviewsAdapter = new ReviewsAdapter();

        isFavorite = false;
        isMovieFavorite();

        mRequestFilter = new IntentFilter();
        mReceiver = new RequestDataReceiver();

        mRequestFilter.addAction(RequestDataTasks.ACTION_REQUESTS_FINISHED);


        mMovieTitle.setText(mMovieDetails.getTitle());
        mMovieOverview.setText(mMovieDetails.getOverview());
        String editedDate = mMovieDetails.getReleaseDate();
        mReleaseDate.setText(editedDate);
        mRatingMovie.setRating(mMovieDetails.getVoteAverage()/2);
        if(isFavorite){
            mBtnAddFav.setText(getString(R.string.remove_favorite));
            mBtnAddFav.setPressed(true);
        }
        mBtnAddFav.setOnClickListener(this);

        LinearLayoutManager manager = new LinearLayoutManager(this);
        LinearLayoutManager managerReview = new LinearLayoutManager(this);


        URL posterUrl = NetworkUtils.buildImageUrl(mMovieDetails.getPosterPath());
        Picasso.with(this).load(posterUrl.toString()).fit().into(mMoviePoster);

        loadTrailers();
        loadReviews();

        mReviewRecyclerView.setLayoutManager(managerReview);
        mReviewRecyclerView.setHasFixedSize(true);
        mReviewRecyclerView.setAdapter(mReviewsAdapter);

        mTrailerRecyclerView.setLayoutManager(manager);
        mTrailerRecyclerView.setAdapter(mTrailersAdapter);
    }

//    public String formatDate(String originalDate){
//        String[] list = originalDate.split("-");
//        String editedDate="";
//        editedDate = list[1]+"/"+list[2]+"/"+list[0];
//        return editedDate;
//    }

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(mReceiver, mRequestFilter);
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(mReceiver);
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
        contentValues.put(FavoriteMoviesContract.FavoritesEntry.COLUMN_ID, mMovieDetails.getId());
        Uri uri = getContentResolver().insert(FavoriteMoviesContract.FavoritesEntry.CONTENT_URI, contentValues);
        if (uri != null) {
            Log.v(MainActivity.class.getSimpleName(), "Favorite added to database, Uri: " + uri);
            Toast.makeText(this, mMovieDetails.getTitle()+" added to favorites", Toast.LENGTH_LONG).show();
        }
    }

    private void removeFavorite(){
        Uri queryUri = FavoriteMoviesContract.FavoritesEntry.CONTENT_URI;
        String where = FavoriteMoviesContract.FavoritesEntry.COLUMN_ID+"=?";
        String[] selectionArgs = new String[]{String.valueOf(mMovieDetails.getId())};
        getContentResolver().delete(queryUri, where, selectionArgs);
    }

    public void isMovieFavorite(){
        Uri queryUri = FavoriteMoviesContract.FavoritesEntry.CONTENT_URI;
        String selection = FavoriteMoviesContract.FavoritesEntry.COLUMN_ID+"=?";
        String[] selectionArgs = new String[]{String.valueOf(mMovieDetails.getId())};
        Cursor cursor = getContentResolver().query(queryUri, null, selection, selectionArgs, null);
        if ((cursor != null)&& (cursor.moveToFirst())){
            isFavorite = true;
        }
    }

    public void loadTrailers(){
        Intent intent = new Intent(this, RequestDataIntentService.class);
        intent.setAction(RequestDataTasks.ACTION_REQUEST_TRAILERS);
        intent.putExtra(RequestDataTasks.MOVIE_ID, mMovieDetails.getId());
        startService(intent);
    }

    public void loadReviews(){
        Intent intent = new Intent(this, RequestDataIntentService.class);
        intent.setAction(RequestDataTasks.ACTION_REQUEST_REVIEWS);
        intent.putExtra(RequestDataTasks.MOVIE_ID, mMovieDetails.getId());
        startService(intent);
    }
    public void updateContent(){
        List<Trailer> trailerList = RequestDataTasks.getTrailersList();
        List<Review> reviewList = RequestDataTasks.getReviewsList();
        if (trailerList != null){
            mTrailersAdapter.setTrailersList(trailerList);
            mTrailerRecyclerView.setVisibility(View.VISIBLE);
        }
        if (reviewList != null){
            mReviewsAdapter.setReviewData(reviewList);
            mReviewRecyclerView.setAdapter(mReviewsAdapter);
        }
    }

    /**TrailerClickHandler onClick method**/
    @Override
    public void onClickTrailer(Trailer trailer) {
        Uri youtubeUri = Uri.parse("vnd.youtube:"+trailer.getKey());
        Intent intent = new Intent(Intent.ACTION_VIEW, youtubeUri);
        Log.i(TAG, "onClick for Trailer item was called. Uri: "+youtubeUri);
        startActivity(intent);
    }


    private class RequestDataReceiver extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {
            updateContent();
        }
    }
}
