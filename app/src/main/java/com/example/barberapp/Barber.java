package com.example.barberapp;

import java.sql.Time;

public class Barber extends User{

    String shopName;
    String shopAddress;
    String workDays;
    String startTime;
    String endTime;



    public Barber(String name, String age, String gender, String phoneNumber, String email, String shopName, String shopAddress, String workDays
            ,String start, String end) {
        super(name, age, gender, phoneNumber, email);
        this.shopName = shopName;
        this.shopAddress = shopAddress;
        this.workDays = workDays;
        this.startTime = start;
        this.endTime = end;
    }

    public Barber() {
    }

    public String getShopName() {
        return shopName;
    }

    public String getShopAddress() {
        return shopAddress;
    }

    public String getWorkDays() {
        return workDays;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public void setShopAddress(String shopAddress) {
        this.shopAddress = shopAddress;
    }

    public void setWorkDays(String workDays) {
        this.workDays = workDays;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndtime() {
        return endTime;
    }

    public void setEndtime(String endtime) {
        this.endTime = endtime;
    }
}
