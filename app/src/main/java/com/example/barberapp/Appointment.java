package com.example.barberapp;

import android.view.View;

import java.sql.Time;

public class Appointment {

    String clientName;
    String time;
    String date;
    String phoneNumber;


    public Appointment( String time,String date , String phoneNumber, String clientName) {
        this.clientName = clientName;
        this.time = time;
        this.phoneNumber = phoneNumber;
        this.date = date;
    }
    public Appointment(){ }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}
