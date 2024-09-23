package com.example.attendanceapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DBHelper extends SQLiteOpenHelper {
    // Class Table
    public static final String CLASS_TABLE_NAME = "CLASS_TABLE";
    public static final String C_ID = "_CID";
    public static final String CLASS_NAME_KEY = "CLASS_NAME";
    public static final String SUBJECT_NAME_KEY = "SUBJECT_NAME";
    public static final String CREATE_CLASS_TABLE =
            "CREATE TABLE " + CLASS_TABLE_NAME + " (" +
                    C_ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +   // Added space
                    CLASS_NAME_KEY + " TEXT NOT NULL, " +                      // Added space
                    SUBJECT_NAME_KEY + " TEXT NOT NULL, " +                    // Added space
                    "UNIQUE (" + CLASS_NAME_KEY + "," + SUBJECT_NAME_KEY + ")" +
                    ");";
    private static final String DROP_CLASS_TABLE = "DROP TABLE IF EXISTS " + CLASS_TABLE_NAME;
    private static final String SELECT_CLASS_TABLE = "SELECT * FROM " + CLASS_TABLE_NAME;

    // Student Table
    public static final String STUDENT_TABLE_NAME = "STUDENT_TABLE";
    public static final String S_ID = "_SID";
    public static final String STUDENT_NAME_KEY = "STUDENT_NAME";
    public static final String STUDENT_ROLL_KEY = "ROLL";
    public static final String CREATE_STUDENT_TABLE =
            "CREATE TABLE " + STUDENT_TABLE_NAME + " (" +
                    S_ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +   // Added space
                    C_ID + " INTEGER NOT NULL, " +                             // Added space
                    STUDENT_NAME_KEY + " TEXT NOT NULL, " +                    // Added space
                    STUDENT_ROLL_KEY + " INTEGER, " +                          // Added space
                    "FOREIGN KEY (" + C_ID + ") REFERENCES " + CLASS_TABLE_NAME + " (" + C_ID + ")" +
                    ");";
    private static final String DROP_STUDENT_TABLE = "DROP TABLE IF EXISTS " + STUDENT_TABLE_NAME;
    private static final String SELECT_STUDENT_TABLE = "SELECT * FROM " + STUDENT_TABLE_NAME;  // Added space

    // Status Table
    public static final String STATUS_TABLE_NAME = "STATUS_TABLE";
    public static final String STATUS_ID = "_STATUS_ID";
    public static final String DATE_KEY = "STATUS_DATE";
    public static final String STATUS_KEY = "STATUS";
    public static final String CREATE_STATUS_TABLE =
            "CREATE TABLE " + STATUS_TABLE_NAME + " (" +
                    STATUS_ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +  // Added space
                    S_ID + " INTEGER NOT NULL, " +                                 // Added space
                    DATE_KEY + " DATE NOT NULL, " +                                // Added space
                    STATUS_KEY + " TEXT NOT NULL, " +                              // Added space
                    "UNIQUE (" + S_ID + ", " + DATE_KEY + "), " +
                    "FOREIGN KEY (" + S_ID + ") REFERENCES " + STUDENT_TABLE_NAME + " (" + S_ID + ")" +
                    ");";
    private static final String DROP_STATUS_TABLE = "DROP TABLE IF EXISTS " + STATUS_TABLE_NAME;
    private static final String SELECT_STATUS_TABLE = "SELECT * FROM " + STATUS_TABLE_NAME;

    private static final int VERSION = 1;

    public DBHelper(@Nullable Context context) {
        super(context, "Attendance.db", null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_CLASS_TABLE);
        db.execSQL(CREATE_STUDENT_TABLE);
        db.execSQL(CREATE_STATUS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        try {
            db.execSQL(DROP_CLASS_TABLE);
            db.execSQL(DROP_STUDENT_TABLE);
            db.execSQL(DROP_STATUS_TABLE);
            onCreate(db);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Method to add a class
    long addClass(String className, String subjectName) {
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(CLASS_NAME_KEY, className);
        values.put(SUBJECT_NAME_KEY, subjectName);
        return database.insert(CLASS_TABLE_NAME, null, values);
    }

    long updateClass(long cid,String className, String subjectName) {
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(CLASS_NAME_KEY, className);
        values.put(SUBJECT_NAME_KEY, subjectName);
        return database.update(CLASS_TABLE_NAME,values,C_ID + "=?", new String[]{String.valueOf(cid)});
    }

    // Method to get all classes
    Cursor getClassTable() {
        SQLiteDatabase database = this.getReadableDatabase();
        return database.rawQuery(SELECT_CLASS_TABLE, null);
    }

    // Method to delete a class
    int deleteClass(long cid) {
        SQLiteDatabase database = this.getWritableDatabase();
        return database.delete(CLASS_TABLE_NAME, C_ID + "=?", new String[]{String.valueOf(cid)});
    }

    long addStudent(long cid,int roll,String name){
        SQLiteDatabase database=this.getWritableDatabase();
        ContentValues values= new ContentValues();
        values.put(C_ID,cid);
        values.put(STUDENT_ROLL_KEY,roll);
        values.put(STUDENT_NAME_KEY,name);
        return database.insert(STUDENT_TABLE_NAME,null,values);
    }

    Cursor getStudentTable(long cid){
        SQLiteDatabase database=this.getReadableDatabase();
        return database.query(STUDENT_TABLE_NAME,null,C_ID+"=?",new String[]{String.valueOf(cid)},null,null,STUDENT_ROLL_KEY);
    }

    int deleteStudent(long sid){
        SQLiteDatabase database=this.getReadableDatabase();
        return database.delete(STUDENT_TABLE_NAME,S_ID+"=?",new String[]{String.valueOf(sid)});
    }

    long updateStudent(long sid,String name){
        SQLiteDatabase database=this.getWritableDatabase();
        ContentValues values=new ContentValues();
        values.put(STUDENT_NAME_KEY,name);
        return database.update(STUDENT_TABLE_NAME,values,S_ID+"=?",new String[]{String.valueOf(sid)});
    }

}