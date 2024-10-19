package com.example.myclassroom.student;

import android.content.Context;
import android.graphics.Color;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myclassroom.Constant;
import com.example.myclassroom.R;

import java.util.ArrayList;
import java.util.Map;
import java.util.Objects;

public class StudentAdapter extends RecyclerView.Adapter<StudentAdapter.StudentViewHolder> {
    private final ArrayList<Student> students;
    OnItemClickListener listener;
    private final Map<Long, String> status;
    Context context;

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public StudentAdapter(Context context, ArrayList<Student> students, Map<Long, String> status,
                          OnItemClickListener listener) {
        this.context = context;
        this.students = students;
        this.listener = listener;
        this.status = status;
    }

    public static class StudentViewHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener {
        private final TextView serial, roll, name, status;
        private final CardView cardView;

        public StudentViewHolder(OnItemClickListener listener, View itemView) {
            super(itemView);
            serial = itemView.findViewById(R.id.serial);
            roll = itemView.findViewById(R.id.roll);
            name = itemView.findViewById(R.id.name);
            status = itemView.findViewById(R.id.status);
            cardView = itemView.findViewById(R.id.cardView);
            itemView.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if(position != -1) listener.onItemClick(position);
            });
            itemView.setOnCreateContextMenuListener(this);
        }


        @Override
        public void onCreateContextMenu(ContextMenu menu, View v,
                                        ContextMenu.ContextMenuInfo menuInfo) {
            if(getAdapterPosition() == -1) return;
            menu.add(getAdapterPosition(), 0, 0, "Edit");
            menu.add(getAdapterPosition(), 1, 0, "Delete");
        }
    }

    @NonNull
    @Override
    public StudentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View attendanceItemView =
                LayoutInflater.from(parent.getContext()).inflate(R.layout.student_item, parent,
                        false);
        return new StudentViewHolder(listener, attendanceItemView);//return the item view to
        // attendance view holder
    }

    @Override
    public void onBindViewHolder(StudentViewHolder holder, int position) {
        if(position == -1) return;
        String statusValue = status.get(students.get(position).getId());
        holder.serial.setText(position<9 ? "0"+(position+1) : String.valueOf(position+1));
        holder.roll.setText(students.get(position).getRoll());
        holder.name.setText(students.get(position).getName());
        holder.status.setText(statusValue == null ? "" : statusValue);
        holder.cardView.setCardBackgroundColor(getColor(position));
    }

    @Override
    public int getItemCount() {
        return students.size();
    }

    private int getColor(int position) {
        String value = status.get(students.get(position).getId());
        if (Objects.equals(value, Constant.PRESENT))
            return Color.parseColor("#" + Integer.toHexString(ContextCompat.getColor(context,
                    R.color.present_color2)));
        else if (Objects.equals(value, Constant.ABSENT))
            return Color.parseColor("#" + Integer.toHexString(ContextCompat.getColor(context,
                    R.color.absent_color2)));

        return Color.parseColor("#" + Integer.toHexString(ContextCompat.getColor(context,
                R.color.normal)));
    }

}
