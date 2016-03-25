package com.example.squad_test;

import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Environment;
import android.renderscript.Script;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationCompat.Builder;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.io.CopyStreamAdapter;
import org.apache.commons.net.io.CopyStreamEvent;
import org.apache.commons.net.io.CopyStreamListener;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Objects;

/**
 * Created by gaurav pc on 29-12-2015.
 */
 public class appupdate extends AsyncTask<Integer, Integer, Void>
    {
        private ProgressDialog dialog=null;
        OutputStream out;
        FTPClient ftpclient;
        private Context context;
        BufferedReader br=null;
        boolean update_avail=false,flag1=false,flag_continue=false;
        public static double cur_version;
        int i;
        File sdCard,dir,file=null;
        long filesize;
        InputStream in;
        long file_recieve=0;
        int percent=0,count_pre=0;
        public appupdate(Context cxt)
        {
            context=cxt;
            dialog=new ProgressDialog(cxt);
        }
        @Override
        protected void onPreExecute() {

            if(MainActivity.flagupdate==true) {
                dialog.setMessage("Downloading Update..");
                dialog.show();
            }
            else
            {
                dialog.setMessage("Checking for update..");
                dialog.show();
            }
        }

        @Override
        protected void onProgressUpdate(Integer... values){

                MainActivity.mBuilder.setProgress(100, values[0], false);
                MainActivity.mBuilder.setContentText("Download in Progress " + (values[0].toString()) + "%");
                MainActivity.mNotifyManager.notify(1, MainActivity.mBuilder.build());


        }

        @Override
        protected Void doInBackground(Integer... params) {
                sdCard = Environment.getExternalStorageDirectory();
                dir = new File(sdCard.getAbsolutePath() + "/squad/app");
                dir.mkdirs();
                file = new File(dir, "config.txt");
            try {
                FTPClient ftpClient=new FTPClient();
                ftpClient.connect("androidsquad.netau.net");
                ftpClient.login("a8779466", "squad");
                ftpClient.changeWorkingDirectory("/public_html/");
                ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
                FTPFile[] files=ftpClient.listFiles();

                for(i=0;i<files.length;i++)
                {
                    if(Objects.equals(files[i].getName(),"app-debug.apk"))
                    {
                        filesize=files[i].getSize();
                        break;
                    }
                }
                if(!file.exists())
                {
                    try {
                        file.createNewFile();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                try {
                    out=new FileOutputStream(file);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }


                int read = 0;
                byte[] bytes = new byte[4096];

                in=ftpClient.retrieveFileStream("config.txt");
                while ((read = in.read(bytes)) != -1) {
                    out.write(bytes, 0, read);
                }
                flag1=true;
                in.close();
                out.close();
                ftpClient.logout();
                ftpClient.disconnect();
                try {
                    String scurrentline;
                    br = new BufferedReader(new FileReader(file));
                    scurrentline=br.readLine();
                    cur_version=Double.parseDouble(scurrentline);
                    if(cur_version!=MainActivity.version)
                    {
                        update_avail=true;
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    try {
                        if (br != null)
                            br.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if(MainActivity.flagupdate && update_avail)
                {
                    ftpClient.connect("androidsquad.netau.net");
                    ftpClient.login("a8779466", "squad");
                    ftpClient.changeWorkingDirectory("/public_html/");
                    ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
                    file = new File(dir, "squadapp.apk");
                    if(!file.exists())
                    {
                        try {
                            file.createNewFile();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    try {
                        out=new FileOutputStream(file);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                    InputStream in1;
                    byte[] bytes1=new byte[1024];

                    in1=ftpClient.retrieveFileStream("app-debug.apk");
                    while ((read = in1.read(bytes1)) != -1) {
                        file_recieve+=read;
                        percent = (int)(file_recieve*100/filesize);
                        if (Objects.equals(percent%5,0)&&(count_pre!=percent)) { // 20 updates every 5%, 10 every 10% and 5 every 20%, etc.
                            publishProgress(percent);
                            count_pre=percent;
                        }
                        out.write(bytes1, 0, read);
                    }

                    in.close();
                    out.close();
                    ftpClient.logout();
                    ftpClient.disconnect();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
        public AsyncResponse delegate = null;
        public appupdate(AsyncResponse delegate)
        {
            this.delegate = delegate;
        }
        @Override
        protected void onPostExecute(Void a)
        {
            dialog.cancel();
            if(flag_continue)
            {
                Toast.makeText(context,"adapter correct",Toast.LENGTH_LONG).show();
            }
            if(update_avail)
            {
                if(MainActivity.flagupdate)
                {
                    MainActivity.mBuilder.setContentText("Download complete")
                            .setProgress(0,0,false);
                    MainActivity.mNotifyManager.notify(1,MainActivity.mBuilder.build());
                    Toast.makeText(context,"Update Downloaded",Toast.LENGTH_LONG).show();
                    delegate.processFinish(1);
                   // MainActivity.mBuilder.setContentText("Download complete");
                    // Removes the progress bar
                    //MainActivity.mBuilder.setProgress(0, 0, false);
                    //MainActivity.mNotifyManager.notify(1, MainActivity.mBuilder.build());
                }
                else
                {
                    Toast.makeText(context,"Update Available",Toast.LENGTH_LONG).show();
                    delegate.processFinish(2);
                }
            }
            else if(flag1)
            {
                Toast.makeText(context,"No update available",Toast.LENGTH_LONG).show();
                delegate.processFinish(0);
            }
            else
            {
                Toast.makeText(context,"No Connection",Toast.LENGTH_LONG).show();
                delegate.processFinish(0);
            }
        }
    }
