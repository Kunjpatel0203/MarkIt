package com.example.myclassroom.classes;

import static com.example.myclassroom.database.DbHelper.CLASS_ID_KEY;
import static com.example.myclassroom.database.DbHelper.DATE_KEY;
import static com.example.myclassroom.database.DbHelper.STATUS_TABLE_NAME;
import static com.example.myclassroom.database.DbHelper.STUDENT_CID_KEY;
import static com.example.myclassroom.database.DbHelper.STUDENT_ID_KEY;
import static com.example.myclassroom.database.DbHelper.STUDENT_TABLE_NAME;
import static com.example.myclassroom.database.DbHelper.S_ID;

import android.content.ContentValues;
import android.database.Cursor;

import com.example.myclassroom.Constant;
import com.example.myclassroom.database.DbHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

class ClassService {

    final DbHelper db;
    final String table = DbHelper.CLASS_TABLE_NAME;

    ClassService(DbHelper db) {
        this.db = db;
    }

    public ArrayList<ClassItem> getAllClasses() {
        ArrayList<ClassItem> classItems = new ArrayList<>();
        Map<Long, Map<String, Integer>> map = new HashMap<>(); // class id, <status, count>

        Cursor cursor = db.query(STATUS_TABLE_NAME, null, DATE_KEY + " =?",
                new String[]{Constant.getToday()});

        while (cursor.moveToNext()) {
            int cidIndex = cursor.getColumnIndex(STUDENT_CID_KEY);
            int statusIndex = cursor.getColumnIndex(DbHelper.STATUS_KEY);

            long classId = cursor.getLong(cidIndex);
            String status = cursor.getString(statusIndex);
            if (map.containsKey(classId)) {
                Map<String, Integer> statusMap = map.get(classId);
                statusMap.put(status, statusMap.get(status) == null ? 1 :
                        statusMap.get(status) + 1);
            } else {
                Map<String, Integer> statusMap = new HashMap<>();
                statusMap.put(status, 1);
                map.put(classId, statusMap);
            }
        }
        cursor = db.query(table, null, null, null);
        int idIndex = cursor.getColumnIndex(DbHelper.C_ID);
        int sectionIndex = cursor.getColumnIndex(DbHelper.SECTION_KEY);
        int courseIndex = cursor.getColumnIndex(DbHelper.COURSE_KEY);
        int codeIndex = cursor.getColumnIndex(DbHelper.CODE_KEY);
        if (idIndex == -1 || sectionIndex == -1 || courseIndex == -1 || codeIndex == -1) return classItems;

        while (cursor.moveToNext()) {
            long id = cursor.getLong(idIndex);
            String section = cursor.getString(sectionIndex);
            String course = cursor.getString(courseIndex);
            String code = cursor.getString(codeIndex);

            int present = map.get(id) == null ? 0 : map.get(id).get(Constant.PRESENT) == null ?
                    0 : map.get(id).get(Constant.PRESENT);
            int absent = map.get(id) == null ? 0 : map.get(id).get(Constant.ABSENT) == null ? 0 :
                    map.get(id).get(Constant.ABSENT);
            classItems.add(new ClassItem(id, section, course,code, present, absent));
        }

        return classItems;
    }

    public long addClass(ClassItem classItem) {
        ContentValues values = new ContentValues();
        values.put(DbHelper.SECTION_KEY, classItem.getSection());
        values.put(DbHelper.COURSE_KEY, classItem.getCourse());
        values.put(DbHelper.CODE_KEY, classItem.getCode());
        try {
            return db.insert(table, values);
        } catch (Exception e) {
            System.out.println(e);
            return -1;
        }
    }

    public int updateClass(ClassItem classItem) {
        ContentValues values = new ContentValues();
        values.put(DbHelper.SECTION_KEY, classItem.getSection());
        values.put(DbHelper.COURSE_KEY, classItem.getCourse());
        values.put(DbHelper.CODE_KEY, classItem.getCode());
        String where = DbHelper.C_ID + "=?";
        String[] args = {String.valueOf(classItem.getId())};

        try {
            return db.update(table, values, where, args);
        } catch (Exception e) {
            return -1;
        }
    }

    public int deleteClass(ClassItem classItem) {
        String where = DbHelper.C_ID + "=?";
        String[] args = {String.valueOf(classItem.getId())};
        try {
            db.runSql("DELETE FROM " + STATUS_TABLE_NAME +
                    " WHERE " + STUDENT_ID_KEY +
                    " IN ( SELECT student." + S_ID +
                    " FROM " + STUDENT_TABLE_NAME +
                    " WHERE " + CLASS_ID_KEY + "=" + classItem.getId() +
                    " )", null);
            db.delete(STUDENT_TABLE_NAME, where, args);
            return db.delete(table, where, args);
        } catch (Exception e) {
            return -1;
        }
    }

}
