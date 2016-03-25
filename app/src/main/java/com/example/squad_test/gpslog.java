package com.example.squad_test;

import android.graphics.Color;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Objects;

/**
 * Created by gaurav pc on 27-12-2015.
 */
public class gpslog {
    BufferedReader br;
    String[] str;
    boolean draw1,draw2;
    double alat=-1,alongi=-1,ideg=-1,imin=-1,lat=-1,longi=-1;
    int date=-1,time=-1;

    public  gpslog(String scurrentline, gpsstruct temp) {
                    str = scurrentline.split(",");
                    draw1 = false;
                    draw2 = false;
                    if (Objects.equals(str[0], "$GPGGA")) {
                        temp.setdate(-1);
                        if(!Objects.equals(str[1],""))
                        {
                            time=(int)Double.parseDouble(str[1]);
                            temp.settime(time);
                        }
                        else
                        {
                            temp.settime(-1);
                        }

                        if (!Objects.equals(str[2], "")) {
                            alat = Double.parseDouble(str[2]);
                            ideg = (int) alat / 100;
                            imin = (alat % 100);
                            imin = (double) Math.round(imin * 100000) / 100000;
                            lat = ideg + (imin / 60);
                            if (str[3] == "S")
                                lat = -lat;
                            draw1 = true;

                        } else {
                            draw1 = false;
                        }
                        if (!Objects.equals(str[4], "")) {
                            alongi = Double.parseDouble(str[4]);
                            ideg = (int) alongi / 100;
                            imin = (alongi % 100);
                            imin = (double) Math.round(imin * 100000) / 100000;
                            longi = ideg + (imin / 60);
                            if (str[5] == "W")
                                longi = -longi;
                            draw2 = true;
                        } else {
                            draw2 = false;
                        }
                        if (draw1 && draw2) {
                            temp.setlatitude(lat);
                            temp.setlongitude(longi);
                            temp.setstatus(true);
                        }
                        else
                        {
                            temp.setstatus(false);
                        }

                    }

                    else if(Objects.equals(str[0],"$GPRMC"))
                    {
                        if(!Objects.equals(str[1],""))
                        {
                            time=(int)Double.parseDouble(str[1]);
                            temp.settime(time);
                        }
                        else
                            temp.settime(-1);
                        if(!Objects.equals(str[9],""))
                        {
                            date=Integer.parseInt(str[9]);
                            temp.setdate(date);
                        }
                        else
                            temp.setdate(-1);

                        if (!Objects.equals(str[3], "")) {
                            alat = Double.parseDouble(str[3]);
                            ideg = (int) alat / 100;
                            imin = (alat % 100);
                            imin = (double) Math.round(imin * 100000) / 100000;
                            lat = ideg + (imin / 60);
                            if (str[4] == "S")
                                lat = -lat;
                            draw1 = true;

                        } else {
                            draw1 = false;
                        }
                        if (!Objects.equals(str[5], "")) {
                            alongi = Double.parseDouble(str[5]);
                            ideg = (int) alongi / 100;
                            imin = (alongi % 100);
                            imin = (double) Math.round(imin * 100000) / 100000;
                            longi = ideg + (imin / 60);
                            if (str[6] == "W")
                                longi = -longi;
                            draw2 = true;
                        } else {
                            draw2 = false;
                        }
                        if (draw1 && draw2) {
                            temp.setlatitude(lat);
                            temp.setlongitude(longi);
                            temp.setstatus(true);
                        }
                        else
                        {
                            temp.setstatus(false);
                        }

                    }
                else
                        temp.setstatus(false);
                }
}
