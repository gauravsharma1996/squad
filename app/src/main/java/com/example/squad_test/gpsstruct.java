package com.example.squad_test;

/**
 * Created by gaurav pc on 27-12-2015.
 */
public class gpsstruct {
    private double latitude,longitude;
    private int time,date;
    boolean status;

    public gpsstruct(double latitude,double longitude,int time,int date,boolean status)
    {
        this.date=date;
        this.latitude=latitude;
        this.longitude=longitude;
        this.time=time;
        this.status=status;
    }
    public double getlatitude(){ return latitude;}
    public double getlongitude(){return longitude;}
    public int gettime(){return time;}
    public int getdate(){return date;}
    public boolean getstatus(){return status;}

    public void setlatitude(double latitude){this.latitude=latitude;}
    public void setlongitude(double longitude){this.longitude=longitude;}
    public void settime(int time){this.time=time;}
    public void setdate(int date){this.date=date;}
    public void setstatus(boolean status){this.status=status;}
}
