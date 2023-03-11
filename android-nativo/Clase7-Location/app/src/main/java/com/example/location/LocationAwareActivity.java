package com.example.location;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.app.Activity;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.view.View;

import com.example.location.databinding.ActivityLocationAwareBinding;
import com.example.location.model.CustomLocation;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.location.Priority;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.Writer;
import java.util.Date;
import java.util.logging.Logger;
import android.Manifest;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;

public class LocationAwareActivity extends AppCompatActivity {
    private ActivityLocationAwareBinding binding;
    // Setup del logger para esta clase
    private static final String TAG = LocationSingleUseActivity.class.getName();
    private Logger logger = Logger.getLogger(TAG);

    // Constantes
    public final static double RADIUS_OF_EARTH_KM = 6371;
    public static final double DORADO_LAT = 4.598102;
    public static final double DORADO_LON = -74.076099;

    // Variables de perrmisos
    private final int LOCATION_PERMISSION_ID = 103;
    public static final int REQUEST_CHECK_SETTINGS = 201;
    String locationPerm = Manifest.permission.ACCESS_FINE_LOCATION;

    // Variables de localización
    private FusedLocationProviderClient mFusedLocationClient;
    private LocationRequest mLocationRequest;
    private LocationCallback mLocationCallback;
    Location mCurrentLocation;
    private JSONArray mStoredLocations = new JSONArray();

    // Views
    private TextView latitud, longitud, elevacion, distancia;
    private Button save;
    LinearLayout listaUbicaciones;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLocationAwareBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        // Inflates
        latitud = binding.latitudeValue;
        longitud = binding.longitudeValue;
        elevacion = binding.elevationValue;
        distancia = binding.distanceValue;
        save = binding.saveLocation;
        listaUbicaciones = binding.listaUbicaciones;

        save.setOnClickListener(new View.OnClickListener () {
           @Override
           public void onClick (View view) {
               writeJSONObject();
               if (mCurrentLocation != null) {
                   TextView tv = new TextView(view.getContext());
                   String cadena = "Lat: "+String.valueOf(mCurrentLocation.getLatitude())
                           +" - Long: "+ String.valueOf(mCurrentLocation.getLongitude())
                           +" on "+(new Date(System.currentTimeMillis())).toString();
                   tv.setText (cadena);
                   listaUbicaciones.addView(tv);
               }
           }
        });
        // onCreate
        mLocationRequest = createLocationRequest();
        // Location
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        requestPermission(this, Manifest.permission.ACCESS_FINE_LOCATION, "El permiso es necesario para acceder a la localización", LOCATION_PERMISSION_ID);

        mLocationCallback = new LocationCallback() {
            @Override
            public void onLocationResult (LocationResult locationResult) {
                Location location = locationResult.getLastLocation();
                logger.info("Location update in the callback: "+location);
                if (location != null) {
                    mCurrentLocation = location;
                    logger.info(String.valueOf(location.getLatitude()));
                    logger.info(String.valueOf(location.getLongitude()));
                    logger.info(String.valueOf(location.getAltitude()));
                    logger.info(distance(location.getLatitude(),location.getLongitude(), DORADO_LAT, DORADO_LON) + " km");
                    latitud.setText(String.valueOf(location.getLatitude()));
                    longitud.setText(String.valueOf(location.getLongitude()));
                    elevacion.setText(String.valueOf(location.getAltitude()));
                    distancia.setText(distance(location.getLatitude(),location.getLongitude(), DORADO_LAT, DORADO_LON) + " km");
                }
            }
        };
        // Pasar a onResume!!
        turnOnLocationAndStartUpdates();
    }

    @Override
    protected void onResume() {
        super.onResume();
        startLocationUpdates();
    }

    @Override
    protected void onPause() {
        super.onPause();
        stopLocationUpdates();
    }

    private void startLocationUpdates() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            mFusedLocationClient.requestLocationUpdates(mLocationRequest, mLocationCallback, null);
        }
    }

    private void stopLocationUpdates() {
        mFusedLocationClient.removeLocationUpdates(mLocationCallback);
    }

    private LocationRequest createLocationRequest() {
        LocationRequest mLocationRequest = new LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY,10000)
                .setWaitForAccurateLocation(false)
                .setMinUpdateIntervalMillis(5000)
                .build();
        return mLocationRequest;
    }

    private void requestPermission (Activity context, String permiso, String justificacion, int idCode) {
        if (ContextCompat.checkSelfPermission(context, permiso) != PackageManager.PERMISSION_GRANTED ) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(context, permiso)) {
                Toast.makeText(context, justificacion, Toast.LENGTH_SHORT).show();
            }
            ActivityCompat.requestPermissions(context, new String[]{permiso}, idCode);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch(requestCode) {
            case LOCATION_PERMISSION_ID: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_DENIED) {
                    Toast.makeText(this,"Ya hay permiso para acceder a la localización", Toast.LENGTH_LONG).show();
                    turnOnLocationAndStartUpdates();
                } else {
                    Toast.makeText(this, "No hay permiso :(", Toast.LENGTH_LONG).show();
                }
                return;
            }
        }
    }

    private void turnOnLocationAndStartUpdates() {
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder().addLocationRequest(mLocationRequest);
        SettingsClient client = LocationServices.getSettingsClient(this);
        Task<LocationSettingsResponse> task = client.checkLocationSettings(builder.build());
        task.addOnSuccessListener(this, locationSettingsResponse -> {
            startLocationUpdates(); // Todas las condiciones para recibiir localizaciones
        });
        task.addOnFailureListener(this, new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                int statusCode = ((ApiException) e).getStatusCode();
                switch (statusCode) {
                    case CommonStatusCodes.RESOLUTION_REQUIRED:
                        // Location setttings are not satisfied, but this can be fixed by showing the user a dialog.
                        try {
                            // Show the dialog by calling startResolutionForResult(), and check the result in onActivityResult()
                            ResolvableApiException resolvable = (ResolvableApiException) e;
                            resolvable.startResolutionForResult(LocationAwareActivity.this, REQUEST_CHECK_SETTINGS);
                        } catch (IntentSender.SendIntentException sendEx) {
                            // Ignore the error
                        }
                        break;
                    case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                        // Location settings are not satisfied. No way to fix the settings so we won't show the dialog.
                        break;
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_CHECK_SETTINGS: {
                if (resultCode == RESULT_OK) {
                    startLocationUpdates();
                } else {
                    Toast.makeText(this, "Sin acceso a localización. Hardware deshabilitado", Toast.LENGTH_LONG);
                }
            }
        }
    }


    private double distance (double lat1, double long1, double lat2, double long2) {
        double latDistance = Math.toRadians(lat1 - lat2);
        double lngDistance = Math.toRadians(long1 - long2);
        double a = Math.sin(latDistance / 2)*
                Math.sin(latDistance / 2)+
                Math.cos(Math.toRadians(lat1))*
                        Math.cos(Math.toRadians(lat2))*
                        Math.sin(lngDistance / 2)*
                        Math.sin(lngDistance / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double result = RADIUS_OF_EARTH_KM * c;
        return Math.round(result*100.0)/100.0;
    }


    private void writeJSONObject () {
        CustomLocation myLocation = new CustomLocation();
        myLocation.setFecha(new Date(System.currentTimeMillis()));
        myLocation.setLatitud(mCurrentLocation.getLatitude());
        myLocation.setLongitud(mCurrentLocation.getLatitude());
        mStoredLocations.put(myLocation.toJSON());
        Writer output = null;
        String filename = "locations.json";
        try {
            File file = new File(getBaseContext().getExternalFilesDir(null), filename);
            logger.info("Ubicación de archivo: "+file);
            output = new BufferedWriter(new FileWriter(file));
            output.write(mStoredLocations.toString());
            output.close();
            Toast.makeText(getApplicationContext(), "Location saved", Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            Toast.makeText(getBaseContext(), e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

}