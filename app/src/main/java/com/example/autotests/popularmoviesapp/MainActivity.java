package com.example.autotests.popularmoviesapp;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
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
import android.view.Display;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.Surface;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.autotests.popularmoviesapp.data.FavoriteMoviesContract.FavoritesEntry;
import com.example.autotests.popularmoviesapp.utils.MoviesJson;
import com.example.autotests.popularmoviesapp.utils.NetworkUtils;
import com.example.autotests.popularmoviesapp.utils.ResultsItem;
import com.google.gson.Gson;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements MoviesAdapter.MoviesAdapterOnClickHandler, LoaderManager.LoaderCallbacks<Cursor> {
    private static final String TAG = MainActivity.class.getSimpleName();

    RecyclerView mMoviesRecyclerView;
    TextView mErrorMsgDisplay;
    ProgressBar mLoadIndicator;
    SwipeRefreshLayout mSwipeRefreshLayout;

    public MoviesAdapter mAdapter;
    private static String mSortBy;
    public List<ResultsItem> mMoviesList;
    private SQLiteDatabase mDb;

    private static final int ID_FAVORITES_LOADER = 40;
    private static final int ID_TMDB_LOADER = 41;

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
        GridLayoutManager manager;

        mAdapter = new MoviesAdapter(this, null);
        mSortBy = POPULARITY;

        if (savedInstanceState != null) {
            mMoviesList = savedInstanceState.getParcelableArrayList(MOVIES);
            mAdapter.setMovieData(mMoviesList);
            mSortBy = savedInstanceState.getString(SORT_BY);
            Log.d(TAG, "onCreate: savedInstanceState is NOT null");
            if (FAVORITES.equals(mSortBy)){
                loadFavorites();
            }
        } else {
            Log.d(TAG, "onCreate: savedInstanceState is null");
            loadTMDBData(mSortBy);
        }

        int orientation = getOrientation();
        if (orientation==VERTICAL) {
            manager = new GridLayoutManager(this, 2);
        } else {
            manager = new GridLayoutManager(this, 3);
        }

        mMoviesRecyclerView.hasFixedSize();

        mMoviesRecyclerView.setLayoutManager(manager);

        mMoviesRecyclerView.setAdapter(mAdapter);
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swiperefresh);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadTMDBData(mSortBy);
                mSwipeRefreshLayout.setRefreshing(false);
            }
        });

    }


    private void loadTMDBData(String sortBy) {
        showMoviesData();
        //mSortBy = sortBy;
        mLoadIndicator.setVisibility(View.VISIBLE);
        mMoviesRecyclerView.setVisibility(View.INVISIBLE);
        new RequestDataAsyncTask().execute(sortBy);
    }
    private void loadFavorites(){
        getSupportLoaderManager().initLoader(ID_FAVORITES_LOADER, null, this);
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
    }

    /* make recycler view with movies list visible */
    public void showMoviesData() {
        mMoviesRecyclerView.setVisibility(View.VISIBLE);
        mErrorMsgDisplay.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onClick(ResultsItem movieDetails) {
        Intent intent = new Intent(this, MovieDetailActivity.class);
        intent.putExtra(EXTRA_MOVIE, movieDetails);
        startActivity(intent);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {

        switch(id){
            case ID_TMDB_LOADER:
                break;
            case ID_FAVORITES_LOADER:
                Uri queryUri = FavoritesEntry.CONTENT_URI;
                return new CursorLoader(this, queryUri, null, null, null, null);
            default:
                throw new RuntimeException("Loader not implemented: "+id);
        }
        return null;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
            mAdapter.setFavoritesCursor(data);
            getSupportLoaderManager()
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mAdapter.setFavoritesCursor(null);
    }


    public class RequestDataAsyncTask extends AsyncTask<String, Void, List<ResultsItem>> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mLoadIndicator = (ProgressBar) findViewById(R.id.pb_loading_indicator);
            mLoadIndicator.setVisibility(View.VISIBLE);
        }

        @Override
        protected List<ResultsItem> doInBackground(String... strings) {
            String queryText = strings[0];
            String resultsAsJson;
            try {
                URL requestUrl = NetworkUtils.buildUrl(queryText);
                resultsAsJson = NetworkUtils.getResponseFromHttpUrl(requestUrl);
                Gson gson = new Gson();
                MoviesJson jsonAsObject = gson.fromJson(resultsAsJson, MoviesJson.class);
                List<ResultsItem> listOfMovies = jsonAsObject.getResults();
                return listOfMovies;
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(List<ResultsItem> listOfMovies) {
            super.onPostExecute(listOfMovies);
            mLoadIndicator.setVisibility(View.INVISIBLE);
            if (listOfMovies == null) {
                showErrorMsg();
            } else {
                showMoviesData();
                mAdapter.setMovieData(listOfMovies);
                mMoviesList = listOfMovies;
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        if (mSortBy == TOP_RATED){
            menu.getItem(0).setChecked(false);

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
    public int getOrientation(){
        Display display = getWindowManager().getDefaultDisplay();
        if (display.getRotation() == Surface.ROTATION_0 || display.getRotation() == Surface.ROTATION_180)
            return VERTICAL;
        else
            return HORIZONTAL;
    }
}
