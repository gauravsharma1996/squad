package com.example.squad_test;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link maps.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link maps#newInstance} factory method to
 * create an instance of this fragment.
 */
public class maps extends Fragment  {

    MapView mmapview;
    private GoogleMap mgooglemap;
    BufferedReader br=null;
    String[] str;
    double ideg=0,isec=0,imin=0;
    double alat=0,alongi=0;
    double lat=0,longi=0;
    double plat=0,plongi=0;
    int date=0,time=0;
    boolean first=false,draw1=false,draw2=false,fileflag=true,check,onepoint=false;
    gpsstruct latlong= new gpsstruct(lat,longi,time,date,check);
    int count=3;
    int i=0,n=0,j=0;
    List<Double> latli,longili;
    List<LatLng> both=new ArrayList<>();




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
     * @return A new instance of fragment maps.
     */
    // TODO: Rename and change types and number of parameters
    public static maps newInstance(String param1, String param2) {
        maps fragment = new maps();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public maps() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v=inflater.inflate(R.layout.fragment_maps, container, false);
        mmapview=(MapView)v.findViewById(R.id.mapView);
        mmapview.onCreate(savedInstanceState);
        mmapview.onResume();
        try{
            MapsInitializer.initialize(getActivity().getApplicationContext());
        }catch (Exception e){
            e.printStackTrace();
        }

        mgooglemap=mmapview.getMap();
        mgooglemap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
        File sdCard = Environment.getExternalStorageDirectory();
        File dir = new File (sdCard.getAbsolutePath() + "/squad/log");
        if(!dir.exists()) {
            dir.mkdirs();
        }
        File file = new File(dir, "log.dat");
        if(!file.exists())
        {
            Toast.makeText(getActivity(),"Log file not found",Toast.LENGTH_LONG).show();
            fileflag=false;
        }
        else
        {
            fileflag=true;
        }
        if(fileflag) {
            try {
                String scurrentline;
                br = new BufferedReader(new FileReader(file));
                Toast.makeText(getActivity(),"file found",Toast.LENGTH_LONG).show();
                while ((scurrentline = br.readLine()) != null) {
                   new gpslog(scurrentline,latlong);
                    check=latlong.getstatus();
                    if(check) {

                        lat = latlong.getlatitude();
                        longi = latlong.getlongitude();
                        if (lat != 2000 && longi != 2000) {
                            if (first) {
                                onepoint=true;
                                mgooglemap.addPolyline(new PolylineOptions().add(both.get(0), both.get(1))
                                        .add(both.get(0), both.get(2)).add(both.get(1), both.get(2))
                                        .width(15).color(Color.rgb(255, 128, 128)));
                                first = true;
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
                                    mgooglemap.addMarker(new MarkerOptions()
                                            .position(new LatLng(lat, longi))
                                            .title("Starting Point")
                                            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));
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
                if(onepoint) {
                    mgooglemap.addMarker(new MarkerOptions()
                            .position(new LatLng(lat, longi))
                            .title("End Point")
                            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));
                }
                CameraUpdate center = CameraUpdateFactory.newLatLng(new LatLng(lat, longi));
                CameraUpdate zoom = CameraUpdateFactory.zoomTo(18);
                //Toast.makeText(getActivity(),"count:"+Integer.toString(n),Toast.LENGTH_LONG).show();
                mgooglemap.moveCamera(center);
                mgooglemap.animateCamera(zoom);
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
        }
        return v;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mListener = (OnFragmentInteractionListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onResume() {
        super.onResume();
        mmapview.onResume();
    }
    @Override
    public void onPause() {
        super.onPause();
        mmapview.onPause();
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        mmapview.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mmapview.onLowMemory();
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
