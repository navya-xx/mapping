package com.example.ashutosh.mapping;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.LocationSource;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;
import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, View.OnClickListener, LocationSource.OnLocationChangedListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private GoogleMap mMap;
    private int numLoc = 0;
    private GoogleApiClient mGoogleApiClient;
    public static List<LatLng> gpsLoc = new ArrayList<>();
    public static LatLng start_loc;
    private LocationRequest mLocationRequest;
    TextView posX,posY;
    private Location mCurrentLocation;
   /* private final SensorManager mSensorManager;
    private final Sensor mAccelerometer;

    public MapsActivity() {
        mSensorManager = (SensorManager)getSystemService(SENSOR_SERVICE);
        mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
    }*/

    @Override
    public void onLocationChanged(Location location) {

        mCurrentLocation = location;
        start_loc = new LatLng(mCurrentLocation.getLatitude(),mCurrentLocation.getLongitude());
        posX.setText(String.valueOf(start_loc.latitude));
        posY.setText(String.valueOf(start_loc.longitude));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(start_loc, 16.0f));
        //mMap.addMarker(mp);
        //mMap.animateCamera(CameraUpdateFactory.newLatLngZoom( new LatLng(location.getLatitude(), location.getLongitude()), 16));

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        final Button button = (Button) findViewById(R.id.start1);
        button.setOnClickListener(this);
        final Button button1 = (Button) findViewById(R.id.start2);
        button1.setOnClickListener(this);
        final Button button2 = (Button) findViewById(R.id.start3);
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MapsActivity.this, com.example.ashutosh.mapping.MapsActivity2.class));
            }
        });
        posX= (TextView) findViewById(R.id.accX);
        posY= (TextView) findViewById(R.id.accY);

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

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
        LatLng defaultLoc = new LatLng(59.3293230,18.0685810);
        //mMap.addMarker(new MarkerOptions().position(defaultLoc).title("Stockholm"));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(defaultLoc, 12.0f));
        //mMap.moveCamera(CameraUpdateFactory.newLatLng(defaultLoc));
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
                numLoc = 4;
                gpsLoc.add(0, new LatLng(59.370376, 18.064167));
                gpsLoc.add(1, new LatLng(59.370267, 18.064895));
                gpsLoc.add(2, new LatLng(59.370663, 18.065433));
                gpsLoc.add(3, new LatLng(59.370626, 18.066620));
                gpsLoc.add(3, new LatLng(59.370453, 18.067459));
                gpsLoc.add(3, new LatLng(59.370421, 18.067847));
                gpsLoc.add(3, new LatLng(59.370199, 18.067784));
                break;
            }
            case R.id.start2:
            {
                PolylineOptions options = new PolylineOptions().width(5).color(Color.BLUE).geodesic(true);
                //LatLngBounds track = new LatLngBounds(gpsLoc.get(0), gpsLoc.get(numLoc - 1));
                //mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(track,0));
                mMap.animateCamera( CameraUpdateFactory.newLatLngZoom( gpsLoc.get(0),16.0f ) );

                for (int i = 0; i < numLoc; i++) {
                    LatLng point = gpsLoc.get(i);
                    options.add(point);
                }
                //map.addMarker(); //add Marker in current position
                mMap.addPolyline(options); //add Polyline*/
            }
        }
    }

    @Override
    public void onConnected(Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

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
        accX.setText(strX);
        TextView accY = (TextView)findViewById(R.id.accY);
        accY.setText(strY);
        TextView accZ = (TextView)findViewById(R.id.accZ);
        accZ.setText(strZ);
    }*/
}

