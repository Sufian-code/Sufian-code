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
import com.google.android.gms.maps.SupportMapFragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class watersample1 extends AppCompatActivity {
    Button btnsvd1, btnpos2,loccation;
//    SupportMapFragment supportMapFragment;
    Spinner spinner1, spinner2, spinner3;
    EditText editText6, editText7, editText30, editText31;


    ProgressDialog progressDialog;
    private ArrayList<String> arrayList;
    private ArrayList<String> markerIdArrayList;
    private String selectedMarkerID;

    private ArrayList<String> zoneNameArrayList;
    private ArrayList<String> zoneIdArrayList;
    private String selectedZoneID;

    private ArrayList<String> farmIdArrayList;
    private ArrayList<String> farmOwnerArrayList;
    private String selectedFarmID;


    private static final String TAG = "IMAGESGPS";
    private LocationManager mLocationManager = null;
    private static final int LOCATION_INTERVAL = 10000;
    private static final float LOCATION_DISTANCE = 0f;
    watersample1.LocationListener[] mLocationListeners;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_watersample);
        btnsvd1=(Button)findViewById(R.id.btnsvd1);
//        loccation = (Button) findViewById(R.id.location);
        spinner1 = (Spinner) findViewById(R.id.spinner1);
        spinner2 = (Spinner) findViewById(R.id.spinner2);
        spinner3 = (Spinner) findViewById(R.id.spinner3);
        btnpos2 = (Button) findViewById(R.id.btnpos2);
        editText6 = (EditText) findViewById(R.id.editText6);
        editText7 = (EditText) findViewById(R.id.editText7);
        editText30 = (EditText) findViewById(R.id.editText30);
        editText31 = (EditText) findViewById(R.id.editText31);
//        supportMapFragment=(SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map_fragment);



//  setting up progress dialog
        progressDialog = new ProgressDialog(watersample1.this);
        progressDialog.setMessage("Loading....");
        progressDialog.setCancelable(false);

        waterSampleDataTask(getString(R.string.waterSampleDataURL));
//        location.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//            }
//        });


        btnsvd1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!selectedZoneID.equals("") && !selectedZoneID.equals("0") &&
                        !selectedMarkerID.equals("") && !selectedMarkerID.equals("0") &&
                        !selectedFarmID.equals("") && !selectedFarmID.equals("0")) {

                    if (editText6.getText().toString().trim().length() > 0 && editText7.getText().toString().trim().length() > 0 &&
                            editText30.getText().toString().trim().length() > 0 && editText31.getText().toString().trim().length() > 0) {

                        insertingWaterSamoleDataTask(editText6.getText().toString().trim(), editText7.getText().toString().trim(),
                                editText30.getText().toString().trim(), editText31.getText().toString().trim(),
                                getString(R.string.insertingWaterSampleDataURL));

                    } else {
                        Toast.makeText(watersample1.this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                    }

                } else {
                    Toast.makeText(watersample1.this, "Please select your zone first", Toast.LENGTH_SHORT).show();
                }


            }
        });

        btnpos2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                final LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

                if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {

                    GPSAlert();

                } else

                {
                    mLocationListeners = new watersample1.LocationListener[]{
                            new watersample1.LocationListener(LocationManager.GPS_PROVIDER),
                            new watersample1.LocationListener(LocationManager.NETWORK_PROVIDER)
                    };


                    Log.e(TAG, "onCreate");
                    initializeLocationManager();
                    try {
                        mLocationManager.requestLocationUpdates(
                                LocationManager.NETWORK_PROVIDER, LOCATION_INTERVAL, LOCATION_DISTANCE,
                                mLocationListeners[1]);
                    } catch (SecurityException ex) {
                        Log.i(TAG, "fail to request location update, ignore", ex);
                    } catch (IllegalArgumentException ex) {
                        Log.d(TAG, "network provider does not exist, " + ex.getMessage());
                    }
                    try {
                        mLocationManager.requestLocationUpdates(
                                LocationManager.GPS_PROVIDER, LOCATION_INTERVAL, LOCATION_DISTANCE,
                                mLocationListeners[0]);
                    } catch (SecurityException ex) {
                        Log.i(TAG, "fail to request location update, ignore", ex);
                    } catch (IllegalArgumentException ex) {
                        Log.d(TAG, "gps provider does not exist " + ex.getMessage());
                    }

                }


            }
        });

    }


    private void waterSampleDataTask(String url) {

        progressDialog.show();

        RequestQueue requestQueue = Volley.newRequestQueue(watersample1.this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        progressDialog.dismiss();

                        if (response != null) {


                            try {

                                JSONObject jsonObject = new JSONObject(response);

                                JSONArray jsonArray = jsonObject.getJSONArray("server_response");
                                JSONArray jsonArray3 = jsonObject.getJSONArray("server_response3");
                                JSONArray jsonArray4 = jsonObject.getJSONArray("server_response4");

                                arrayList = new ArrayList<>();
                                markerIdArrayList = new ArrayList<>();


                                zoneIdArrayList = new ArrayList<>();
                                zoneNameArrayList = new ArrayList<>();

                                farmIdArrayList = new ArrayList<>();
                                farmOwnerArrayList = new ArrayList<>();

                                for (int i = 0; i < jsonArray.length(); i++) {

                                    JSONObject object = jsonArray.getJSONObject(i);

                                    arrayList.add(object.getString("marker_name"));
                                    markerIdArrayList.add(object.getString("marker_id"));

                                }

                                for (int i = 0; i < jsonArray3.length(); i++) {

                                    JSONObject object = jsonArray3.getJSONObject(i);

                                    zoneIdArrayList.add(object.getString("zone_id"));
                                    zoneNameArrayList.add(object.getString("zone_name"));

                                }


                                for (int i = 0; i < jsonArray4.length(); i++) {

                                    JSONObject object = jsonArray4.getJSONObject(i);

                                    farmIdArrayList.add(object.getString("farm_id"));
                                    farmOwnerArrayList.add(object.getString("farm_owner"));

                                }



                                if (arrayList.size() > 0) {

                                    arrayList.add(0, "Select Main Marker");
                                    markerIdArrayList.add(0, "0");

                                    ArrayAdapter adapter = new ArrayAdapter(getApplicationContext(), R.layout.custom_spinner_text, arrayList.toArray());
                                    spinner1.setAdapter(adapter);

                                    spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                        @Override
                                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                                            selectedMarkerID = markerIdArrayList.get(position);

                                        }

                                        @Override
                                        public void onNothingSelected(AdapterView<?> parent) {

                                        }
                                    });

                                } else {
                                    Toast.makeText(watersample1.this, "No data", Toast.LENGTH_SHORT).show();
                                }


                                if (zoneNameArrayList.size() > 0) {

                                    zoneNameArrayList.add(0, "Select Zone");
                                    zoneIdArrayList.add(0, "0");

                                    ArrayAdapter adapter = new ArrayAdapter(getApplicationContext(), R.layout.custom_spinner_text, zoneNameArrayList.toArray());
                                    spinner2.setAdapter(adapter);

                                    spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                        @Override
                                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                                            selectedZoneID = zoneIdArrayList.get(position);

                                        }

                                        @Override
                                        public void onNothingSelected(AdapterView<?> parent) {

                                        }
                                    });

                                } else {
                                    Toast.makeText(watersample1.this, "No data", Toast.LENGTH_SHORT).show();
                                }


                                if (farmIdArrayList.size() > 0) {

                                    farmOwnerArrayList.add(0, "Select Farm Id");
                                    farmIdArrayList.add(0, "0");

                                    ArrayAdapter adapter = new ArrayAdapter(getApplicationContext(), R.layout.custom_spinner_text, farmOwnerArrayList.toArray());
                                    spinner3.setAdapter(adapter);

                                    spinner3.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                        @Override
                                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                                            selectedFarmID = farmIdArrayList.get(position);

                                        }

                                        @Override
                                        public void onNothingSelected(AdapterView<?> parent) {

                                        }
                                    });

                                } else {
                                    Toast.makeText(watersample1.this, "No data", Toast.LENGTH_SHORT).show();
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

                Toast.makeText(watersample1.this, message, Toast.LENGTH_SHORT).show();


            }
        });

        requestQueue.add(stringRequest);

    }


    private void insertingWaterSamoleDataTask(final String editText6, final String editText7, final String editText30, final String editText31, String url) {

        progressDialog.show();

        RequestQueue requestQueue = Volley.newRequestQueue(watersample1.this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        progressDialog.dismiss();

                        if (response != null) {

                            if (response.equals("true")) {

                                Toast.makeText(watersample1.this, "Data inserted", Toast.LENGTH_SHORT).show();
                                finish();
                            }else {
                                Toast.makeText(watersample1.this, "Something goes wrong", Toast.LENGTH_SHORT).show();
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

                Toast.makeText(watersample1.this, message, Toast.LENGTH_SHORT).show();



            }
        }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                HashMap<String, String> params = new HashMap<>();

                params.put("main_marker", selectedMarkerID);
                params.put("date", editText6);
                params.put("child_marker", editText7);
                params.put("lat", editText30);
                params.put("long", editText31);
                params.put("zone", selectedZoneID);
                params.put("farm_id", selectedFarmID);


                return params;

            }
        };

        requestQueue.add(stringRequest);

    }




    private void GPSAlert() {

        AlertDialog.Builder builder = new AlertDialog.Builder(watersample1.this);
        builder.setMessage("Your GPS seems to be disabled, do you want to enable it?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));

                        if (ActivityCompat.checkSelfPermission(watersample1.this, Manifest.permission.ACCESS_FINE_LOCATION)
                                != PackageManager.PERMISSION_GRANTED) {

                            ActivityCompat.requestPermissions(watersample1.this,
                                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 111);

                        } else {

                            ActivityCompat.requestPermissions(watersample1.this,
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

        if (requestCode == 111) {

            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                mLocationListeners = new watersample1.LocationListener[]{
                        new watersample1.LocationListener(LocationManager.GPS_PROVIDER),
                        new watersample1.LocationListener(LocationManager.NETWORK_PROVIDER)
                };


                Log.e(TAG, "onCreate");
                initializeLocationManager();
                try {
                    mLocationManager.requestLocationUpdates(
                            LocationManager.NETWORK_PROVIDER, LOCATION_INTERVAL, LOCATION_DISTANCE,
                            mLocationListeners[1]);
                } catch (SecurityException ex) {
                    Log.i(TAG, "fail to request location update, ignore", ex);
                } catch (IllegalArgumentException ex) {
                    Log.d(TAG, "network provider does not exist, " + ex.getMessage());
                }
                try {
                    mLocationManager.requestLocationUpdates(
                            LocationManager.GPS_PROVIDER, LOCATION_INTERVAL, LOCATION_DISTANCE,
                            mLocationListeners[0]);
                } catch (SecurityException ex) {
                    Log.i(TAG, "fail to request location update, ignore", ex);
                } catch (IllegalArgumentException ex) {
                    Log.d(TAG, "gps provider does not exist " + ex.getMessage());
                }

            } else {

                if (ActivityCompat.checkSelfPermission(watersample1.this, Manifest.permission.ACCESS_FINE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED) {

                    ActivityCompat.requestPermissions(watersample1.this,
                            new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 111);

                } else {

                    Toast.makeText(this, "Done!", Toast.LENGTH_SHORT).show();

                    mLocationListeners = new watersample1.LocationListener[]{
                            new watersample1.LocationListener(LocationManager.GPS_PROVIDER),
                            new watersample1.LocationListener(LocationManager.NETWORK_PROVIDER)
                    };


                    Log.e(TAG, "onCreate");
                    initializeLocationManager();
                    try {
                        mLocationManager.requestLocationUpdates(
                                LocationManager.NETWORK_PROVIDER, LOCATION_INTERVAL, LOCATION_DISTANCE,
                                mLocationListeners[1]);
                    } catch (SecurityException ex) {
                        Log.i(TAG, "fail to request location update, ignore", ex);
                    } catch (IllegalArgumentException ex) {
                        Log.d(TAG, "network provider does not exist, " + ex.getMessage());
                    }
                    try {
                        mLocationManager.requestLocationUpdates(
                                LocationManager.GPS_PROVIDER, LOCATION_INTERVAL, LOCATION_DISTANCE,
                                mLocationListeners[0]);
                    } catch (SecurityException ex) {
                        Log.i(TAG, "fail to request location update, ignore", ex);
                    } catch (IllegalArgumentException ex) {
                        Log.d(TAG, "gps provider does not exist " + ex.getMessage());
                    }

                }

            }

        }

    }

    private void initializeLocationManager() {
        Log.e(TAG, "initializeLocationManager");
        if (mLocationManager == null) {
            mLocationManager = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
        }
    }

    private class LocationListener implements android.location.LocationListener {
        Location mLastLocation;
        String provi = "";

        public LocationListener(String provider) {
            Log.e(TAG, "LocationListener " + provider);
            provi = provider;
            mLastLocation = new Location(provider);
        }

        @Override
        public void onLocationChanged(Location location) {
            Log.e(TAG, "onLocationChanged: " + location);
            mLastLocation.set(location);

            String latitude = String.valueOf(location.getLatitude());
            String longitude = String.valueOf(location.getLongitude());

            editText30.setText(latitude);
            editText31.setText(longitude);

        }

        @Override
        public void onProviderDisabled(String provider) {
            Log.e(TAG, "onProviderDisabled: " + provider);
        }

        @Override
        public void onProviderEnabled(String provider) {
            Log.e(TAG, "onProviderEnabled: " + provider);
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
            Log.e(TAG, "onStatusChanged: " + provider);
        }
    }

    @Override
    public void onDestroy()
    {
        Log.e(TAG, "onDestroy");
        super.onDestroy();
        if (mLocationManager != null) {
            for (int i = 0; i < mLocationListeners.length; i++) {
                try {
                    mLocationManager.removeUpdates(mLocationListeners[i]);
                } catch (Exception ex) {
                    Log.i(TAG, "fail to remove location listners, ignore", ex);
                }
            }
        }
    }

}
