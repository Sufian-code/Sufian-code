package com.example.fdas;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.Dash;
import com.google.android.gms.maps.model.Dot;
import com.google.android.gms.maps.model.Gap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PatternItem;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class surveydata1 extends AppCompatActivity implements OnMapReadyCallback,
        GoogleMap.OnPolylineClickListener,
        GoogleMap.OnPolygonClickListener {
    SupportMapFragment supportMapFragment;
    Spinner spinner;
    public Button btnsvd, button, locationBtn;
    EditText txtfrmarea, editText2, editText4, editText5, editText28, editText29;
    FusedLocationProviderClient client;
    ArrayList<String> arrayList;
    ArrayList<String> zoneIdArrayList;

    ProgressDialog progressDialog;

    String selectedZone = "";

    double longitude;
    double latitude;


    private static final String TAG = "IMAGESGPS";
    private LocationManager mLocationManager = null;
    private static final int LOCATION_INTERVAL = 10000;
    private static final float LOCATION_DISTANCE = 0f;
    private static final int COLOR_BLACK_ARGB = 0xff000000;
    private static final int POLYLINE_STROKE_WIDTH_PX = 12;
    private static final int PATTERN_GAP_LENGTH_PX = 20;
    private static final int COLOR_WHITE_ARGB = 0xffffffff;
    private static final int COLOR_GREEN_ARGB = 0xff388E3C;
    private static final int COLOR_PURPLE_ARGB = 0xff81C784;
    private static final int COLOR_ORANGE_ARGB = 0xffF57F17;
    private static final int COLOR_BLUE_ARGB = 0xffF9A825;
    private static final int POLYGON_STROKE_WIDTH_PX = 8;
    private static final int PATTERN_DASH_LENGTH_PX = 20;
    private static final PatternItem DASH = new Dash(PATTERN_DASH_LENGTH_PX);

    private static final PatternItem DOT = new Dot();
    private static final PatternItem GAP = new Gap(PATTERN_GAP_LENGTH_PX);
//    surveydata1.LocationListener[] mLocationListeners;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_surveydata);
        btnsvd = (Button) findViewById(R.id.btnsvd);
        button = (Button) findViewById(R.id.button);
        locationBtn = (Button) findViewById(R.id.loccation);
        spinner = (Spinner) findViewById(R.id.spinner);
        txtfrmarea = (EditText) findViewById(R.id.txtfrmarea);
        editText2 = (EditText) findViewById(R.id.txtpassword);
        editText4 = (EditText) findViewById(R.id.editText4);
        editText5 = (EditText) findViewById(R.id.editText5);
        editText28 =  findViewById(R.id.editText28);
        editText29 =  findViewById(R.id.editText29);
        supportMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.mapp_fragment);

        //  setting up progress dialog
        progressDialog = new ProgressDialog(surveydata1.this);
        progressDialog.setMessage("Loading....");
        progressDialog.setCancelable(false);


        surveyDataTask(getString(R.string.surveyDataURL));

        locationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestPermissionLocation();
            }
        });


        btnsvd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!selectedZone.equals("") && !selectedZone.equals("Select Zone") && !selectedZone.equals("0")) {

                    if (txtfrmarea.getText().toString().trim().length() > 0 && editText2.getText().toString().trim().length() > 0 &&
                            editText4.getText().toString().trim().length() > 0 && editText5.getText().toString().trim().length() > 0 &&
                            editText28.getText().toString().trim().length() > 0 && editText29.getText().toString().trim().length() > 0) {

                        insertingSurveyDataTask(txtfrmarea.getText().toString().trim(), editText2.getText().toString().trim(),
                                editText4.getText().toString().trim(), editText5.getText().toString().trim(),
                                editText28.getText().toString().trim(), editText29.getText().toString().trim(), getString(R.string.insertingSurveyDataURL));

                    } else {
                        Toast.makeText(surveydata1.this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                    }

                } else {
                    Toast.makeText(surveydata1.this, "Please select your zone first", Toast.LENGTH_SHORT).show();
                }

                /*Intent toy = new Intent(surveydata.this, home.class);
                startActivity(toy);*/

            }
        });

//        button.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//
//
//                final LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
//
//                if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
//
//                    GPSAlert();
//
//                } else
//
//                {
//                    mLocationListeners = new surveydata1.LocationListener[]{
//                            new surveydata1.LocationListener(LocationManager.GPS_PROVIDER),
//                            new surveydata1.LocationListener(LocationManager.NETWORK_PROVIDER)
//                    };
//
//
//                    Log.e(TAG, "onCreate");
//                    initializeLocationManager();
//                    try {
//                        mLocationManager.requestLocationUpdates(
//                                LocationManager.NETWORK_PROVIDER, LOCATION_INTERVAL, LOCATION_DISTANCE,
//                                mLocationListeners[1]);
//                    } catch (SecurityException ex) {
//                        Log.i(TAG, "fail to request location update, ignore", ex);
//                    } catch (IllegalArgumentException ex) {
//                        Log.d(TAG, "network provider does not exist, " + ex.getMessage());
//                    }
//                    try {
//                        mLocationManager.requestLocationUpdates(
//                                LocationManager.GPS_PROVIDER, LOCATION_INTERVAL, LOCATION_DISTANCE,
//                                mLocationListeners[0]);
//                    } catch (SecurityException ex) {
//                        Log.i(TAG, "fail to request location update, ignore", ex);
//                    } catch (IllegalArgumentException ex) {
//                        Log.d(TAG, "gps provider does not exist " + ex.getMessage());
//                    }
//
//                }
//
//
//
//
//            }
//        });

    }

    private void surveyDataTask(String url) {

        progressDialog.show();

        RequestQueue requestQueue = Volley.newRequestQueue(surveydata1.this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        progressDialog.dismiss();

                        if (response != null) {


                            try {

                                JSONObject jsonObject = new JSONObject(response);

                                JSONArray jsonArray = jsonObject.getJSONArray("server_response");

                                arrayList = new ArrayList<>();
                                zoneIdArrayList = new ArrayList<>();

                                for (int i = 0; i < jsonArray.length(); i++) {

                                    JSONObject object = jsonArray.getJSONObject(i);

                                    arrayList.add(object.getString("zone_name"));
                                    zoneIdArrayList.add(object.getString("zone_id"));

                                }

                                if (arrayList.size() > 0) {

                                    arrayList.add(0, "Select Zone");
                                    zoneIdArrayList.add(0, "0");

                                    ArrayAdapter adapter = new ArrayAdapter(getApplicationContext(), R.layout.custom_spinner_text, arrayList.toArray());
                                    spinner.setAdapter(adapter);

                                    spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                        @Override
                                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                                            selectedZone = zoneIdArrayList.get(position);

                                        }

                                        @Override
                                        public void onNothingSelected(AdapterView<?> parent) {

                                        }
                                    });

                                } else {
                                    Toast.makeText(surveydata1.this, "No data", Toast.LENGTH_SHORT).show();
                                }


                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }


                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

                progressDialog.dismiss();

                String message = null;
                if (volleyError instanceof NetworkError) {
                    message = "Cannot connect to Internet...Please check your connection!";
                } else if (volleyError instanceof ServerError) {
                    message = "The server could not be found. Please try again after some time!!";
                } else if (volleyError instanceof AuthFailureError) {
                    message = "Cannot connect to Internet...Please check your connection!";
                } else if (volleyError instanceof ParseError) {
                    message = "Parsing error! Please try again after some time!!";
                } else if (volleyError instanceof NoConnectionError) {
                    message = "Cannot connect to Internet...Please check your connection!";
                } else if (volleyError instanceof TimeoutError) {
                    message = "Connection TimeOut! Please check your internet connection.";
                } else {
                    message = "Something goes wrong";
                }

                Toast.makeText(surveydata1.this, message, Toast.LENGTH_SHORT).show();


            }
        });

        requestQueue.add(stringRequest);

    }


    private void insertingSurveyDataTask(final String frmarea, final String et2, final String et4, final String et5, final String et28, final String et29, String url) {

        progressDialog.show();

        RequestQueue requestQueue = Volley.newRequestQueue(surveydata1.this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        progressDialog.dismiss();

                        if (response != null) {
                            if (response.equals("true")) {

                                Toast.makeText(surveydata1.this, "Data inserted", Toast.LENGTH_SHORT).show();
                                finish();
                            } else {
                                Toast.makeText(surveydata1.this, "Something goes wrong", Toast.LENGTH_SHORT).show();
                            }
                        }


                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

                progressDialog.dismiss();

                String message = null;
                if (volleyError instanceof NetworkError) {
                    message = "Cannot connect to Internet...Please check your connection!";
                } else if (volleyError instanceof ServerError) {
                    message = "The server could not be found. Please try again after some time!!";
                } else if (volleyError instanceof AuthFailureError) {
                    message = "Cannot connect to Internet...Please check your connection!";
                } else if (volleyError instanceof ParseError) {
                    message = "Parsing error! Please try again after some time!!";
                } else if (volleyError instanceof NoConnectionError) {
                    message = "Cannot connect to Internet...Please check your connection!";
                } else if (volleyError instanceof TimeoutError) {
                    message = "Connection TimeOut! Please check your internet connection.";
                } else {
                    message = "Something goes wrong";
                }

                Toast.makeText(surveydata1.this, message, Toast.LENGTH_SHORT).show();


            }
        }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                HashMap<String, String> params = new HashMap<>();

                params.put("area", frmarea);
                params.put("owner", et2);
                params.put("contact", et4);
                params.put("marker_name", et5);
                params.put("lat", et28);
                params.put("long", et29);
                params.put("zone_name", selectedZone);


                return params;

            }
        };

        requestQueue.add(stringRequest);

    }


    private void GPSAlert() {

        AlertDialog.Builder builder = new AlertDialog.Builder(surveydata1.this);
        builder.setMessage("Your GPS seems to be disabled, do you want to enable it?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));

                        if (ActivityCompat.checkSelfPermission(surveydata1.this, Manifest.permission.ACCESS_FINE_LOCATION)
                                != PackageManager.PERMISSION_GRANTED) {

                            ActivityCompat.requestPermissions(surveydata1.this,
                                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 111);

                        } else {

                            ActivityCompat.requestPermissions(surveydata1.this,
                                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 111);
                        }

                    }
                }).setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {


        if (requestCode == 1122) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getCurrentLocation();
            } else {
                Toast.makeText(this, "Location Permission Denied", Toast.LENGTH_SHORT).show();
            }

        }


//        if (requestCode == 111) {
//
//            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//
//                mLocationListeners = new surveydata1.LocationListener[]{
//                        new surveydata1.LocationListener(LocationManager.GPS_PROVIDER),
//                        new surveydata1.LocationListener(LocationManager.NETWORK_PROVIDER)
//                };
//
//
//                Log.e(TAG, "onCreate");
//                initializeLocationManager();
//                try {
//                    mLocationManager.requestLocationUpdates(
//                            LocationManager.NETWORK_PROVIDER, LOCATION_INTERVAL, LOCATION_DISTANCE,
//                            mLocationListeners[1]);
//                } catch (SecurityException ex) {
//                    Log.i(TAG, "fail to request location update, ignore", ex);
//                } catch (IllegalArgumentException ex) {
//                    Log.d(TAG, "network provider does not exist, " + ex.getMessage());
//                }
//                try {
//                    mLocationManager.requestLocationUpdates(
//                            LocationManager.GPS_PROVIDER, LOCATION_INTERVAL, LOCATION_DISTANCE,
//                            mLocationListeners[0]);
//                } catch (SecurityException ex) {
//                    Log.i(TAG, "fail to request location update, ignore", ex);
//                } catch (IllegalArgumentException ex) {
//                    Log.d(TAG, "gps provider does not exist " + ex.getMessage());
//                }
//
//            } else {
//
//                if (ActivityCompat.checkSelfPermission(surveydata1.this, Manifest.permission.ACCESS_FINE_LOCATION)
//                        != PackageManager.PERMISSION_GRANTED) {
//
//                    ActivityCompat.requestPermissions(surveydata1.this,
//                            new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 111);
//
//                } else {
//
//                    Toast.makeText(this, "Done!", Toast.LENGTH_SHORT).show();
//
//                    mLocationListeners = new surveydata1.LocationListener[]{
//                            new surveydata1.LocationListener(LocationManager.GPS_PROVIDER),
//                            new surveydata1.LocationListener(LocationManager.NETWORK_PROVIDER)
//                    };
//
//
//                    Log.e(TAG, "onCreate");
//                    initializeLocationManager();
//                    try {
//                        mLocationManager.requestLocationUpdates(
//                                LocationManager.NETWORK_PROVIDER, LOCATION_INTERVAL, LOCATION_DISTANCE,
//                                mLocationListeners[1]);
//                    } catch (SecurityException ex) {
//                        Log.i(TAG, "fail to request location update, ignore", ex);
//                    } catch (IllegalArgumentException ex) {
//                        Log.d(TAG, "network provider does not exist, " + ex.getMessage());
//                    }
//                    try {
//                        mLocationManager.requestLocationUpdates(
//                                LocationManager.GPS_PROVIDER, LOCATION_INTERVAL, LOCATION_DISTANCE,
//                                mLocationListeners[0]);
//                    } catch (SecurityException ex) {
//                        Log.i(TAG, "fail to request location update, ignore", ex);
//                    } catch (IllegalArgumentException ex) {
//                        Log.d(TAG, "gps provider does not exist " + ex.getMessage());
//                    }
//
//                }
//
//            }
//
//        }

    }

//    private void initializeLocationManager() {
//        Log.e(TAG, "initializeLocationManager");
//        if (mLocationManager == null) {
//            mLocationManager = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
//        }
//    }
//
//    private class LocationListener implements android.location.LocationListener {
//        Location mLastLocation;
//        String provi = "";
//
//        public LocationListener(String provider) {
//            Log.e(TAG, "LocationListener " + provider);
//            provi = provider;
//            mLastLocation = new Location(provider);
//        }
//
//        @Override
//        public void onLocationChanged(Location location) {
//            Log.e(TAG, "onLocationChanged: " + location);
//            mLastLocation.set(location);
//
//            String latitude = String.valueOf(location.getLatitude());
//            String longitude = String.valueOf(location.getLongitude());
//
//            editText28.setText(latitude);
//            editText29.setText(longitude);
//
//        }
//
//        @Override
//        public void onProviderDisabled(String provider) {
//            Log.e(TAG, "onProviderDisabled: " + provider);
//        }
//
//        @Override
//        public void onProviderEnabled(String provider) {
//            Log.e(TAG, "onProviderEnabled: " + provider);
//        }
//
//        @Override
//        public void onStatusChanged(String provider, int status, Bundle extras) {
//            Log.e(TAG, "onStatusChanged: " + provider);
//        }
//    }

//    @Override
//    public void onDestroy()
//    {
//        Log.e(TAG, "onDestroy");
//        super.onDestroy();
//        if (mLocationManager != null) {
//            for (int i = 0; i < mLocationListeners.length; i++) {
//                try {
//                    mLocationManager.removeUpdates(mLocationListeners[i]);
//                } catch (Exception ex) {
//                    Log.i(TAG, "fail to remove location listners, ignore", ex);
//                }
//            }
//        }
//    }


    private void getCurrentLocation() {
        client = LocationServices.getFusedLocationProviderClient(this);
        Toast.makeText(this, "wwww", Toast.LENGTH_SHORT).show();
        Task<Location> task = client.getLastLocation();
        task.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(final Location location) {

                if (location!=null){

                    supportMapFragment.getMapAsync(new OnMapReadyCallback() {
                        @Override
                        public void onMapReady(GoogleMap googleMap) {

                            LatLng   latLng = new LatLng(location.getLatitude(), location.getLongitude());
                            MarkerOptions markerOptions=new MarkerOptions().position(latLng).title("You are here");
                            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng,10));
                            googleMap.addMarker(markerOptions);
                        }
                    });


                }

            }
        });

    }



    private void requestPermissionLocation() {
        if (ActivityCompat.checkSelfPermission(surveydata1.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            getCurrentLocation();
        } else {
            ActivityCompat.requestPermissions(surveydata1.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1122);
        }
    }


    @Override
    public void onPolygonClick(Polygon polygon) {
        GoogleMap googleMap = null;

        Polyline polyline1 = googleMap.addPolyline(new PolylineOptions()
                .clickable(true)
                .add(
                        new LatLng(	30.40229000, 71.87362700)
                ));



    }

    @Override
    public void onPolylineClick(Polyline polyline) {
        GoogleMap googleMap = null;
        Polyline polyline1 = googleMap.addPolyline(new PolylineOptions()
                .clickable(true)
                .add(
                        new LatLng(	30.40229000, 71.87362700)
                ));
// Store a data object with the polyline, used here to indicate an arbitrary type.
        polyline1.setTag("A");

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        Polyline polyline1 = googleMap.addPolyline(new PolylineOptions()
                .clickable(true)
                .add(
                        new LatLng(	30.40229000, 71.87362700)
                ));

        // Position the map's camera near Alice Springs in the center of Australia,
        // and set the zoom factor so most of Australia shows on the screen.
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(	30.40229000, 71.87362700), 4));

        // Set listeners for click events.
        googleMap.setOnPolylineClickListener(this);
        googleMap.setOnPolygonClickListener(this);

    }
}