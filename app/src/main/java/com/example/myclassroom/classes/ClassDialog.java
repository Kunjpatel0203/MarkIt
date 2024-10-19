package com.example.myclassroom.classes;

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

public class ClassDialog extends DialogFragment {
    private ClassItem classItem;

    public ClassDialog(OnButtonClickListener onButtonClickListener) {
        this.onButtonClickListener = onButtonClickListener;
    }

    public ClassDialog(ClassItem classItem, OnButtonClickListener onButtonClickListener) {
        this.onButtonClickListener = onButtonClickListener;
        this.classItem = classItem;
    }

    private final OnButtonClickListener onButtonClickListener;

    public interface OnButtonClickListener {
        void onClassDialogOkClick(DialogFragment dialog, String section, String course, String code);
    }


    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        final View view = LayoutInflater.from(getActivity()).inflate(R.layout.dialog, null);
        builder.setView(view);
        AlertDialog dialog = builder.create();
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        if (classItem == null) setAddClassDialog(view);
        else setUpdateClassDialog(view, classItem);

        Button cancel = view.findViewById(R.id.cancel);
        cancel.setOnClickListener(v -> dialog.dismiss());
        return dialog;

    }

    void showDialog(FragmentManager fragmentManager) {
        show(fragmentManager, "classDialog");
    }

    public void setAddClassDialog(View view) {
        TextView title = view.findViewById(R.id.dialogTitle);
        view.findViewById(R.id.til3).setVisibility(View.VISIBLE);
        TextInputEditText section = view.findViewById(R.id.tied1);
        TextInputEditText course = view.findViewById(R.id.tied2);
        TextInputEditText code = view.findViewById(R.id.tied3);

        Button add = view.findViewById(R.id.add);

        title.setText("ADD NEW CLASS");

        section.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_CHARACTERS);
        course.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_CHARACTERS);

        add.setOnClickListener(v -> {
            onButtonClickListener.onClassDialogOkClick(this, section.getText().toString().trim(),
                    course.getText().toString().trim(), code.getText().toString().trim());
        });
    }

    public void setUpdateClassDialog(View view, ClassItem item) {
        TextView title = view.findViewById(R.id.dialogTitle);
        view.findViewById(R.id.til3).setVisibility(View.VISIBLE);
        TextInputEditText section = view.findViewById(R.id.tied1);
        TextInputEditText course = view.findViewById(R.id.tied2);
        TextInputEditText code = view.findViewById(R.id.tied3);


        section.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_CHARACTERS);
        course.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_CHARACTERS);


        section.setText(item.getSection());
        course.setText(item.getCourse());
        code.setText(item.getCode());

        Button update = view.findViewById(R.id.add);
        update.setText("UPDATE");

        title.setText("EDIT CLASS INFO");

        update.setOnClickListener(v -> {
            onButtonClickListener.onClassDialogOkClick(this, section.getText().toString().trim(),
                    course.getText().toString().trim(), code.getText().toString().trim());
        });
    }
}
