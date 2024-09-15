package com.example.attendanceapp;

public class StudentItem {
    private String roll;
    private String name;
    private String status;

    public StudentItem(String name,String roll) {
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

    public void setRoll(String roll) {
        this.roll = roll;
    }

    public String getName() {
        return name;
    }

    public String getStatus() {
        return status;
    }

    public String getRoll() {
        return roll;
    }
}
