package com.example.myclassroom;

import java.util.Calendar;

public class Constant {
    public static final String PRESENT = "P";
    public static final String ABSENT = "A";

    public static String getDateString(int year, int month, int day) {
        String date;
        String mDay = String.valueOf(day);
        if (mDay.length() == 1) mDay = "0" + mDay;
        String mMonth = String.valueOf(month + 1);
        if (mMonth.length() == 1) mMonth = "0" + mMonth;
        String mYear = String.valueOf(year);
        //mYear = mYear.substring(2, 4);
        date = mYear + "-" + mMonth + "-" + mDay;
        return date;
    }

    public static String getToday() {
        Calendar calendar = Calendar.getInstance();
        return getDateString(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH));
    }

    public static String getCurrentTime() {
        Calendar calendar = Calendar.getInstance();
        return calendar.get(Calendar.HOUR_OF_DAY) + ":" + calendar.get(Calendar.MINUTE) + ":" + calendar.get(Calendar.SECOND);
    }

    public static String getTodayWithCurrentTime() {
        return getCurrentTime() + "   " + getToday();
    }


    public static String getMonthString(int month){
        switch (month) {
            case 1:
                return "January";
            case 2:
                return "February";
            case 3:
                return "March";
            case 4:
                return "April";
            case 5:
                return "May";
            case 6:
                return "June";
            case 7:
                return "July";
            case 8:
                return "August";
            case 9:
                return "September";
            case 10:
                return "October";
            case 11:
                return "November";
            case 12:
                return "December";
            default:
                return "####";
        }

    }

    public static int getMonthSize(int month,int year) {
        switch (month) {
            case 4:
            case 6:
            case 9:
            case 11:
                return 30;
            case 2:
                boolean isLeap = year % 4 == 0 && year % 100 != 0 || year % 400 == 0;
                return isLeap ? 29 : 28;
            default:
                return 31;
        }
    }
}























/*

package com.example.mysqlitedbtopdffile;

        import android.content.ContentValues;
        import android.content.pm.PackageManager;
        import android.database.Cursor;
        import android.database.sqlite.SQLiteDatabase;
        import android.graphics.Paint;
        import android.graphics.pdf.PdfDocument;
        import android.os.Bundle;
        import android.os.Environment;
        import android.view.View;
        import android.widget.EditText;
        import android.widget.TextView;

        import androidx.appcompat.app.AppCompatActivity;
        import androidx.core.app.ActivityCompat;

        import java.io.File;
        import java.io.FileOutputStream;
        import java.io.IOException;

        import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
        import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

public class MainActivity extends AppCompatActivity {

    private mySQLiteDBHandler sqlLiteDBHandler;
    private EditText editTextSerialNumberInsert;
    private EditText editTextSerialNumberFetch;
    private EditText editTextInsert;
    private TextView textViewDisplay;

    private SQLiteDatabase sqLiteDatabase;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ActivityCompat.requestPermissions(this,new String[]{READ_EXTERNAL_STORAGE, WRITE_EXTERNAL_STORAGE}, PackageManager.PERMISSION_GRANTED);

        try {
            sqlLiteDBHandler = new mySQLiteDBHandler(this,"PDFDatabase", null,1);
            sqLiteDatabase = sqlLiteDBHandler.getWritableDatabase();
            sqLiteDatabase.execSQL("CREATE TABLE PDFTable(SerialNumber TEXT, Text TEXT)");
        }
        catch (Exception e){
            e.printStackTrace();
        }
        editTextInsert = findViewById(R.id.editText2);
        editTextSerialNumberInsert = findViewById(R.id.editText);
        editTextSerialNumberFetch = findViewById(R.id.editText3);
        textViewDisplay = findViewById(R.id.textView);
    }

    public void InsertUpdatedButton(View view){
        ContentValues contentValues = new ContentValues();
        contentValues.put("SerialNumber", editTextSerialNumberInsert.getText().toString());
        contentValues.put("Text", editTextInsert.getText().toString());
        sqLiteDatabase.insert("PDFTable",null,contentValues);
        sqLiteDatabase.update("PDFTable", contentValues, null,null);
    }

    public  void CreatePDF(View view){
        String query = "Select Text from PDFTable where SerialNumber=" + editTextSerialNumberFetch.getText().toString();
        Cursor cursor = sqLiteDatabase.rawQuery(query,null);
        try {
            cursor.moveToFirst();
            textViewDisplay.setText(cursor.getString(0));
        }
        catch (Exception e){
            e.printStackTrace();
            textViewDisplay.setText("");
            return;
        }

        PdfDocument pdfDocument = new PdfDocument();
        PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(300, 600,1).create();
        PdfDocument.Page page = pdfDocument.startPage(pageInfo);
        page.getCanvas().drawText(cursor.getString(0),10, 25, new Paint());
        pdfDocument.finishPage(page);
        String filePath = Environment.getExternalStorageDirectory().getPath()+"/Download/"+editTextSerialNumberFetch.getText().toString()+".pdf";
        File file = new File(filePath);
        try {
            pdfDocument.writeTo(new FileOutputStream(file));
        } catch (IOException e) {
            e.printStackTrace();
        }
        pdfDocument.close();
    }
}

--------------

        package com.example.mysqlitedbtopdffile;

        import android.content.Context;
        import android.database.sqlite.SQLiteDatabase;
        import android.database.sqlite.SQLiteOpenHelper;
        import androidx.annotation.Nullable;

public class mySQLiteDBHandler extends SQLiteOpenHelper {
    public mySQLiteDBHandler(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
    }
}*/