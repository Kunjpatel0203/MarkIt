package com.example.myclassroom.classes;

import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myclassroom.R;

import java.util.ArrayList;

public class ClassAdapter extends RecyclerView.Adapter<ClassAdapter.ClassViewHolder> {

    private final ArrayList<ClassItem> classItems;
    OnItemClickListener listener;

    public ClassAdapter(ArrayList<ClassItem> classItems, OnItemClickListener listener) {
        this.classItems = classItems;
        this.listener = listener;
    }

    public interface OnItemClickListener {
        void onItemClick(int position);
    }


    public static class ClassViewHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener {
        private final TextView course, section, status, present, absent;

        public ClassViewHolder(OnItemClickListener listener, View itemView) {
            super(itemView);
            course = itemView.findViewById(R.id.course);
            section = itemView.findViewById(R.id.section);
            status = itemView.findViewById(R.id.status_class);
            present = itemView.findViewById(R.id.present_text);
            absent = itemView.findViewById(R.id.absent_text);

            itemView.setOnClickListener(v -> listener.onItemClick(getBindingAdapterPosition()));

            itemView.setOnCreateContextMenuListener(this);
        }

        @Override
        public void onCreateContextMenu(ContextMenu menu, View v,
                                        ContextMenu.ContextMenuInfo menuInfo) {
            menu.add(getBindingAdapterPosition(), 0, 0, "Open");
            menu.add(getBindingAdapterPosition(), 1, 0, "Edit");
            menu.add(getBindingAdapterPosition(), 2, 0, "Delete");
        }

    }

    @NonNull
    @Override
    public ClassViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View classItemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.class_item
                , parent, false);
        return new ClassViewHolder(listener, classItemView);//return the item view to class view
        // holder
    }

    @Override
    public void onBindViewHolder(ClassViewHolder holder, int position) {
        ClassItem item = classItems.get(position);
        boolean attendanceTaken = item.getPresent()>0 || item.getAbsent()>0;
        holder.course.setText(classItems.get(position).getCourse());
        holder.section.setText(classItems.get(position).getSection());

        holder.status.setVisibility(attendanceTaken ? View.GONE : View.VISIBLE);
        holder.status.setText(R.string.attendanceNotTakenStatus);
        holder.present.setVisibility(attendanceTaken ? View.VISIBLE : View.GONE);
        holder.absent.setVisibility(attendanceTaken ? View.VISIBLE : View.GONE);
        holder.present.setText(String.valueOf(item.getPresent()));
        holder.absent.setText(String.valueOf(item.getAbsent()));
    }

    @Override
    public int getItemCount() {
        return classItems.size();
    }
    
}
