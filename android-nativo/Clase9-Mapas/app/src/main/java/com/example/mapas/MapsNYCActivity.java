package com.example.mapas;

import androidx.fragment.app.FragmentActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.MarkerOptions;
import com.example.mapas.databinding.ActivityMapsNycBinding;

import java.io.IOException;
import java.util.List;

public class MapsNYCActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private ActivityMapsNycBinding binding;
    private final static int INITIAL_ZOOM_LEVEL = 15;
    private Geocoder mGeocoder;

    SensorManager sensorManager;
    Sensor lightSensor;
    SensorEventListener lightSensorListener;

    EditText mAddress;
    public static final double lowerLeftLatitude = 40.65129083311869;
    public static final double lowerLeftLongitude = -74.08836562217077;
    public static final double upperRightLatitude = 40.89464984144272;
    public static final double upperRightLongitude = -73.82023056066367;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMapsNycBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        mAddress = binding.address;
        mAddress.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEND) {
                    findAddress();
                }
                return false;
            }
        });

        // Initialize the sensors
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        lightSensor = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);

        // Initialize th listener
        lightSensorListener = new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent event) {
                if (mMap != null) {
                    if (event.values[0] < 5000) {
                        Log.i("MAPS", "DARK MAP " + event.values[0]);
                        mMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(MapsNYCActivity.this, R.raw.style_night));
                    } else {
                        Log.i("MAPS", "LIGHT MAP " + event.values[0]);
                        mMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(MapsNYCActivity.this, R.raw.style_day_retro));
                    }
                }
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int i) {}
        };

        // Initialize the geocoder
        mGeocoder = new Geocoder(getBaseContext());
    }

    @Override
    protected void onResume() {
        super.onResume();
        sensorManager.registerListener(lightSensorListener,
                lightSensor,
                SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(lightSensorListener);
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setBuildingsEnabled(true);


        // Add a marker in New York City and move the camera
        LatLng empireState = new LatLng(40.74845723587678, -73.98566346902886);
        mMap.addMarker(new MarkerOptions().position(empireState).title("Empire State Building"));
        Bitmap b = BitmapFactory.decodeResource(getResources(), R.drawable.empire_state);
        Bitmap smallMarker = Bitmap.createScaledBitmap(b, 200, 200, false);
        mMap.addMarker(new MarkerOptions().position(empireState)
                .title("Empire State")
                .snippet("Empire State Building, New York, EE.UU.")
                .alpha(0.9f)
                .icon(BitmapDescriptorFactory.fromBitmap(smallMarker)));

        // Set initial zoom level
        mMap.moveCamera(CameraUpdateFactory.zoomTo(INITIAL_ZOOM_LEVEL));
        // Enable touch gestures
        mMap.getUiSettings().setAllGesturesEnabled(true);
        /// Add UI controls
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.getUiSettings().setCompassEnabled(true);
        mMap.getUiSettings().setMyLocationButtonEnabled(true);

        mMap.moveCamera(CameraUpdateFactory.newLatLng(empireState));
    }

    public void search(View view) {
        mMap.clear();
        findAddress();
    }

    private void findAddress() {
        String addressString = mAddress.getText().toString();
        if (!addressString.isEmpty()) {
            try {
                List<Address> addresses = mGeocoder.getFromLocationName(addressString,2,
                        lowerLeftLatitude, lowerLeftLongitude,
                        upperRightLatitude, upperRightLongitude);
                if (addresses != null && !addresses.isEmpty()) {
                    Address addressResult = addresses.get(0);
                    LatLng position = new LatLng(addressResult.getLatitude(), addressResult.getLongitude());
                    if (mMap != null) {
                        mMap.addMarker(new MarkerOptions().position(position)
                                .title(addressResult.getFeatureName())
                                .snippet(addressResult.getAddressLine(0))
                                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));
                        mMap.moveCamera(CameraUpdateFactory.newLatLng(position));
                    }
                } else {
                    Toast.makeText(MapsNYCActivity.this, "Dirección no encontrada", Toast.LENGTH_SHORT).show();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            Toast.makeText(MapsNYCActivity.this, "La dirección está vacía", Toast.LENGTH_SHORT).show();
        }
    }
}