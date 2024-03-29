package com.example.autotests.popularmoviesapp.adapter;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.autotests.popularmoviesapp.R;
import com.example.autotests.popularmoviesapp.model.videos.Trailer;

import java.util.List;

/**
 * Created by karina.bernice on 11/01/2018.
 */

public class TrailersAdapter extends RecyclerView.Adapter<TrailersAdapter.TrailersAdapterViewHolder> {
    private List<Trailer> mTrailersList;
    private final TrailerClickHandler mOnClickHandler;

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
        Trailer trailer = mTrailersList.get(position);
        holder.mTrailerName.setText(trailer.getName());
    }

    @Override
    public int getItemCount() {
        if (mTrailersList != null){
            return mTrailersList.size();
        }
        return 0;
    }

    public interface TrailerClickHandler{
        void onClickTrailer (Trailer trailer);
    }

    class TrailersAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        final Context context;

        final TextView mTrailerName;

        public TrailersAdapterViewHolder(View itemView) {
            super(itemView);
            context = itemView.getContext();
            mTrailerName = (TextView) itemView.findViewById(R.id.tv_trailer_item_name);
            itemView.setOnClickListener(this);
        }
        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
            Trailer trailer = mTrailersList.get(position);
            mOnClickHandler.onClickTrailer(trailer);
        }

    }

    public void setTrailersList(List<Trailer> trailersList){
        this.mTrailersList = trailersList;
        notifyDataSetChanged();
    }
}
