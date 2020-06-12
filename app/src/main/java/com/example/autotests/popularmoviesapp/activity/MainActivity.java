package com.example.autotests.popularmoviesapp.activity;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.autotests.popularmoviesapp.R;
import com.example.autotests.popularmoviesapp.adapter.MoviesAdapter;
import com.example.autotests.popularmoviesapp.data.FavoriteMoviesContract;
import com.example.autotests.popularmoviesapp.data.FavoriteMoviesContract.FavoritesEntry;
import com.example.autotests.popularmoviesapp.interfaces.RequestDataHandlerInterface;
import com.example.autotests.popularmoviesapp.model.Movie;
import com.example.autotests.popularmoviesapp.task.RequestMoviesAsyncTask;
import com.example.autotests.popularmoviesapp.utils.DisplayUtils;
import com.example.autotests.popularmoviesapp.model.MoviesApiResult;
import com.example.autotests.popularmoviesapp.utils.NetworkUtils;
import com.google.gson.Gson;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements MoviesAdapter.MoviesAdapterOnClickHandler,
        LoaderManager.LoaderCallbacks<Cursor>, RequestDataHandlerInterface {
    private static final String TAG = MainActivity.class.getSimpleName();

    RecyclerView mMoviesRecyclerView;
    TextView mErrorMsgDisplay;
    TextView mNoFavesMsgDisplay;
    ProgressBar mLoadIndicator;
    SwipeRefreshLayout mSwipeRefreshLayout;
    LoaderManager mLoaderManager;
    DisplayUtils mDisplayUtils;

    public MoviesAdapter mAdapter;
    private static String mSortBy;
    public List<Movie> mMoviesList;

    private RequestMoviesAsyncTask mRequestMoviesAsyncTask;

    private static final int ID_FAVORITES_LOADER = 40;

    public static final String EXTRA_MOVIE = "Movie";
    public static final String MOVIES = "Movies";
    public static final String POPULARITY = "popular";
    public static final String TOP_RATED = "top_rated";
    public static final String FAVORITES = "favorites";
    public static final String SORT_BY = "sort_by";
    public static final int VERTICAL = 0;
    public static final int HORIZONTAL = 1;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mMoviesRecyclerView = (RecyclerView)findViewById(R.id.rv_movies_list);
        mErrorMsgDisplay = (TextView)findViewById(R.id.tv_error_message);
        mLoadIndicator = (ProgressBar)findViewById(R.id.pb_loading_indicator);
        mNoFavesMsgDisplay = (TextView)findViewById(R.id.tv_no_favorites_message);

        mLoaderManager = getSupportLoaderManager();
        mDisplayUtils = new DisplayUtils(getWindowManager(), this);
        mAdapter = new MoviesAdapter(this);

        mRequestMoviesAsyncTask = new RequestMoviesAsyncTask(this);
        GridLayoutManager manager;

        mSortBy = POPULARITY;

        if (savedInstanceState != null) {
            mMoviesList = savedInstanceState.getParcelableArrayList(MOVIES);
            mAdapter.setMovieData(mMoviesList);
            mSortBy = savedInstanceState.getString(SORT_BY);
            Log.d(TAG, "onCreate: savedInstanceState is NOT null");
            if (FAVORITES.equals(mSortBy)&&(mMoviesList.isEmpty())){
                mLoaderManager.initLoader(ID_FAVORITES_LOADER, null, this);
            }
        } else {
            Log.d(TAG, "onCreate: savedInstanceState is null");
            loadTMDBData(mSortBy);
        }

        int[] spanValues = mDisplayUtils.getSpanByDisplay();
        manager = new GridLayoutManager(this, spanValues[0]);

        mMoviesRecyclerView.hasFixedSize();
        mMoviesRecyclerView.setLayoutManager(manager);
        mMoviesRecyclerView.setAdapter(mAdapter);

        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swiperefresh);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (!FAVORITES.equals(mSortBy)) {
                    Log.i(TAG, "onRefresh: loadTMDBData");
                    loadTMDBData(mSortBy);
                } else {
                    Log.i(TAG, "onRefresh: loadFavorites");
                    loadFavorites();
                }
                mSwipeRefreshLayout.setRefreshing(false);
            }
        });

    }


    private void loadTMDBData(String sortBy) {
        showMoviesData();
        mLoadIndicator.setVisibility(View.VISIBLE);
        mSortBy = sortBy;
        mRequestMoviesAsyncTask.execute(sortBy);
    }

    private void loadFavorites(){
        showMoviesData();
        if(mLoaderManager.getLoader(ID_FAVORITES_LOADER) == null){
            mLoaderManager.initLoader(ID_FAVORITES_LOADER, null, this);
        } else {
            mLoaderManager.restartLoader(ID_FAVORITES_LOADER, null, this);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        Log.i(TAG,"onSaveInstanceState was called");
        if (mMoviesList != null){
            outState.putParcelableArrayList(MOVIES, (ArrayList<? extends Parcelable>) mMoviesList);
            outState.putString(SORT_BY, mSortBy);
            Log.d(TAG, "onSaveInstanceState -> Sort by: "+mSortBy);
            //outPersistentState.putString(SORT_BY, mSortBy);
        }
        super.onSaveInstanceState(outState);
    }

    /* make error message visible */
    public void showErrorMsg() {
        mMoviesRecyclerView.setVisibility(View.INVISIBLE);
        mErrorMsgDisplay.setVisibility(View.VISIBLE);
        mNoFavesMsgDisplay.setVisibility(View.INVISIBLE);
    }

    /* make recycler view with movies list visible */
    public void showMoviesData() {
        mMoviesRecyclerView.setVisibility(View.VISIBLE);
        mErrorMsgDisplay.setVisibility(View.INVISIBLE);
        mNoFavesMsgDisplay.setVisibility(View.INVISIBLE);
    }

    /* make no favorites message visible */
    public void showNoFavoritesMsg(){
        mNoFavesMsgDisplay.setVisibility(View.VISIBLE);
        mMoviesRecyclerView.setVisibility(View.INVISIBLE);
        mErrorMsgDisplay.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onClick(Movie movieDetails) {
        Intent intent = new Intent(this, MovieDetailActivity.class);
        intent.putExtra(EXTRA_MOVIE, movieDetails);
        startActivity(intent);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Log.i(TAG, "onCreateLoader was called.");
        mLoadIndicator.setVisibility(View.VISIBLE);
        mMoviesRecyclerView.setVisibility(View.INVISIBLE);

        switch(id){
            case ID_FAVORITES_LOADER:
                Uri queryUri = FavoritesEntry.CONTENT_URI;
                return new CursorLoader(this, queryUri, null, null, null, null);
            default:
                throw new RuntimeException("Loader not implemented: "+id);
        }
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        Log.i(TAG, "onLoadFinished was called");
        mLoadIndicator.setVisibility(View.INVISIBLE);
        List<Movie> favoritesList = new ArrayList<>();
        while (data.moveToNext()){
            Gson gson = new Gson();
            Movie movieItem = gson.fromJson(data.getString(data.getColumnIndex(FavoriteMoviesContract.FavoritesEntry.COLUMN_TITLE)), Movie.class);
            Log.i(TAG, "Movie Item name: " + movieItem.getTitle());
            favoritesList.add(movieItem);
        }
        if (favoritesList.isEmpty()){
            Log.i(TAG, "onLoadFinished: favoritesList is empty");
            showNoFavoritesMsg();
            return;
        }
        showMoviesData();
        mMoviesList = favoritesList;
        mAdapter.setMovieData(favoritesList);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        Log.i(TAG, "onLoaderReset was called");
        mAdapter.setMovieData(null);
    }

    @Override
    public void onRequestDataFinished(List<Movie> movies) {
        mLoadIndicator.setVisibility(View.INVISIBLE);
        if(movies == null || movies.isEmpty()) {
            showErrorMsg();
            return;
        }
        mMoviesList = movies;
        mAdapter.setMovieData(movies);
        showMoviesData();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        if (TOP_RATED.equals(mSortBy)){
            menu.getItem(1).setChecked(true);
        } else if (FAVORITES.equals(mSortBy)){
            menu.getItem(0).setChecked(true);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int actionId = item.getItemId();

        switch(actionId) {
            case R.id.option_popular:
                if (!item.isChecked()) {
                    loadTMDBData(POPULARITY);
                    item.setChecked(true);
                    mSortBy = POPULARITY;
                }
                return true;
            case R.id.option_top_rated:
                if (!item.isChecked()){
                    loadTMDBData(TOP_RATED);
                    item.setChecked(true);
                    mSortBy = TOP_RATED;
                }
                return true;
            case R.id.option_favorites:
                if(!item.isChecked()){
                    item.setChecked(true);
                    loadFavorites();
                    mSortBy = FAVORITES;
                }
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
