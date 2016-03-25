package com.example.squad_test;

import java.io.File;
import java.io.FileDescriptor;
import com.jcraft.jsch.*;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.PublicKey;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.logging.LogRecord;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.annotation.TargetApi;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
//import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.ClipData.Item;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Environment;
import android.os.StrictMode;
import android.os.SystemClock;
import android.os.Vibrator;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationCompat.Builder;
import android.util.Base64;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;
import android.os.Handler;

import com.example.squad_test.util.gesture;


public class  MainActivity extends FragmentActivity implements gesture.OnFragmentInteractionListener,AsyncResponse,virtual_reality.OnFragmentInteractionListener{
	SensorManager sensorManager;
	Sensor mSensor;
	SharedPreferences sharedpreferences;
	public static NotificationManager mNotifyManager=null;
	public static NotificationCompat.Builder mBuilder=null,mBuilder1=null;

	public int id=1,divisions=5;
	public static final String MyPREFERENCES = "MyPrefs" ;
	public static final String mid_yawkey = "mid_yawkey";
	public static final String mid_rollkey = "mid_rollkey";
	public static final String mid_pitchkey = "mid_pitchkey";
	public static final String max_throtkey = "max_throtkey";
	public static final String min_throtkey = "min_throtkey";
	public static final String ip_key = "ip_key";
	public static final String min_yawkey = "min_yawkey";
	public static final String min_rollkey = "min_rollkey";
	public static final String min_pitchkey = "min_pitchkey";
	public static final String max_yawkey = "max_yawkey";
	public static final String max_rollkey = "max_rollkey";
	public static final String max_pitchkey = "max_pitchkey";
	public static final String ard_tkey = "ard_tkey";
	public static final String ard_ykey = "ard_ykey";
	public static final String ard_pkey = "ard_pkey";
	public static final String ard_rkey = "ard_rkey";
	String setup_str_t,setup_str_p,setup_str_r,setup_str_y;



	private static final int MAX_SAMPLE_SIZE = 10;
	float[] nrotangle =new float[3];
	float[] protangle =new float[3];
	List<Float>[] rollingAverage = new List[3];
	final float[] gravity =new float[3];
	final float[] gravity_c =new float[3];
	final int[] linear_acceleration =new int[3];
	final int[] plinear_acceleration =new int[3];

	float a,b,c,d;
	boolean socketcreated = false; 
	private static final int REQUEST_ENABLE_BT = 1;
	private BluetoothAdapter btAdapter = null;
	private BluetoothSocket btSocket = null;

	private OutputStream outStream = null;
	
	private static UUID MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

	boolean baro=false,mag=false,horizon=false,angle=false,noyw=false;
	int au1va=1500,au2va=1500;
	public static String ip_address="http://192.168.0.88";
	String pass_check="squad";
	
	int pt,py,pp,pr,pau1va,pau2va;
	int max_throttle=1500,min_throttle=1151,mid_throttle;
	int max_yaw=1891,min_yaw=1127,mid_yaw=1500;
	int ard_t=0,ard_y=0,ard_p=0,ard_r=0;


	public int max_pitch=1800,min_pitch=1135,mid_pitch=1500;
	float calib_ct1,calib_ct2,mult_ct1,mult_ct2;

	public int max_roll=1875,min_roll=1100,mid_roll=1500;
	EditText thmin,thmax,emidy,emidp,emidr,ip_t;
	EditText eminy,eminp,eminr,emaxy,emaxp,emaxr;

	String q,ex1,ex2,ex3;
	boolean gyro=false,init=false,init_1=false,init_2=false,init_calib_1=false,init_calib_2=false,flag_admin=false,flag_bt=true,arm_flag=false,one=false,flag_end=false;
	boolean exec=false,ctrl_flag=false,flag_naza=true,flag_normalstream=false,flag_facestream=false,flag_fingerstream=false,flag_objectstream=false;
	boolean arm_btn=false,flag_setup=false,flag_device_select=false;
	public int t=min_throttle,y=mid_yaw,p=mid_pitch,r=mid_roll;
	float cent_x1,cent_y1,cent_x2,cent_y2;
	float left_x=0,left_y=0,right_x=0,right_y=0;
	Fragment fr;
	public boolean gest_on=false,vr_on=false;
	
	Button blue,calibration,up,down,ax1,ax2,baromod,anglemod,horizonmod,magmod,bmenu,midmenu,ip_b,minmaxmenu,arm,modes;
	ToggleButton disarm;
	String a1,b1,a2,b2;
	TextView throt,yaw,roll,pitch,tvout,tvin,yaw1,throt1,arm_view;
	ImageView smallcircle1,smallcircle2,bar1,arrow,arrowb,bar2,bar3,front_i,front_i1,front_i2,sql_view,blue_img,blue_img1,up_i,left_i,quad,quadrot,nazaimg1,nazaimg2,nazaimg3;
	ImageView setting_button;
	RelativeLayout bigcircle1,bigcircle2,front_l;
	RelativeLayout fragmen;
	LinearLayout main_c;
	public static String sql_ip="192.168.0.102";
	String sql_url="jdbc:mysql://"+sql_ip+":3306/g",img_load;
	String sql_user="s";
	String sql_pass="1234";
	Thread tp,tp1;
	int flag=0,flag_load,tem;
	Connection conn;
	char ch;
	Bitmap load;
	byte[] decode;
	String[] temp_img;
	String sql;
	int j,i,blue_test=0;
	ResultSet r1,r2;
	long timei;
	int pq=0;
	float pang=(float)0,pang1=(float)0;
	float nang,nang1;
	PreparedStatement statement;
	Dialog ad;
	LinearLayout ctrl,bgrp;
	RelativeLayout lquad,rquad,cquad,leftside,rightside,whole;
	float nquad_man_x,nquad_man_y,pquad_man_y=(float)0,pquad_man_x=(float)0;
	ObjectAnimator obquad,obquad1,obnaza1,obnaza2,obnaza3;
	AnimatorSet fornaza,fornaza1,fornaza2;
	Dialog sq;
	Handler hand;
	FragmentManager fm;
	boolean pathflag=false;
	boolean pathflagonmain=false;
	public static boolean flagupdate=false,flagsend=false;
	int timecount=0;
	int ghy=0;
	Handler htest;
	public static final Double version=1.00;
	Handler handler=null,handler1=null;
	WebView reqrec,reqlog;
	Boolean recrequest=false,logreq=false,flag_btlistener=false;
	WebView webv;
	int temp_t=0,temp_y=0,temp_r=0,temp_p=0;
	Vibrator vb;
	String setup_str=null;
	ArrayAdapter<String> btarrayadapter,btarrayadapter1;
	IntentFilter filter1,filter2;
	ftpdownload asynctask1;
	RelativeLayout maponmain;


	//private static String address = "00:13:04:01:09:22";
	//private static String address = "20:15:05:18:55:70";
	private static String address = "98:D3:31:20:81:F7";




	void select_connect()
	{

		if (!iscon) {

			main_c.setAlpha(1);
			//blue.setText("Connected");
			hand = new Handler();
			blue_test = checkBTState();

			if (blue_test == 1&&flag_device_select) {

				Thread ty = new Thread() {
					@Override
					public void run() {
						while (flag_bt) {
							hand.post(new Runnable() {
								public void run() {
									blue_img.setVisibility(View.INVISIBLE);
									blue_img1.setVisibility(View.VISIBLE);
								}
							});
							try {
								Thread.sleep(200);
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
							hand.post(new Runnable() {
								public void run() {
									blue_img.setVisibility(View.VISIBLE);
									blue_img1.setVisibility(View.INVISIBLE);
								}
							});
							try {
								Thread.sleep(200);
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
						}
						flag_naza=false;
					}
				};
				ty.start();

				Thread conbt = new Thread() {
					@Override
					public void run() {
						connectBT();

						hand.post(new Runnable() {
							public void run() {

								flag_bt = false;
								//blue.setBackgroundColor(Color.parseColor("#aa00ff66"));

								AnimatorSet flip = (AnimatorSet) AnimatorInflater.loadAnimator(getApplicationContext(),
										R.animator.left_out);
								flip.setTarget(front_l);
								final AnimatorSet flip1 = (AnimatorSet) AnimatorInflater.loadAnimator(getApplicationContext(),
										R.animator.left_in);
								flip1.setTarget(main_c);
								flip.start();
								flip1.start();
								flip1.addListener(new Animator.AnimatorListener() {
									@Override
									public void onAnimationStart(Animator animator) {

									}

									@Override
									public void onAnimationEnd(Animator animator) {
										if (!ctrl_flag) {
											ObjectAnimator ob3 = ObjectAnimator.ofFloat(bigcircle1, "translationX", -bigcircle1.getWidth(), 0.0f);
											ob3.setDuration(1000);
											ObjectAnimator ob4 = ObjectAnimator.ofFloat(leftside, "alpha", 0.0f, 1.0f);
											ob3.setDuration(1000);
											AnimatorSet leftright = new AnimatorSet();
											leftright.playTogether(ob3, ob4);
											//ObjectAnimator ob5 = ObjectAnimator.ofFloat(bigcircle2, "translationX",bigcircle2.getPivotX(),bigcircle2.getPivotX()-bigcircle2.getPivotX());
											ObjectAnimator ob5 = ObjectAnimator.ofFloat(bigcircle2, "translationX", bigcircle2.getPivotX() + bigcircle2.getWidth() / 2, 0.0f);
											ob5.setDuration(1000);
											ObjectAnimator ob6 = ObjectAnimator.ofFloat(rightside, "alpha", 0.0f, 1.0f);
											ob5.setDuration(1000);
											AnimatorSet leftright1 = new AnimatorSet();
											leftright1.playTogether(ob5, ob6);
											leftright.start();
											leftright1.start();
											ctrl_flag = true;

											leftright1.addListener(new Animator.AnimatorListener() {
												@Override
												public void onAnimationStart(Animator animator) {

												}

												@Override
												public void onAnimationEnd(Animator animator) {
													ObjectAnimator ob15 = ObjectAnimator.ofFloat(lquad, "translationY", lquad.getPivotY() + lquad.getWidth() / 2, 0.0f);
													ob15.setDuration(1000);
													ObjectAnimator ob16 = ObjectAnimator.ofFloat(lquad, "alpha", 0.0f, 1.0f);
													ObjectAnimator ob17 = ObjectAnimator.ofFloat(rquad, "translationY", rquad.getPivotY() + rquad.getWidth() / 2, 0.0f);
													ob17.setDuration(1000);
													ObjectAnimator ob18 = ObjectAnimator.ofFloat(rquad, "alpha", 0.0f, 1.0f);
													ObjectAnimator ob19 = ObjectAnimator.ofFloat(cquad, "translationY", cquad.getPivotY() + cquad.getWidth() / 2, 0.0f);
													ob19.setDuration(1000);
													ObjectAnimator ob20 = ObjectAnimator.ofFloat(cquad, "alpha", 0.0f, 1.0f);
													ObjectAnimator ob21 = ObjectAnimator.ofFloat(bgrp, "alpha", 0.0f, 1.0f);
													ob21.setDuration(1000);
													AnimatorSet updown = new AnimatorSet();
													updown.playTogether(ob15, ob16, ob17, ob18, ob19, ob20, ob21);
													updown.start();
													a1 = Integer.toString(mid_yaw);
													yaw.setText("Y: " + a1);
													a1 = Integer.toString(mid_throttle);
													throt.setText("T: " + a1);
													a1 = Integer.toString(mid_pitch);
													pitch.setText("P: " + a1);
													a1 = Integer.toString(mid_roll);
													roll.setText("R: " + a1);
												}

												@Override
												public void onAnimationCancel(Animator animator) {

												}

												@Override
												public void onAnimationRepeat(Animator animator) {

												}
											});
										}
									}
									@Override
									public void onAnimationCancel(Animator animator) {

									}

									@Override
									public void onAnimationRepeat(Animator animator) {

									}
								});
								iscon = true;
								flagsend=true;
							}
						});
					}
				};
				conbt.start();
			}

		} else {
			front_l.setVisibility(View.GONE);
		}

	}
void flightmod()
{

	if(baro==true)
	{
		au2va=1000;
		//baromod.setBackgroundColor(Color.parseColor("#9900ff99"));
		//magmod.setBackgroundColor(Color.parseColor("#3fC8C8C8"));
	}
	
	else if(mag==true)
	{
		au2va=2000;
		//baromod.setBackgroundColor(Color.parseColor("#3fC8C8C8"));
		//magmod.setBackgroundColor(Color.parseColor("#9900ff99"));
	}
	else
	{
		
		au2va=1500;
		//baromod.setBackgroundColor(Color.parseColor("#3fC8C8C8"));
		//magmod.setBackgroundColor(Color.parseColor("#3fC8C8C8"));
	}
	if(angle)
	{
		au1va=1000;
		//horizonmod.setBackgroundColor(Color.parseColor("#3fC8C8C8"));
		//anglemod.setBackgroundColor(Color.parseColor("#9900ff99"));
	}
	
	else if(horizon==true)
	{
		au1va=2000;
		//anglemod.setBackgroundColor(Color.parseColor("#3fC8C8C8"));
		//horizonmod.setBackgroundColor(Color.parseColor("#9900ff99"));
	}
	else 
	{
		au1va=1500;
		//anglemod.setBackgroundColor(Color.parseColor("#3fC8C8C8"));
		//horizonmod.setBackgroundColor(Color.parseColor("#3fC8C8C8"));
	}
	if(!angle&&!horizon&&!mag&&!baro)
	{
		//anglemod.setBackgroundColor(Color.parseColor("#3fC8C8C8"));
		//horizonmod.setBackgroundColor(Color.parseColor("#3fC8C8C8"));
		//baromod.setBackgroundColor(Color.parseColor("#3fC8C8C8"));
		//magmod.setBackgroundColor(Color.parseColor("#3fC8C8C8"));
	}
}
	
	private int checkBTState() {
		filter1 = new IntentFilter(BluetoothDevice.ACTION_ACL_CONNECTED);

		filter2 = new IntentFilter(BluetoothDevice.ACTION_ACL_DISCONNECTED);
		this.registerReceiver(mReceiver, filter1);
		this.registerReceiver(mReceiver, filter2);
		flag_btlistener=true;

		    // Check for Bluetooth support and then check to make sure it is turned on
		
		    // Emulator doesn't support Bluetooth and will return null
		    if(btAdapter==null) { 
		      Toast.makeText(getApplicationContext(), "connection failed", Toast.LENGTH_SHORT).show();
				return 0;

		    	//errorExit("Fatal Error", "Bluetooth Not supported. Aborting.");
		    } else {
		      if (btAdapter.isEnabled()) {
				  if(!flag_device_select) {
					  Set<BluetoothDevice> pairedDevices = btAdapter.getBondedDevices();
					  if (pairedDevices.size() > 0) {
						  for (BluetoothDevice device : pairedDevices) {
							  btarrayadapter.add(device.getName() + "\n" + device.getAddress());
							  btarrayadapter1.add(device.getAddress());
						  }
					  }
					  final Dialog btdialog = new Dialog(MainActivity.this);
					  btdialog.setContentView(R.layout.btlist);
					  btdialog.setTitle("Select Device:");
					  final ListView btlistview = (ListView) btdialog.findViewById(R.id.listView2);
					  btlistview.setAdapter(btarrayadapter);
					  btdialog.show();
					  btlistview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
						  @Override
						  public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
							  address = btarrayadapter1.getItem(i);
							  flag_device_select = true;
							  btdialog.dismiss();
							  select_connect();
						  }
					  });
				  }

				  return 1;

		    	  /*if(btSocket.isConnected())
		    	  {
		    		  Toast.makeText(this, "is conn", Toast.LENGTH_SHORT).show();
		    		  try {
						btSocket.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
		    	  }*/
		    	  
		     //   Log.d(TAG, "...Bluetooth is enabled...");
		      } else {
		        //Prompt user to turn on Bluetooth

		        Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
		        startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
				  return 0;
		      }
		    }
		  }
		
	
	
	 boolean connected;

	public boolean connectBT()
	  {
		  //checkBTState();
		
		connected =false;

		  
		  BluetoothDevice device = btAdapter.getRemoteDevice(address);
		  
		    // Two things are needed to make a connection:
		    //   A MAC address, which we got above.
		    //   A Service ID or UUID.  In this case we are using the
		    //     UUID for SPP.
		  
		    if(socketcreated)
		    {
		    	//if(btSocket.isConnected())
		      {
		    		try {
		      
						btSocket.close();
						//Toast.makeText(this, "closed", Toast.LENGTH_SHORT);
						socketcreated =false;
					} catch (IOException e) {
						Toast.makeText(this, "close error", Toast.LENGTH_SHORT);
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
		      }
		    }
		  
		    try {
		    	//if(!socketcreated)
		      btSocket = device.createRfcommSocketToServiceRecord(MY_UUID);
		      socketcreated =true;
		    } 
		    catch (IOException e) {
		      //errorExit("Fatal Error", "In onResume() and socket create failed: " + e.getMessage() + ".");
		    }
		    
		    // Discovery is resource intensive.  Make sure it isn't going on
		    // when you attempt to connect and pass your message.
		    
		    btAdapter.cancelDiscovery();
		  
		    // Establish the connection.  This will block until it connects.
		    // Log.d(TAG, "...Connecting to Remote...");
		    
		    try
		    {
		      btSocket.connect();
				hand.post(new Runnable() {
					@Override
					public void run() {
						Toast.makeText(getApplicationContext(), "Connected", Toast.LENGTH_SHORT).show();
					}
				});

		      connected =true;
		      // Log.d(TAG, "...Connection established and data link opened...");
		    } 
		    catch (IOException e) {
		      try {
		        btSocket.close();
		      } catch (IOException e2) {
		        //errorExit("Fatal Error", "In onResume() and unable to close socket during connection failure" + e2.getMessage() + ".");
		      }
		    }
		    
		    // Create a data stream so we can talk to server.
		    // Log.d(TAG, "...Creating Socket...");
	
		    try {
		      outStream = btSocket.getOutputStream();
		      //inStream =  btSocket.getInputStream();
		      
		    } catch (IOException e) {
		     // errorExit("Fatal Error", "In onResume() and output stream creation failed:" + e.getMessage() + ".");
		    }
		    return connected;
	
	}
	
	  public void sendData(String message)
	  {
	      byte[] msgBuffer = message.getBytes();
	      byte[] inBuffer = null;
	
		   // Log.d(TAG, "...Sending data: " + message + "...");
	
		    try 
		    {
		      outStream.write(msgBuffer);
		      Log.e("data", message);
		      //inStream.read(inBuffer);
		      
		      
		    }
		    catch (IOException e) 
		    {
		      String msg = "In onResume() and an exception occurred during write: " + e.getMessage();
		      if (address.equals("00:00:00:00:00:00")) 
		        msg = msg + ".\n\nUpdate your server address from 00:00:00:00:00:00 to the correct address on line 37 in the java code";
		      msg = msg +  ".\n\nCheck that the SPP UUID: " + MY_UUID.toString() + " exists on server.\n\n";
		      //errorExit("Fatal Error", msg);     
		    }
		    //tvin.setText(inBuffer.toString());
	  }


	  public void sendoutput()
	  {
		  String out = ""; //= "$"+Integer.toString(t)+"#" +"%"+Integer.toString(r)+"#" +"^"+Integer.toString(p)+"#" +"&"+Integer.toString(y)+"#" ;

		  //tvout.setText(out);
		  if(iscon) {
			  if((t<(min_throttle+50)&&y>=max_yaw&&p<(min_pitch+50)&&r<(min_roll+50))||(t<(min_throttle+50)&&y<=(min_yaw)&&p<(min_pitch+50)&&r>(max_roll-50))||(t<(min_throttle+50)&&y>=(max_yaw)&&p<(min_pitch+50)&&r>(max_roll-50)))
			  {
				  //t=min_throttle;
				  //y=max_yaw;
				  //p=min_pitch;
				  //r=min_roll;
				  runOnUiThread(new Runnable() {
					  @Override
					  public void run() {
						  arm_view.setVisibility(View.VISIBLE);
					  }
				  });

				  arm_flag=true;
				  one=true;
				  //flightmod();
			  }
			  if(timecount<1000) {
				  if (y != py) {
					  py = y;
					  out += "&" + Integer.toString(y) + "###";
				  }
				  if (t != pt) {
					  pt = t;
					  out += "$" + Integer.toString(t) + "###";
				  }
				  if (r != pr) {
					  pr = r;
					  out += "%" + Integer.toString(r) + "###";
				  }
				  if (p != pp) {
					  pp = p;
					  out += "^" + Integer.toString(p) + "###";
				  }

				  if (au1va != pau1va) {
					  pau1va = au1va;
					  // out += "!"+Integer.toString(au1va)+"#";
				  }
				  if (au2va != pau2va) {
					  pau2va = au2va;
					  //out += "@"+Integer.toString(au2va)+"#";
				  }
			  }
			  else
			  {
				  timecount=0;
				  py=y;
				  out += "&"+Integer.toString(y)+"###";
				  pt=t;
				  out += "$"+Integer.toString(t)+"###";
				  pr=r;
				  out += "%"+Integer.toString(r)+"###";
				  pp=p;
				  out += "^"+Integer.toString(p)+"###";

				  pau1va=au1va;
				  //out += "!"+Integer.toString(au1va)+"###";
				  pau2va=au2va;
				  //out += "@"+Integer.toString(au2va)+"###";

			  }
			  if(flag_setup)
			  {
				  sendData(setup_str);
				  flag_setup=false;
			  }

			  if(out!=null)
			  sendData(out);
		  }
	  
	  }
	  
	  

	  @Override
		  protected void onDestroy() {
			// TODO Auto-generated method stub
			super.onDestroy();
			    //Log.d(TAG, "...In onPause()...");
		
		    if (outStream != null) {
		      try {
		        outStream.flush();
		      } catch (IOException e) {
		        //errorExit("Fatal Error", "In onPause() and failed to flush output stream: " + e.getMessage() + ".");
		      }
		    }
		
		    try     {
				if(socketcreated)
		      		btSocket.close();
		    } catch (IOException e2) {
		      //errorExit("Fatal Error", "In onPause() and failed to close socket." + e2.getMessage() + ".");
		    }
		  }
	  
	boolean iscon =false;
	boolean isarm=false;
	boolean iscalib=false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ro);
		fm= getFragmentManager();
		fragmen=(RelativeLayout)findViewById(R.id.frag);




		sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
		mid_pitch=sharedpreferences.getInt(mid_pitchkey, mid_pitch);
		mid_roll=sharedpreferences.getInt(mid_rollkey, mid_roll);
		mid_yaw=sharedpreferences.getInt(mid_yawkey, mid_yaw);
		min_pitch=sharedpreferences.getInt(min_pitchkey, min_pitch);
		min_roll=sharedpreferences.getInt(min_rollkey,min_roll);
		min_yaw=sharedpreferences.getInt(min_yawkey,min_yaw);
		max_pitch=sharedpreferences.getInt(max_pitchkey, max_pitch);
		max_roll=sharedpreferences.getInt(max_rollkey,max_roll);
		max_yaw=sharedpreferences.getInt(max_yawkey,max_yaw);
		max_throttle=sharedpreferences.getInt(max_throtkey,max_throttle);
		min_throttle=sharedpreferences.getInt(min_throtkey, min_throttle);
		ip_address=sharedpreferences.getString(ip_key, ip_address);
		ard_t=sharedpreferences.getInt(ard_tkey, ard_t);
		ard_y=sharedpreferences.getInt(ard_ykey,ard_y);
		ard_p=sharedpreferences.getInt(ard_pkey,ard_p);
		ard_r=sharedpreferences.getInt(ard_rkey,ard_r);
		sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
		mSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
		btAdapter = BluetoothAdapter.getDefaultAdapter();
		btarrayadapter=new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1);
		btarrayadapter1=new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1);

		//checkBTState();
		maponmain=(RelativeLayout)findViewById(R.id.mapfrag);
		throt=(TextView)findViewById(R.id.throttle);
		throt1=(TextView)findViewById(R.id.thh);
		yaw=(TextView)findViewById(R.id.yaww);
		yaw1=(TextView)findViewById(R.id.yww);
		roll=(TextView)findViewById(R.id.rolll);
		//tvout=(TextView)findViewById(R.id.tvout);
		//tvin=(TextView)findViewById(R.id.tvin);

		pitch=(TextView)findViewById(R.id.pitchh);
		blue=(Button)findViewById(R.id.bluetooth);
		arm_view=(TextView)findViewById(R.id.arm_show);
		//disarm=(ToggleButton)findViewById(R.id.arming);
		//calibration=(Button)findViewById(R.id.calib);
		//up=(Button)findViewById(R.id.tup);
		//baromod=(Button)findViewById(R.id.barom);
		
		
		//anglemod=(Button)findViewById(R.id.ang);
		//magmod=(Button)findViewById(R.id.magne);
		//horizonmod=(Button)findViewById(R.id.hori);
		modes=(Button)findViewById(R.id.mode);
		//arm=(Button)findViewById(R.id.arming);
		
		
		//down=(Button)findViewById(R.id.tdown);
		
		bigcircle1=(RelativeLayout)findViewById(R.id.rl1);
		bar1=(ImageView)findViewById(R.id.ivback);
		bar2=(ImageView)findViewById(R.id.ivback1);
		bar3=(ImageView)findViewById(R.id.ivback2);
		front_l=(RelativeLayout)findViewById(R.id.fron);
		front_i=(ImageView)findViewById(R.id.nazaview1);
		front_i1=(ImageView)findViewById(R.id.nazaview2);
		front_i2=(ImageView)findViewById(R.id.nazaview3);
		main_c=(LinearLayout)findViewById(R.id.control);
		
		
		//arrow = (ImageView)findViewById(R.id.ivarrow);
		//arrowb = (ImageView)findViewById(R.id.ivarrowb);
		smallcircle1=(ImageView)findViewById(R.id.ImageView02);
		bigcircle2=(RelativeLayout)findViewById(R.id.rl2);
		smallcircle2=(ImageView)findViewById(R.id.imageView2);
		blue_img=(ImageView)findViewById(R.id.bimg);
		blue_img1=(ImageView)findViewById(R.id.bimg1);
		quad=(ImageView)findViewById(R.id.quadr);
		ctrl=(LinearLayout)findViewById(R.id.control);
		leftside=(RelativeLayout)findViewById(R.id.rl1);
		rightside=(RelativeLayout)findViewById(R.id.rl2);
		lquad=(RelativeLayout)findViewById(R.id.quadleft);
		rquad=(RelativeLayout)findViewById(R.id.quadright);
		cquad=(RelativeLayout)findViewById(R.id.quadcenter);
		bgrp=(LinearLayout)findViewById(R.id.buttongrp);
		nazaimg1=(ImageView)findViewById(R.id.nazaview1);
		nazaimg2=(ImageView)findViewById(R.id.nazaview2);
		nazaimg3=(ImageView)findViewById(R.id.nazaview3);
		setting_button=(ImageView)findViewById(R.id.sett_button);
		//sq = new Dialog(MainActivity.this, android.R.style.Theme_Black_NoTitleBar_Fullscreen);
		//sq.requestWindowFeature(Window.FEATURE_NO_TITLE);
		//sq.setContentView(R.layout.sqlstream);
		//sql_view = (ImageView) sq.findViewById(R.id.imageView);

		vb=(Vibrator)this.getSystemService(Context.VIBRATOR_SERVICE);
		registerForContextMenu(modes);

		handler = new Handler();
		htest= new Handler();
		handler1=new Handler();

		Thread send=new Thread(new Runnable() {
			@Override
			public void run() {
				while(true) {


					if (flagsend) {
						flagsend=false;
						sendoutput();

						timecount = 0;
					} else {
						try {
							Thread.sleep(1);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
						timecount++;
					}
					if (timecount > 1000) {
						if(flagsend)
							flagsend=false;
						sendoutput();
					}
				}

			}
		});

		send.start();

		fornaza=new AnimatorSet();
		fornaza1=new AnimatorSet();
		fornaza2=new AnimatorSet();
		final AnimatorSet nazalh = (AnimatorSet) AnimatorInflater.loadAnimator(getApplicationContext(),
				R.animator.naza1);
		final AnimatorSet nazahl=(AnimatorSet)AnimatorInflater.loadAnimator(getApplicationContext(),R.animator.naza2);
		final AnimatorSet nazalh1=(AnimatorSet)AnimatorInflater.loadAnimator(getApplicationContext(),R.animator.naza1);
		final AnimatorSet nazahl1=(AnimatorSet)AnimatorInflater.loadAnimator(getApplicationContext(),R.animator.naza2);
		final Handler nazaanim=new Handler();
					nazalh.setTarget(nazaimg2);
					nazalh.start();
					nazalh.addListener(new Animator.AnimatorListener() {
						@Override
						public void onAnimationStart(Animator animator) {

						}

						@Override
						public void onAnimationEnd(Animator animator) {
							nazalh1.setTarget(nazaimg3);
							if (flag_naza) {
								nazalh1.start();
							}
							nazalh1.addListener(new Animator.AnimatorListener() {
								@Override
								public void onAnimationStart(Animator animator) {

								}

								@Override
								public void onAnimationEnd(Animator animator) {
									nazahl.setTarget(nazaimg3);
									if (flag_naza) {
										nazahl.start();
									}
									nazahl.addListener(new Animator.AnimatorListener() {
										@Override
										public void onAnimationStart(Animator animator) {

										}

										@Override
										public void onAnimationEnd(Animator animator) {
											nazahl1.setTarget(nazaimg2);
											if (flag_naza) {
												nazahl1.start();

											}
											nazahl1.addListener(new Animator.AnimatorListener() {
												@Override
												public void onAnimationStart(Animator animator) {

												}

												@Override
												public void onAnimationEnd(Animator animator) {
													nazalh.setTarget(nazaimg2);

													if (flag_naza) {
														nazalh.start();
													}

												}

												@Override
												public void onAnimationCancel(Animator animator) {

												}

												@Override
												public void onAnimationRepeat(Animator animator) {

												}
											});

										}

										@Override
										public void onAnimationCancel(Animator animator) {

										}

										@Override
										public void onAnimationRepeat(Animator animator) {

										}
									});

								}

								@Override
								public void onAnimationCancel(Animator animator) {

								}

								@Override
								public void onAnimationRepeat(Animator animator) {

								}
							});

						}

						@Override
						public void onAnimationCancel(Animator animator) {

						}

						@Override
						public void onAnimationRepeat(Animator animator) {

						}
					});



		//appupdate asyncTask;
		//asyncTask=new appupdate((Context) MainActivity.this);
		//asyncTask.delegate=this;
		//asyncTask.execute();

		setting_button.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {

				PopupMenu popup = new PopupMenu(getApplicationContext(), view);
				MenuInflater inflate = popup.getMenuInflater();
				if (flag_admin) {
					inflate.inflate(R.menu.main, popup.getMenu());
				} else {
					inflate.inflate(R.menu.main, popup.getMenu());
				}
				popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
					@Override
					public boolean onMenuItemClick(MenuItem menuItem) {
						switch (menuItem.getItemId()) {
							case R.id.item1:
								final Dialog d = new Dialog(MainActivity.this, android.R.style.Theme_Black_NoTitleBar_Fullscreen);
								d.setContentView(R.layout.activity_throtmenu);
								thmin = (EditText) d.findViewById(R.id.thmint);
								thmax = (EditText) d.findViewById(R.id.thmaxt);
								thmin.setText("" + min_throttle);
								thmax.setText("" + max_throttle);
								bmenu = (Button) d.findViewById(R.id.button1);
								d.show();
								bmenu.setOnClickListener(new OnClickListener() {

									@Override
									public void onClick(View v) {
										q = thmax.getText().toString();
										max_throttle = Integer.parseInt(q);
										q = thmin.getText().toString();
										min_throttle = Integer.parseInt(q);
										SharedPreferences.Editor editor = sharedpreferences.edit();
										editor.putInt(max_throtkey, max_throttle);
										editor.putInt(min_throtkey, min_throttle);
										editor.commit();
										d.dismiss();
										// TODO Auto-generated method stub

									}
								});

								break;
							case R.id.noyaww:
								if (noyw) {
									menuItem.setTitle("Yaw-Off");
									noyw = false;
								} else {
									noyw = true;
									menuItem.setTitle("Yaw-On");
								}
								break;
							case R.id.midd:
								final Dialog m = new Dialog(MainActivity.this, android.R.style.Theme_Black_NoTitleBar_Fullscreen);
								m.setContentView(R.layout.mid);

								emidy = (EditText) m.findViewById(R.id.midyawt);
								emidp = (EditText) m.findViewById(R.id.midpitcht);
								emidr = (EditText) m.findViewById(R.id.midrollt);
								midmenu = (Button) m.findViewById(R.id.button1);
								emidy.setText("" + mid_yaw);
								emidp.setText("" + mid_pitch);
								emidr.setText("" + mid_roll);
								m.show();
								midmenu.setOnClickListener(new OnClickListener() {

									@Override
									public void onClick(View v) {
										// TODO Auto-generated method stub
										q = emidy.getText().toString();
										mid_yaw = Integer.parseInt(q);
										q = emidp.getText().toString();
										mid_pitch = Integer.parseInt(q);
										q = emidr.getText().toString();
										mid_roll = Integer.parseInt(q);
										SharedPreferences.Editor editor = sharedpreferences.edit();
										editor.putInt(mid_pitchkey, mid_pitch);
										editor.putInt(mid_yawkey, mid_yaw);
										editor.putInt(mid_rollkey, mid_roll);
										editor.commit();

										m.dismiss();


									}
								});
								break;
							case R.id.gyr:
								if (!gyro) {
									//smallcircle1.setVisibility(View.INVISIBLE);
									//smallcircle2.setVisibility(View.INVISIBLE);
									bar2.setVisibility(View.INVISIBLE);
									menuItem.setTitle("Gyro-Off");
									roll.setTextColor(Color.RED);
									pitch.setTextColor(Color.RED);
									yaw.setVisibility(View.INVISIBLE);
									throt.setVisibility(View.INVISIBLE);
									yaw1.setVisibility(View.VISIBLE);
									throt1.setVisibility(View.VISIBLE);
									a1 = Integer.toString(y);
									yaw1.setText("Y: " + a1);
									a1 = Integer.toString(t);
									throt1.setText("T: " + a1);
									gyro = true;
								} else {
									//smallcircle1.setVisibility(View.VISIBLE);
									//smallcircle2.setVisibility(View.VISIBLE);
									bar2.setVisibility(View.VISIBLE);

									gyro = false;
									roll.setTextColor(Color.WHITE);
									pitch.setTextColor(Color.WHITE);
									yaw.setVisibility(View.VISIBLE);
									throt.setVisibility(View.VISIBLE);
									yaw1.setVisibility(View.INVISIBLE);
									throt1.setVisibility(View.INVISIBLE);
									menuItem.setTitle("Gyro-On");
								}
								break;

							case R.id.gyro_calib:
								if (!init) {
									init = true;
								} else {
									init_calib_2 = false;
									init_calib_1 = false;
									init = false;
								}
								break;
							case R.id.surv:
								Dialog d_vid;

								d_vid = new Dialog(MainActivity.this, android.R.style.Theme_Black_NoTitleBar_Fullscreen);
								d_vid.requestWindowFeature(Window.FEATURE_NO_TITLE);

								d_vid.setContentView(R.layout.web);
								webv = (WebView) d_vid.findViewById(R.id.webView);
								registerForContextMenu(webv);
								webv.clearCache(true);
								webv.loadUrl(ip_address + ":8080/cam.mjpg");
								d_vid.show();


								d_vid.setOnDismissListener(new DialogInterface.OnDismissListener() {
									@Override
									public void onDismiss(DialogInterface dialog) {
										webv.destroy();
										Toast.makeText(getApplicationContext(), "Closed", Toast.LENGTH_SHORT).show();
									}
								});
								break;
							case R.id.naza:
								t = min_throttle;
								y = min_yaw;
								p = min_pitch;
								r = min_roll;
								flagsend = true;

								Thread tp = new Thread() {
									public void run() {
										try {
											Thread.sleep(1000);
											t = max_throttle;
											y = max_yaw;
											p = max_pitch;
											r = max_roll;
											flagsend = true;

										} catch (InterruptedException e) {
											// TODO Auto-generated catch block
											e.printStackTrace();
										}
									}

								};
								tp.start();
								break;

							case R.id.ip:
								final Dialog ip_d = new Dialog(MainActivity.this, android.R.style.Theme_Black_NoTitleBar_Fullscreen);
								ip_d.setContentView(R.layout.ip_set);

								ip_t = (EditText) ip_d.findViewById(R.id.ip_ed);
								ip_b = (Button) ip_d.findViewById(R.id.ip_b);
								ip_t.setText("" + ip_address);
								ip_d.show();
								ip_b.setOnClickListener(new OnClickListener() {
									@Override
									public void onClick(View view) {
										String ipc = ip_t.getText().toString();
										ip_address = ipc;
										SharedPreferences.Editor editor = sharedpreferences.edit();
										editor.putString(ip_key, ip_address);
										editor.commit();
										ip_d.dismiss();


									}
								});
								break;
							case R.id.minmax:
								final Dialog de = new Dialog(MainActivity.this, android.R.style.Theme_Black_NoTitleBar_Fullscreen);
								de.setContentView(R.layout.minmaxset);
								eminy = (EditText) de.findViewById(R.id.minyawt);
								eminp = (EditText) de.findViewById(R.id.minpitcht);
								eminr = (EditText) de.findViewById(R.id.minrollt);
								emaxy = (EditText) de.findViewById(R.id.maxyawt);
								emaxp = (EditText) de.findViewById(R.id.maxpitcht);
								emaxr = (EditText) de.findViewById(R.id.maxrollt);
								minmaxmenu = (Button) de.findViewById(R.id.button1);
								eminy.setText("" + min_yaw);
								eminp.setText("" + min_pitch);
								eminr.setText("" + min_roll);
								emaxy.setText("" + max_yaw);
								emaxp.setText("" + max_pitch);
								emaxr.setText("" + max_roll);
								de.show();
								minmaxmenu.setOnClickListener(new OnClickListener() {
									@Override
									public void onClick(View view) {
										q = eminy.getText().toString();
										min_yaw = Integer.parseInt(q);
										q = eminp.getText().toString();
										min_pitch = Integer.parseInt(q);
										q = eminr.getText().toString();
										min_roll = Integer.parseInt(q);
										q = emaxy.getText().toString();
										max_yaw = Integer.parseInt(q);
										q = emaxp.getText().toString();
										max_pitch = Integer.parseInt(q);
										q = emaxr.getText().toString();
										max_roll = Integer.parseInt(q);
										SharedPreferences.Editor editor = sharedpreferences.edit();
										editor.putInt(min_pitchkey, min_pitch);
										editor.putInt(min_yawkey, min_yaw);
										editor.putInt(min_rollkey, min_roll);
										editor.putInt(max_pitchkey, max_pitch);
										editor.putInt(max_yawkey, max_yaw);
										editor.putInt(max_rollkey, max_roll);
										editor.commit();

										de.dismiss();
									}
								});
								break;
							case R.id.gesture:

								if (gest_on) {
									gest_on = false;
									fragmen.setVisibility(View.GONE);
									FragmentTransaction ft = fm.beginTransaction();
									ft.hide(fr);
									ft.commit();
								} else {
									fr = new gesture();
									ctrl.setVisibility(View.GONE);
									fr.setArguments(getIntent().getExtras());
									fragmen.setVisibility(View.VISIBLE);
									fragmen.setAlpha((float) 1.0);
									gest_on = true;
									FragmentTransaction ft = fm.beginTransaction();
									ft.add(fragmen.getId(), new gesture());
									ft.commit();
								}

								break;
							case R.id.rec:
								/*
								mNotifyManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
								mBuilder = new NotificationCompat.Builder(MainActivity.this);
								mBuilder.setContentTitle("Downloading Video")
										.setContentText("Download in progress 0%")
										.setSmallIcon(R.drawable.iconcircle3);
								mBuilder.setProgress(100, 0, false);
								mNotifyManager.notify(id, mBuilder.build());
								*/
								new ftpdownload(MainActivity.this).execute(1);
								break;
							case R.id.update:
								appupdate asyncTask;
								asyncTask = new appupdate((Context) MainActivity.this);
								asyncTask.delegate = MainActivity.this;
								asyncTask.execute();
								break;

							case R.id.pathplot:
								if (!pathflag) {
									fr = new maps();
									ctrl.setVisibility(View.GONE);
									Toast.makeText(getApplicationContext(), "Maps Loading...", Toast.LENGTH_LONG).show();
									fr.setArguments(getIntent().getExtras());
									fragmen.setVisibility(View.VISIBLE);
									fragmen.setAlpha((float) 1.0);
									pathflag = true;
									FragmentTransaction ft = fm.beginTransaction();
									ft.add(fragmen.getId(), fr);
									ft.commit();
								} else {
									pathflag = false;
									fragmen.setVisibility(View.GONE);
									FragmentTransaction ft = fm.beginTransaction();
									ft.hide(fr);
									ft.commit();
								}
								break;
							case  R.id.log:
								new ftpdownload(MainActivity.this).execute(2);
								break;

							case R.id.recrequest:
								if (!recrequest) {
									reqrec = new WebView(getApplicationContext());
									reqrec.clearCache(true);
									reqrec.loadUrl(ip_address + ":8080/record/cam.mjpg");
									recrequest = true;
									reqrec.destroy();
									menuItem.setTitle("Stop Onboard Recording");
								} else {
									reqrec = new WebView(getApplicationContext());
									reqrec.clearCache(true);
									reqrec.loadUrl(ip_address + ":8080/record_end/cam.mjpg");
									reqrec.destroy();
									recrequest = false;
									menuItem.setTitle("Onboard Recording");
								}

								break;
							case R.id.logrequest:
								if (!logreq) {
									reqlog = new WebView(getApplicationContext());
									reqlog.clearCache(true);
									reqlog.loadUrl(ip_address + ":");
									logreq = true;
								} else {
									logreq = false;
									reqlog.destroy();
								}
								break;
							case R.id.setupval:
								final EditText e1,e2,e3,e4;
								Button b;
								final Dialog m1 = new Dialog(MainActivity.this,android.R.style.Theme_Black_NoTitleBar_Fullscreen);
								m1.setContentView(R.layout.ardsetup);

								e1= (EditText) m1.findViewById(R.id.th);
								e2= (EditText) m1.findViewById(R.id.ya);
								e3 = (EditText) m1.findViewById(R.id.pi);
								e4=(EditText)m1.findViewById(R.id.ro);
								b = (Button) m1.findViewById(R.id.button1);
								e1.setText("" + ard_t);
								e2.setText("" + ard_y);
								e3.setText("" + ard_p);
								e4.setText("" + ard_r);
								m1.show();
								b.setOnClickListener(new OnClickListener() {

									@Override
									public void onClick(View v) {
										// TODO Auto-generated method stub
										ard_t = Integer.parseInt(e1.getText().toString());
										ard_y = Integer.parseInt(e2.getText().toString());
										ard_p=Integer.parseInt(e3.getText().toString());
										ard_r=Integer.parseInt(e4.getText().toString());
										SharedPreferences.Editor editor = sharedpreferences.edit();
										editor.putInt(ard_tkey, ard_t);
										editor.putInt(ard_ykey, ard_y);
										editor.putInt(ard_pkey, ard_p);
										editor.putInt(ard_rkey, ard_r);
										editor.commit();
										setup_str="t"+ard_t+"#"+"y"+ard_y+"#"+"p"+ard_p+"#"+"r"+ard_r+"#";
										if(iscon)
										{
											flag_setup=true;
										}
										m1.dismiss();
									}
								});


								break;

							case R.id.maponmain:
								if (!pathflagonmain) {
									bigcircle2.setBackgroundColor(Color.TRANSPARENT);
									fr = new livemaps();
									//Toast.makeText(getApplicationContext(), "Maps Loading...", Toast.LENGTH_LONG).show();
									fr.setArguments(getIntent().getExtras());
									pathflagonmain = true;
									FragmentTransaction ft = fm.beginTransaction();
									ft.add(maponmain.getId(), fr);
									ft.commit();
								} else {
									pathflagonmain = false;
									FragmentTransaction ft = fm.beginTransaction();
									bigcircle2.setBackgroundResource(R.drawable.backright1);
									ft.hide(fr);
									ft.commit();
								}
								break;
							default:
								break;
						}
						return false;
					}
				});
				popup.show();
			}
		});

		quad.setOnLongClickListener(new View.OnLongClickListener() {
			@Override
			public boolean onLongClick(View view) {
				final Dialog sq = new Dialog(MainActivity.this, android.R.style.Theme_Black_NoTitleBar_Fullscreen);
				//sq.requestWindowFeature(Window.FEATURE_NO_TITLE);
				sq.setContentView(R.layout.adminlogin);
				final EditText passf = (EditText) sq.findViewById(R.id.editText);
				Button passb = (Button) sq.findViewById(R.id.button);
				sq.show();
				passb.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View view) {
						String pass_enter = passf.getText().toString();
						if (pass_check.equals(pass_enter)) {
							Toast.makeText(getApplicationContext(), "Logged as Admin", Toast.LENGTH_LONG).show();
							flag_admin = true;
							invalidateOptionsMenu();
							sq.dismiss();

						} else {
							Toast.makeText(getApplicationContext(), "Wrong Password.", Toast.LENGTH_LONG).show();
						}

					}
				});
				return false;
			}
		});






		Animation anim_1=AnimationUtils.loadAnimation(getApplicationContext(), R.anim.start_1);
		front_l.startAnimation(anim_1);


		ctrl.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				if (!ctrl_flag) {
					ObjectAnimator ob3 = ObjectAnimator.ofFloat(bigcircle1, "translationX", 0.0f, bigcircle1.getWidth());
					ob3.setDuration(2000);
					ObjectAnimator ob4 = ObjectAnimator.ofFloat(bigcircle1, "alpha", 0.0f, 1.0f);
					ob3.setDuration(2000);
					AnimatorSet aset = new AnimatorSet();
					aset.playTogether(ob3, ob4);
					aset.start();
					ctrl_flag = true;
				}


			}
		});


		front_i.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				if (!iscon) {
					Toast.makeText(getApplicationContext(), "Click on bLuetooth to connect", Toast.LENGTH_SHORT).show();
				} else {
					front_l.setVisibility(View.GONE);
				}
			}
		});
		front_i1.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				if (!iscon) {
					Toast.makeText(getApplicationContext(), "Click on bLuetooth to connect", Toast.LENGTH_SHORT).show();
				} else {
					front_l.setVisibility(View.GONE);
				}
			}
		});

		front_i2.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				if (!iscon) {
					Toast.makeText(getApplicationContext(), "Click on bLuetooth to connect", Toast.LENGTH_SHORT).show();
				} else {
					front_l.setVisibility(View.GONE);
				}
			}
		});

		blue_img.setOnLongClickListener(new View.OnLongClickListener() {
			@Override
			public boolean onLongClick(View view) {
				final Dialog sq = new Dialog(MainActivity.this, android.R.style.Theme_Black_NoTitleBar_Fullscreen);
				//sq.requestWindowFeature(Window.FEATURE_NO_TITLE);
				sq.setContentView(R.layout.adminlogin);
				final EditText passf = (EditText) sq.findViewById(R.id.editText);
				Button passb = (Button) sq.findViewById(R.id.button);
				sq.show();
				passb.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View view) {
						String pass_enter = passf.getText().toString();
						if (pass_check.equals(pass_enter)) {
							Toast.makeText(getApplicationContext(), "Logged as Admin", Toast.LENGTH_LONG).show();
							flag_admin = true;
							invalidateOptionsMenu();
							sq.dismiss();

						} else {
							Toast.makeText(getApplicationContext(), "Wrong Password.", Toast.LENGTH_LONG).show();
						}
					}
				});
				return false;
			}
		});
		
		
		blue_img.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

			select_connect();


			}
		});
		/*
		arm.setOnClickListener(new OnClickListener() {
								   @Override
								   public void onClick(View view) {
									   y = max_yaw;
									   t = min_throttle;
									   p = min_pitch;
									   r = min_roll;
									   arm_flag = true;
									   arm_btn=true;
									   one = true;
									   flightmod();
									   flagsend=true;
								   }
							   });
							   */

		
		/*
		baromod.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(baro==false)
				{
					baro=true;
					mag=false;
				}
				else
					baro=false;
				// TODO Auto-generated method stub
				flightmod();
			}
		});
		
		
		anglemod.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(angle==false)
				{
					angle=true;
					horizon=false;
				}
				else
					angle=false;
				// TODO Auto-generated method stub
				flightmod();
			}
		});
		
		magmod.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(mag==false)
				{
					mag=true;
					baro=false;
				}
				else
					mag=false;
				// TODO Auto-generated method stub
				flightmod();
			}
		});
		
		horizonmod.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(horizon==false)
				{
					horizon=true;
					angle=false;
				}
				else
					horizon=false;
				// TODO Auto-generated method stub
				flightmod();
			}
		});
		*/
		/*
		up.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				cent_x1 = (bigcircle1.getX() + bigcircle1.getWidth()/ 2-smallcircle1.getWidth()/2);
				t=t+4;
				y=mid_yaw;
				if(t>max_throttle)
					t=max_throttle;
				else if(t<min_throttle)
					t=min_throttle;
				b1 = Integer.toString(t);
				throt.setText("T: " +b1);
				a1=Integer.toString(y);
				yaw.setText("Y: " +a1);
				
				smallcircle1.setX(cent_x1);
				b=((((max_throttle-t)*bigcircle1.getHeight())/(max_throttle-min_throttle))+bigcircle1.getY()-smallcircle1.getHeight()/2);
				smallcircle1.setY(b);
				bar1.setY((((max_throttle-t)*bigcircle1.getHeight())/(max_throttle-min_throttle))+bigcircle1.getY()-bar1.getHeight()/2);
				arrow.setY((((max_throttle-t)*bigcircle1.getHeight())/(max_throttle-min_throttle))+bigcircle1.getY()-arrow.getHeight()/2);
				sendoutput();
				//alternate for event.getY==(((max_throttle-t)*bigcircle1.getHeight())/(max_throttle-min_throttle))
				// TODO Auto-generated method stub
				
			}
		});
		down.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				cent_x1 = (bigcircle1.getX() + bigcircle1.getWidth()/ 2-smallcircle1.getWidth()/2);
				t=t-4;
				y=mid_yaw;
				if(t>max_throttle)
					t=max_throttle;
				else if(t<min_throttle)
					t=min_throttle;
				b1 = Integer.toString(t);
				throt.setText("T: " +b1);
				a1=Integer.toString(y);
				yaw.setText("Y: " +a1);
				smallcircle1.setX(cent_x1);
				b=((((max_throttle-t)*bigcircle1.getHeight())/(max_throttle-min_throttle))+bigcircle1.getY()-smallcircle1.getHeight()/2);
				smallcircle1.setY(b);
				bar1.setY((((max_throttle-t)*bigcircle1.getHeight())/(max_throttle-min_throttle))+bigcircle1.getY()-bar1.getHeight()/2);
				arrow.setY((((max_throttle-t)*bigcircle1.getHeight())/(max_throttle-min_throttle))+bigcircle1.getY()-arrow.getHeight()/2);
				sendoutput();
				//alternate for event.getY==(((max_throttle-t)*bigcircle1.getHeight())/(max_throttle-min_throttle))
				// TODO Auto-generated method stub
				
			}
		});
		
		
		disarm.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(!isarm)
				{
					y=max_yaw;
					t=min_throttle;
					r=mid_roll;
					p=mid_pitch;
					a=(bigcircle1.getWidth()+bigcircle1.getX()-smallcircle1.getWidth()/2);
					b=(bigcircle1.getHeight()+bigcircle1.getY()-smallcircle1.getHeight()/2);
					c=(bigcircle1.getWidth()+bigcircle2.getX()-smallcircle2.getWidth()/2);
					d=(bigcircle1.getHeight()+bigcircle2.getY()-smallcircle2.getHeight()/2);
					
					a1 = Integer.toString(y);
					
					yaw.setText("Y: " +a1);
					b1 = Integer.toString(t);
					throt.setText("T: " +b1);
					
					
					smallcircle1.setX(a);
					smallcircle1.setY(b);
					bar1.setY(bigcircle1.getHeight()+bigcircle1.getY()-bar1.getHeight()/2);
					arrow.setY(bigcircle1.getHeight()+bigcircle1.getY());
					
					a2 = Integer.toString(r);
					roll.setText("R: " +a2);
					b2 = Integer.toString(p);
					pitch.setText("P: " +b2);
					
					cent_x2 =( bigcircle2.getX() + bigcircle2.getWidth()/ 2-smallcircle2.getWidth()/2);
					cent_y2 = (bigcircle2.getY() + bigcircle2.getHeight()/ 2-smallcircle2.getHeight()/2);
					
					smallcircle2.setX(cent_x2);
					smallcircle2.setY(cent_y2);
					bar2.setY(bigcircle2.getY() + bigcircle2.getHeight()/ 2-bar2.getHeight()/2);
					bar3.setX(bigcircle2.getX() + bigcircle2.getWidth()/ 2-bar3.getWidth()/2);
					int r=0,g=0,b=0;
					
					if(t<1400)
					{
						r=255;
						g=0;	
					}
					else if(t<1600)
					{
						r=255;
						g=255;	
					}
					else
					{
						g=255;
						r=0;	
					}
					
					arrowb.setBackgroundColor(Color.argb(155, r, g, b));
					
					sendoutput();
					isarm=true;
					//disarm.setBackgroundColor(Color.RED);
				}
				else
				{
						y=min_yaw;
						t=min_throttle;
						a=(bigcircle1.getX()-smallcircle1.getWidth()/2);
						b=(bigcircle1.getHeight()+bigcircle1.getY()-smallcircle1.getHeight()/2);
						a1 = Integer.toString(y);
						
						yaw.setText("Y: " + a1);
						b1 = Integer.toString(t);
						throt.setText("T: " +b1);
						
						smallcircle1.setX(a);
						smallcircle1.setY(b);
						bar1.setY(bigcircle1.getHeight()+bigcircle1.getY()-bar1.getHeight()/2);
						arrow.setY(bigcircle1.getHeight()+bigcircle1.getY());
						r=(int)(mid_roll);
						p=(int)(mid_pitch);
									a2 = Integer.toString(r);
						roll.setText("R: " +a2);
						b2 = Integer.toString(p);
						pitch.setText("P: " +b2);
						cent_x2 =( bigcircle2.getX() + bigcircle2.getWidth()/ 2-smallcircle2.getWidth()/2);
						cent_y2 = (bigcircle2.getY() + bigcircle2.getHeight()/ 2-smallcircle2.getHeight()/2);
						smallcircle2.setX(cent_x2);
						smallcircle2.setY(cent_y2);
						bar2.setY(bigcircle2.getY() + bigcircle2.getHeight()/ 2-bar2.getHeight()/2);
						bar3.setX(bigcircle2.getX() + bigcircle2.getWidth()/ 2-bar3.getWidth()/2);
						int r=0,g=0,b=0;
						
						if(t<1400)
						{
							r=255;
							g=0;	
						}
						else if(t<1600)
						{
							r=255;
							g=255;	
						}
						else
						{
							g=255;
							r=0;	
						}
						
						arrowb.setBackgroundColor(Color.argb(155, r, g, b));
						
						sendoutput();
						isarm=false;
						//disarm.setBackgroundColor(Color.TRANSPARENT);
				}
				
				// TODO Auto-generated method stub
				
			}
		});
		
		calibration.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(!iscalib)
				{
					y=min_yaw;
					t=min_throttle;
					p=min_pitch;
					a=(bigcircle1.getX()-smallcircle1.getWidth()/2);
					b=(bigcircle1.getHeight()+bigcircle1.getY()-smallcircle1.getHeight()/2);
					d=(int)(bigcircle2.getHeight()+bigcircle2.getY()-smallcircle2.getHeight()/2);
					a1 = Integer.toString(y);
					
					yaw.setText("Y: " + a1);
					b1 = Integer.toString(t);
					throt.setText("T: " +b1);
					
					smallcircle1.setX(a);
					smallcircle1.setY(b);
					bar1.setY(bigcircle1.getHeight()+bigcircle1.getY()-bar1.getHeight()/2);
					arrow.setY(bigcircle1.getHeight()+bigcircle1.getY()-arrow.getHeight()/2);
					
					b2=Integer.toString(p);
					pitch.setText("P: " +b2);
					bar2.setY(bigcircle2.getHeight()+bigcircle2.getY()-bar2.getHeight()/2);
					smallcircle2.setY(d);
					sendoutput();
					iscalib=true;
					}
				// TODO Auto-generated method stub
				
			}
		});
		
		
		*/
	blue.setOnClickListener(new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			if(iscon)
			{
				iscon = false;
				if (outStream != null) {
				      try {
				        outStream.flush();
				      } catch (IOException e) {
				        //errorExit("Fatal Error", "In onPause() and failed to flush output stream: " + e.getMessage() + ".");
				      }
				    }
				
				    try     {
				      btSocket.close();
				    } catch (IOException e2) {
				      //errorExit("Fatal Error", "In onPause() and failed to close socket." + e2.getMessage() + ".");
				    }
				   // blue.setBackgroundColor(Color.parseColor("#aae80000"));
				   // blue.setText("Disconected");
				//Toast.makeText(getApplicationContext(), "Already connected", Toast.LENGTH_SHORT).show();
				//blue.setBackgroundColor(Color.BLUE);
			}
			
			// TODO Auto-generated method stub
			
		}
	});

	
	final SensorEventListener mEventListener = new SensorEventListener() {
		
		ObjectAnimator ob,ob1,ob2,ob9,ob10,ob11;
		float xx,xy;
		int rolll=1500,pitchh=1500;
		int step_roll=0,step_pitch=0;
		int counter_p=0,counter_r=0;
		long sum_p=0,sum_r=0;
		String a,b;
		
		@Override
		public void onSensorChanged(SensorEvent event){
			xx=event.values[1];
			xy=event.values[0];

			nang=xx*10;
			ob2=ObjectAnimator.ofFloat(blue_img,"rotationY",pang,nang);
			ob2.setDuration(50);
			ob2.setInterpolator(new AccelerateDecelerateInterpolator());
			ob10=ObjectAnimator.ofFloat(blue_img1,"rotationY",pang,nang);
			ob10.setDuration(50);
			ob10.setInterpolator(new AccelerateDecelerateInterpolator());

			nang1=xy*10;
			ob9=ObjectAnimator.ofFloat(blue_img,"rotationX",-pang1,-nang1);
			ob9.setDuration(50);
			ob9.setInterpolator(new AccelerateDecelerateInterpolator());
			ob11=ObjectAnimator.ofFloat(blue_img1,"rotationX",-pang1,-nang1);
			ob11.setDuration(50);
			ob11.setInterpolator(new AccelerateDecelerateInterpolator());
			ob2.start();
			ob10.start();
			ob9.start();
			ob11.start();


			if(gyro) {
				ob = ObjectAnimator.ofFloat(quad, "rotationX", -pang1, -nang1);
				ob.setDuration(10);
				ob.setInterpolator(new AccelerateDecelerateInterpolator());


				ob1 = ObjectAnimator.ofFloat(quad, "rotationY", pang, nang);
				ob1.setDuration(10);
				ob1.setInterpolator(new AccelerateDecelerateInterpolator());
				ob.start();
				ob1.start();
			}
			pang=nang;
			pang1=nang1;
			if(gyro)
			{
				init_1=false;
				init_2=false;
					
				if(!init)
				{
					rollingAverage[0] = new ArrayList<Float>();
		            rollingAverage[1] = new ArrayList<Float>();
		            rollingAverage[2] = new ArrayList<Float>();

		            rollingAverage[0] = rolla(rollingAverage[0], event.values[0]);
		            rollingAverage[1] = rolla(rollingAverage[1], event.values[1]);
		            rollingAverage[2] = rolla(rollingAverage[2], event.values[2]);

		            gravity[0]= averageList(rollingAverage[0]);
		            gravity[1]= averageList(rollingAverage[1]);
		            gravity[2]= averageList(rollingAverage[2]);
					if(Math.abs(gravity[0])>3)
					{
						init=false;
						if(!init_calib_1) {
							Toast.makeText(getApplicationContext(), "Not Calibrated", Toast.LENGTH_SHORT).show();
							init_calib_1=true;
						}
					}
					else if(Math.abs(gravity[0])<3)
					{
						init_calib_1=false;
						init_1=true;
						if(gravity[0]>=0)
						{
							calib_ct1=6-gravity[0];
						}
						else
						{
							calib_ct1=6+gravity[0];
						}
						
						mult_ct1=(int)((9/calib_ct1)*((55*((max_pitch-min_pitch)/2))/500));
					}
					if(Math.abs(gravity[1])>3)
					{
						init=false;
						if(!init_calib_2) {
								init_calib_2=true;
								Toast.makeText(getApplicationContext(), "Not Calibrated", Toast.LENGTH_SHORT).show();
						}
					}
					else if(Math.abs(gravity[1])<3)
					{
						init_2=true;
						init_calib_2=false;
						if(gravity[1]>=0)
						{
							calib_ct2=6-gravity[0];
						}
						else
						{
							calib_ct2=6+gravity[0];
						}
						
						mult_ct2=(int)((9/calib_ct2)*((55*((max_roll-min_roll)/2))/500));
					}
					if(init_1&&init_2)
					{
						plinear_acceleration[0]=(int)(gravity[0]*mult_ct1);
			            plinear_acceleration[1]=(int)(gravity[1]*mult_ct2);
			            gravity_c[0]=gravity[0];
			            gravity_c[1]=gravity[1];
			            
			            init=true;
					}
					
					
				}
				else
				{
					rollingAverage[0] = rolla(rollingAverage[0], event.values[0]);
		            rollingAverage[1] = rolla(rollingAverage[1], event.values[1]);
		            rollingAverage[2] = rolla(rollingAverage[2], event.values[2]);
		            gravity[0] = averageList(rollingAverage[0]);
		            gravity[1]= averageList(rollingAverage[1]);
		            gravity[2]= averageList(rollingAverage[2]);
					  // Remove the gravity contribution with the high-pass filter.
					  linear_acceleration[0] = (int)((gravity[0] - gravity_c[0])*mult_ct1);
					  linear_acceleration[1] = (int)((gravity[1] - gravity_c[1])*mult_ct2);

						nrotangle[0]=(int)(gravity[0]-gravity_c[0])*10;
						nrotangle[1]=(int)(gravity[1]-gravity_c[1])*10;

					if(nrotangle[0]>70||nrotangle[0]<(-70))
					{
						protangle[0]=nrotangle[0];
					}
					if(nrotangle[1]>70||nrotangle[1]<(-70))
					{
						protangle[1]=nrotangle[1];
					}
					if((Math.abs(linear_acceleration[0]-plinear_acceleration[0]))>5)
					{
						  if(linear_acceleration[0]>(max_pitch-mid_pitch))
						  {
							  linear_acceleration[0]=max_pitch-mid_pitch;
						  }
						  else if(linear_acceleration[0]<(min_pitch-mid_pitch))
						  {
							  linear_acceleration[0]=min_pitch-mid_pitch;
						  }
						  
						  plinear_acceleration[0]=linear_acceleration[0];
							  p=mid_pitch-linear_acceleration[0];

							  //bar.setY((((max_pitch-pitchh)*rel.getHeight())/(max_pitch-min_pitch))+rel.getY()-bar.getHeight()/2);
							 // smallcircle2.setY((((max_pitch-p)*bigcircle2.getHeight())/(max_pitch-min_pitch))+bigcircle2.getY()-smallcircle2.getHeight()/2);
						  //up_i.setY((((max_pitch-p)*bigcircle2.getHeight())/(max_pitch-min_pitch))+bigcircle2.getY()-up_i.getHeight()/2);
							   a="P:"+Integer.toString(p);
							  pitch.setText(a);
						  
					  }
					
							
					  if((Math.abs(linear_acceleration[1]-plinear_acceleration[1]))>5)
					  {
						  if(linear_acceleration[1]>(max_roll-mid_roll))
						  {
							  linear_acceleration[1]=(max_roll-mid_roll);
						  }
						  else if(linear_acceleration[1]<(min_roll-mid_roll))
						  {
							  linear_acceleration[1]=min_roll-mid_roll;
						  }
						  plinear_acceleration[1]=linear_acceleration[1];
						  
						  
							  r=mid_roll+linear_acceleration[1];
							  b="R:"+Integer.toString(r);
							  //smallcircle2.setX((((r-min_roll)*bigcircle2.getWidth())/(max_roll-min_roll))+bigcircle2.getX()-smallcircle2.getWidth()/2);
							  //left_i.setX((((r-min_roll)*smallcircle2.getWidth())/(max_roll-min_roll))+smallcircle2.getX()-left_i.getWidth()/2);
						  	  roll.setText(b);
						  
					  }
					}
			  
			  
			 
			  
			  
			}
			  
			 
			}
		public void onAccuracyChanged(Sensor sensor,int accuracy){
			
		}
	};
	
		sensorManager.registerListener(mEventListener, mSensor, SensorManager.SENSOR_DELAY_FASTEST);
	

		bigcircle1.setOnTouchListener(new View.OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				cent_x1 = (bigcircle1.getX() + bigcircle1.getWidth() / 2 - smallcircle1.getWidth() / 2);
				cent_y1 = (bigcircle1.getY() + bigcircle1.getHeight() / 2 - smallcircle1.getHeight() / 2);
				switch (event.getAction()) {
					case MotionEvent.ACTION_DOWN:
						a = (event.getX() + bigcircle1.getX() - smallcircle1.getWidth() / 2);
						b = (event.getY() + bigcircle1.getY() - smallcircle1.getHeight() / 2);

						if ((event.getX() >= bigcircle1.getX()) && (event.getX() <= bigcircle1.getWidth() + bigcircle1.getX()) && (event.getY() >= bigcircle1.getY()) && (event.getY() <= bigcircle1.getHeight() + bigcircle1.getY())) {
							if (!gyro) {

								if ((event.getX() - bigcircle1.getX()) < (bigcircle1.getWidth() / 4)) {
									y = min_yaw;
								}
								else if((event.getX()-bigcircle1.getX())>((bigcircle1.getWidth()*3)/4))
								{
									y=max_yaw;
								}
								else {
									y=mid_yaw;
								}
								a1 = Integer.toString(y);
								yaw.setText("Y: " + a1);
								smallcircle1.setX(a);


								/*
								if (!noyw) {
									y = (int) (min_yaw + ((event.getX() * (max_yaw - min_yaw)) / (bigcircle1.getWidth())));
								} else {
									y = mid_yaw;
								}
								a1 = Integer.toString(y);
								yaw.setText("Y: " + a1);
								if (!noyw) {
									smallcircle1.setX(a);
								}
								*/

							}

							//temp_t=(int)(max_throttle-((((max_throttle-min_throttle)*10))*((bigcircle1.getHeight())));
							temp_t=(int)(max_throttle-((event.getY()*(max_throttle-min_throttle))/(bigcircle1.getHeight())));

							//temp_t = (int) (max_throttle - ((max_throttle - min_throttle)) * (int) ((event.getY()  / (bigcircle1.getHeight()/10))));
							if (Math.abs(event.getY() - left_x)>smallcircle1.getWidth()/3) {
								t = temp_t;
								left_x=event.getY();
								smallcircle1.setY(b);
								bar1.setY(event.getY() + bigcircle1.getY() - bar1.getHeight() / 2);
								vb.vibrate(200);
							}

							else if((event.getY()-bigcircle1.getY())<smallcircle1.getWidth()/2)
							{
								t=max_throttle;
								left_x=event.getY();
								smallcircle1.setY(b);
								bar1.setY(event.getY() + bigcircle1.getY() - bar1.getHeight() / 2);
							}

							//else if(event.getY()>(bigcircle1.getY()+bigcircle1.getHeight()-smallcircle1.getWidth()/2))
							else if((event.getY()-bigcircle1.getY())>(bigcircle1.getHeight()-smallcircle1.getWidth()/2))
							{
								t=min_throttle;
								left_x=event.getY();
								smallcircle1.setY(b);
								bar1.setY(event.getY() + bigcircle1.getY() - bar1.getHeight() / 2);

							}

							b1 = Integer.toString(t);
							throt.setText("T: " + b1);
							throt1.setText("T: " + b1);


							//arrow.setY(event.getY()+bigcircle1.getY()-arrow.getHeight()/2);
							//int r = 0, g = 0, b = 0;

							if (t < 1400) {
								//r = 255;
								//g = 0;
							} else if (t < 1600) {
								//r = 200;
								//g = 200;
							} else {
								//g = 255;
								r = 0;
							}

							//arrowb.setBackgroundColor(Color.argb(155, r, g, b));
							flagsend = true;

						}
						break;

					case MotionEvent.ACTION_MOVE:

						a = (int) (event.getX() + bigcircle1.getX() - smallcircle1.getWidth() / 2);
						b = (int) (event.getY() + bigcircle1.getY() - smallcircle1.getHeight() / 2);

						if ((event.getX() >= bigcircle1.getX()) && (event.getX() <= bigcircle1.getWidth() + bigcircle1.getX()) && (event.getY() >= bigcircle1.getY()) && (event.getY() <= bigcircle1.getHeight() + bigcircle1.getY())) {
							if (!gyro) {
								if ((event.getX() - bigcircle1.getX()) < (bigcircle1.getWidth() / 4)) {
									y = min_yaw;
								}
								else if((event.getX()-bigcircle1.getX())>((bigcircle1.getWidth()*3)/4))
								{
									y=max_yaw;
								}
								else {
									y=mid_yaw;
								}
								a1 = Integer.toString(y);
								yaw.setText("Y: " + a1);
								smallcircle1.setX(a);
							}
							temp_t=(int)(max_throttle-((event.getY()*(max_throttle-min_throttle))/(bigcircle1.getHeight())));
							//temp_t = (int) (max_throttle - ((max_throttle - min_throttle) / 10) * (int) ((event.getY() / (bigcircle1.getHeight()/10))));
							if (Math.abs(event.getY()-left_x)>smallcircle1.getWidth()/3) {
								t = temp_t;
								left_x=event.getY();
								smallcircle1.setY(b);
								bar1.setY(event.getY() + bigcircle1.getY() - bar1.getHeight() / 2);
								vb.vibrate(200);
							}
							else if((event.getY()-bigcircle1.getY())<smallcircle1.getWidth()/2)
							{
								t=max_throttle;
								left_x=event.getY();
								smallcircle1.setY(b);
								bar1.setY(event.getY() + bigcircle1.getY() - bar1.getHeight() / 2);
							}
							else if((event.getY()-bigcircle1.getY())>(bigcircle1.getHeight()-smallcircle1.getWidth()/2))
							{
								t=min_throttle;
								left_x=event.getY();
								smallcircle1.setY(b);
								bar1.setY(event.getY() + bigcircle1.getY() - bar1.getHeight() / 2);

							}




							//temp_t=(int)(max_throttle-((event.getY()*(max_throttle-min_throttle))/(bigcircle1.getHeight())));
							//if(Math.abs(t-temp_t)>(int)((max_throttle-min_throttle)/10))
							//{
							//	t=temp_t;
							//	smallcircle1.setY(b);
							//	bar1.setY(event.getY()+bigcircle1.getY() - bar1.getHeight()/2);
							//}

							b1 = Integer.toString(t);
							throt.setText("T: " + b1);
							throt1.setText("T: " + b1);


							//arrow.setY(event.getY()+bigcircle1.getY()-arrow.getHeight()/2);
							//int r=0,g=0,b=0;

							if (t < 1400) {
								//r=255;
								//g=0;
							} else if (t < 1600) {
								//r=200;
								//g=200;
							} else {
								//g=255;
								//r=0;
							}

							//arrowb.setBackgroundColor(Color.argb(155, r, g, b));
							flagsend = true;

						}
						break;


					case MotionEvent.ACTION_UP:
						if (!gyro) {
							y = mid_yaw;
							//t=(1763-(((cent_y1+30)*612)/320));
							//a1 = Integer.toString(t);
						//throt.setText(a1);
						b1=Integer.toString(y);
						yaw.setText("Y: " +b1);
						smallcircle1.setX(cent_x1);
					}
					//smallcircle1.setY(cent_y1);
					flagsend=true;
					break;
				 
				}
				return true;
			}
		});
		
bigcircle2.setOnTouchListener(new View.OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				cent_x2 =( bigcircle2.getX() + bigcircle2.getWidth()/ 2-smallcircle2.getWidth()/2);
				cent_y2 = (bigcircle2.getY() + bigcircle2.getHeight()/ 2-smallcircle2.getHeight()/2);
				switch (event.getAction())
				{
				case MotionEvent.ACTION_DOWN:

					c=(event.getX()+bigcircle2.getX()-smallcircle2.getWidth()/2);
					d=(event.getY()+bigcircle2.getY()-smallcircle2.getHeight()/2);
					if((event.getX()>=0)&&(event.getX()<=bigcircle2.getWidth())&&(event.getY()>=0)&&(event.getY()<=bigcircle2.getHeight()))
					{
						if(!gyro)
						{
							temp_r=(int)(min_roll+((event.getX()*(max_roll-min_roll))/(bigcircle2.getWidth())));

							if(Math.abs(event.getX()-right_x)>smallcircle2.getWidth()/4)
							{
								r=temp_r;
								right_x=event.getX();
								bar3.setX(event.getX() + bigcircle2.getX() - bar3.getWidth() / 2);
								smallcircle2.setX(c);
							}
							else if((event.getX()-bigcircle2.getX())<smallcircle2.getWidth()/2)
							{
								r=min_roll;
								right_x=event.getX();
								bar3.setX(event.getX() + bigcircle2.getX() - bar3.getWidth() / 2);
								smallcircle2.setX(c);
							}
							else if((event.getX()-bigcircle2.getX())>(bigcircle2.getWidth()-(smallcircle2.getWidth()/2)))
							{
								r=max_roll;
								right_x=event.getX();
								bar3.setX(event.getX() + bigcircle2.getX() - bar3.getWidth() / 2);
								smallcircle2.setX(c);

							}




							temp_p=(int)(max_pitch-((event.getY()*(max_pitch-min_pitch))/(bigcircle2.getHeight())));


							//temp_t = (int) (max_throttle - ((max_throttle - min_throttle)) * (int) ((event.getY()  / (bigcircle1.getHeight()/10))));
							if (Math.abs(event.getY() - right_y)>smallcircle2.getWidth()/4) {
								p = temp_p;
								right_y=event.getY();
								smallcircle2.setY(d);
								bar2.setY(event.getY() + bigcircle2.getY() - bar2.getHeight() / 2);
							}

							else if((event.getY()-bigcircle2.getY())<smallcircle2.getWidth()/2)
							{
								p=max_pitch;
								right_y=event.getY();
								smallcircle2.setY(d);
								bar2.setY(event.getY() + bigcircle2.getY() - bar2.getHeight() / 2);
							}

							//else if(event.getY()>(bigcircle1.getY()+bigcircle1.getHeight()-smallcircle1.getWidth()/2))
							else if((event.getY()-bigcircle2.getY())>(bigcircle2.getHeight()-smallcircle2.getWidth()/2))
							{
								p=min_pitch;
								right_y=event.getY();
								smallcircle2.setY(d);
								bar2.setY(event.getY() + bigcircle2.getY() - bar2.getHeight() / 2);

							}

							a2 = Integer.toString(r);
							roll.setText("R: " +a2);
							b2 = Integer.toString(p);
							pitch.setText("P: " + b2);
							nquad_man_x = ((r - mid_roll)*70)/((max_roll-min_roll)/2);
							nquad_man_y = ((p - mid_pitch)*70)/((max_pitch-min_pitch)/ 2);
							quad.setRotationY(nquad_man_x);
							quad.setRotationX(nquad_man_y);

						}
						else
						{
							y=(int)(min_yaw+((event.getX()*(max_yaw-min_yaw))/(bigcircle2.getWidth())));
							a2 = Integer.toString(y);
							yaw1.setText("Y: "+a2);
							// yaw.setText("Y:"+a2);
							smallcircle2.setX(c);
							bar3.setX(event.getX() + bigcircle2.getX() - bar3.getWidth() / 2);
						}
						flagsend=true;

					}
				break;
				
				case MotionEvent.ACTION_MOVE:
					
					 c=(int)(event.getX()+bigcircle2.getX()-smallcircle2.getWidth()/2);
						
					d=(int)(event.getY()+bigcircle2.getY()-smallcircle2.getHeight()/2);
					if((event.getX()>=0)&&(event.getX()<=bigcircle2.getWidth())&&(event.getY()>=0)&&(event.getY()<=bigcircle2.getHeight()))
					{
						if(!gyro)
						{
							temp_r=(int)(min_roll+((event.getX()*(max_roll-min_roll))/(bigcircle2.getWidth())));

							if(Math.abs(event.getX()-right_x)>smallcircle2.getWidth()/4)
							{
								r=temp_r;
								right_x=event.getX();
								bar3.setX(event.getX() + bigcircle2.getX() - bar3.getWidth() / 2);
								smallcircle2.setX(c);
							}
							else if((event.getX()-bigcircle2.getX())<smallcircle2.getWidth()/2)
							{
								r=min_roll;
								right_x=event.getX();
								bar3.setX(event.getX() + bigcircle2.getX() - bar3.getWidth() / 2);
								smallcircle2.setX(c);
							}
							else if((event.getX()-bigcircle2.getX())>(bigcircle2.getWidth()-(smallcircle2.getWidth()/2)))
							{
								r=max_roll;
								right_x=event.getX();
								bar3.setX(event.getX() + bigcircle2.getX() - bar3.getWidth() / 2);
								smallcircle2.setX(c);

							}
							temp_p=(int)(max_pitch-((event.getY()*(max_pitch-min_pitch))/(bigcircle2.getHeight())));


							//temp_t = (int) (max_throttle - ((max_throttle - min_throttle)) * (int) ((event.getY()  / (bigcircle1.getHeight()/10))));
							if (Math.abs(event.getY() - right_y)>smallcircle2.getWidth()/4) {
								p = temp_p;
								right_y=event.getY();
								smallcircle2.setY(d);
								bar2.setY(event.getY() + bigcircle2.getY() - bar2.getHeight() / 2);
							}

							else if((event.getY()-bigcircle2.getY())<smallcircle2.getWidth()/2)
							{
								p=max_pitch;
								right_y=event.getY();
								smallcircle2.setY(d);
								bar2.setY(event.getY() + bigcircle2.getY() - bar2.getHeight() / 2);
							}

							//else if(event.getY()>(bigcircle1.getY()+bigcircle1.getHeight()-smallcircle1.getWidth()/2))
							else if((event.getY()-bigcircle2.getY())>(bigcircle2.getHeight()-smallcircle2.getWidth()/2))
							{
								p=min_pitch;
								right_y=event.getY();
								smallcircle2.setY(d);
								bar2.setY(event.getY() + bigcircle2.getY() - bar2.getHeight() / 2);

							}
							a2 = Integer.toString(r);
							roll.setText("R: " +a2);
							b2 = Integer.toString(p);
							pitch.setText("P: " + b2);
							nquad_man_x = ((r - mid_roll)*70)/((max_roll-min_roll)/2);
							nquad_man_y = ((p - mid_pitch)*70)/((max_pitch-min_pitch)/ 2);
							quad.setRotationY(nquad_man_x);
							quad.setRotationX(nquad_man_y);


						} else {
							y = (int) (min_yaw+((event.getX()*(max_yaw-min_yaw))/(bigcircle2.getWidth())));
							a2 = Integer.toString(y);
							//yaw.setText("Y:"+a2);
							yaw1.setText("Y: "+a2);
							smallcircle2.setX(c);
							bar3.setX(event.getX() + bigcircle2.getX() - bar3.getWidth() / 2);
						}
						flagsend=true;
						}
						break;
				
				
				case MotionEvent.ACTION_UP:
					if(!gyro)
					{
						r= mid_roll;
						p= mid_pitch;
						a2 = Integer.toString(r);
						roll.setText("R: " + a2);
						b2 = Integer.toString(p);
						pitch.setText("P: " +b2);
						smallcircle2.setX(cent_x2);
						smallcircle2.setY(cent_y2);
						bar2.setY(bigcircle2.getY() + bigcircle2.getHeight() / 2 - bar2.getHeight() / 2);
						bar3.setX(bigcircle2.getX() + bigcircle2.getWidth() / 2 - bar3.getWidth()/2);
						//nquad_man_x = ((r - mid_roll) / ((max_roll - min_roll) / 2)) * 70;
						//nquad_man_y = ((p - mid_pitch) / ((max_pitch - min_pitch) / 2)) * 70;
						nquad_man_x = ((r - mid_roll)*70)/((max_roll-min_roll)/2);
						nquad_man_y = ((p - mid_pitch)*70)/((max_pitch-min_pitch)/ 2);
						quad.setRotationY(nquad_man_x);
						quad.setRotationX(nquad_man_y);
					}
					else
					{
						y= mid_yaw;
						b2 = Integer.toString(y);
						//yaw.setText("Y: " + b2);
						yaw1.setText("Y: "+a2);
						smallcircle2.setX(cent_x2);
						bar3.setX(bigcircle2.getX() + bigcircle2.getWidth()/ 2-bar3.getWidth()/2);
					}
					flagsend=true;
					break;
				 
				}
				return true;
			}	
		});
	
	}
		
	public List<Float> rolla(List<Float> list, float newMember){
        if(list.size() == MAX_SAMPLE_SIZE){
            list.remove(0);
        }
        list.add(newMember);
        return list;
    }

    public float averageList(List<Float> tallyUp){

        float total=0;
        for(float item : tallyUp ){
            total+=item;
        }
        total = total/tallyUp.size();

        return total;
    }

	@Override
	public boolean onPrepareOptionsMenu(Menu menu){
		menu.clear();
		if(flag_admin&&iscon) {
			getMenuInflater().inflate(R.menu.main, menu);
		}
		else
		{
			getMenuInflater().inflate(R.menu.main,menu);
		}
		return super.onPrepareOptionsMenu(menu);

	}



	@Override
	public void onCreateContextMenu(ContextMenu menu,View v,ContextMenu.ContextMenuInfo menuInfo)
	{
		super.onCreateContextMenu(menu, v, menuInfo);

		//menu.setHeaderTitle("Select Mode:");
		if(v==modes)
		{
			getMenuInflater().inflate(R.menu.modemenu,menu);
		}
		else if(v==webv)
		{
			getMenuInflater().inflate(R.menu.streammenu,menu);
		}


		MenuItem.OnMenuItemClickListener listener = new MenuItem.OnMenuItemClickListener() {
			@Override
			public boolean onMenuItemClick(MenuItem item) {
				onContextItemSelected(item);
				return true;
			}
		};
		for (int s = 0; s < menu.size(); s++)
			menu.getItem(s).setOnMenuItemClickListener(listener);
	}

	@Override
	public boolean onContextItemSelected(MenuItem item)
	{
		switch (item.getItemId())
		{
			case R.id.ang:
				Toast.makeText(getApplicationContext(),"Angle Mode",Toast.LENGTH_LONG).show();
				break;
			case R.id.bar:
				Toast.makeText(getApplicationContext(),"Barometer Mode",Toast.LENGTH_LONG).show();
				break;
			case R.id.nrstream:
				webv.loadUrl(ip_address+":8080/cam.mjpg");
				flag_normalstream=true;
				break;
			case R.id.facestream:
				webv.loadUrl(ip_address+":8080/face_detect/cam.mjpg");
				flag_facestream=true;
				break;
			case R.id.fingerstream:
				webv.loadUrl(ip_address+":8080/finger_count/cam.mjpg");
				flag_fingerstream=true;
				break;
			case R.id.objecstream:
				webv.loadUrl(ip_address+":8080/track_object/cam.mjpg");
				flag_objectstream=true;
				break;
			default:
				return false;
		}
		return true;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
			getMenuInflater().inflate(R.menu.main_user,menu);
		return true;
	}
	@TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
	public boolean onOptionsItemSelected(MenuItem item) {

		switch (item.getItemId()) {
			case R.id.item1:
				final Dialog d = new Dialog(MainActivity.this,android.R.style.Theme_Black_NoTitleBar_Fullscreen);
				d.setContentView(R.layout.activity_throtmenu);
				thmin = (EditText) d.findViewById(R.id.thmint);
				thmax = (EditText) d.findViewById(R.id.thmaxt);
				thmin.setText("" + min_throttle);
				thmax.setText("" + max_throttle);
				bmenu = (Button) d.findViewById(R.id.button1);
				d.show();
				bmenu.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						q = thmax.getText().toString();
						max_throttle = Integer.parseInt(q);
						q = thmin.getText().toString();
						min_throttle = Integer.parseInt(q);
						SharedPreferences.Editor editor = sharedpreferences.edit();
						editor.putInt(max_throtkey, max_throttle);
						editor.putInt(min_throtkey, min_throttle);
						editor.commit();
						d.dismiss();
						// TODO Auto-generated method stub

					}
				});

				break;
			case R.id.noyaww:
				if (noyw) {
					item.setTitle("Yaw-Off");
					noyw = false;
				} else {
					noyw = true;
					item.setTitle("Yaw-On");
				}
				break;
			case R.id.midd:
				final Dialog m = new Dialog(MainActivity.this,android.R.style.Theme_Black_NoTitleBar_Fullscreen);
				m.setContentView(R.layout.mid);

				emidy = (EditText) m.findViewById(R.id.midyawt);
				emidp = (EditText) m.findViewById(R.id.midpitcht);
				emidr = (EditText) m.findViewById(R.id.midrollt);
				midmenu = (Button) m.findViewById(R.id.button1);
				emidy.setText("" + mid_yaw);
				emidp.setText("" + mid_pitch);
				emidr.setText("" + mid_roll);
				m.show();
				midmenu.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						q = emidy.getText().toString();
						mid_yaw = Integer.parseInt(q);
						q = emidp.getText().toString();
						mid_pitch = Integer.parseInt(q);
						q = emidr.getText().toString();
						mid_roll = Integer.parseInt(q);
						SharedPreferences.Editor editor = sharedpreferences.edit();
						editor.putInt(mid_pitchkey, mid_pitch);
						editor.putInt(mid_yawkey, mid_yaw);
						editor.putInt(mid_rollkey, mid_roll);
						editor.commit();

						m.dismiss();


					}
				});
				break;
			case R.id.gyr:
				if (!gyro) {
					//smallcircle1.setVisibility(View.INVISIBLE);
					//smallcircle2.setVisibility(View.INVISIBLE);
					bar2.setVisibility(View.INVISIBLE);
					item.setTitle("Gyro-Off");
					roll.setTextColor(Color.RED);
					pitch.setTextColor(Color.RED);
					yaw.setVisibility(View.INVISIBLE);
					throt.setVisibility(View.INVISIBLE);
					yaw1.setVisibility(View.VISIBLE);
					throt1.setVisibility(View.VISIBLE);
					a1 = Integer.toString(y);
					yaw1.setText("Y: " + a1);
					a1 = Integer.toString(t);
					throt1.setText("T: "+a1);
					gyro = true;
				} else {
					//smallcircle1.setVisibility(View.VISIBLE);
					//smallcircle2.setVisibility(View.VISIBLE);
					bar2.setVisibility(View.VISIBLE);

					gyro = false;
					roll.setTextColor(Color.WHITE);
					pitch.setTextColor(Color.WHITE);
					yaw.setVisibility(View.VISIBLE);
					throt.setVisibility(View.VISIBLE);
					yaw1.setVisibility(View.INVISIBLE);
					throt1.setVisibility(View.INVISIBLE);


					item.setTitle("Gyro-On");
				}
				break;

			case R.id.gyro_calib:
				if (!init) {
					init = true;
				} else {
					init_calib_2 = false;
					init_calib_1 = false;
					init = false;
				}
				break;
			case R.id.surv:
				Dialog d_vid;
				d_vid = new Dialog(MainActivity.this, android.R.style.Theme_Black_NoTitleBar_Fullscreen);
				d_vid.requestWindowFeature(Window.FEATURE_NO_TITLE);

				d_vid.setContentView(R.layout.web);
				webv = (WebView) d_vid.findViewById(R.id.webView);
				registerForContextMenu(webv);

				webv.clearCache(true);
				webv.loadUrl(ip_address+":8080/cam.mjpg");
				d_vid.show();

				d_vid.setOnDismissListener(new DialogInterface.OnDismissListener() {
					@Override
					public void onDismiss(DialogInterface dialog) {
						webv.destroy();
						Toast.makeText(getApplicationContext(), "Closed", Toast.LENGTH_SHORT).show();
					}
				});


				break;
			case R.id.naza:
				t = min_throttle;
				y = min_yaw;
				p = min_pitch;
				r = min_roll;
				flagsend=true;

				Thread tp = new Thread() {
					public void run() {
						try {
							Thread.sleep(1000);
							t = max_throttle;
							y = max_yaw;
							p = max_pitch;
							r = max_roll;
							flagsend=true;

							//Toast.makeText(getApplicationContext(),"Naza Calibrated",Toast.LENGTH_SHORT).show();

						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}

				};
				tp.start();
				break;

			case R.id.ip:
				final Dialog ip_d = new Dialog(MainActivity.this,android.R.style.Theme_Black_NoTitleBar_Fullscreen);
				ip_d.setContentView(R.layout.ip_set);

				ip_t = (EditText) ip_d.findViewById(R.id.ip_ed);
				ip_b = (Button) ip_d.findViewById(R.id.ip_b);
				ip_t.setText("" + ip_address);
				ip_d.show();
				ip_b.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View view) {
						String ipc = ip_t.getText().toString();
						ip_address = ipc;
						SharedPreferences.Editor editor = sharedpreferences.edit();
						editor.putString(ip_key, ip_address);
						editor.commit();
						ip_d.dismiss();


					}
				});
				break;
			case R.id.minmax:
				final Dialog de = new Dialog(MainActivity.this,android.R.style.Theme_Black_NoTitleBar_Fullscreen);
				de.setContentView(R.layout.minmaxset);
				eminy = (EditText) de.findViewById(R.id.minyawt);
				eminp = (EditText) de.findViewById(R.id.minpitcht);
				eminr = (EditText) de.findViewById(R.id.minrollt);
				emaxy = (EditText) de.findViewById(R.id.maxyawt);
				emaxp = (EditText) de.findViewById(R.id.maxpitcht);
				emaxr = (EditText) de.findViewById(R.id.maxrollt);
				minmaxmenu = (Button) de.findViewById(R.id.button1);
				eminy.setText("" + min_yaw);
				eminp.setText("" + min_pitch);
				eminr.setText("" + min_roll);
				emaxy.setText("" + max_yaw);
				emaxp.setText("" + max_pitch);
				emaxr.setText("" + max_roll);
				de.show();
				minmaxmenu.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View view) {
						q = eminy.getText().toString();
						min_yaw = Integer.parseInt(q);
						q = eminp.getText().toString();
						min_pitch = Integer.parseInt(q);
						q = eminr.getText().toString();
						min_roll = Integer.parseInt(q);
						q = emaxy.getText().toString();
						max_yaw = Integer.parseInt(q);
						q = emaxp.getText().toString();
						max_pitch = Integer.parseInt(q);
						q = emaxr.getText().toString();
						max_roll = Integer.parseInt(q);
						SharedPreferences.Editor editor = sharedpreferences.edit();
						editor.putInt(min_pitchkey, min_pitch);
						editor.putInt(min_yawkey, min_yaw);
						editor.putInt(min_rollkey, min_roll);
						editor.putInt(max_pitchkey, max_pitch);
						editor.putInt(max_yawkey, max_yaw);
						editor.putInt(max_rollkey, max_roll);
						editor.commit();

						de.dismiss();
					}
				});
				break;
			case R.id.gesture:
				if(gest_on)
				{
					gest_on=false;
					fragmen.setVisibility(View.GONE);
					FragmentTransaction ft = fm.beginTransaction();
					ft.hide(fr);
					ft.commit();
				}
				else {
					fr = new gesture();
					ctrl.setVisibility(View.GONE);
					fr.setArguments(getIntent().getExtras());
					fragmen.setVisibility(View.VISIBLE);
					fragmen.setAlpha((float) 1.0);
					gest_on = true;
					FragmentTransaction ft = fm.beginTransaction();
					ft.add(fragmen.getId(), new gesture());
					ft.commit();
				}
				break;
			case R.id.rec:
				/*
				mNotifyManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
				mBuilder = new NotificationCompat.Builder(MainActivity.this);
				mBuilder.setContentTitle("Downloading Video")
						.setContentText("Download in progress 0%")
						.setSmallIcon(R.drawable.iconcircle3);
				mBuilder.setProgress(100, 0, false);
				mNotifyManager.notify(id, mBuilder.build());
				*/
				new ftpdownload(MainActivity.this).execute(1);

				break;
			case R.id.update:
				appupdate asyncTask;
				asyncTask=new appupdate((Context) MainActivity.this);
				asyncTask.delegate=this;
				asyncTask.execute();
				break;
			case R.id.pathplot:
				if(!pathflag)
				{
					fr= new maps();
					ctrl.setVisibility(View.GONE);
					Toast.makeText(getApplicationContext(),"maps starting",Toast.LENGTH_LONG).show();
					fr.setArguments(getIntent().getExtras());
					fragmen.setVisibility(View.VISIBLE);
					fragmen.setAlpha((float) 1.0);
					pathflag=true;
					FragmentTransaction ft=fm.beginTransaction();
					ft.add(fragmen.getId(),fr);
					ft.commit();
				}
				else
				{
					pathflag=false;
					fragmen.setVisibility(View.GONE);
					FragmentTransaction ft = fm.beginTransaction();
					ft.hide(fr);
					ft.commit();
				}
				break;
			case  R.id.log:
				new ftpdownload(MainActivity.this).execute(2);
				break;
			case R.id.recrequest:
				if(!recrequest)
				{
					reqrec=new WebView(getApplicationContext());
					reqrec.clearCache(true);
					reqrec.loadUrl(ip_address + ":8080/record/cam.mjpg");
					recrequest=true;
					reqrec.destroy();
					item.setTitle("Stop Onboard Recording");
				}
				else
				{
					reqrec=new WebView(getApplicationContext());
					reqrec.clearCache(true);
					reqrec.loadUrl(ip_address + ":8080/record_end/cam.mjpg");
					reqrec.destroy();
					recrequest=false;
					item.setTitle("Onboard Recording");
				}

				break;
			case R.id.logrequest:
				if(!logreq)
				{
					reqlog=new WebView(getApplicationContext());
					reqlog.clearCache(true);
					reqlog.loadUrl(ip_address + ":");
					logreq=true;
				}
				else
				{
					logreq=false;
					reqlog.destroy();
				}
				break;
			case R.id.setupval:
				final EditText e1,e2,e3,e4;
				Button b;
				final Dialog m1 = new Dialog(MainActivity.this,android.R.style.Theme_Black_NoTitleBar_Fullscreen);
				m1.setContentView(R.layout.ardsetup);

				e1= (EditText) m1.findViewById(R.id.th);
				e2= (EditText) m1.findViewById(R.id.ya);
				e3 = (EditText) m1.findViewById(R.id.pi);
				e4=(EditText)m1.findViewById(R.id.ro);
				b = (Button) m1.findViewById(R.id.button1);
				e1.setText("" + ard_t);
				e2.setText("" + ard_y);
				e3.setText("" + ard_p);
				e4.setText("" + ard_r);
				m1.show();
				b.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						ard_t = Integer.parseInt(e1.getText().toString());
						ard_y = Integer.parseInt(e2.getText().toString());
						ard_p=Integer.parseInt(e3.getText().toString());
						ard_r=Integer.parseInt(e4.getText().toString());
						SharedPreferences.Editor editor = sharedpreferences.edit();
						editor.putInt(ard_tkey, ard_t);
						editor.putInt(ard_ykey, ard_y);
						editor.putInt(ard_pkey, ard_p);
						editor.putInt(ard_rkey, ard_r);
						editor.commit();
						setup_str="t"+(Integer.toString(ard_t))+"#"+"y"+(Integer.toString(ard_y))+"#"+"p"+(Integer.toString(ard_p))+"#"+"r"+(Integer.toString(ard_r))+"#";
						setup_str_t="t"+(Integer.toString(ard_t))+"#";
						setup_str_y="y"+(Integer.toString(ard_y))+"#";
						setup_str_p="p"+(Integer.toString(ard_p))+"#";
						setup_str_r="r"+(Integer.toString(ard_r))+"#";
						if(iscon)
						{
							flag_setup=true;
						}
						m1.dismiss();
					}
				});
				break;

			case R.id.maponmain:
				if (!pathflagonmain) {
					bigcircle2.setBackgroundColor(Color.TRANSPARENT);
					fr = new livemaps();
					fr.setArguments(getIntent().getExtras());
					pathflagonmain = true;
					FragmentTransaction ft = fm.beginTransaction();
					ft.add(maponmain.getId(), fr);
					ft.commit();
				} else {
					pathflagonmain = false;
					FragmentTransaction ft = fm.beginTransaction();
					bigcircle2.setBackgroundResource(R.drawable.backright1);
					ft.hide(fr);
					ft.commit();
				}
				break;

			case R.id.virtual_reality:
				if(vr_on)
				{
					vr_on=false;
					fragmen.setVisibility(View.GONE);
					FragmentTransaction ft = fm.beginTransaction();
					ft.hide(fr);
					ft.commit();
				}
				else {
					fr = new virtual_reality();
					ctrl.setVisibility(View.GONE);
					fr.setArguments(getIntent().getExtras());
					fragmen.setVisibility(View.VISIBLE);
					fragmen.setAlpha((float) 1.0);
					vr_on = true;
					FragmentTransaction ft = fm.beginTransaction();
					ft.add(fragmen.getId(), new virtual_reality());
					ft.commit();
				}
				break;

			default:
				break;
		}
		return true;
	}
	public static Bitmap getBitmapFromURL(String src) {
		try {
			Log.e("src",src);
			URL url = new URL(src);
			HttpURLConnection connection = (HttpURLConnection)url.openConnection();
			connection.setDoInput(true);
			connection.connect();
			InputStream input = connection.getInputStream();
			Bitmap myBitmap = BitmapFactory.decodeStream(input);
			Log.e("Bitmap","returned");
			return myBitmap;
		} catch (IOException e) {
			e.printStackTrace();
			Log.e("Exception",e.getMessage());
			return null;
		}
	}
	public boolean onTouchEvent(MotionEvent event) {
        // MotionEvent object holds X-Y values
       
        return super.onTouchEvent(event);
    }

	@Override
	public void processFinish(int output) {
		if(output==1)
		{
			flagupdate=false;
			File sdCard = Environment.getExternalStorageDirectory();
			File dir = new File (sdCard.getAbsolutePath() + "/squad/app");
			File file = new File(dir, "squadapp.apk");
			Intent a=new Intent();
			Intent promptInstall = new Intent(Intent.ACTION_VIEW)
					.setDataAndType(Uri.fromFile(file),"application/vnd.android.package-archive")
					.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			startActivity(promptInstall);
		}
		else if(output==2)
		{
			new AlertDialog.Builder(this).setTitle("Update Available:"+Double.toString(appupdate.cur_version)).setMessage("Do you want to upgrade?").setPositiveButton("Yes", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					flagupdate=true;
					mNotifyManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
					mBuilder = new NotificationCompat.Builder(MainActivity.this);
					mBuilder.setContentTitle("Download")
							.setContentText("Download in progress 0%")
							.setSmallIcon(R.drawable.iconcircle3);
					mBuilder.setProgress(100, 0, false);
					mNotifyManager.notify(id, mBuilder.build());
					appupdate asyncTask;
					asyncTask=new appupdate((Context) MainActivity.this);
					asyncTask.delegate=MainActivity.this;
					asyncTask.execute();
				}
			}).setNegativeButton("No", null).show();
		}

	}


	public void onBackPressed()
	{
		if(gest_on) {
			new AlertDialog.Builder(this).setTitle("").setMessage("Exit Gesture Mode?").setPositiveButton("Yes",new DialogInterface.OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					gest_on=false;
					ctrl.setVisibility(View.VISIBLE);
					fragmen.setVisibility(View.GONE);
					//fm.popBackStack();
					FragmentTransaction ft = fm.beginTransaction();
					ft.remove(fr);
					ft.commit();
				}
			}).setNegativeButton("No", null).show();
		}
		else if(pathflag){
			new AlertDialog.Builder(this).setTitle("").setMessage("Exit Path Mode?").setPositiveButton("Yes",new DialogInterface.OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					pathflag=false;
					ctrl.setVisibility(View.VISIBLE);
					fragmen.setVisibility(View.GONE);
					fm.popBackStack();
					FragmentTransaction ft = fm.beginTransaction();
					fm.popBackStackImmediate();
					ft.commit();

				}
			}).setNegativeButton("No", null).show();

		}
		else {
			new AlertDialog.Builder(this).setTitle("Exit").setMessage("Are you sure you want to exit?").setPositiveButton("Yes", new DialogInterface.OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					flag_end = true;
					if (flag_btlistener) {
						unregisterReceiver(mReceiver);
						flag_btlistener=false;
					}
					finish();

				}
			}).setNegativeButton("No", null).show();
		}
	}

	@Override
	public void onFragmentInteraction(Uri uri) {
		if(gest_on) {
			setContentView(R.layout.fragment_gesture);
		}
		else if(pathflag)
		{
			setContentView(R.layout.fragment_maps);
		}
		else if(vr_on) {
			setContentView(R.layout.virtual_reality_xml);
		}

	}
	final BroadcastReceiver mReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);

			if (BluetoothDevice.ACTION_FOUND.equals(action)) {
				//Device found
			}
			else if (BluetoothDevice.ACTION_ACL_CONNECTED.equals(action)) {

				//Device is now connected
			}
			else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {
				//Done searching
			}
			else if (BluetoothDevice.ACTION_ACL_DISCONNECT_REQUESTED.equals(action)) {
				//Device is about to disconnect
			}
			else if (BluetoothDevice.ACTION_ACL_DISCONNECTED.equals(action)) {
				//Device has disconnected
				if (flag_btlistener) {
					unregisterReceiver(mReceiver);
					flag_btlistener=false;
				}
				iscon=false;
				Toast.makeText(getApplicationContext(),"Device Disconnected",Toast.LENGTH_LONG).show();
				flag_bt=true;
				flag_naza=true;
				front_l.setVisibility(View.VISIBLE);
				AnimatorSet flip = (AnimatorSet) AnimatorInflater.loadAnimator(getApplicationContext(),
						R.animator.left_out);
				flip.setTarget(main_c);
				final AnimatorSet flip1 = (AnimatorSet) AnimatorInflater.loadAnimator(getApplicationContext(),
						R.animator.left_in);
				flip1.setTarget(front_l);
				flip.start();
				flip1.start();
				t=min_throttle;
				y=mid_yaw;
				p=mid_pitch;
				r=mid_roll;
				a2 = Integer.toString(r);
				roll.setText("R: " + a2);
				b2 = Integer.toString(p);
				pitch.setText("P: " + b2);
				smallcircle2.setX(cent_x2);
				smallcircle2.setY(cent_y2);
				bar2.setY(bigcircle2.getY() + bigcircle2.getHeight() / 2 - bar2.getHeight() / 2);
				bar3.setX(bigcircle2.getX() + bigcircle2.getWidth() / 2 - bar3.getWidth() / 2);
				nquad_man_x = ((r - mid_roll)*70)/((max_roll-min_roll)/2);
				nquad_man_y = ((p - mid_pitch)*70)/((max_pitch-min_pitch)/ 2);
				quad.setRotationY(nquad_man_x);
				quad.setRotationX(nquad_man_y);
				cent_x1 = (bigcircle1.getX() + bigcircle1.getWidth() / 2 - smallcircle1.getWidth() / 2);
				b1=Integer.toString(y);
				yaw.setText("Y: " + b1);
				smallcircle1.setX(cent_x1);
				b1 = Integer.toString(t);
				throt.setText("T: " + b1);
				throt1.setText("T: " + b1);
				bar1.setY((((max_throttle - t) * bigcircle1.getHeight()) / (max_throttle - min_throttle)) + bigcircle1.getY() - bar1.getHeight() / 2);
				smallcircle1.setY((((max_throttle - t) * bigcircle1.getHeight()) / (max_throttle - min_throttle)) + bigcircle1.getY() - bar1.getHeight() / 2);
				flag_device_select=false;
				arm_view.setVisibility(View.GONE);
				//temp_t=(int)(max_throttle-((event.getY()*(max_throttle-min_throttle))/(bigcircle1.getHeight())));
				//tt=(((max_throttle-t)*bigcircle1.getHeight())/(max_throttle-min_throttle));





			}
		}
	};
}

