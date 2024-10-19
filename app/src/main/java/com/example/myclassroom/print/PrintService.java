package com.example.myclassroom.print;

import static com.example.myclassroom.database.DbHelper.CLASS_TABLE_NAME;
import static com.example.myclassroom.database.DbHelper.C_ID;

import android.database.Cursor;

import com.example.myclassroom.classes.ClassItem;
import com.example.myclassroom.database.DbHelper;
import com.example.myclassroom.record.Record;
import com.example.myclassroom.student.Student;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

class PrintService {

    final Record record;
    final DbHelper db;
    final String table = DbHelper.STUDENT_TABLE_NAME;

    PrintService(DbHelper db, Record record) {
        this.record = record;
        this.db = db;
    }

    public ClassItem getClassItem() {
        Cursor cursor = db.query(CLASS_TABLE_NAME, null, C_ID + "=?", new String[]{record.getCid() + ""});
        int idIndex = cursor.getColumnIndex(DbHelper.C_ID);
        int sectionIndex = cursor.getColumnIndex(DbHelper.SECTION_KEY);
        int courseIndex = cursor.getColumnIndex(DbHelper.COURSE_KEY);
        int codeIndex = cursor.getColumnIndex(DbHelper.CODE_KEY);

        if (cursor.moveToFirst()) {
            long id = cursor.getLong(idIndex);
            String section = cursor.getString(sectionIndex);
            String course = cursor.getString(courseIndex);
            String code = cursor.getString(codeIndex);
            return new ClassItem(id, section, course, code, 0, 0);
        }

        return null;
    }

    public ArrayList<Student> getAllStudents() {
        ArrayList<Student> students = new ArrayList<>();

        Cursor cursor = db.query(table, null, DbHelper.CLASS_ID_KEY + "=?",
                new String[]{String.valueOf(record.getCid())});
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

    public Map<Long, Map<Integer, String>> getMonthStatus() {
        Map<Long, Map<Integer, String>> status = new HashMap<>();
        String month = record.getMonth() < 10 ? "0" + record.getMonth() : record.getMonth() + "";
        String dateSubString = record.getYear() + "-" + month;

        Cursor cursor = db.query(DbHelper.STATUS_TABLE_NAME, null,
                "substr(" + DbHelper.DATE_KEY + ",1,7)=? AND " + DbHelper.STUDENT_CID_KEY + "=?",
                new String[]{dateSubString, String.valueOf(record.getCid())});
        int dayIndex = cursor.getColumnIndex(DbHelper.DATE_KEY);
        int idIndex = cursor.getColumnIndex(DbHelper.STUDENT_ID_KEY);
        int statusIndex = cursor.getColumnIndex(DbHelper.STATUS_KEY);
        if (idIndex == -1 || statusIndex == -1) return status;

        while (cursor.moveToNext()) {
            int day = Integer.parseInt(cursor.getString(dayIndex).substring(8));
            Long id = cursor.getLong(idIndex);
            String value = cursor.getString(statusIndex);

            if (status.containsKey(id)) {
                status.get(id).put(day, value);
            } else {
                Map<Integer, String> map = new HashMap<>();
                map.put(day, value);
                status.put(id, map);
            }
        }
        return status;
    }


}
