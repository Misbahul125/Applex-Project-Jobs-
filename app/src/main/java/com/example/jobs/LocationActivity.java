package com.example.jobs;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.media.Image;
import android.os.Bundle;
import android.os.Looper;
import android.provider.Settings;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import static androidx.constraintlayout.motion.widget.Debug.getLocation;

public class LocationActivity extends AppCompatActivity {

    RadioGroup location_type;
    RadioButton radioButtonLocation;
    EditText pincode , houseNo_buildingName , landmark , area , city , state , country , full_address;
    ImageButton imgBtn_getLocation , imgBtn_setLocation;
     public String data_address_type;

    FusedLocationProviderClient fusedLocationProviderClient;

    FirebaseFirestore firebaseFirestore;
    CollectionReference collectionReference , collectionReference2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);

        getSupportActionBar().setTitle("Location");

        location_type = findViewById(R.id.location_type);
        pincode = findViewById(R.id.pin);
        houseNo_buildingName = findViewById(R.id.houseNo_buildingName);
        landmark = findViewById(R.id.landmark);
        area = findViewById(R.id.area);
        city = findViewById(R.id.city);
        state = findViewById(R.id.state);
        country = findViewById(R.id.country);
        full_address = findViewById(R.id.full_addrress);
        imgBtn_getLocation = findViewById(R.id.imgBtn_getLocation);
        imgBtn_setLocation = findViewById(R.id.imgBtn_setLocation);

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(LocationActivity.this);                    // initialize fusedLocationProviderClient

        firebaseFirestore = FirebaseFirestore.getInstance();
        collectionReference = firebaseFirestore.collection("location");
        collectionReference2 = firebaseFirestore.collection("locations");



        imgBtn_getLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ActivityCompat.checkSelfPermission(LocationActivity.this,                                                   // check permission
                        Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(LocationActivity.this ,
                        Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    locate();                                                                                                         // if both permisssions are granted
                } else {                                                                                                             // if permission denied
                    ActivityCompat.requestPermissions(LocationActivity.this,
                            new String[]{Manifest.permission.ACCESS_FINE_LOCATION , Manifest.permission.ACCESS_COARSE_LOCATION},
                            100);
                }
            }
        });

        imgBtn_setLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String data_pincode = pincode.getText().toString().trim();
                String data_houseNo_buildingName = houseNo_buildingName.getText().toString().trim();
                String data_landmark = landmark.getText().toString().trim();
                String data_area = area.getText().toString().trim();
                String data_city = city.getText().toString().trim();
                String data_state = state.getText().toString().trim();
                String data_country = country.getText().toString().trim();
                String data_full_address = full_address.getText().toString().trim();

                if ((TextUtils.isEmpty(data_pincode)) || (TextUtils.isEmpty(data_houseNo_buildingName)) || (TextUtils.isEmpty(data_area)) || (TextUtils.isEmpty(data_city))|| (TextUtils.isEmpty(data_state)) || (TextUtils.isEmpty(data_country)) || (TextUtils.isEmpty(data_full_address))) {
                    Toast.makeText(LocationActivity.this, "Please fill out all the fields", Toast.LENGTH_SHORT).show();
                } else {
                    final LocationModel locationModel = new LocationModel();

                    locationModel.setAddress_type(data_address_type);
                    locationModel.setPincode(data_pincode);
                    locationModel.setHouseNo(data_houseNo_buildingName);
                    locationModel.setLandmark(data_landmark);
                    locationModel.setArea(data_area);
                    locationModel.setCity(data_city);
                    locationModel.setState(data_state);
                    locationModel.setCountry(data_country);
                    locationModel.setFull_address(data_full_address);

                    collectionReference.add(locationModel);

                    Toast.makeText(LocationActivity.this, "Location added successfully", Toast.LENGTH_SHORT).show();
                }
            }


        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 100 && grantResults.length > 0 && (grantResults[0] + grantResults[1] == PackageManager.PERMISSION_GRANTED)) {
            locate();                                                                                        // when permission granted , call method
        } else {                                                                                             // when permission denied
            Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show();
        }
    }

    @SuppressLint("MissingPermission")
    private void locate() {
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);     // initialize location manager

        if(locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)){
            fusedLocationProviderClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {        // when location service is enabled , get last location
                @Override
                public void onComplete(@NonNull Task<Location> task) {
                    Location location = task.getResult();                                                   // initialize location
                    if (location != null) {
                        try {
                            Geocoder geocoder = new Geocoder(LocationActivity.this, Locale.getDefault());          // initialize geocoder
                            List<Address> addresses = geocoder.getFromLocation(location.getLatitude(),             // initialize adderess list
                                    location.getLongitude(), 1);
                            /*//set latitude on textview
                        city.setText(Html.fromHtml("<font color='#547398'><b>Latitude :</b><br></font>"
                                + addresses.get(0).getLatitude()
                        ));
                        // set longitude on textview
                        city.setText(Html.fromHtml("<font color='#547398'><b>Longitude :</b><br></font>"
                                + addresses.get(0).getLongitude()
                        ));*/
                            // set pin on text view
                            pincode.setText(addresses.get(0).getPostalCode());
                            // set house no. or building name on text view
                            houseNo_buildingName.setText(addresses.get(0).getPremises());
                            // set area or locality on text view
                            area.setText(addresses.get(0).getSubLocality());
                            // set city on textview
                            city.setText(addresses.get(0).getLocality());
                            //set state on textview
                            state.setText(addresses.get(0).getAdminArea());
                            // set country on text view
                            country.setText(addresses.get(0).getCountryName());
                            // set full address on text view
                            full_address.setText(addresses.get(0).getAddressLine(0));

                            GeoPoint geoPoint = new GeoPoint(location.getLatitude() , location.getLongitude());
                            LocationModel locationModel = new LocationModel();
                            locationModel.setGeoPoint(geoPoint);

                            collectionReference2.add(locationModel);

                            Toast.makeText(LocationActivity.this, "Location fetched successfully", Toast.LENGTH_SHORT).show();
                        } catch (IOException e) {
                            e.printStackTrace();
                            Toast.makeText(LocationActivity.this, "Location cannot be fetched", Toast.LENGTH_SHORT).show();
                        }

                    } else {
                        LocationRequest locationRequest = new LocationRequest()                             // initialize location request
                        .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                                .setInterval(10000)
                                .setFastestInterval(1000)
                                .setNumUpdates(1);
                        LocationCallback locationCallback = new LocationCallback() {                        // initialize location callback
                            @Override
                            public void onLocationResult(LocationResult locationResult) {
                                Location location1 = locationResult.getLastLocation();                      // initialize location
                                try {
                                    Geocoder geocoder1 = new Geocoder(LocationActivity.this, Locale.getDefault());          // initialize geocoder
                                    List<Address> addresses = geocoder1.getFromLocation(location1.getLatitude(),             // initialize adderess list
                                            location1.getLongitude() , 1);
                            /*//set latitude on textview
                        city.setText(Html.fromHtml("<font color='#547398'><b>Latitude :</b><br></font>"
                                + addresses.get(0).getLatitude()
                        ));
                        // set longitude on textview
                        city.setText(Html.fromHtml("<font color='#547398'><b>Longitude :</b><br></font>"
                                + addresses.get(0).getLongitude()
                        ));*/
                                    // set pin on text view
                                    pincode.setText(addresses.get(0).getPostalCode());
                                    // set city on textview
                                    city.setText(addresses.get(0).getLocality());
                                    //set state on textview
                                    state.setText(addresses.get(0).getAdminArea());
                                    // set country on text view
                                    country.setText(addresses.get(0).getCountryName());
                                    // set full address on text view
                                    full_address.setText(addresses.get(0).getAddressLine(0));

                                    GeoPoint geoPoint = new GeoPoint(location1.getLatitude() , location1.getLongitude());
                                    LocationModel locationModel = new LocationModel();
                                    locationModel.setGeoPoint(geoPoint);
                                    collectionReference2.add(locationModel);

                                    Toast.makeText(LocationActivity.this, "Location fetched successfully", Toast.LENGTH_SHORT).show();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                    Toast.makeText(LocationActivity.this, "Location cannot be fetched", Toast.LENGTH_SHORT).show();
                                }
                            }
                        };
                        fusedLocationProviderClient.requestLocationUpdates(locationRequest , locationCallback , Looper.myLooper());         //request location updates
                    }
                }
            });
        } else {                                                                                            // when location service is not enabled , open location settings
            startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
            .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
        }

    }

    public void checkButton(View view) {
        int radioId = location_type.getCheckedRadioButtonId();
        radioButtonLocation = findViewById(radioId);
        data_address_type = radioButtonLocation.getText().toString();
    }
}