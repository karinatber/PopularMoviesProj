package com.example.autotests.popularmoviesapp;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.autotests.popularmoviesapp.utils.NetworkUtils;
import com.example.autotests.popularmoviesapp.utils.ResultsItem;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.net.URL;
import java.util.List;

/**
 * Created by karina.bernice on 29/08/2017.
 */

public class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.MoviesAdapterViewHolder> {
    private List<ResultsItem> mMovieData;
    private Cursor mCursor;
    private final MoviesAdapterOnClickHandler mOnClickHandler;

    public interface MoviesAdapterOnClickHandler{
        void onClick (ResultsItem movieDetails);
    }

    public MoviesAdapter(MoviesAdapterOnClickHandler mOnClickHandler, Cursor cursor){
        this.mCursor = cursor;
        this.mOnClickHandler = mOnClickHandler;
    }


    @Override
    public MoviesAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(R.layout.movies_list_item, parent, shouldAttachToParentImmediately);

        return new MoviesAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MoviesAdapterViewHolder holder, int position) {
        // Move the mCursor to the position of the item to be displayed
        if (!mCursor.moveToPosition(position))
            return; // fail if returned null
        String title;
        URL imageUrlPath;

        if (mCursor != null){

        } else {
            ResultsItem movieItem = mMovieData.get(position);
            String title = movieItem.getTitle();
            holder.mMovieTextItem.setText(title);
            URL imageUrlPath = NetworkUtils.buildImageUrl(movieItem.getPosterPath());
            //Picasso.with(holder.context).load(imageUrlPath.toString()).centerCrop().resize(200, 200).into(holder.mMovieImage);
            Picasso.with(holder.context).load(imageUrlPath.toString()).fit().into(holder.mMovieImage);
        }
    }

    @Override
    public int getItemCount() {
        if (null == mMovieData) return 0;
        return mMovieData.size();
    }

    public class MoviesAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public final TextView mMovieTextItem;
        public final ImageView mMovieImage;
        public final Context context;

        public MoviesAdapterViewHolder(View itemView) {
            super(itemView);
            this.mMovieTextItem = (TextView)itemView.findViewById(R.id.tv_movies_item_title);
            this.mMovieImage = (ImageView)itemView.findViewById(R.id.iv_movie_poster);
            this.context = itemView.getContext();
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int adapterPosition = getAdapterPosition();
            ResultsItem movieData = mMovieData.get(adapterPosition);
            mOnClickHandler.onClick(movieData);
        }
    }

    public void setMovieData(List<ResultsItem> movieData){
        mMovieData = movieData;
        notifyDataSetChanged();
    }

}
