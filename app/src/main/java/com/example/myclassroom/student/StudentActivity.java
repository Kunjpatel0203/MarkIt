package com.example.myclassroom.student;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myclassroom.Constant;
import com.example.myclassroom.R;
import com.example.myclassroom.database.DbHelper;
import com.example.myclassroom.record.RecordActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Calendar;
import java.util.Objects;

public class StudentActivity extends AppCompatActivity {
    private StudentController controller;
    private StudentAdapter adapter;

    private static final String PRESENT = "P";
    private static final String ABSENT = "A";

    private RecyclerView recyclerView;
    private TextView subtitle;

    private TextView presentFab, absentFab;
    private FloatingActionButton mainFab;
    private Animation openFab, closeFab, rotateForward, rotateBackward;
    boolean isFabOpen = false;
    boolean hasChanged = false;
    long classId;

    int i = 0;

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student);

        Intent intent = getIntent();
        classId = intent.getLongExtra("id", -1);
        String section = intent.getStringExtra("section");
        String course = intent.getStringExtra("course");

        DbHelper dbHelper = new DbHelper(this);
        StudentService service = new StudentService(dbHelper, classId);
        controller = new StudentController(service);

        setToolbar(section, course);
        initWidgets();

        if (controller.getStudents().isEmpty()) showAddDialog();

    }

    private void initWidgets() {

        adapter = new StudentAdapter(this, controller.getStudents(), controller.getStatus(),
                this::setStatus);
        recyclerView = findViewById(R.id.recyclerStudent);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        setEmptyListView();
        setViews();
    }


    public void setEmptyListView() {
        if (controller.getStudents().size() > 1) return;

        findViewById(R.id.emptyListText).setVisibility(controller.getStudents().isEmpty() ?
                View.VISIBLE : View.GONE);
        findViewById(R.id.fab_main).setVisibility(controller.getStudents().isEmpty() ?
                View.GONE : View.VISIBLE);
    }


    private void setViews() {
        mainFab = findViewById(R.id.fab_main);
        presentFab = findViewById(R.id.fab_present);
        absentFab = findViewById(R.id.fab_absent);

        mainFab.setOnClickListener(v -> animateFab());
        presentFab.setOnClickListener(this::pNaFabClicked);
        absentFab.setOnClickListener(this::pNaFabClicked);

        presentFab.setOnLongClickListener(this::pNaLongClicked);
        absentFab.setOnLongClickListener(this::pNaLongClicked);

        openFab = AnimationUtils.loadAnimation(this, R.anim.fab_open);
        closeFab = AnimationUtils.loadAnimation(this, R.anim.fab_close);
        rotateForward = AnimationUtils.loadAnimation(this, R.anim.rotate_forward);
        rotateBackward = AnimationUtils.loadAnimation(this, R.anim.rotate_backward);
    }

    private void initializeFABs(int i) {
        if (controller.getStudents().size() > 0 && i < controller.getStudents().size()) {
            presentFab.setText(controller.getStudents().get(i).getRoll() + "");
            absentFab.setText(controller.getStudents().get(i).getRoll() + "");
        }
        adapter.notifyDataSetChanged();
    }

    private boolean pNaLongClicked(View view) {
        hasChanged = true;
        String status;
        if (view.getId() == R.id.fab_present) {
            status = Constant.PRESENT;
            showMessage("All checked present");
        } else {
            status = Constant.ABSENT;
            showMessage("All checked absent");
        }
        controller.getStatus().clear();
        for (int i = 0; i < controller.getStudents().size(); i++) {
            controller.getStatus().put(controller.getStudents().get(i).getId(), status);
        }
        adapter.notifyDataSetChanged();

        return true;
    }

    private void pNaFabClicked(View view) {
        hasChanged = true;
        if (i < controller.getStudents().size()) {
            scrollToPosition(i + 1);
            initializeFABs(i + 1);

            controller.getStatus().put(controller.getStudents().get(i++).getId(),
                    view.getId() == R.id.fab_present ?
                            Constant.PRESENT :
                            Constant.ABSENT);
            adapter.notifyDataSetChanged();
        }
    }

    private void animateFab() {
        if (isFabOpen) {
            mainFab.startAnimation(rotateBackward);
            presentFab.startAnimation(closeFab);
            absentFab.setAnimation(closeFab);
            presentFab.setClickable(false);
            absentFab.setClickable(false);
            isFabOpen = false;
        } else {
            mainFab.startAnimation(rotateForward);
            presentFab.startAnimation(openFab);
            absentFab.setAnimation(openFab);
            presentFab.setClickable(true);
            absentFab.setClickable(true);
            isFabOpen = true;
        }
    }

    private void setToolbar(String section, String course) {
        Toolbar toolbar = findViewById(R.id.toolbar);
        TextView title = toolbar.findViewById(R.id.toolbar_title);
        title.setText(section + " | " + course);
        subtitle = toolbar.findViewById(R.id.toolbar_subTitle);
        subtitle.setVisibility(View.VISIBLE);
        subtitle.setText("Today");
        toolbar.inflateMenu(R.menu.menu_student);
        toolbar.setOnMenuItemClickListener(this::onMenuItemClick);
    }

    private void setStatus(int position) {
        hasChanged = true;
        i = position + 1; //set i for fab control status
        initializeFABs(i);
        String status = controller.getStatus().get(controller.getStudents().get(position).getId());

        controller.getStatus().put(controller.getStudents().get(position).getId(),
                Objects.equals(status, PRESENT) ? ABSENT : PRESENT);

        adapter.notifyItemChanged(position);
    }


    private void showDatePicker() {
        i = 0; //if picker is ever opened then 90% sure that user will open new record,so i
        // should reinitialized
        initializeFABs(i);
        Calendar calendar = Calendar.getInstance();

        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                ((view, year, month, dayOfMonth) -> {
                    String date = Constant.getDateString(year, month, dayOfMonth);
                    controller.updateAttendance(date);
                    adapter.notifyDataSetChanged();

                    subtitle.setText(Constant.getToday().equals(date) ? "TODAY" : date);
                }),
                calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }

    private boolean onMenuItemClick(MenuItem menuItem) {
        int id = menuItem.getItemId();
        if (id == R.id.add) {
            showAddDialog();
        } else if (id == R.id.changeDate) {
            showDatePicker();
        } else if (id == R.id.save) {
            showSaveConfirmationDialog();
        } else if (id == R.id.printAttendance) {
            if (controller.getStudents().isEmpty()) {
                showMessage("Add student to Print data");
            } else {
                Intent intent = new Intent(this, RecordActivity.class);
                intent.putExtra("cid", classId);
                startActivity(intent);
            }
        }

        return true;
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case 0: //update key
                showUpdateDialog(item.getGroupId());
                break;
            case 1: //delete key
                controller.deleteStudent(item.getGroupId());
                adapter.notifyItemRemoved(item.getGroupId());
                setEmptyListView();
                break;
        }
        return true;
    }

    private void showAddDialog() {
        new StudentDialog((dialog, roll, name) -> {
            if (roll.isEmpty() || name.isEmpty()) {
                showMessage("Enter valid data");
                return;
            }
            try {
                //a regex for check a string contains only 0-9
                roll = roll.trim();
                boolean match = roll.matches("^[0-9]+$");

                if (!match)
                    throw new NumberFormatException();

                controller.addStudent(new Student(roll, name));
                adapter.notifyDataSetChanged();


                dialog.roll.setText(nextRoll(roll));
                dialog.name.setText("");


                setEmptyListView();
            } catch (NumberFormatException e) {
                showMessage("Enter valid roll number");
            } catch (Exception e) {
                showMessage(e.getMessage());
            }
        }).showDialog(getSupportFragmentManager());

    }

    private String nextRoll(String roll) {
        char[] chars = roll.toCharArray();
        int i = chars.length - 1;
        while (i >= 0 && chars[i] == '9') chars[i--] = '0';

        if (i >= 0) {
            chars[i]++;
            return new String(chars);
        }
        return "1" + new String(chars);
    }

    private void showUpdateDialog(int position) {
        Student item = controller.getStudents().get(position);
        new StudentDialog(item, (dialog, roll, name) -> {
            if (roll.isEmpty() || name.isEmpty()) {
                showMessage("Enter valid data");
                return;
            }
            try {
                roll = roll.trim();
                boolean match = roll.matches("^[0-9]+$");

                if (!match)
                    throw new NumberFormatException();

                long id = item.getId();
                controller.updateStudent(position, new Student(id, roll, name));
                adapter.notifyItemChanged(position);
                dialog.dismiss();
            } catch (Exception e) {
                showMessage("Enter valid roll number");
            }
        }).showDialog(getSupportFragmentManager());
    }

    private void showSaveConfirmationDialog() {
        if (adapter.getItemCount() != 0) {
            int present =
                    controller.getStatus().values().stream().filter(s -> s.equals(Constant.PRESENT)).toArray().length;
            int absent = controller.getStudents().size() - present;
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Save changes?");
            builder.setMessage("PRESENT: " + present + "\nABSENT: " + absent);
            builder.setPositiveButton("save", (d, w) -> {
                hasChanged = false;
                controller.saveStatusData();
                adapter.notifyDataSetChanged();
                showMessage("Saved");
                d.dismiss();
            });
            builder.setNegativeButton("Cancel", null);
            AlertDialog dialog = builder.create();
            dialog.show();
        } else showMessage("No data found to save");
    }

    public void scrollToPosition(int position) {
        if (position < adapter.getItemCount())
            recyclerView.post(() -> recyclerView.scrollToPosition(position));
    }


    public void showMessage(String m) {
        Toast.makeText(this, m, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onBackPressed() {
        if (hasChanged) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Discard changes?");
            builder.setPositiveButton("Discard", (d, v) -> super.onBackPressed());
            builder.setNegativeButton("Cancel", null);
            builder.create().show();
        } else super.onBackPressed();
    }
}