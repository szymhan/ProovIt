package com.example.proovit.utils;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.proovit.R;

import java.util.List;

public class RecyclerViewAdapterAll extends RecyclerView.Adapter<RecyclerViewAdapterAll.ViewHolder> {

    private LayoutInflater mInflater;
    private RecyclerViewAdapterAll.ItemClickListener mClickListener;
    private List<String> reportedSitesList;

    public RecyclerViewAdapterAll(Context context, List<String> data) {
        this.mInflater = LayoutInflater.from(context);
        this.reportedSitesList = data;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.recyclerview_row, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        String show = reportedSitesList.get(position);
        holder.myTextView.setText(show);
    }


    @Override
    public int getItemCount() {
        return reportedSitesList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView myTextView;

        ViewHolder(View itemView) {
            super(itemView);
            myTextView = itemView.findViewById(R.id.singleRun);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
        }
    }


    String getItem(int id) {
        return reportedSitesList.get(id);
    }

    public void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }


    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }
}