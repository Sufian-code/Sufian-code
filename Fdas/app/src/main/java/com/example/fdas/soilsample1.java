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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class soilsample1 extends AppCompatActivity {

    EditText editText3, editText8, editText32, editText33;
    Button btnnxt, button3;
    Spinner spinner2, spinner3, spinner1, spinner4;

    ProgressDialog progressDialog;
    private ArrayList<String> arrayList;
    private ArrayList<String> markerIdArrayList;
    private String selectedMarkerID;

    private ArrayList<String> depthIdArrayList;
    private ArrayList<String> depthArrayList;
    private String selectedDepthID;

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
    soilsample1.LocationListener[] mLocationListeners;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_soilsample);
        btnnxt=(Button)findViewById(R.id.btnnxt);
        button3 = (Button) findViewById(R.id.button3);
        spinner2 = (Spinner) findViewById(R.id.spinner2);
        spinner3 = (Spinner) findViewById(R.id.spinner3);
        spinner1 = (Spinner) findViewById(R.id.spinner1);
        spinner4 = (Spinner) findViewById(R.id.spinner4);
        editText3 = (EditText) findViewById(R.id.editText3);
        editText8 = (EditText) findViewById(R.id.editText8);
        editText32 = (EditText) findViewById(R.id.editText32);
        editText33 = (EditText) findViewById(R.id.editText33);



        //  setting up progress dialog
        progressDialog = new ProgressDialog(soilsample1.this);
        progressDialog.setMessage("Loading....");
        progressDialog.setCancelable(false);


        soilSampleDataTask(getString(R.string.soilSampleDataURL));

        btnnxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!selectedZoneID.equals("") && !selectedZoneID.equals("0") &&
                        !selectedMarkerID.equals("") && !selectedMarkerID.equals("0") &&
                        !selectedDepthID.equals("") && !selectedDepthID.equals("0") &&
                        !selectedFarmID.equals("") && !selectedFarmID.equals("0")) {

                    if (editText3.getText().toString().trim().length() > 0 && editText8.getText().toString().trim().length() > 0 &&
                            editText32.getText().toString().trim().length() > 0 && editText33.getText().toString().trim().length() > 0) {

                        insertingSoilSamoleDataTask(editText3.getText().toString().trim(), editText8.getText().toString().trim(),
                                editText32.getText().toString().trim(), editText33.getText().toString().trim(),
                                getString(R.string.insertingSoilSampleDataURL));

                    } else {
                        Toast.makeText(soilsample1.this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                    }

                } else {
                    Toast.makeText(soilsample1.this, "Please select your zone first", Toast.LENGTH_SHORT).show();
                }


            }
        });

        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                final LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

                if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {

                    GPSAlert();

                } else

                {
                    mLocationListeners = new soilsample1.LocationListener[]{
                            new soilsample1.LocationListener(LocationManager.GPS_PROVIDER),
                            new soilsample1.LocationListener(LocationManager.NETWORK_PROVIDER)
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

    private void soilSampleDataTask(String url) {

        progressDialog.show();

        RequestQueue requestQueue = Volley.newRequestQueue(soilsample1.this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        progressDialog.dismiss();

                        if (response != null) {


                            try {

                                JSONObject jsonObject = new JSONObject(response);

                                JSONArray jsonArray = jsonObject.getJSONArray("server_response");
                                JSONArray jsonArray2 = jsonObject.getJSONArray("server_response2");
                                JSONArray jsonArray3 = jsonObject.getJSONArray("server_response3");
                                JSONArray jsonArray4 = jsonObject.getJSONArray("server_response4");

                                arrayList = new ArrayList<>();
                                markerIdArrayList = new ArrayList<>();

                                depthIdArrayList = new ArrayList<>();
                                depthArrayList = new ArrayList<>();

                                zoneIdArrayList = new ArrayList<>();
                                zoneNameArrayList = new ArrayList<>();

                                farmIdArrayList = new ArrayList<>();
                                farmOwnerArrayList = new ArrayList<>();

                                for (int i = 0; i < jsonArray.length(); i++) {

                                    JSONObject object = jsonArray.getJSONObject(i);

                                    arrayList.add(object.getString("marker_name"));
                                    markerIdArrayList.add(object.getString("marker_id"));

                                }

                                for (int i = 0; i < jsonArray2.length(); i++) {

                                    JSONObject object = jsonArray2.getJSONObject(i);

                                    depthIdArrayList.add(object.getString("depth_id"));
                                    depthArrayList.add(object.getString("depth"));

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
                                    spinner2.setAdapter(adapter);

                                    spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                        @Override
                                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                                            selectedMarkerID = markerIdArrayList.get(position);

                                        }

                                        @Override
                                        public void onNothingSelected(AdapterView<?> parent) {

                                        }
                                    });

                                } else {
                                    Toast.makeText(soilsample1.this, "No data", Toast.LENGTH_SHORT).show();
                                }


                                if (depthArrayList.size() > 0) {

                                    depthArrayList.add(0, "Select Depth");
                                    depthIdArrayList.add(0, "0");

                                    ArrayAdapter adapter = new ArrayAdapter(getApplicationContext(), R.layout.custom_spinner_text, depthArrayList.toArray());
                                    spinner3.setAdapter(adapter);

                                    spinner3.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                        @Override
                                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                                            selectedDepthID = depthIdArrayList.get(position);

                                        }

                                        @Override
                                        public void onNothingSelected(AdapterView<?> parent) {

                                        }
                                    });

                                } else {
                                    Toast.makeText(soilsample1.this, "No data", Toast.LENGTH_SHORT).show();
                                }



                                if (zoneNameArrayList.size() > 0) {

                                    zoneNameArrayList.add(0, "Select Zone");
                                    zoneIdArrayList.add(0, "0");

                                    ArrayAdapter adapter = new ArrayAdapter(getApplicationContext(), R.layout.custom_spinner_text, zoneNameArrayList.toArray());
                                    spinner1.setAdapter(adapter);

                                    spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                        @Override
                                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                                            selectedZoneID = zoneIdArrayList.get(position);

                                        }

                                        @Override
                                        public void onNothingSelected(AdapterView<?> parent) {

                                        }
                                    });

                                } else {
                                    Toast.makeText(soilsample1.this, "No data", Toast.LENGTH_SHORT).show();
                                }


                                if (farmIdArrayList.size() > 0) {

                                    farmOwnerArrayList.add(0, "Select Farm Id");
                                    farmIdArrayList.add(0, "0");

                                    ArrayAdapter adapter = new ArrayAdapter(getApplicationContext(), R.layout.custom_spinner_text, farmOwnerArrayList.toArray());
                                    spinner4.setAdapter(adapter);

                                    spinner4.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                        @Override
                                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                                            selectedFarmID = farmIdArrayList.get(position);

                                        }

                                        @Override
                                        public void onNothingSelected(AdapterView<?> parent) {

                                        }
                                    });

                                } else {
                                    Toast.makeText(soilsample1.this, "No data", Toast.LENGTH_SHORT).show();
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

                Toast.makeText(soilsample1.this, message, Toast.LENGTH_SHORT).show();


            }
        });

        requestQueue.add(stringRequest);

    }

    private void insertingSoilSamoleDataTask(final String editText3, final String editText8, final String editText32, final String editText33, String url) {

        progressDialog.show();

        RequestQueue requestQueue = Volley.newRequestQueue(soilsample1.this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        progressDialog.dismiss();

                        if (response != null) {

                            if (response.equals("true")) {

                                Toast.makeText(soilsample1.this, "Data inserted", Toast.LENGTH_SHORT).show();
                                finish();
                            }else {
                                Toast.makeText(soilsample1.this, "Something goes wrong", Toast.LENGTH_SHORT).show();
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

                Toast.makeText(soilsample1.this, message, Toast.LENGTH_SHORT).show();


            }
        }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                HashMap<String, String> params = new HashMap<>();

                params.put("main_marker", selectedMarkerID);
                params.put("depth", selectedDepthID);
                params.put("date", editText3);
                params.put("child_marker", editText8);
                params.put("lat", editText32);
                params.put("long", editText33);
                params.put("zone", selectedZoneID);
                params.put("farm_id", selectedFarmID);


                return params;

            }
        };

        requestQueue.add(stringRequest);

    }




    private void GPSAlert() {

        AlertDialog.Builder builder = new AlertDialog.Builder(soilsample1.this);
        builder.setMessage("Your GPS seems to be disabled, do you want to enable it?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));

                        if (ActivityCompat.checkSelfPermission(soilsample1.this, Manifest.permission.ACCESS_FINE_LOCATION)
                                != PackageManager.PERMISSION_GRANTED) {

                            ActivityCompat.requestPermissions(soilsample1.this,
                                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 111);

                        } else {

                            ActivityCompat.requestPermissions(soilsample1.this,
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

                mLocationListeners = new soilsample1.LocationListener[]{
                        new soilsample1.LocationListener(LocationManager.GPS_PROVIDER),
                        new soilsample1.LocationListener(LocationManager.NETWORK_PROVIDER)
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

                if (ActivityCompat.checkSelfPermission(soilsample1.this, Manifest.permission.ACCESS_FINE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED) {

                    ActivityCompat.requestPermissions(soilsample1.this,
                            new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 111);

                } else {

                    Toast.makeText(this, "Done!", Toast.LENGTH_SHORT).show();

                    mLocationListeners = new soilsample1.LocationListener[]{
                            new soilsample1.LocationListener(LocationManager.GPS_PROVIDER),
                            new soilsample1.LocationListener(LocationManager.NETWORK_PROVIDER)
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

            editText32.setText(latitude);
            editText33.setText(longitude);

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
