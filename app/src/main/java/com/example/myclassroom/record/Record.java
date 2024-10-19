package com.example.myclassroom.record;

import com.example.myclassroom.Constant;

public class Record {
    private final long cid;
    private final int month;
    private final int year;

    public Record(long cid, int month, int year) {
        this.cid = cid;
        this.month = month;
        this.year = year;
    }

    public long getCid() {
        return cid;
    }

    public int getMonth() {
        return month;
    }

    public int getYear() {
        return year;
    }

    public String format() {
        return Constant.getMonthString(month) +" " + year;
    }
}
