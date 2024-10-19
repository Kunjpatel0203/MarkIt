package com.example.myclassroom.student;

public class Student {
    private long id;
    private String roll;
    private String name;
    private long classId;

    public Student(String roll, String name) {
        this.roll = roll;
        this.name = name;
    }

    public Student(String roll, String name, long classId) {
        this.roll = roll;
        this.name = name;
        this.classId = classId;
    }

    public Student(long id,String roll, String name,long classId) {
        this.id = id;
        this.roll = roll;
        this.name = name;
        this.classId = classId;
    }
    public Student(long id,String roll, String name) {
        this.id = id;
        this.roll = roll;
        this.name = name;
    }

    public long getClassId() {
        return classId;
    }

    public void setClassId(long classId) {
        this.classId = classId;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getRoll() {
        return roll;
    }

    public void setRoll(String roll) {
        this.roll = roll;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
