package com.example.attendanceapp;

public class ClassItem {
    String classname;
    String  subjectName;

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }

    public void setClassname(String classname) {
        this.classname = classname;
    }

    public String getSubjectName() {
        return subjectName;
    }

    public String getClassname() {
        return classname;
    }



    public ClassItem(String classname,String subjectName) {
        this.classname=classname;
        this.subjectName=subjectName;

    }
}
