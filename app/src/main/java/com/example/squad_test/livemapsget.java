package com.example.squad_test;

import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by gaurav pc on 21-03-2016.
 */
public class livemapsget extends AsyncTask<Void,Integer,Void> {
    public static InputStream input=null;
    BufferedReader br=null;
    double lat=0,longi=0;
    int date=0,time=0;
    boolean first=false,draw1=false,draw2=false,fileflag=true,check,onepoint=false;
    List<LatLng> both=new ArrayList<>();
    gpsstruct latlong= new gpsstruct(lat,longi,time,date,check);
    int count=3,i;
    CameraUpdate zoom = CameraUpdateFactory.zoomTo(20);
    Context context;

    public livemapsget(Context cxt)
    {
        context=cxt;
    }


    @Override
    protected void onProgressUpdate(Integer... values){
        if(values[0]==1)
        {
            livemaps.mgooglemap.addPolyline(new PolylineOptions().add(both.get(0), both.get(1))
                    .add(both.get(0), both.get(2)).add(both.get(1), both.get(2))
                    .width(15).color(Color.rgb(255, 128, 128)));
            CameraUpdate center = CameraUpdateFactory.newLatLng(new LatLng(both.get(2).latitude, both.get(2).longitude));
            livemaps.mgooglemap.moveCamera(center);
            livemaps.mgooglemap.animateCamera(zoom);



        }
        else if(values[0]==2)
        {
            Toast.makeText(context,"Plotting Started",Toast.LENGTH_LONG).show();
            livemaps.mgooglemap.addMarker(new MarkerOptions()
                    .position(new LatLng(lat, longi))
                    .title("Starting Point")
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));
        }
    }

    @Override
    protected Void doInBackground(Void... voids) {
        if(input!=null) {
            String scurrentline;
            br = new BufferedReader(new InputStreamReader(input));
            try {
                while ((scurrentline = br.readLine()) != null) {
                    new gpslog(scurrentline, latlong);
                    check = latlong.getstatus();
                    if (check) {
                        lat = latlong.getlatitude();
                        longi = latlong.getlongitude();
                        if (lat != 2000 && longi != 2000) {
                            if (first) {
                                onepoint = true;
                                publishProgress(1);
                                /*
                                livemaps.mgooglemap.addPolyline(new PolylineOptions().add(both.get(0), both.get(1))
                                        .add(both.get(0), both.get(2)).add(both.get(1), both.get(2))
                                        .width(15).color(Color.rgb(255, 128, 128)));
                                CameraUpdate center = CameraUpdateFactory.newLatLng(new LatLng(both.get(3).latitude, both.get(3).longitude));
                                livemaps.mgooglemap.moveCamera(center);
                                livemaps.mgooglemap.animateCamera(zoom);
                                */
                                //first = true;
                                // CameraUpdate center = CameraUpdateFactory.newLatLng(new LatLng(lat, longi));
                                //CameraUpdate zoom = CameraUpdateFactory.zoomTo(15);
                                //mgooglemap.moveCamera(center);
                                //mgooglemap.animateCamera(zoom);
                                for (i = 0; i < count - 1; i++) {
                                    both.set(i, both.get(i + 1));
                                }
                                both.set(count - 1, new LatLng(lat, longi));


                            } else {
                                if (i == 0) {
                                    publishProgress(2);
                                    /*
                                    livemaps.mgooglemap.addMarker(new MarkerOptions()
                                            .position(new LatLng(lat, longi))
                                            .title("Starting Point")
                                            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));
                                            */
                                }
                                if (i < count) {
                                    both.add(i, new LatLng(lat, longi));
                                } else {
                                    first = true;
                                }
                                i++;
                            }
                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
    @Override
    protected void onPostExecute(Void a)
    {

    }
}
