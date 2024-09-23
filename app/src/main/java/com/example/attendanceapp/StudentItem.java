package com.example.attendanceapp;

public class StudentItem {
    private long sid;
    private int roll;
    private String name;
    private String status;

    public StudentItem(long sid,int roll,String name) {
        this.sid=sid;
        this.name = name;
        this.roll=roll;
        status="";
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setRoll(int roll) {
        this.roll = roll;
    }

    public String getName() {
        return name;
    }

    public String getStatus() {
        return status;
    }

    public int getRoll() {
        return roll;
    }

    public long getSid() {
        return sid;
    }

    public void setSid(long sid) {
        this.sid = sid;
    }
}
