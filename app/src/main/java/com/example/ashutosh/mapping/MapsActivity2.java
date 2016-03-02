package com.example.ashutosh.mapping;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Location;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.util.Base64InputStream;
import android.util.Base64OutputStream;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Serializable;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

//import com.google.maps.android.ui.IconGenerator;


public class MapsActivity2 extends Activity implements
        LocationListener,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, OnMapReadyCallback {


    public MapsActivity2() {

    }


    protected void createLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(INTERVAL);
        mLocationRequest.setFastestInterval(FASTEST_INTERVAL);
        mLocationRequest.setSmallestDisplacement(SMALLEST_DISPLACEMENT);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    private static final String TAG = "MainActivity";
    private static final long INTERVAL = 500; //1 sec
    private final long FASTEST_INTERVAL = 500; // 1sec
    private static final float SMALLEST_DISPLACEMENT = 0.01F; //unit is meter
    private LocationRequest mLocationRequest;
    private GoogleApiClient mGoogleApiClient;
    private Location mCurrentLocation;
    private String mLastUpdateTime;
    private String city = "";
    private String country = "";
    private String area = "";
    private String title;
    private String requiredArea = "";
    private GoogleMap googleMap;
    private List<Address> addresses;
    private ArrayList<Long> time;
    Polyline line; //added
    File file;
    OutputStream fos;
    private TextView acc_view, dist, sz;
    double distance=0;
    protected ArrayList<LatLng> points= new ArrayList<LatLng>(){{
        if(MapsActivity.start_loc != null)
            add(0,MapsActivity.start_loc);
        else
            add(0,new LatLng(0,0));
    }}; //added



    protected void dist_calc(double l1, double l2, double lo1, double lo2){
        double lat1=Math.toRadians(l1);
        double lat2=Math.toRadians(l2);
        double lon1=Math.toRadians(lo1);
        double lon2=Math.toRadians(lo2);
        double dlat=Math.toRadians(lat2 - lat1);
        double dlon=Math.toRadians(lon2-lon1);
        double a = Math.sin(dlat/2) * Math.sin(dlat/2) + Math.cos(lat1) * Math.cos(lat2) * Math.sin(dlon/2) * Math.sin(dlon/2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
        double raz=6371000;
        distance += raz * c;
    }




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //points.add(0, new LatLng(0,0));
        Log.d(TAG, "onCreate ...............................");
        //show error dialog if GoolglePlayServices not available
        /*if (!isGooglePlayServicesAvailable()) {

            Toast.makeText(this, "Google Play Services is not available", Toast.LENGTH_LONG).show();

            finish();
        }*/
        //FileOutputStream outputStream;

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
        setContentView(R.layout.activity_maps2);
        Log.d(TAG, "onCreate2 ...............................");

        MapFragment mapFragment =
                (MapFragment) getFragmentManager().findFragmentById(R.id.map2);

        Log.d(TAG, "onCreate3 ...............................");
        mapFragment.getMapAsync(this);

        Log.d(TAG, "onCreate4 ...............................");

        acc_view = (TextView) findViewById(R.id.accu);
        sz = (TextView) findViewById(R.id.sz);
        dist = (TextView)findViewById(R.id.dist);


        final Button button_save = (Button) findViewById(R.id.save);
        button_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                write_points();
            }
        });

    }

    public void write_points(){
        try {
            File newFolder = new File(Environment.getExternalStorageDirectory(), "TestFolder");
            if (!newFolder.exists()) {
                newFolder.mkdir();
            }
            try {
                file = new File(newFolder, "MyTest" + ".txt");
                file.createNewFile();
            } catch (Exception ex) {
                System.out.println("ex: " + ex);
            }
        } catch (Exception e) {
            System.out.println("e: " + e);
        }

        try {
            fos = new FileOutputStream(file);
            DataOutputStream myOutWriter =new DataOutputStream(fos);
            myOutWriter.writeChars("Database of the run: \n");
            for(int i=0; i<points.size();i++){
                try {
                    myOutWriter.writeChars(String.valueOf(i+1));
                    myOutWriter.writeChars(") Latitude: ");
                    myOutWriter.writeChars(String.valueOf(points.get(i).latitude));
                    myOutWriter.writeChars(", Longitude: ");
                    myOutWriter.writeChars(String.valueOf(points.get(i).longitude));
                    myOutWriter.writeChars(System.getProperty("line.separator"));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            sz.setText(String.valueOf(points.size()));
            myOutWriter.flush();
            myOutWriter.close();
            fos.flush();
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static class SerializeObject {
        private final static String TAG = "SerializeObject";

        /**
         * Create a String from the Object using Base64 encoding
         * @param object - any Object that is Serializable
         * @return - Base64 encoded string.
         */
        public static String objectToString(Serializable object) {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            try {
                new ObjectOutputStream(out).writeObject(object);
                byte[] data = out.toByteArray();
                out.close();

                out = new ByteArrayOutputStream();
                Base64OutputStream b64 = new Base64OutputStream(out,0);
                b64.write(data);
                b64.close();
                out.close();

                return new String(out.toByteArray());
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        /**
         * Creates a generic object that needs to be cast to its proper object
         * from a Base64 ecoded string.
         *
         * @param encodedObject
         * @return
         */
        public static Object stringToObject(String encodedObject) {
            try {
                return new ObjectInputStream(new Base64InputStream(
                        new ByteArrayInputStream(encodedObject.getBytes()), 0)).readObject();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        /**
         * Save serialized settings to a file
         * @param context
         * @param data
         */
        public static void WriteSettings(Context context, String data, String filename){
            FileOutputStream fOut = null;
            OutputStreamWriter osw = null;

            try{
                fOut = context.openFileOutput(filename, Context.MODE_PRIVATE);
                osw = new OutputStreamWriter(fOut);
                osw.write(data);
                osw.flush();
                //Toast.makeText(context, "Settings saved",Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                e.printStackTrace();
                // Toast.makeText(context, "Settings not saved",Toast.LENGTH_SHORT).show();
            }
            finally {
                try {
                    if(osw!=null)
                        osw.close();
                    if (fOut != null)
                        fOut.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        /**
         * Read data from file and put it into a string
         * @param context
         * @param filename - fully qualified string name
         * @return
         */
        public static String ReadSettings(Context context, String filename){
            StringBuffer dataBuffer = new StringBuffer();
            try{
                // open the file for reading
                InputStream instream = context.openFileInput(filename);
                // if file the available for reading
                if (instream != null) {
                    // prepare the file for reading
                    InputStreamReader inputreader = new InputStreamReader(instream);
                    BufferedReader buffreader = new BufferedReader(inputreader);

                    String newLine;
                    // read every line of the file into the line-variable, on line at the time
                    while (( newLine = buffreader.readLine()) != null) {
                        // do something with the settings from the file
                        dataBuffer.append(newLine);
                    }
                    // close the file again
                    instream.close();
                }

            } catch (java.io.FileNotFoundException f) {
                // do something if the myfilename.txt does not exits
                Log.e(TAG, "FileNot Found in ReadSettings filename = " + filename);
                try {
                    context.openFileOutput(filename, Context.MODE_PRIVATE);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            } catch (IOException e) {
                Log.e(TAG, "IO Error in ReadSettings filename = " + filename);
            }

            return dataBuffer.toString();
        }

    }

    //
    @Override
    public void onStart() {
        super.onStart();
        Log.d(TAG, "onStart fired ..............");
        mGoogleApiClient.connect();
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d(TAG, "onStop fired ..............");
        mGoogleApiClient.disconnect();
        Log.d(TAG, "isConnected ...............: " + mGoogleApiClient.isConnected());
    }

    private boolean isGooglePlayServicesAvailable() {
        GoogleApiAvailability googleAPI = GoogleApiAvailability.getInstance();
        int status = googleAPI.isGooglePlayServicesAvailable(this);
        if (ConnectionResult.SUCCESS == status) {
            return true;
        } else {
            googleAPI.getErrorDialog(this, status, 0).show();
            Toast.makeText(getApplicationContext(), "Google Play Services is not Available", Toast.LENGTH_LONG).show();
            return false;
        }
    }

    @Override
    public void onConnected(Bundle bundle) {
        Log.d(TAG, "onConnected - isConnected ...............: " + mGoogleApiClient.isConnected());
        startLocationUpdates();
    }

    protected void startLocationUpdates() {
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
        PendingResult<Status> pendingResult = LocationServices.FusedLocationApi.requestLocationUpdates(
                mGoogleApiClient, mLocationRequest, this);
        Log.d(TAG, "Location update started ..............: ");
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.d(TAG, "Connection failed: " + connectionResult.toString());
    }

    @Override
    public void onLocationChanged(Location location) {
        Log.d(TAG, "Firing onLocationChanged..............................................");
        mCurrentLocation = location;
        mLastUpdateTime = DateFormat.getTimeInstance().format(new Date());
        //addMarker();
        float accuracy = location.getAccuracy();

        Log.d("iFocus", "The amount of accuracy is " + accuracy);
        double latitude = location.getLatitude();
        double longitude = location.getLongitude();
        Bundle extras = location.getExtras();
        Boolean has = location.hasAccuracy();
        String provider = location.getProvider();
        //Long time = location.getTime();

//        Location locationB = new Location("Begur");
//        double lati = 12.8723;
//        double longi =  77.6329;
//        locationB.setLatitude(lati);
//        locationB.setLongitude(longi);
//        Float distance = location.distanceTo(locationB);


        Log.d(TAG, "before points");
        LatLng latLng = new LatLng(latitude, longitude);
        Long time_temp= location.getTime();
        points.add(latLng); //added
        //googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 16));

        TextView lat = (TextView) findViewById(R.id.lat);
        lat.setText(String.valueOf(latitude));
        TextView longi = (TextView) findViewById(R.id.longi);
        longi.setText(String.valueOf(longitude));
        acc_view.setText(String.format("%.2f", accuracy));
        int sz = points.size();
        dist_calc(points.get(sz-1).latitude, points.get(sz - 2).latitude, points.get(sz-1).longitude, points.get(sz-2).longitude );
        dist.setText(String.format("%.2f", distance));
        Log.d(TAG, "after points");
        if(points.size()>2)
            redrawLine();
        //time.add(time_temp);
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(time_temp);
        Log.d(TAG, "after points2");
        //TextView time_view = (TextView) findViewById(R.id.time);
        //time_view.setText(String.valueOf(time_temp));
        TextView speed_view = (TextView) findViewById(R.id.speed);
        speed_view.setText(String.valueOf(location.getSpeed()));
        Log.d(TAG, "after points3");
        int mYear = calendar.get(Calendar.YEAR);
        int mMonth = calendar.get(Calendar.MONTH) + 1;
        int mDay = calendar.get(Calendar.DAY_OF_MONTH);

        String formattedTime = mDay + ":" + mMonth + ":" + mYear;
        Log.d("iFocus", "The name of provider is " + provider);
        Log.d("iFocus", "The value of has is " + has);
        Log.d("iFocus", "The value of extras is " + extras);
        Log.d("iFocus", "The value of Month is " + mMonth);
        Log.d("iFocus", "The value of Day is " + mDay);
        Log.d("iFocus", "The value of Year is " + mYear);
        Log.d("iFocus", "The value of Time is " + formattedTime);
        //Log.d("iFocus", "The value of distance is "+distance);
        /*Geocoder geocoder = new Geocoder(this, Locale.getDefault());

        try {
            addresses = geocoder.getFromLocation(latitude, longitude, 1);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        String cityName = addresses.get(0).getAddressLine(0);
        String stateName = addresses.get(0).getAddressLine(1);
        String countryName = addresses.get(0).getAddressLine(2);

        String[] splittedStateName = stateName.split(",");
        requiredArea = splittedStateName[2];
        Log.d("iFocus", "The value of required area is " + requiredArea);

        city = addresses.get(0).getLocality();
        area = addresses.get(0).getSubLocality();
        String adminArea = addresses.get(0).getAdminArea();
        String premises = addresses.get(0).getPremises();
        String subAdminArea = addresses.get(0).getSubAdminArea();
        String featureName = addresses.get(0).getFeatureName();
        String phone = addresses.get(0).getPhone();
        country = addresses.get(0).getCountryName();
        /*Log.d("iFocus", "The name of city is " + city);
        Log.d("iFocus", "The name of area is " + area);
        Log.d("iFocus", "The name of country is " + country);
        Log.d("iFocus", "The value of cityName is " + cityName);
        Log.d("iFocus", "The value of StateName is " + stateName);
        Log.d("iFocus", "The value of CountryName is " + countryName);*/

        /*Toast.makeText(this, cityName + " " + stateName + " " + countryName, Toast.LENGTH_LONG).show();

        SharedPreferences sharedPreferences = getSharedPreferences("MyValues", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("CITY", cityName);
        editor.putString("STATE", stateName);
        editor.putString("COUNTRY", countryName);
        editor.commit();

        TextView mapTitle = (TextView) findViewById(R.id.textViewTitle);

        if (requiredArea != "" && city != "" && country != "") {
            title = mLastUpdateTime.concat(", " + requiredArea).concat(", " + city).concat(", " + country);
        } else {
            title = mLastUpdateTime.concat(", " + area).concat(", " + city).concat(", " + country);
        }
        mapTitle.setText(title);
        //addMarker();// newly added
*/
        /*final String xmlFile = "userData.xml";

        try {
            // FileOutputStream fos = new  FileOutputStream("userData.xml");
            FileOutputStream fos = openFileOutput(xmlFile, Context.MODE_PRIVATE);
            XmlSerializer xmlSerializer = Xml.newSerializer();
            StringWriter writer = new StringWriter();
            xmlSerializer.setOutput(writer);
            xmlSerializer.startDocument("UTF-8", true);
            xmlSerializer.startTag(null, "userData");
            xmlSerializer.startTag(null, "Time");
            xmlSerializer.text(mLastUpdateTime);
            xmlSerializer.endTag(null, "Time");
            xmlSerializer.startTag(null, "Latitude");
            xmlSerializer.text(String.valueOf(latitude));
            xmlSerializer.endTag(null, "Latitude");
            xmlSerializer.startTag(null, "Longitude");
            xmlSerializer.text(String.valueOf(longitude));
            xmlSerializer.endTag(null, "Longitude");
            xmlSerializer.startTag(null, "Accu");
            xmlSerializer.text(String.valueOf(accuracy));
            xmlSerializer.endTag(null, "Accu");
            /*xmlSerializer.startTag(null, "Area");
            if (requiredArea != "") {
                xmlSerializer.text(requiredArea);
            } else {
                xmlSerializer.text(area);
            }
            xmlSerializer.endTag(null, "Area");
            xmlSerializer.startTag(null, "City");
            xmlSerializer.text(city);
            xmlSerializer.endTag(null, "City");
            xmlSerializer.endTag(null, "userData");
            xmlSerializer.endDocument();
            xmlSerializer.flush();
            String dataWrite = writer.toString();
            fos.write(dataWrite.getBytes());
            fos.close();
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IllegalStateException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        String dir = getFilesDir().getAbsolutePath();
        Log.d("Pana", "The value of Dir is " + dir);*/

    }

    private void redrawLine(){
        Log.d(TAG, "redraw0.1 .......................");

            //line.remove();  //clears all Markers and Polylines
            Log.d(TAG, "redraw0 .......................");

        /*PolylineOptions options = new PolylineOptions().width(5).color(Color.BLUE).geodesic(true);
        for (int i = 0; i < points.size(); i++) {
            LatLng point = points.get(i);
            options.add(point);
        }
        Log.d(TAG, "redraw1 .......................");

        //addMarker(); //add Marker in current position

        Log.d(TAG, "redraw2 .......................");
        line = googleMap.addPolyline(options); //add Polyline
        Log.d(TAG, "redraw3.......................");*/

        line.setPoints(points);
        //googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(points.get(points.size() - 1), 16));

    }

    private void addMarker() {
        MarkerOptions options = new MarkerOptions();

        Log.d(TAG, "addmarker1 .......................");

        // following four lines requires 'Google Maps Android API Utility Library'
        // https://developers.google.com/maps/documentation/android/utility/
        // I have used this to display the time as title for location markers
        // you can safely comment the following four lines but for this info
        /*IconGenerator iconFactory = new IconGenerator(this);
        iconFactory.setStyle(IconGenerator.STYLE_PURPLE);
        options.icon(BitmapDescriptorFactory.fromBitmap(iconFactory.makeIcon(mLastUpdateTime + requiredArea + city)));
        options.icon(BitmapDescriptorFactory.fromBitmap(iconFactory.makeIcon(requiredArea + ", " + city)));
        options.anchor(iconFactory.getAnchorU(), iconFactory.getAnchorV());*/
        Log.d(TAG, "addmarker2 .......................");

        LatLng currentLatLng = new LatLng(mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude());
        Log.d(TAG, "addmarker3.......................");

        options.position(currentLatLng);

        Log.d(TAG, "addmarker3.1.......................");

        //Marker mapMarker= googleMap.addMarker(new MarkerOptions().position(currentLatLng));
        //Marker mapMarker = googleMap.addMarker(options);

        //long atTime = mCurrentLocation.getTime();
        Log.d(TAG, "addmarker4.......................");

        //mLastUpdateTime = DateFormat.getTimeInstance().format(new Date(atTime));
        //Log.d(TAG, "addmarker4 .......................");

        String title = mLastUpdateTime.concat(", " + requiredArea).concat(", " + city).concat(", " + country);
        Log.d(TAG, "addmarker5 .......................");

        //mapMarker.setTitle(title);


        TextView mapTitle = (TextView) findViewById(R.id.textViewTitle);
        mapTitle.setText(title);

        Log.d(TAG, "Marker added.............................");
        //googleMap.moveCamera(CameraUpdateFactory.newLatLng(gpsLoc.get(0)));
        //googleMap.moveCamera(CameraUpdateFactory.newLatLng(currentLatLng));
        Log.d(TAG, "Zoom done.............................");
    }

    @Override
    protected void onPause() {
        super.onPause();
        stopLocationUpdates();
    }

    protected void stopLocationUpdates() {
        LocationServices.FusedLocationApi.removeLocationUpdates(
                mGoogleApiClient, this);
        Log.d(TAG, "Location update stopped .......................");
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mGoogleApiClient.isConnected()) {
            startLocationUpdates();
            Log.d(TAG, "Location update resumed .....................");
        }
    }

    @Override
    public void onMapReady(final GoogleMap googleMap) {
        Log.d(TAG, "onMapReady .......................");
        createLocationRequest();
        Log.d(TAG, "onMapReady2 .......................");

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
        Log.d(TAG, "onMapReady3 .......................");
        googleMap.setMyLocationEnabled(true);
        Log.d(TAG, "onMapReady4.......................");


        googleMap.setOnMyLocationButtonClickListener(new GoogleMap.OnMyLocationButtonClickListener() {
            @Override
            public boolean onMyLocationButtonClick() {

                Toast.makeText(getApplicationContext(), "Location button has been clicked", Toast.LENGTH_LONG).show();
                if (points.size() > 0)
                    googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(points.get(points.size() - 1), 12));
                return true;
            }
        });
        Log.d(TAG, "onMapReady5 .......................");


        googleMap.getUiSettings().setZoomControlsEnabled(true);
        googleMap.getUiSettings().setAllGesturesEnabled(true);
        PolylineOptions options = new PolylineOptions().width(5).color(Color.BLUE).geodesic(true);
        //LatLngBounds track = new LatLngBounds(gpsLoc.get(0), gpsLoc.get(numLoc - 1));
        //mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(track,0));

        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(MapsActivity.gpsLoc.get(0), 16.0f));
        for (int i = 0; i < MapsActivity.numLoc; i++) {
            LatLng point = MapsActivity.gpsLoc.get(i);
            options.add(point);
        }
        //map.addMarker(); //add Marker in current position
        googleMap.addPolyline(options); //add Polyline*/

        line = googleMap.addPolyline(new PolylineOptions().width(3).color(Color.RED));
        Log.d(TAG, "onMapReady6 .......................");


    }
}