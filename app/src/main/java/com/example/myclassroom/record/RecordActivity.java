package com.example.myclassroom.record;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myclassroom.R;
import com.example.myclassroom.database.DbHelper;
import com.example.myclassroom.print.PrintActivity;

public class RecordActivity extends AppCompatActivity {
    private RecordController controller;
    private RecordAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record);

        long cid = getIntent().getLongExtra("cid", -1);
        DbHelper dbHelper = new DbHelper(this);
        RecordService service = new RecordService(dbHelper,cid);
        controller = new RecordController(service);

        initWidgets();
    }

    private void initWidgets() {
        adapter = new RecordAdapter(controller.records, this::gotoItemActivity);
        RecyclerView recyclerView = findViewById(R.id.monthList);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        setEmptyListView();
    }

    private void setEmptyListView() {
        if (controller.records.size() == 0) findViewById(R.id.emptyRecords).setVisibility(View.VISIBLE);
    }

    private void gotoItemActivity(int i) {
        Toast.makeText(this, "Loading...", Toast.LENGTH_SHORT).show();
        final Record item = controller.records.get(i);
        Intent currentRecord = new Intent(this, PrintActivity.class);

        currentRecord.putExtra("cid", item.getCid());
        currentRecord.putExtra("month", item.getMonth());
        currentRecord.putExtra("year", item.getYear());
        startActivity(currentRecord);
    }
}