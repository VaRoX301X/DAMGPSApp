package com.dam.gpsapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.dam.gpsapp.db.DbHelper;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationAvailability;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import java.time.Instant;
import java.util.Date;

public class MainActivity extends AppCompatActivity {
    private static final int PERMISSIONS_FINE_LOCATION = 99;
    private static final int INTERVALO_NORMAL = 7;
    private static final int INTERVALO_RAPIDO = 4;
    private static final int PERMISSIONS_FINE_LOCATION_S = 98;

    TextView tv_lat, tv_lon, tv_altitude, tv_accuracy, tv_speed, tv_sensor, tv_updates, tv_address;


    Switch sw_locationupdates, sw_gps;

    ListView listLocation;


    LocationRequest locationRequest;

    LocationCallback locationCallBack;

    // google api for location services
    FusedLocationProviderClient fusedLocationProviderClient;

    //Databases
    DbHelper dbHelper;
    SQLiteDatabase db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tv_lat = findViewById(R.id.tv_lat);
        tv_lon = findViewById(R.id.tv_lon);
        tv_altitude = findViewById(R.id.tv_altitude);
        tv_accuracy = findViewById(R.id.tv_accuracy);
        tv_speed = findViewById(R.id.tv_speed);
        tv_sensor = findViewById(R.id.tv_sensor);
        tv_updates = findViewById(R.id.tv_updates);

        listLocation = findViewById(R.id.listLocation);

        listLocation.setAdapter();


        tv_updates.setText("La localizacion NO esta siendo actualizada");
        tv_lat.setText("No hay seguimiento GPS");
        tv_lon.setText("No hay seguimiento GPS");
        tv_altitude.setText("No hay seguimiento GPS");
        tv_accuracy.setText("No hay seguimiento GPS");
        tv_speed.setText("No hay seguimiento GPS");
        tv_speed.setText("No hay seguimiento GPS");


        sw_locationupdates = findViewById(R.id.sw_locationsupdates);
        sw_gps = findViewById(R.id.sw_gps);

        // set props of location requrest
        locationRequest = new LocationRequest();
        locationRequest.setInterval(1000*INTERVALO_NORMAL);
        locationRequest.setFastestInterval(1000*INTERVALO_RAPIDO);
        locationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);

        locationCallBack = new LocationCallback() {
            @Override
            public void onLocationAvailability(@NonNull LocationAvailability locationAvailability) {
                super.onLocationAvailability(locationAvailability);


            }

            @Override
            public void onLocationResult(@NonNull LocationResult locationResult) {
                super.onLocationResult(locationResult);

                updateUIValues(locationResult.getLastLocation());
            }
        };

        sw_gps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (sw_gps.isChecked()) {
                    // use gps sensors
                    locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
                    tv_sensor.setText("GPS");
                } else {
                    locationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
                    tv_sensor.setText("Torres movil + Wifi");
                }
            }
        });


        sw_locationupdates.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (sw_locationupdates.isChecked()){
                    startLocationUpdates();
                } else {
                    stopLocationUpdates();
                }
            }
        });

        // Crear base de datos sqlite
        dbHelper = new DbHelper(MainActivity.this);
        db = dbHelper.getWritableDatabase();
        if (db != null) {
            Toast.makeText(MainActivity.this, "DB CREADA", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(MainActivity.this, "FALLO AL CREAR DB", Toast.LENGTH_LONG).show();
            finish();
        }

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
            Toast.makeText(MainActivity.this, "La aplicación tiene permisos GPS", Toast.LENGTH_SHORT).show();
            fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(MainActivity.this);
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                requestPermissions(new String[] {Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSIONS_FINE_LOCATION_S);
            }
        }

    }

    private void stopLocationUpdates() {
        tv_updates.setText("La localizacion NO esta siendo actualizada");
        fusedLocationProviderClient.removeLocationUpdates(locationCallBack);
        tv_lat.setText("No hay seguimiento GPS");
        tv_lon.setText("No hay seguimiento GPS");
        tv_altitude.setText("No hay seguimiento GPS");
        tv_accuracy.setText("No hay seguimiento GPS");
        tv_speed.setText("No hay seguimiento GPS");
        tv_sensor.setText("Torres movil + Wifi");
        tv_speed.setText("No hay seguimiento GPS");

    }

    @SuppressLint("MissingPermission")
    private void startLocationUpdates() {
        tv_updates.setText("La localizacion esta siendo actualizada");
        fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallBack, null);
        updateGPS();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case PERMISSIONS_FINE_LOCATION:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    updateGPS();
                } else {
                    Toast.makeText(this, "Se necesitan permisos para que la app funcione correctamente", Toast.LENGTH_LONG).show();
                    finish();
                }
                break;
            case PERMISSIONS_FINE_LOCATION_S:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    Toast.makeText(this, "Permisos obtenidos. La app puede funcionar con normalidad", Toast.LENGTH_LONG).show();
                    fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(MainActivity.this);
                } else {
                    Toast.makeText(this, "Se necesitan permisos para que la app funcione correctamente", Toast.LENGTH_LONG).show();
                    finish();
                }
                break;
        }

    }

    private void updateGPS() {



        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
            fusedLocationProviderClient.getLastLocation().addOnSuccessListener(this, new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {
                    // we got permissions. Put the values of location.
                    updateUIValues(location);
                }
            });
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                requestPermissions(new String[] {Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSIONS_FINE_LOCATION);
            }
        }

    }

    private void updateUIValues(Location location) {
        // update all text objects
        tv_lat.setText(String.valueOf(location.getLatitude()));
        tv_lon.setText(String.valueOf(location.getLongitude()));
        tv_accuracy.setText(String.valueOf(location.getAccuracy()));
        if (location.hasAltitude()) {
            tv_altitude.setText(String.valueOf(location.getAltitude()));
        } else {
            tv_altitude.setText("No disponible");
        }
        if (location.hasSpeed()) {
            tv_speed.setText(String.valueOf(location.getSpeed()));
        } else {
            tv_speed.setText("No disponible");
        }

        ContentValues values = new ContentValues();
        values.put("fecha", Date.from(Instant.now()).toString());
        values.put("latitud", String.valueOf(location.getLatitude()));
        values.put("longitud", String.valueOf(location.getLongitude()));

        // Insert the new row, returning the primary key value of the new row
        long newRowId = db.insert(DbHelper.TABLE_LOCATION, null, values);
        Log.d("db", String.valueOf(newRowId));
        Log.d("db", values.toString());




    }

}