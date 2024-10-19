package com.example.myclassroom.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DbHelper extends SQLiteOpenHelper {
    private static final String DB_NAME = "classroom.db";
    private static final int VERSION = 1;

    //====================================CLASS TABLE INFO==========================================

    public static final String CLASS_TABLE_NAME = "class";
    public static final String C_ID = "_id";
    public static final String SECTION_KEY = "_class";
    public static final String COURSE_KEY = "_course";
    public static final String CODE_KEY = "_code";

    private static final String CREATE_CLASS_TABLE =
            "CREATE TABLE " + CLASS_TABLE_NAME +
                    "(" +
                    C_ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                    SECTION_KEY + " TEXT NOT NULL, " +
                    COURSE_KEY + " TEXT NOT NULL, " +
                    CODE_KEY + " TEXT NOT NULL, " +
                    "UNIQUE (" + SECTION_KEY + "," + COURSE_KEY + ")" +
                    ");";

    private static final String DROP_CLASS_TABLE = "DROP TABLE IF EXISTS " + CLASS_TABLE_NAME;

    //====================================STUDENT TABLE INFO=======================================
    public static final String STUDENT_TABLE_NAME = "student";
    public static final String S_ID = "_id";
    public static final String CLASS_ID_KEY = "Class_ID";
    public static final String STUDENT_ROLL_KEY = "roll";
    public static final String STUDENT_NAME_KEY = "name";

    private static final String CREATE_STUDENT_TABLE =
            "CREATE TABLE " + STUDENT_TABLE_NAME +
                    "(" +
                    S_ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                    CLASS_ID_KEY + " INTEGER NOT NULL, " +
                    STUDENT_ROLL_KEY + " TEXT NOT NULL, " +
                    STUDENT_NAME_KEY + " TEXT NOT NULL, " +
                    " FOREIGN KEY(" + CLASS_ID_KEY + ") REFERENCES " + CLASS_TABLE_NAME + "(" + C_ID + ")" +
                    " ON DELETE CASCADE, "+
                    " UNIQUE (" + CLASS_ID_KEY + ", " + STUDENT_ROLL_KEY + ")" +
                    ");";


    private static final String DROP_STUDENT_TABLE = "DROP TABLE IF EXISTS " + STUDENT_TABLE_NAME;

    //====================================STATUS TABLE INFO========================================
    public static final String STATUS_TABLE_NAME = "status_table";
    public static final String STATUS_ID = "_id";
    public static final String STUDENT_ID_KEY = "sid";
    public static final String STUDENT_CID_KEY = "cid";
    public static final String DATE_KEY = "date";
    public static final String STATUS_KEY = "status";

    private static final String CREATE_STATUS_TABLE =
            "CREATE TABLE " + STATUS_TABLE_NAME +
                    "( " +
                    STATUS_ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                    STUDENT_ID_KEY + " INTEGER NOT NULL, " +
                    STUDENT_CID_KEY + " INTEGER NOT NULL, " +
                    DATE_KEY + " DATE NOT NULL, " +
                    STATUS_KEY + " TEXT NOT NULL, " +
                    " UNIQUE (" + STUDENT_ID_KEY + "," + DATE_KEY + ")," +
                    " FOREIGN KEY(" + STUDENT_ID_KEY + ") REFERENCES " + STUDENT_TABLE_NAME + "(" + S_ID + ")" +
                    " ON DELETE CASCADE, " +
                    " FOREIGN KEY(" + STUDENT_CID_KEY + ") REFERENCES " + CLASS_TABLE_NAME + "(" + C_ID + ")" +
                    " ON DELETE CASCADE " +
                    ");";

    private static final String DROP_STATUS_TABLE = "DROP TABLE IF EXISTS " + STATUS_TABLE_NAME;
    public static final String SELECT_STATUS_TABLE = "SELECT * FROM " + STATUS_TABLE_NAME;

    //=============================================================================================


    public DbHelper(Context context) {
        super(context, DB_NAME, null, VERSION);
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
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onConfigure(SQLiteDatabase db) {
        super.onConfigure(db);
        db.setForeignKeyConstraintsEnabled(true);
    }

    public Cursor query(String table, String[] columns, String whereClause, String[] whereArgs){
        return this.getReadableDatabase().query(table, columns, whereClause, whereArgs, null, null, null);
    }
    public Cursor query(String table, String[] columns,String whereClause, String[] whereArgs,String groupBy, String orderBy){
        return this.getReadableDatabase().query(table, columns, whereClause, whereArgs, groupBy, null, orderBy);
    }

    public Cursor runSql(String sql, String[] selectionArgs){
       return this.getReadableDatabase().rawQuery(sql,selectionArgs);
    }

    public long insert(String table, ContentValues values) {
        return this.getWritableDatabase().insert(table, null, values);
    }

    public int update(String table, ContentValues values, String whereClause, String[] whereArgs) {
        return this.getWritableDatabase().update(table, values, whereClause, whereArgs);
    }


    public int delete(String table, String whereClause, String[] whereArgs) {
        return this.getReadableDatabase().delete(table, whereClause, whereArgs);
    }
}

