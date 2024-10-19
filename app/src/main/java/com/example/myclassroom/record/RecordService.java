package com.example.myclassroom.record;

import android.database.Cursor;

import com.example.myclassroom.database.DbHelper;

import java.util.ArrayList;

class RecordService {

    final DbHelper db;
    final long cid;
    final String table = DbHelper.STATUS_TABLE_NAME;

    RecordService(DbHelper db, long cid) {
        this.db = db;
        this.cid = cid;
    }

    public ArrayList<Record> getAllRecords() {
        ArrayList<Record> records = new ArrayList<>();

        Cursor cursor = db.query(table, new String[]{DbHelper.DATE_KEY},
                DbHelper.STUDENT_CID_KEY + " =?", new String[]{String.valueOf(cid)}, "substr" +
                        "(date,1,7)", "substr(date,1,4) DESC , " + "substr(date,6,2) DESC");

        while (cursor.moveToNext()) {
            String date = cursor.getString(0);
            String[] parts = date.split("-");
            records.add(new Record(cid, Integer.parseInt(parts[1]), Integer.parseInt(parts[0])));
        }
        return records;
    }
}
