package com.example.autotests.popularmoviesapp;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.os.PersistableBundle;
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

import com.example.autotests.popularmoviesapp.utils.MoviesJson;
import com.example.autotests.popularmoviesapp.utils.NetworkUtils;
import com.example.autotests.popularmoviesapp.utils.ResultsItem;
import com.google.gson.Gson;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements MoviesAdapter.MoviesAdapterOnClickHandler {
    private static final String TAG = MainActivity.class.getSimpleName();

    RecyclerView mMoviesRecyclerView;
    TextView mErrorMsgDisplay;
    ProgressBar mLoadIndicator;
    SwipeRefreshLayout mSwipeRefreshLayout;

    public MoviesAdapter mAdapter;
    private static String mSortBy;
    public List<ResultsItem> mMoviesList;

    public static final String EXTRA_MOVIE = "Movie";
    public static final String MOVIES = "Movies";
    public static final String POPULARITY = "popular";
    public static final String TOP_RATED = "top_rated";
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

        mAdapter = new MoviesAdapter(this);

        if (savedInstanceState != null) {
            mMoviesList = savedInstanceState.getParcelableArrayList(MOVIES);
            mAdapter.setMovieData(mMoviesList);
            Log.d(TAG, "onCreate: savedInstanceState is NOT null");
        } else {
            Log.d(TAG, "onCreate: savedInstanceState is null");
            loadTMDBData(POPULARITY);
        }

        int orientation = getOrientation(this);
        if (orientation==VERTICAL) {
            manager = new GridLayoutManager(this, 2);
        } else {
            manager = new GridLayoutManager(this, 3);
        }
       /* LayoutParams lp = manager.generateDefaultLayoutParams();
        lp.setMargins(10, 10, 10, 10);
        manager.generateLayoutParams(lp);
*/
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
        mSortBy = sortBy;
        mLoadIndicator.setVisibility(View.VISIBLE);
        mMoviesRecyclerView.setVisibility(View.INVISIBLE);
        new RequestDataAsyncTask().execute(sortBy);
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        if (mMoviesList != null){
            outState.putParcelableArrayList(MOVIES, (ArrayList<? extends Parcelable>) mMoviesList);
            //outPersistentState.putString(SORT_BY, mSortBy);
        }
        super.onSaveInstanceState(outState, outPersistentState);
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
                }
                return true;
            case R.id.option_top_rated:
                if (!item.isChecked()){
                    loadTMDBData(TOP_RATED);
                    item.setChecked(true);
                }
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
    public int getOrientation(Context context){
        Display display = getWindowManager().getDefaultDisplay();
        if (display.getRotation() == Surface.ROTATION_0 || display.getRotation() == Surface.ROTATION_180)
            return VERTICAL;
        else
            return HORIZONTAL;
    }
}
