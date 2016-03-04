package com.example.ashutosh.mapping;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;
import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, View.OnClickListener,
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private static final String TAG = "Mapwa";
    private GoogleMap mMap;
    public static int numLoc = 0;
    private GoogleApiClient mGoogleApiClient;
    public static List<LatLng> gpsLoc = new ArrayList<>();

    public static LatLng start_loc;
    private LocationRequest mLocationRequest;
    //TextView posX, posY;
    private Location mCurrentLocation;
   /* private final SensorManager mSensorManager;
    private final Sensor mAccelerometer;

    public MapsActivity() {
        mSensorManager = (SensorManager)getSystemService(SENSOR_SERVICE);
        mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
    }*/


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map1);
        mapFragment.getMapAsync(this);

        final Button button = (Button) findViewById(R.id.start1);
        button.setOnClickListener(this);
        final Button button1 = (Button) findViewById(R.id.start2);
        button1.setOnClickListener(this);
        final Button button2 = (Button) findViewById(R.id.start3);
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MapsActivity.this, MapsActivity2.class));
            }
        });
        //posX = (TextView) findViewById(R.id.positionX);
        //posY = (TextView) findViewById(R.id.positionY);

        // ATTENTION: This "addApi(AppIndex.API)"was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(AppIndex.API).build();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
        createLocationRequest();
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mMap.setMyLocationEnabled(true);
        LatLng defaultLoc = new LatLng(59.3293230, 18.0685810);
        //mMap.addMarker(new MarkerOptions().position(defaultLoc).title("Stockholm"));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(defaultLoc, 4.0f));
        //mMap.moveCamera(CameraUpdateFactory.newLatLng(defaultLoc));


        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        // Create a criteria object to retrieve provider
        Criteria criteria = new Criteria();

        // Get the name of the best provider
        String provider = locationManager.getBestProvider(criteria, true);

        // Get Current Location
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        Location myLocation = locationManager.getLastKnownLocation(provider);

        // set map type
        //mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);

        // Get latitude of the current location
        double latitude = myLocation.getLatitude();

        // Get longitude of the current location
        double longitude = myLocation.getLongitude();

        // Create a LatLng object for the current location
        LatLng latLng = new LatLng(latitude, longitude);
        start_loc = latLng;

        // Show the current location in Google Map
        //mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));

        // Zoom in the Google Map
        //mMap.animateCamera(CameraUpdateFactory.zoomTo(16));
        mMap.addMarker(new MarkerOptions().position(new LatLng(latitude, longitude)).title("You are here!").snippet("Consider yourself located"));
    }


    protected void createLocationRequest() {
        final long INTERV = 1000; //1 sec
        final long FASTEST_INTERV = 1000; // 1sec
        final float SMALLEST_DISPLACE = 0.001F; //unit is meter
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(INTERV);
        mLocationRequest.setFastestInterval(FASTEST_INTERV);
        mLocationRequest.setSmallestDisplacement(SMALLEST_DISPLACE);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

    }

    public void onClick(View v){

        switch(v.getId())
        {
            case R.id.start1:
            {
                // Perform action on click
                            /*
                            try {
                        // Create a URL for the desired page
                        URL url = new URL(inputURL.getText().toString());

                        // Read all the text returned by the server
                        BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
                        String str;
                        int count=0;
                        while ((str = in.readLine()) != null) {
                            if(count==0) {
                                numLoc = Integer.parseInt(str);
                            }
                            else {
                                if(count>numLoc){
                                    break;
                                }
                                String delim = "[,]";
                                String[] tokens = str.split(delim);
                                gpsLoc.add(count-1, new LatLng(Double.parseDouble(tokens[0]),Double.parseDouble(tokens[1])));
                                }
                            count++;
                            }
                        in.close();
                    } catch (MalformedURLException e) {
                    } catch (IOException e) {
                    }*/

                Log.d(TAG, "Loading Track");

                if(com.example.ashutosh.mapping.List.trackId == 0){
                    nearest_track(start_loc);
                    Log.d(TAG, "Loading Track nearest");
                }
                else {
                    TrackList tTrack;
                    tTrack=com.example.ashutosh.mapping.List.tracks.get(com.example.ashutosh.mapping.List.trackId -1);
                    numLoc=tTrack.listLen;
                    gpsLoc=tTrack.gpsList;
                    Log.d(TAG, "Loading Track num");
                }

                /*switch (com.example.ashutosh.mapping.List.trackId) {
                    case 0:
                        nearest_track(start_loc);
                        break;
                    case 1:
                        numLoc = 6;
                        gpsLoc.add(0, new LatLng(59.370376, 18.064167));
                        gpsLoc.add(1, new LatLng(59.370267, 18.064895));
                        gpsLoc.add(2, new LatLng(59.370663, 18.065433));
                        gpsLoc.add(3, new LatLng(59.370626, 18.066620));
                        gpsLoc.add(4, new LatLng(59.370453, 18.067459));
                        gpsLoc.add(5, new LatLng(59.370421, 18.067847));
                        break;
                    case 2:
                        numLoc = 5;
                        gpsLoc.add(0, new LatLng(59.370376, 18.064167));
                        gpsLoc.add(1, new LatLng(59.370267, 18.064895));
                        gpsLoc.add(2, new LatLng(59.370663, 18.065433));
                        gpsLoc.add(3, new LatLng(59.370626, 18.066620));
                        gpsLoc.add(4, new LatLng(59.370453, 18.067459));
                        break;
                    case 3:
                        numLoc = 4;
                        gpsLoc.add(0, new LatLng(59.370376, 18.064167));
                        gpsLoc.add(1, new LatLng(59.370267, 18.064895));
                        gpsLoc.add(2, new LatLng(59.370663, 18.065433));
                        gpsLoc.add(3, new LatLng(59.370626, 18.066620));
                        break;
                    case 4:
                        numLoc = 4;
                        gpsLoc.add(0, new LatLng(59.370376, 18.064167));
                        gpsLoc.add(1, new LatLng(59.370267, 18.064895));
                        gpsLoc.add(2, new LatLng(59.370663, 18.065433));
                        gpsLoc.add(3, new LatLng(59.370626, 18.066620));
                        break;
                    case 5:
                        numLoc = 7;
                        gpsLoc.add(0, new LatLng(59.370376, 18.064167));
                        gpsLoc.add(1, new LatLng(59.370267, 18.064895));
                        gpsLoc.add(2, new LatLng(59.370663, 18.065433));
                        gpsLoc.add(3, new LatLng(59.370626, 18.066620));
                        gpsLoc.add(4, new LatLng(59.370453, 18.067459));
                        gpsLoc.add(5, new LatLng(59.370421, 18.067847));
                        gpsLoc.add(6, new LatLng(59.370199, 18.067784));
                        break;
                    default:
                        numLoc = 4;
                        gpsLoc.add(0, new LatLng(59.370376, 18.064167));
                        gpsLoc.add(1, new LatLng(59.370267, 18.064895));
                        gpsLoc.add(2, new LatLng(59.370663, 18.065433));
                        gpsLoc.add(3, new LatLng(59.370626, 18.066620));
                        gpsLoc.add(3, new LatLng(59.370453, 18.067459));
                        gpsLoc.add(3, new LatLng(59.370421, 18.067847));
                }*/
                break;
            }
            case R.id.start2:
            {
                PolylineOptions options = new PolylineOptions().width(5).color(Color.BLUE).geodesic(true);
                //LatLngBounds track = new LatLngBounds(gpsLoc.get(0), gpsLoc.get(numLoc - 1));
                //mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(track,0));
                Log.d(TAG, "Poly op");
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(gpsLoc.get(0), 16.0f));
                Log.d(TAG, "Cam anim");
                for (int i = 0; i < numLoc; i++) {
                    LatLng point = gpsLoc.get(i);
                    options.add(point);
                }
                Log.d(TAG, "track drawn");
                //map.addMarker(); //add Marker in current position
                mMap.addPolyline(options); //add Polyline*/



            }
        }
    }

    @Override
    public void onConnected(Bundle bundle) {

    }

    public void nearest_track(LatLng start_loc){
        float distMin=10000000;
        int idTrack=0;
        TrackList tempTrack;
        for(int i=0; i< com.example.ashutosh.mapping.List.numTracks; i++){
            tempTrack=com.example.ashutosh.mapping.List.tracks.get(i);
            if(i==0){
                distMin=distanceGps(start_loc,tempTrack.startPoint);
                idTrack=i+1;
            }
            else{
                float t=distanceGps(start_loc,tempTrack.startPoint);
                if(t<distMin){
                    distMin=t;
                    idTrack=i+1;
                }
            }
        }
        tempTrack=com.example.ashutosh.mapping.List.tracks.get(idTrack-1);
        numLoc = tempTrack.listLen;
        gpsLoc = tempTrack.gpsList;
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        mGoogleApiClient.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Maps Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://com.example.ashutosh.mapping/http/host/path")
        );
        AppIndex.AppIndexApi.start(mGoogleApiClient, viewAction);
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Maps Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://com.example.ashutosh.mapping/http/host/path")
        );
        AppIndex.AppIndexApi.end(mGoogleApiClient, viewAction);
        mGoogleApiClient.disconnect();
    }



/*
    protected void onResume() {
        super.onResume();
        mSensorManager.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);
    }

    protected void onPause() {
        super.onPause();
        mSensorManager.unregisterListener(this);
    }

    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }

    public void onSensorChanged(SensorEvent event) {
        //Right in here is where you put code to read the current sensor values and
        //update any views you might have that are displaying the sensor information
        //You'd get accelerometer values like this:
        if (event.sensor.getType() != Sensor.TYPE_ACCELEROMETER)
            return;
        String strX=new String().valueOf(event.values[0]);
        String strY=new String().valueOf(event.values[1]);
        String strZ=new String().valueOf(event.values[2]);
        TextView accX = (TextView)findViewById(R.id.accX);
        accX.stxt(strX);
        TextView accY = (TextView)findViewById(R.id.accY);
        accY.setText(strY);
        TextView accZ = (TextView)findViewById(R.id.accZ);
        accZ.setText(strZ);
    }*/

    public float distanceGps(LatLng a, LatLng b){
        Location loc1 = new Location("");
        loc1.setLatitude(a.latitude);
        loc1.setLongitude(a.longitude);

        Location loc2 = new Location("");
        loc2.setLatitude(b.latitude);
        loc2.setLongitude(b.longitude);

        return loc1.distanceTo(loc2);
    }
}

