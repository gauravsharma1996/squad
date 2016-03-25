package com.example.squad_test;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Environment;
import android.widget.Toast;

import com.example.squad_test.MainActivity;
import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpATTRS;
import com.jcraft.jsch.SftpException;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Objects;
import java.util.Vector;

/**
 * Created by gaurav pc on 29-12-2015.
 */
 public class ftpdownload extends AsyncTask<Integer, Integer, Void>
    {
        private ProgressDialog dialog=null;
        OutputStream out;
        private Context context;
        boolean flag_timeout=false;
        boolean flag_filenot=false;
        long file_recieve=0;
        int percent=0,count_pre=0;
        long filesize=0;
        int i=0,notify=3;
        ChannelSftp.LsEntry lsEntry = null;
        SftpATTRS attrs = null;
        JSch jsch;
        Session session = null;
        File sdCard,dir,file=null;


        public ftpdownload(Context cxt)
        {
            context=cxt;
            dialog=new ProgressDialog(cxt);

        }
        @Override
        protected void onPreExecute() {
            this.dialog.setMessage("Downloading...");
            this.dialog.show();
        }


        @Override
        protected Void doInBackground(Integer... params) {
            jsch = new JSch();
            if(params[0]==1) {
                sdCard = Environment.getExternalStorageDirectory();
                dir = new File(sdCard.getAbsolutePath() + "/squad/video");
                dir.mkdirs();
                file = new File(dir, "quadvideo.mp4");
            }
            else if(params[0]==2)
            {
                sdCard = Environment.getExternalStorageDirectory();
                dir = new File(sdCard.getAbsolutePath() + "/squad/log");
                dir.mkdirs();
                file = new File(dir, "log.dat");
            }

            if(!file.exists())
            {
                try {
                    file.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            int read = 0;
            byte[] bytes = new byte[4096];
            try {
                session = jsch.getSession("pi", "192.168.0.88");
                session.setConfig("StrictHostKeyChecking", "no");
                session.setPassword("raspberry");

                session.connect();
                Channel channel = session.openChannel("sftp");
                channel.connect();
                ChannelSftp sftpChannel = (ChannelSftp) channel;
                if(params[0]==1) {
                    sftpChannel.cd("/home/pi/v/");
                }
                else if(params[0]==2)
                    sftpChannel.cd("/home/pi/gps/");

                out=new FileOutputStream(file);


                InputStream in;
                if(params[0]==1)
                    in=sftpChannel.get("myvid.mp4");
                else
                    in=sftpChannel.get("gps-log.dat");

                while ((read = in.read(bytes)) != -1) {
                    out.write(bytes, 0, read);
                }

                in.close();
                out.close();
                sftpChannel.exit();
                session.disconnect();
            } catch (JSchException e) {
                flag_timeout=true;
                e.printStackTrace();
            } catch (SftpException e) {
                flag_filenot=true;
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }


            return null;

        }

        @Override
        protected void onPostExecute(Void a)
        {
            Toast.makeText(context,Long.toString(file_recieve),Toast.LENGTH_LONG).show();
            this.dialog.cancel();
            if(flag_timeout)
            {
                Toast.makeText(context, "Connection Timed Out...", Toast.LENGTH_LONG).show();
                MainActivity.mBuilder.setContentText("Download Failed")
                        .setProgress(0,0,false);
                MainActivity.mNotifyManager.notify(1, MainActivity.mBuilder.build());
            }
            else if(flag_filenot)
            {
                Toast.makeText(context, "File Not Found", Toast.LENGTH_LONG).show();
                MainActivity.mBuilder.setContentText("Download Failed")
                        .setProgress(0,0,false);
                MainActivity.mNotifyManager.notify(1, MainActivity.mBuilder.build());
            }
            else {
                Toast.makeText(context, "Downloaded", Toast.LENGTH_LONG).show();
                /*
                if(Objects.equals(notify,1))
                {
                    MainActivity.mBuilder.setContentText("Download complete")
                            .setProgress(0,0,false);
                    MainActivity.mNotifyManager.notify(1,MainActivity.mBuilder.build());
                }
                */
            }
        }


    }
