package com.example.squad_test;

import android.app.Activity;
import android.app.Dialog;
import android.app.Fragment;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link virtual_reality.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link virtual_reality#newInstance} factory method to
 * create an instance of this fragment.
 */




    // Sending protocol( VR Servo)(
    // "~an#" to set camera to center
    // "~p(+,-)7#" to send pan difference
    // "~t(+,-)10#" to send tilt difference



public class virtual_reality extends Fragment {
    BufferedReader buffer;
    public static boolean boolforservo=false,first=true,second=true;
    SensorManager sensorManager;
    boolean b=false;
    Sensor mSensor,mSensor1;
    int ro,pi;
    List<Float>[] rollingAverage = new List[3];
    List<Float>[] rollingAverage1 = new List[3];
    final float[] gravity =new float[3];
    final int[] linear_acceleration=new int[3];
    final float[] gravity_c =new float[3];
    private static final int MAX_SAMPLE_SIZE = 5;
    final float[] plinear_acceleration =new float[3];
    float[] gravity1=new float[3];
    float[] magnetic1=new float[3];
    float[] anglechange =new float[3];
    float[] rforangle =new float[3];
    float[] pforangle =new float[9];
    float[] R2 = new float[9];




    float[] gravity2=new float[3];
    float[] magnetic2=new float[3];
    float[] nrotangle =new float[3];
    float[] protangle =new float[3];
    int max_pitch,min_pitch,mid_pitch;
    int max_roll,min_roll,mid_roll;
    int mult_ct1=1;
    int mult_ct2=1;
    TextView tt;
    ImageView u;
    ImageView d;
    ImageView l;
    ImageView r;
    TextView forx,fory,forz;
    String strsend="";
    byte[] buff = new byte[1024];
    int read;
    int servo_init_angle=0;
    int sendangle=0;
    double sendangle1=0;

    int psendangle=0;
    int tempangle=0;
    int ptempangle=0;
    Thread recieve_thread;


    private static String address = "98:D3:31:20:81:F7";
    boolean socketcreated = false;
    private static final int REQUEST_ENABLE_BT = 1;
    private BluetoothAdapter btAdapter = null;
    private BluetoothSocket btSocket = null;

    private OutputStream outStream = null;
    private InputStream inStream = null;

    private static UUID MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    IntentFilter filter1,filter2;
    Boolean recrequest=false,logreq=false,flag_btlistener=false;
    ArrayAdapter<String> btarrayadapter,btarrayadapter1;
    boolean arm_btn=false,flag_setup=false,flag_device_select=false,initialconfig=false;
    int j,i,blue_test=0;
    boolean iscon =false;
    Handler hand;
    String str_send="";
    String line="";
    double orien1[] = new double[3];
    WebView web;
    double factor=0.5;






    void select_connect()
    {

        if (!iscon) {


            //blue.setText("Connected");
            blue_test = checkBTState();

            if (blue_test == 1&&flag_device_select) {
                Thread conbt = new Thread() {
                    @Override
                    public void run() {

                        connectBT();
                    }
                };
                conbt.start();
            }

        } else {
        }

    }

    private int checkBTState() {
        filter1 = new IntentFilter(BluetoothDevice.ACTION_ACL_CONNECTED);

        filter2 = new IntentFilter(BluetoothDevice.ACTION_ACL_DISCONNECTED);
        flag_btlistener=true;

        // Check for Bluetooth support and then check to make sure it is turned on

        // Emulator doesn't support Bluetooth and will return null
        if(btAdapter==null) {
            Toast.makeText(getActivity().getApplicationContext(), "connection failed", Toast.LENGTH_SHORT).show();
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
                    final Dialog btdialog = new Dialog(getActivity());
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
                    //Toast.makeText(this, "close error", Toast.LENGTH_SHORT);
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
                    Toast.makeText(getActivity().getApplicationContext(), "Connected", Toast.LENGTH_SHORT).show();
                    iscon=true;
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
            inStream =  btSocket.getInputStream();
            buffer = new BufferedReader(new InputStreamReader(inStream));
            recieve_thread.start();

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



    public void init_servo()
    {
        if(iscon)
        {
            sendData("~an#");
            /*
            BufferedReader r = new BufferedReader(new InputStreamReader(inStream));
            StringBuilder total = new StringBuilder();
            String line="";
            try {
                while ((line = r.readLine()) != null) {
                    total.append(line);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            servo_init_angle=Integer.parseInt(line);
            */
            virtual_reality.boolforservo=false;

        }
        else
        {
            //Toast.makeText(getActivity().getApplicationContext(),"Not connected",Toast.LENGTH_LONG).show();
            virtual_reality.boolforservo=true;
        }

    }



    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment virtual_reality.
     */
    // TODO: Rename and change types and number of parameters
    public static virtual_reality newInstance(String param1, String param2) {
        virtual_reality fragment = new virtual_reality();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public virtual_reality() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



    }

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View vie=inflater.inflate(R.layout.virtual_reality_xml,container,false);
        forx=(TextView)vie.findViewById(R.id.x);
        fory=(TextView)vie.findViewById(R.id.y);
        forz=(TextView)vie.findViewById(R.id.z);
        web=(WebView)vie.findViewById(R.id.webView);
        web.clearCache(true);
        web.loadUrl(MainActivity.ip_address+":8080/cam.mjpg");
        recieve_thread = new Thread() {
            @Override
            public void run() {

                //StringBuilder total = new StringBuilder();


                    try {
                        while ((line = buffer.readLine()) != null) {
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    forz.setText(line);
                                }
                            });

                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

            }
        };



        btAdapter = BluetoothAdapter.getDefaultAdapter();
        btarrayadapter=new ArrayAdapter<String>(getActivity(),android.R.layout.simple_list_item_1);
        btarrayadapter1=new ArrayAdapter<String>(getActivity(),android.R.layout.simple_list_item_1);
        hand=new Handler();
        select_connect();
        init_servo();


        sensorManager = (SensorManager)getActivity().getSystemService(Context.SENSOR_SERVICE);
        mSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mSensor1=sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

        gravity_c[0]=0;
        gravity_c[1]=0;
        gravity_c[2]=0;
        rollingAverage[0] = new ArrayList<Float>();
        rollingAverage[1] = new ArrayList<Float>();
        rollingAverage[2] = new ArrayList<Float>();
        rollingAverage1[0] = new ArrayList<Float>();
        rollingAverage1[1] = new ArrayList<Float>();
        rollingAverage1[2] = new ArrayList<Float>();
        pforangle[0]=0;pforangle[1]=0;pforangle[2]=0;

        final SensorEventListener eventlisten=new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent event) {

                if (((MainActivity) getActivity()).vr_on) {

                    if(event.sensor.getType()==Sensor.TYPE_ACCELEROMETER) {
                        gravity1 = event.values;
                    }
                    if (event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD)
                        magnetic1 = event.values;
                    if (gravity1 != null && magnetic1 != null) {

                        float R1[] = new float[9];
                        float I1[] = new float[9];

                        if (SensorManager.getRotationMatrix(R1, I1, gravity1, magnetic1)) {
                            SensorManager.remapCoordinateSystem(R1, SensorManager.AXIS_Y, SensorManager.AXIS_MINUS_X, R2);
                            float[] orien = new float[3];
                            SensorManager.getOrientation(R1, orien);
                            BigDecimal bd = new BigDecimal(Float.toString(orien[0]));
                            bd = bd.setScale(3, BigDecimal.ROUND_HALF_UP);
                            orien[0] = (float) (bd.floatValue() + Math.PI);
                            orien1[0] = orien[0];
                            orien1[0] = roundToDecimalPlaces(orien1[0], 3);
                            //new sendtowifiserver().execute(Float.toString(orien[0]));
                            tempangle = (int) Math.toDegrees(orien1[0]);
                            if (iscon)
                            {
                                //forx.setText(Float.toString(gravity[0]));
                                fory.setText(Double.toString(orien1[0]));
                                if (boolforservo) {
                                    init_servo();
                                } else {
                                    str_send = "";
                                    if (first) {
                                        ptempangle = tempangle;
                                        first = false;
                                    } else {

                                        sendangle1 = (1-factor)*psendangle+((factor)*(ptempangle - tempangle));
                                        sendangle=(int)sendangle1;
                                        if (sendangle != 0) {
                                            if ((Math.abs(sendangle) >= 2)&&(Math.abs(sendangle)<20)) {
                                                forx.setText(Double.toString(orien1[0])+"S:"+Integer.toString(sendangle)+"P:"+Integer.toString(ptempangle)+"T:"+Integer.toString(tempangle));
                                                ptempangle = tempangle;
                                                psendangle=sendangle;
                                                if (sendangle >= 0) {
                                                    str_send = "~p+" + Integer.toString(sendangle) + "#";
                                                } else {
                                                    str_send = "~p" + Integer.toString(sendangle) + "#";
                                                }
                                                if(iscon)
                                                    sendData(str_send);

                                            }
                                            else if(Math.abs(sendangle)>180)
                                            {
                                                ptempangle=tempangle;
                                            }
                                            second = true;
                                        } else {
                                            str_send = "";
                                            //if (second)
                                            //    sendData("");
                                            second = false;
                                        }

                                    }
                                }
                            }


                        }
                    }
                    // if(gyro)
                    {
                        //init_1=false;
                        //init_2=false;

                        //else
                    /*
                    {
                        rollingAverage[0] = rolla(rollingAverage[0], event.values[0]);
                        rollingAverage[1] = rolla(rollingAverage[1], event.values[1]);
                        rollingAverage[2] = rolla(rollingAverage[2], event.values[2]);


                        gravity[0] = averageList(rollingAverage[0]);
                        gravity[1] = averageList(rollingAverage[1]);
                        gravity[2] = averageList(rollingAverage[2]);

                        //gravity[0] = alpha * gravity[0] + (1 - alpha) * event.values[0];
                        //gravity[1] = alpha * gravity[1] + (1 - alpha) * event.values[1];
                        //gravity[2] = alpha * gravity[2] + (1 - alpha) * event.values[2];

                        // Remove the gravity contribution with the high-pass filter.
                        linear_acceleration[0] = (int) ((gravity[0] - gravity_c[0]) * mult_ct1);
                        linear_acceleration[1] = (int) ((gravity[1] - gravity_c[1]) * mult_ct2);
                        linear_acceleration[2] = (int) ((gravity[2] - gravity_c[2]) * mult_ct2);

                        nrotangle[0] =  Math.round((gravity[0] - gravity_c[0]) * 1000) / 100;
                        nrotangle[1] =  Math.round((gravity[1] - gravity_c[1]) * 1000) / 100;
                        nrotangle[2] =  Math.round((gravity[2] - gravity_c[2]) * 1000) / 100;

                        if (((MainActivity) getActivity()).gest_on) {
                        }

                    }
                    */
                    }

                }
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int i) {

            }

        };
        sensorManager.registerListener(eventlisten, mSensor, SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener(eventlisten,mSensor1, SensorManager.SENSOR_DELAY_NORMAL);


        return vie;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
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

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    double roundToDecimalPlaces(double value, int decimalPlaces)
    {
        double shift = Math.pow(10,decimalPlaces);
        return Math.round(value*shift)/shift;
    }
}
