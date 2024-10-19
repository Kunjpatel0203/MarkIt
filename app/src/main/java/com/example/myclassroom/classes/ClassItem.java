package com.example.myclassroom.classes;

public class ClassItem {
    private long id;
    private String section, course, code;
    private int present, absent;

    public ClassItem(String section, String course, String code) {
        this.section = section;
        this.course = course;
        this.code = code;
    }

    public ClassItem(long id, String section, String course, String code, int present, int absent) {
        this.id = id;
        this.section = section;
        this.course = course;
        this.code = code;
        this.present = present;
        this.absent = absent;
    }

    public int getAbsent() {
        return absent;
    }

    public int getPresent() {
        return present;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getSection() {
        return section;
    }

    public void setSection(String section) {
        this.section = section;
    }

    public String getCourse() {
        return course;
    }

    public void setCourse(String course) {
        this.course = course;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
