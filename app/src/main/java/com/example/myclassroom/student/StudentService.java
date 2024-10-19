package com.example.myclassroom.student;

import android.content.ContentValues;
import android.database.Cursor;

import com.example.myclassroom.database.DbHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

class StudentService {

    private final long classId;
    private final DbHelper db;
    private final String table = DbHelper.STUDENT_TABLE_NAME;

    StudentService(DbHelper db, long classId) {
        this.db = db;
        this.classId = classId;
    }

    public ArrayList<Student> getAllStudents() {
        ArrayList<Student> students = new ArrayList<>();

        Cursor cursor = db.query(table, null, DbHelper.CLASS_ID_KEY + "=?",
                new String[]{String.valueOf(classId)});
        int idIndex = cursor.getColumnIndex(DbHelper.S_ID);
        int cidIndex = cursor.getColumnIndex(DbHelper.CLASS_ID_KEY);
        int rollIndex = cursor.getColumnIndex(DbHelper.STUDENT_ROLL_KEY);
        int nameIndex = cursor.getColumnIndex(DbHelper.STUDENT_NAME_KEY);
        if (idIndex == -1 || rollIndex == -1 || nameIndex == -1 || cidIndex == -1) return students;

        while (cursor.moveToNext()) {
            int id = cursor.getInt(idIndex);
            int cid = cursor.getInt(cidIndex);
            String roll = cursor.getString(rollIndex);
            String name = cursor.getString(nameIndex);
            students.add(new Student(id, roll, name, cid));
        }

        return students;
    }


    public Map<Long, String> getStatus(String date) {
        Map<Long, String> status = new HashMap<>();

        Cursor cursor = db.query(DbHelper.STATUS_TABLE_NAME, null,
                DbHelper.DATE_KEY + "=? AND " + DbHelper.STUDENT_CID_KEY + "=?",
                new String[]{date, String.valueOf(classId)});

        int idIndex = cursor.getColumnIndex(DbHelper.STUDENT_ID_KEY);
        int statusIndex = cursor.getColumnIndex(DbHelper.STATUS_KEY);
        if (idIndex == -1 || statusIndex == -1) return status;

        while (cursor.moveToNext()) {
            Long id = cursor.getLong(idIndex);
            String value = cursor.getString(statusIndex);
            status.put(id, value);
        }
        return status;
    }


    public long addStudent(Student student) {
        ContentValues values = new ContentValues();
        values.put(DbHelper.STUDENT_ROLL_KEY, student.getRoll());
        values.put(DbHelper.STUDENT_NAME_KEY, student.getName());
        values.put(DbHelper.CLASS_ID_KEY, classId);
        try {
            return db.insert(table, values);
        } catch (Exception e) {
            return -1;
        }
    }

    public int updateStudent(Student student) {
        ContentValues values = new ContentValues();
        values.put(DbHelper.STUDENT_ROLL_KEY, student.getRoll());
        values.put(DbHelper.STUDENT_NAME_KEY, student.getName());
        String where = DbHelper.S_ID + "=?";
        String[] args = {String.valueOf(student.getId())};

        try {
            return db.update(table, values, where, args);
        } catch (Exception e) {
            return -1;
        }
    }

    public int deleteStudent(Student student) {
        String where = DbHelper.S_ID + "=?";
        String[] args = {String.valueOf(student.getId())};
        try {
            db.delete(DbHelper.STATUS_TABLE_NAME, where, args);
            return db.delete(table, where, args);
        } catch (Exception e) {
            return -1;
        }
    }

    public void saveStatusData(Map<Long, String> status, String date) {
        for (Map.Entry<Long, String> entry : status.entrySet()) {
            ContentValues values = new ContentValues();
            values.put(DbHelper.STUDENT_ID_KEY, entry.getKey());
            values.put(DbHelper.STUDENT_CID_KEY, classId);
            values.put(DbHelper.DATE_KEY, date);
            values.put(DbHelper.STATUS_KEY, entry.getValue());
            long value = db.insert(DbHelper.STATUS_TABLE_NAME, values);
            if (value == -1) {
               int a = db.update(DbHelper.STATUS_TABLE_NAME, values,
                        DbHelper.S_ID + "=? AND " + DbHelper.DATE_KEY + "=?",
                        new String[]{String.valueOf(entry.getKey()), date});
            }
        }
    }
}
