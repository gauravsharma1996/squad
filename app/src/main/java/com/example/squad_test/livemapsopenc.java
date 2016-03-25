package com.example.squad_test;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import java.io.IOException;
import java.net.URL;

/**
 * Created by gaurav pc on 26-01-2016.
 */
public class livemapsopenc extends AsyncTask<Void, Void, Void> {
    boolean connectionest=true;
    String connectionlog="";
    Context ctxt;
    @Override
    protected Void doInBackground(Void... voids) {
        try {
            livemapsget.input=new URL(MainActivity.ip_address+":8080/gps_data.html").openConnection().getInputStream();
        } catch (IOException e) {
            e.printStackTrace();
            connectionlog=e.toString();
            connectionest=false;
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void a){
        if(!connectionest)
        Toast.makeText(ctxt,connectionlog+"gfdhgfhjg",Toast.LENGTH_SHORT).show();

    }
    public livemapsopenc(Context cxt){
        ctxt=cxt;
    }

}
