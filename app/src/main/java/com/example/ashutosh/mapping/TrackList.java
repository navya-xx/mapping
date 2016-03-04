package com.example.ashutosh.mapping;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by Ashutosh on 04-03-2016.
 */
public class TrackList {
    private int id;
    public int listLen;
    public LatLng startPoint;
    public java.util.List<LatLng> gpsList;

    public void setPara(int tId, int l, LatLng s, java.util.List<LatLng> g){
        id = tId;
        listLen=l;
        startPoint=s;
        gpsList=g;
    }
}
