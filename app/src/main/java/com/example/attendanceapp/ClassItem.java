package com.example.attendanceapp;

public class ClassItem {

    private long cid;

    public ClassItem(long cid, String classname, String subjectName) {
        this.cid = cid;
        this.classname = classname;
        this.subjectName = subjectName;
    }

    private String classname;
    private String  subjectName;

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

    public long getCid() {
        return cid;
    }

    public void setCid(long cid) {
        this.cid = cid;
    }
}
