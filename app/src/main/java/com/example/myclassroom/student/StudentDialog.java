package com.example.myclassroom.student;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;

import com.example.myclassroom.R;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class StudentDialog extends DialogFragment {
    private Student student;
    public TextInputEditText roll,name;
    public StudentDialog(OnButtonClickListener onButtonClickListener) {
        this.onButtonClickListener = onButtonClickListener;
    }

    public StudentDialog(Student student, OnButtonClickListener onButtonClickListener) {
        this.onButtonClickListener = onButtonClickListener;
        this.student = student;
    }

    private final OnButtonClickListener onButtonClickListener;

    public interface OnButtonClickListener {
        void onStudentDialogOkClick(StudentDialog dialog,String section, String course);
    }


    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        final View view = LayoutInflater.from(getActivity()).inflate(R.layout.dialog, null);
        builder.setView(view);
        AlertDialog dialog = builder.create();
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        TextInputLayout roll_til = view.findViewById(R.id.til1);
        TextInputLayout name_til = view.findViewById(R.id.til2);
        roll_til.setHint("Roll");
        name_til.setHint("Name");

        roll = view.findViewById(R.id.tied1);
        name = view.findViewById(R.id.tied2);
        roll.setInputType(InputType.TYPE_CLASS_NUMBER);
        name.setInputType(InputType.TYPE_TEXT_FLAG_CAP_WORDS);

        if(student!=null){
            roll.setText(String.valueOf(student.getRoll()));
            name.setText(student.getName());
        }


        TextView title = view.findViewById(R.id.dialogTitle);
        Button ok = view.findViewById(R.id.add);

        title.setText(student==null? "ADD NEW STUDENT":"EDIT STUDENT INFO");
        ok.setText(student==null? "ADD":"UPDATE");

        ok.setOnClickListener(v -> {
            onButtonClickListener.onStudentDialogOkClick(this,roll.getText().toString().trim(),
                    name.getText().toString().trim());
        });

        Button cancel = view.findViewById(R.id.cancel);
        cancel.setOnClickListener(v -> dialog.dismiss());
        return dialog;

    }

    void showDialog(FragmentManager fragmentManager){
        show(fragmentManager, "classDialog");
    }

}
