package com.example.myclassroom.record;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myclassroom.R;

import java.util.ArrayList;

public class RecordAdapter extends RecyclerView.Adapter<RecordAdapter.SheetListViewHolder> {
    private final ArrayList<Record> records;

    public RecordAdapter(ArrayList<Record> records, OnItemClickListener listener) {
        this.records = records;
        this.listener = listener;
    }

    private final OnItemClickListener listener;

    public interface OnItemClickListener {
        void onClick(int position);
    }

    static class SheetListViewHolder extends RecyclerView.ViewHolder {
        TextView sheetTitle;

        SheetListViewHolder(@NonNull View itemView, OnItemClickListener onItemClickListener) {
            super(itemView);
            sheetTitle = itemView.findViewById(R.id.sheetItemTitle);
            itemView.setOnClickListener(v -> onItemClickListener.onClick(getAdapterPosition()));
        }
    }

    @NonNull
    @Override
    public SheetListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.record_item, parent, false);
        return new SheetListViewHolder(itemView, listener);
    }

    @Override
    public void onBindViewHolder(@NonNull SheetListViewHolder holder, int position) {
        holder.sheetTitle.setText(records.get(position).format());
    }

    @Override
    public int getItemCount() {
        return records.size();
    }
}
