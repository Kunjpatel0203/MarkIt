package com.example.myclassroom.print;

import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.myclassroom.Constant;
import com.example.myclassroom.R;
import com.example.myclassroom.database.DbHelper;
import com.example.myclassroom.record.Record;
import com.example.myclassroom.student.Student;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class PrintActivity extends AppCompatActivity {
    private static final int REQUEST_CODE_ASK_PERMISSIONS = 111;
    private PrintController controller;
    private Record record;
    private int MONTH_SIZE;
    private TableLayout table;
    private TextView title;
    private TableRow[] row;
    private TextView[] id, name;
    private TextView[] status;
    private TextView[][] allStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_print);

        Intent intent = getIntent();
        long cid = intent.getLongExtra("cid", -1);
        int month = intent.getIntExtra("month", -1);
        int year = intent.getIntExtra("year", -1);

        MONTH_SIZE = Constant.getMonthSize(month, year);

        record = new Record(cid, month, year);
        DbHelper dbHelper = new DbHelper(this);
        PrintService printService = new PrintService(dbHelper, record);
        controller = new PrintController(printService);

        initWidgets();

        generateTable();
    }

    void initWidgets() {
        table = findViewById(R.id.sheetTable);
        title = findViewById(R.id.title_table);
        title.setText(record.format());
        findViewById(R.id.printPdf).setOnClickListener(v -> generatePDF());

    }

    private void generateTable() {
        int size = controller.students.size() + 1;
        row = new TableRow[size];

        //set table columns sizes
        id = new TextView[size];
        name = new TextView[size];
        allStatus = new TextView[size][MONTH_SIZE + 2];

        //setup the weights for columns
        TableRow.LayoutParams idParam = new TableRow.LayoutParams(0,
                TableRow.LayoutParams.WRAP_CONTENT, .1f);
        TableRow.LayoutParams nameParam = new TableRow.LayoutParams(0,
                TableRow.LayoutParams.WRAP_CONTENT, .1f);
        TableRow.LayoutParams statusParam = new TableRow.LayoutParams(0,
                TableRow.LayoutParams.WRAP_CONTENT, .1f);

        //initialize views(columns)
        for (int i = 0; i < size; i++) {
            id[i] = new TextView(this);
            name[i] = new TextView(this);
            for (int j = 0; j < MONTH_SIZE + 2; j++) {
                allStatus[i][j] = new TextView(this);
            }
        }
        //setting header
        id[0].setText("ROLL");
        id[0].setTypeface(id[0].getTypeface(), Typeface.BOLD);
        name[0].setText("NAME");
        name[0].setTypeface(name[0].getTypeface(), Typeface.BOLD);
        for (int j = 1; j <= MONTH_SIZE; j++) {
            allStatus[0][j].setText(String.valueOf(j));
            allStatus[0][j].setTypeface(allStatus[0][j].getTypeface(), Typeface.BOLD);
        }
        allStatus[0][MONTH_SIZE + 1].setText("Per.");
        allStatus[0][MONTH_SIZE + 1].setTypeface(allStatus[0][MONTH_SIZE + 1].getTypeface(), Typeface.BOLD);

        for (int i = 1; i <= controller.students.size(); i++) {
            Student student = controller.students.get(i - 1);
            id[i].setText(student.getRoll());
            name[i].setText(student.getName());
            Map<Integer, String> monthStatus = controller.getStudentStatus(student.getId());
            if (monthStatus == null) monthStatus = new HashMap<>();
            int present = 0;
            int absent = 0;
            for (int j = 1; j <= MONTH_SIZE; j++) {//months starts with j=1 ends with j=monthSize
                String sStatus = monthStatus.get(j);
                if (sStatus == null) sStatus = "";
                allStatus[i][j].setText(sStatus);

                if (sStatus.equals(Constant.ABSENT)) {
                    absent++;
                    allStatus[i][j].setBackgroundColor(Color.parseColor("#33FF0000"));
                } else if (sStatus.equals(Constant.PRESENT)) {
                    present++;
                } else if (sStatus.equals("")) {
                    allStatus[i][j].setBackgroundColor(Color.parseColor("#33A5A5A5"));
                }
            }
            int percent = present == 0 && absent == 0 ? 0 : (100 * present / (present + absent));
            allStatus[i][MONTH_SIZE + 1].setText(percent + "%");
        }

        for (int i = 0; i < size; i++) {

            //initializing row
            row[i] = new TableRow(this);

            //set the background of row
            if (i % 2 == 0) row[i].setBackgroundColor(Color.parseColor("#EEEEEE"));
            else row[i].setBackgroundColor(Color.parseColor("#E4E4E4"));

            //set divider between columns
            row[i].setShowDividers(TableRow.SHOW_DIVIDER_MIDDLE | TableRow.SHOW_DIVIDER_BEGINNING | TableRow.SHOW_DIVIDER_END);
            row[i].setDividerDrawable(getDrawable(android.R.drawable.divider_horizontal_bright));


            // id[i].setLayoutParams(idParam);
            row[i].addView(id[i]);
            id[i].setPadding(16, 16, 16, 16);

            // name[i].setLayoutParams(nameParam);
            name[i].setPadding(16, 16, 16, 16);
            row[i].addView(name[i]);
            for (int j = 1; j < MONTH_SIZE + 2; j++) {
                //  allStatus[i][j].setLayoutParams(statusParam);
                allStatus[i][j].setPadding(16, 16, 16, 16);
                row[i].addView(allStatus[i][j]);
            }

            table.addView(row[i]);
        }
        table.setShowDividers(TableLayout.SHOW_DIVIDER_MIDDLE);

    }

    private void generatePDF() {
        boolean hasPermission =
                ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
        if (hasPermission || Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            try {

                Toast.makeText(this, "Saving...", Toast.LENGTH_SHORT).show();
                createPdf();
                Toast.makeText(this, "Saved to Documents", Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        } else if (shouldShowRequestPermissionRationale(WRITE_EXTERNAL_STORAGE)) {
            System.out.println("Requesting permission ask permission");
            askPermission();
        } else {
            System.out.println("NOT Requesting ");
            requestPermissions(new String[]{WRITE_EXTERNAL_STORAGE}, REQUEST_CODE_ASK_PERMISSIONS);
        }
    }

    private void askPermission() {
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                REQUEST_CODE_ASK_PERMISSIONS);
    }

    private Phrase cellText(String text) {
        return new Phrase(text, FontFactory.getFont(FontFactory.TIMES_ROMAN, 10, Font.NORMAL));
    }

    private void createPdf() throws FileNotFoundException, DocumentException {
//        File file = new File(Environment.getExternalStorageDirectory(), "MyClassroom");
        File file = new File("/storage/emulated/0/Documents");
        boolean isDirectoryCreated = file.exists();
        if (!file.exists()) isDirectoryCreated = file.mkdir();

        if (!isDirectoryCreated) throw new FileNotFoundException("Directory not found");

        //set the file name
        int i = 0;
        File pdfFile;
        do {
            String ext = "";
            if (i != 0) ext = "(" + i + ")";

            String pdfName =
                    controller.classItem.getSection() + "_" + controller.classItem.getCourse() +
                            "_" + record.format() + ext + ".pdf";
            pdfFile = new File(file.getAbsolutePath(), pdfName);

            i++;
        } while (pdfFile.exists());

        OutputStream output = new FileOutputStream(pdfFile);


        //working with the doc and pages
        Document document = new Document(PageSize.A4.rotate());

        //as i work with a table
        float[] columnSize = new float[MONTH_SIZE + 3];
        columnSize[0] = 4.f;
        columnSize[1] = 4.f;
        for (i = 2; i < columnSize.length; i++) {
            columnSize[i] = .9f;
        }
        columnSize[columnSize.length - 1] = 1.5f;
        Font font = FontFactory.getFont(FontFactory.TIMES_ROMAN, 10, Font.NORMAL);
        PdfPTable table = new PdfPTable(columnSize);

        table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
        table.getDefaultCell().setFixedHeight(16);
        table.setTotalWidth(PageSize.LETTER.rotate().getWidth());
        table.setWidthPercentage(100);
        table.getDefaultCell().setVerticalAlignment(Element.ALIGN_MIDDLE);

        //add the column to the table
        table.addCell(cellText("ROLL"));
        table.addCell(cellText("NAME"));
        for (i = 1; i <= MONTH_SIZE; i++) {
            table.addCell(cellText(String.valueOf(i)));
        }
        table.addCell(cellText("Per."));
        table.setHeaderRows(1);

        //---------------------------------data to table---------------------------------//

        for (Student student : controller.students) {
            table.addCell(cellText(student.getRoll()));
            table.addCell(cellText(student.getName()));
            Map<Integer, String> monthStatus = controller.getStudentStatus(student.getId());
            int present = 0;
            int absent = 0;
            Font absentFont = FontFactory.getFont(FontFactory.TIMES_ROMAN, 10, Font.NORMAL, BaseColor.RED);
            for (int j = 1; j <= MONTH_SIZE; j++) {
                String s = monthStatus.get(j);
                if (Objects.equals(s, Constant.PRESENT)) {
                    present++;
                    table.addCell(cellText(s));
                } else if (Objects.equals(s, Constant.ABSENT)) {
                    absent++;
                    table.addCell(new Phrase(s, absentFont));
                } else {
                    table.addCell(cellText(""));
                }
            }
            int percent = present == 0 && absent == 0 ? 0 : (100 * present / (present + absent));
            table.addCell(cellText(percent + "%"));
        }


        PdfWriter.getInstance(document, output);
        document.open();

        Font title_pdf_font = new Font(Font.FontFamily.TIMES_ROMAN, 20.0f, Font.BOLD, BaseColor.BLACK);
        Font subtitle_pdf_font = new Font(Font.FontFamily.TIMES_ROMAN, 16.0f, Font.BOLD, BaseColor.BLACK);
        Font date_pdf_font = new Font(Font.FontFamily.TIMES_ROMAN, 14.0f, Font.BOLDITALIC, BaseColor.BLACK);
        Font generate_pdf_font = new Font(Font.FontFamily.TIMES_ROMAN, 10.0f, Font.NORMAL, BaseColor.GRAY);


        Paragraph section = new Paragraph(controller.classItem.getSection(), title_pdf_font);
        Paragraph code = new Paragraph(controller.classItem.getCode(), subtitle_pdf_font);
        Paragraph course = new Paragraph(controller.classItem.getCourse(), subtitle_pdf_font);
        Paragraph attendanceDate = new Paragraph("Attendance Sheet: " + record.format() + "\n\n", date_pdf_font);
        Paragraph generateTime = new Paragraph("Generated:  " + Constant.getTodayWithCurrentTime(), generate_pdf_font);
        // Paragraph appName = new Paragraph("Created with: "+getString(R.string.app_name),generate_pdf_font);
        section.setAlignment(Element.ALIGN_CENTER);
        code.setAlignment(Element.ALIGN_CENTER);
        course.setAlignment(Element.ALIGN_CENTER);
        attendanceDate.setAlignment(Element.ALIGN_CENTER);
        generateTime.setAlignment(Element.ALIGN_LEFT);
        // appName.setAlignment(Element.ALIGN_RIGHT);
        document.add(section);
        document.add(code);
        document.add(course);
        document.add(attendanceDate);
        document.add(table);
        document.add(generateTime);
        // document.add(appName);

        document.close();
    }
}