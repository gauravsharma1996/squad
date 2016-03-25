package com.example.squad_test.util;

import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.squad_test.MainActivity;
import com.example.squad_test.R;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Handler;
import java.util.logging.LogRecord;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link gesture.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link gesture#newInstance} factory method to
 * create an instance of this fragment.
 */
public class gesture extends Fragment {
    SensorManager sensorManager;
    boolean b=false;
    Sensor mSensor;
    int ro,pi;
    List<Float>[] rollingAverage = new List[3];
    final float[] gravity =new float[3];
    final int[] linear_acceleration=new int[3];
    final float[] gravity_c =new float[3];
    private static final int MAX_SAMPLE_SIZE = 10;
    final int[] plinear_acceleration =new int[3];
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
     * @return A new instance of fragment gesture.
     */
    // TODO: Rename and change types and number of parameters
    public static gesture newInstance(String param1, String param2) {
        gesture fragment = new gesture();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public gesture() {
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
      //  getActivity().setContentView(R.layout.fragment_gesture);
        max_pitch=((MainActivity)getActivity()).max_pitch;
        max_roll=((MainActivity)getActivity()).max_roll;
        min_roll=((MainActivity)getActivity()).min_roll;
        min_pitch=((MainActivity)getActivity()).min_pitch;
        mid_pitch=((MainActivity)getActivity()).mid_pitch;
        mid_roll=((MainActivity)getActivity()).mid_roll;
        View vie=inflater.inflate(R.layout.fragment_gesture,container,false);
        u=(ImageView)vie.findViewById(R.id.up);
        d=(ImageView)vie.findViewById(R.id.down);
        l=(ImageView)vie.findViewById(R.id.le);
        r=(ImageView)vie.findViewById(R.id.ri);
        //tt=(TextView)vie.findViewById(R.id.dis);

       // upp.setAlpha((float)1.0);

       // Toast.makeText(getActivity(),"aaaa",Toast.LENGTH_SHORT).show();
        sensorManager = (SensorManager)getActivity().getSystemService(Context.SENSOR_SERVICE);
        mSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

        gravity_c[0]=0;
        gravity_c[1]=0;
        rollingAverage[0] = new ArrayList<Float>();
        rollingAverage[1] = new ArrayList<Float>();
        rollingAverage[2] = new ArrayList<Float>();

        final SensorEventListener eventlisten=new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent event) {

                // if(gyro)
                {
                    //init_1=false;
                    //init_2=false;

                    //else
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

                        nrotangle[0] = (int) (gravity[0] - gravity_c[0]) * 10;
                        nrotangle[1] = (int) (gravity[1] - gravity_c[1]) * 10;


                        if (nrotangle[0] > 70 || nrotangle[0] < (-70)) {
                            protangle[0] = nrotangle[0];
                        }
                        if (nrotangle[1] > 70 || nrotangle[1] < (-70)) {
                            protangle[1] = nrotangle[1];
                        }

                        if (((MainActivity) getActivity()).gest_on) {

                            if (event.values[0] > 6) {
                                pi = mid_pitch - 100;
                                d.setAlpha((float) 1.0);
                                u.setAlpha((float) 0.0);
                            } else if (event.values[0] > 3) {
                                pi = mid_pitch - 100;
                                d.setAlpha((float) 0.4);
                                u.setAlpha((float) 0.0);
                            } else if (event.values[0] < -6) {
                                pi = mid_pitch + 100;
                                u.setAlpha((float) 1.0);
                                d.setAlpha((float) 0.0);
                            } else if (event.values[0] < -3) {
                                pi = mid_pitch + 100;
                                u.setAlpha((float) 0.4);
                                d.setAlpha((float) 0.0);
                            } else {
                                pi = mid_pitch;
                                d.setAlpha((float) 0.0);
                                u.setAlpha((float) 0.0);
                            }


                            if (event.values[1] > 6) {
                                ro = mid_roll + 100;
                                r.setAlpha((float) 1.0);
                                l.setAlpha((float) 0.0);
                            } else if (event.values[1] > 3) {
                                ro = mid_roll + 100;
                                r.setAlpha((float) 0.4);
                                l.setAlpha((float) 0.0);
                            } else if (event.values[1] < -6) {
                                ro = mid_roll - 100;
                                l.setAlpha((float) 1.0);
                                r.setAlpha((float) 0.0);
                            } else if (event.values[1] < -3) {
                                ro = mid_roll - 100;
                                l.setAlpha((float) 0.4);
                                r.setAlpha((float) 0.0);
                            } else {
                                ro = mid_roll;
                                l.setAlpha((float) 0.0);
                                r.setAlpha((float) 0.0);
                            }


                            //linear_acceleration[0]=event.values[0];
                            //linear_acceleration[1]=event.values[1];

                        /*
                        if ((Math.abs(linear_acceleration[0] - plinear_acceleration[0])) > 5) {
                            if (linear_acceleration[0] > (max_pitch - mid_pitch)) {
                                linear_acceleration[0] = max_pitch - mid_pitch;
                            } else if (linear_acceleration[0] < (min_pitch - mid_pitch)) {
                                linear_acceleration[0] = min_pitch - mid_pitch;
                            }

                            plinear_acceleration[0] = linear_acceleration[0];
                            //linear_acceleration[0]=(int)(sum_p/2);
                            //sum_p=0;
                            //counter_p=0;
                            pi = mid_pitch - linear_acceleration[0];

                            //bar.setY((((max_pitch-pitchh)*rel.getHeight())/(max_pitch-min_pitch))+rel.getY()-bar.getHeight()/2);
                            // smallcircle2.setY((((max_pitch-p)*bigcircle2.getHeight())/(max_pitch-min_pitch))+bigcircle2.getY()-smallcircle2.getHeight()/2);
                            //up_i.setY((((max_pitch-p)*bigcircle2.getHeight())/(max_pitch-min_pitch))+bigcircle2.getY()-up_i.getHeight()/2);
                            // a="P:"+Integer.toString(p);
                            //pitch.setText(a);

                        }



                        if ((Math.abs(linear_acceleration[1] - plinear_acceleration[1])) > 5) {
                            if (linear_acceleration[1] > (max_roll - mid_roll)) {
                                linear_acceleration[1] = (max_roll - mid_roll);
                            } else if (linear_acceleration[1] < (min_roll - mid_roll)) {
                                linear_acceleration[1] = min_roll - mid_roll;
                            }
                            plinear_acceleration[1] = linear_acceleration[1];


                            ro = mid_roll + linear_acceleration[1];
                            // b="R:"+Integer.toString(r);
                            //smallcircle2.setX((((r-min_roll)*bigcircle2.getWidth())/(max_roll-min_roll))+bigcircle2.getX()-smallcircle2.getWidth()/2);
                            //left_i.setX((((r-min_roll)*smallcircle2.getWidth())/(max_roll-min_roll))+smallcircle2.getX()-left_i.getWidth()/2);
                            //roll.setText(b);
                        }
                        */

                    /*
                    if(pi>(mid_pitch+200))
                    {
                        pi=mid_pitch+200;
                    }
                    */
                            ((MainActivity) getActivity()).p = pi;
                            ((MainActivity) getActivity()).r = ro;

                            ((MainActivity) getActivity()).sendoutput();
                        }
                    }
                }

            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int i) {

            }
        };
        sensorManager.registerListener(eventlisten, mSensor, SensorManager.SENSOR_DELAY_FASTEST);


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

}
