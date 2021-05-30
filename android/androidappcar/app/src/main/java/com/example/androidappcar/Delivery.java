package com.example.androidappcar;

public class Delivery {
    String patient;
    String parcel;
    String location;
    String date;
    String time;

    public Delivery(String patient,String parcel,String location, String date, String time){
        this.patient = patient;
        this.parcel = parcel;
        this.location = location;
        this.date = date;
        this.time = time;
    }



    public String getPatient() {
        return patient;
    }

    public String getParcel() {
        return parcel;
    }

    public String getLocation() {
        return location;
    }

    public String getDate() {
        return date;
    }

    public String getTime() {
        return time;
    }

    public void setPatient(String patient) {
        this.patient = patient;
    }

    public void setParcel(String parcel) {
        this.parcel = parcel;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
