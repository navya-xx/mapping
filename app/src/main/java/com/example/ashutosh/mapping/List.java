package com.example.ashutosh.mapping;

import android.app.Activity;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

public class List extends Activity {
    static public int trackId=0;
    public static java.util.List<TrackList> tracks = new ArrayList<>();
    public static int numTracks=5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        loadAllTracks();

        String[] runTracks = {"Nearest Track", "Track1", "Track2", "Track3", "Track4", "Track5"};
        ListAdapter list_Adapter = new CustomAdapter(this, runTracks);
        ListView run_ListView = (ListView) findViewById(R.id.ListView);
        run_ListView.setAdapter(list_Adapter);

        run_ListView.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        String pos = String.valueOf(parent.getItemAtPosition(position));
                        Toast.makeText(List.this, pos, Toast.LENGTH_LONG).show();
                        switch (position) {
                            case 0:
                                trackId = 0;
                                break;
                            case 1:
                                trackId = 1;
                                break;
                            case 2:
                                trackId=2;
                                break;
                            case 3:
                                trackId=3;
                                break;
                            case 4:
                                trackId=4;
                                break;
                            case 5:
                                trackId=5;
                                break;
                            default:
                                trackId=0;
                        }
                        startActivity(new Intent(List.this, MapsActivity.class));
                    }
                }
        );
    }

    public void loadAllTracks(){
        load1();
        load2();
        load3();
        load4();
        load5();
    }

    public float distanceGps(LatLng a, LatLng b){
        Location loc1 = new Location("");
        loc1.setLatitude(a.latitude);
        loc1.setLongitude(a.longitude);

        Location loc2 = new Location("");
        loc2.setLatitude(b.latitude);
        loc2.setLongitude(b.longitude);

        return loc1.distanceTo(loc2);
    }

    public void load1(){
        TrackList tTrack = new TrackList();
        int tTrackLen, tId;
        LatLng tStart;
        java.util.List<LatLng> tGpsLoc = new ArrayList<>();

        //Track1
        tId=1;
        tTrackLen= 6;
        tStart=new LatLng(0,0);
        tGpsLoc.add(0, new LatLng(59.370376, 18.064167));
        tGpsLoc.add(1, new LatLng(59.370267, 18.064895));
        tGpsLoc.add(2, new LatLng(59.370663, 18.065433));
        tGpsLoc.add(3, new LatLng(59.370626, 18.066620));
        tGpsLoc.add(4, new LatLng(59.370453, 18.067459));
        tGpsLoc.add(5, new LatLng(59.370421, 18.067847));
        tTrack.setPara(tId, tTrackLen, tStart, tGpsLoc);
        tracks.add(0,tTrack);
    }

    public void load2(){
        TrackList tTrack = new TrackList();
        int tTrackLen, tId;
        LatLng tStart;
        java.util.List<LatLng> tGpsLoc = new ArrayList<>();

        //Track2
        tId=2;
        tTrackLen= 5;
        tStart=new LatLng(0,0);
        tGpsLoc.add(0, new LatLng(59.370376, 18.064167));
        tGpsLoc.add(1, new LatLng(59.370267, 18.064895));
        tGpsLoc.add(2, new LatLng(59.370663, 18.065433));
        tGpsLoc.add(3, new LatLng(59.370626, 18.066620));
        tGpsLoc.add(4, new LatLng(59.370453, 18.067459));
        tTrack.setPara(tId, tTrackLen, tStart, tGpsLoc);
        tracks.add(tTrack);
    }
    public void load3(){
        TrackList tTrack = new TrackList();
        int tTrackLen, tId;
        LatLng tStart;
        java.util.List<LatLng> tGpsLoc = new ArrayList<>();

        //Track3
        tId=3;
        tTrackLen= 4;
        tStart=new LatLng(59.370376, 18.064167);
        tGpsLoc.add(0, new LatLng(59.370376, 18.064167));
        tGpsLoc.add(1, new LatLng(59.370267, 18.064895));
        tGpsLoc.add(2, new LatLng(59.370663, 18.065433));
        tGpsLoc.add(3, new LatLng(59.370626, 18.066620));
        tTrack.setPara(tId, tTrackLen, tStart, tGpsLoc);
        tracks.add(tTrack);
    }
    public void load4(){
        TrackList tTrack = new TrackList();
        int tTrackLen, tId;
        LatLng tStart;
        java.util.List<LatLng> tGpsLoc = new ArrayList<>();

        //Track4
        tId=4;
        tTrackLen= 4;
        tStart=new LatLng(0, 0);
        tGpsLoc.add(0, new LatLng(59.370376, 18.064167));
        tGpsLoc.add(1, new LatLng(59.370267, 18.064895));
        tGpsLoc.add(2, new LatLng(59.370663, 18.065433));
        tGpsLoc.add(3, new LatLng(59.370626, 18.066620));
        tTrack.setPara(tId, tTrackLen, tStart, tGpsLoc);
        tracks.add(tTrack);

    }
    public void load5(){
        TrackList tTrack = new TrackList();
        int tTrackLen, tId;
        LatLng tStart;
        java.util.List<LatLng> tGpsLoc = new ArrayList<>();

        //Track5
        tId=5;
        tTrackLen= 7;
        tStart=new LatLng(0, 0);
        tGpsLoc.add(0, new LatLng(59.370376, 18.064167));
        tGpsLoc.add(1, new LatLng(59.370267, 18.064895));
        tGpsLoc.add(2, new LatLng(59.370663, 18.065433));
        tGpsLoc.add(3, new LatLng(59.370626, 18.066620));
        tGpsLoc.add(4, new LatLng(59.370453, 18.067459));
        tGpsLoc.add(5, new LatLng(59.370421, 18.067847));
        tGpsLoc.add(6, new LatLng(59.370199, 18.067784));
        tTrack.setPara(tId, tTrackLen, tStart, tGpsLoc);
        tracks.add(tTrack);
    }



}