package com.example.autotests.popularmoviesapp.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.autotests.popularmoviesapp.R;
import com.example.autotests.popularmoviesapp.utils.videos.VideoResultsItem;

import java.util.List;

/**
 * Created by karina.bernice on 11/01/2018.
 */

public class TrailersAdapter extends RecyclerView.Adapter<TrailersAdapter.TrailersAdapterViewHolder> {
    final String TAG = getClass().getSimpleName();
    List<VideoResultsItem> mTrailersList;
    TrailerClickHandler mOnClickHandler;

    public TrailersAdapter(TrailerClickHandler clickHandler){
        mOnClickHandler = clickHandler;
    }

    @Override
    public TrailersAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(R.layout.trailer_item, parent, shouldAttachToParentImmediately);
        return new TrailersAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(TrailersAdapterViewHolder holder, int position) {
        VideoResultsItem trailer = mTrailersList.get(position);
        holder.mTrailerName.setText(trailer.getName());
    }

    @Override
    public int getItemCount() {
        if (mTrailersList != null){
            return mTrailersList.size();
        }
        return 0;
    }

    class TrailersAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        final Context context;
        final TextView mTrailerName;

        public TrailersAdapterViewHolder(View itemView) {
            super(itemView);
            context = itemView.getContext();
            mTrailerName = (TextView) itemView.findViewById(R.id.tv_trailer_item_name);
        }

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
            VideoResultsItem trailer = mTrailersList.get(position);
            mOnClickHandler.onClick(trailer);
        }
    }

    public interface TrailerClickHandler{
        void onClick (VideoResultsItem trailer);
    }

    public void setTrailersList(List<VideoResultsItem> trailersList){
        this.mTrailersList = trailersList;
    }
}
