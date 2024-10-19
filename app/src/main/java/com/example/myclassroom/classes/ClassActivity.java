package com.example.myclassroom.classes;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myclassroom.R;
import com.example.myclassroom.database.DbHelper;
import com.example.myclassroom.student.StudentActivity;

public class ClassActivity extends AppCompatActivity {
    private ClassController controller;
    private ClassAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_class);
        DbHelper dbHelper = new DbHelper(this);
        ClassService classService = new ClassService(dbHelper);
        controller = new ClassController(classService);

        initWidgets();
    }

    private void initWidgets() {
        findViewById(R.id.fab).setOnClickListener(v -> showAddDialog());

        adapter = new ClassAdapter(controller.classItems, this::gotoItemActivity);
        RecyclerView recyclerView = findViewById(R.id.recyclerClass);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        setEmptyListView();
    }



    private void gotoItemActivity(int i) {
        final ClassItem item = controller.classItems.get(i);
        Intent currentClass = new Intent(this, StudentActivity.class);

        currentClass.putExtra("id", item.getId());
        currentClass.putExtra("section", item.getSection());
        currentClass.putExtra("course", item.getCourse());
        startActivity(currentClass);
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == 0) {
            //open
            gotoItemActivity(item.getGroupId());
        } else if (id == 1) {
            //edit
            showUpdateDialog(item.getGroupId());
        } else if (id == 2) {
            //delete
            controller.deleteClass(item.getGroupId());
            adapter.notifyItemRemoved(item.getGroupId());
            setEmptyListView();
        }
        return true;
    }

    private void showAddDialog() {
        new ClassDialog((dialog, section, course, code) -> {
            if (section.isEmpty() || course.isEmpty() || code.isEmpty()) {
                Toast.makeText(this, "Enter valid data", Toast.LENGTH_SHORT).show();
                return;
            }
            controller.addClass(new ClassItem(section, course, code));
            adapter.notifyItemInserted(controller.classItems.size() - 1);
            setEmptyListView();
            dialog.dismiss();
        }).showDialog(getSupportFragmentManager());

    }

    private void showUpdateDialog(int position) {
        ClassItem item = controller.classItems.get(position);
        new ClassDialog(item, (dialog, section, course, code) -> {
            if (section.isEmpty() || course.isEmpty() || code.isEmpty()) {
                Toast.makeText(this, "Enter valid data", Toast.LENGTH_SHORT).show();
                return;
            }
            controller.updateClass(position, new ClassItem(item.getId(), section, course, code,
                    item.getPresent(), item.getAbsent()));
            adapter.notifyItemChanged(position);
            dialog.dismiss();
        }).showDialog(getSupportFragmentManager());
    }

    private void setEmptyListView() {
        if (controller.classItems.size() > 1) return;
        findViewById(R.id.empty_view).setVisibility(controller.classItems.isEmpty() ?
                View.VISIBLE : View.GONE);

    }

    @Override
    protected void onResume() {
        controller.refreshClasses();
        adapter.notifyDataSetChanged();
        super.onResume();
    }
}