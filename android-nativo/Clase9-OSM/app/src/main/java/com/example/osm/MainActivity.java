package com.example.osm;

import androidx.appcompat.app.AppCompatActivity;

import android.app.UiModeManager;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.util.Log;

import org.osmdroid.api.IMapController;
import org.osmdroid.bonuspack.routing.OSRMRoadManager;
import org.osmdroid.bonuspack.routing.Road;
import org.osmdroid.bonuspack.routing.RoadManager;
import org.osmdroid.config.Configuration;
import org.osmdroid.events.MapEventsReceiver;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.MapEventsOverlay;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.Polyline;
import org.osmdroid.views.overlay.TilesOverlay;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MyActivity";
    MapView map;
    //starting point for the map
    double latitude = 4.6269938175930525;
    double longitude = -74.0638974995316;
    GeoPoint startPoint = new GeoPoint(latitude,longitude);
    Marker longPressedMarker;
    RoadManager roadManager;
    // Attribute
    Polyline roadOverlay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Context ctx = getApplicationContext();
        Configuration.getInstance().load(ctx, PreferenceManager.getDefaultSharedPreferences(ctx));
        setContentView(R.layout.activity_main);
        map = findViewById(R.id.osmMap);
        map.setTileSource(TileSourceFactory.MAPNIK);
        map.setMultiTouchControls(true);

        //Add Marker
        GeoPoint markerPoint = new GeoPoint(latitude, longitude);
        Marker marker = new Marker(map);
        marker.setTitle("Bogotá");
        marker.setSubDescription("Marcador en Bogotá");
        Drawable myIcon = getResources().getDrawable(R.drawable.location_red, this.getTheme());
        marker.setIcon(myIcon);
        marker.setPosition(markerPoint);
        marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
        map.getOverlays().add(marker);

        map.getOverlays().add(createOverlayEvents());

        roadManager = new OSRMRoadManager(this, "ANDROID");

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
    }


    @Override
    protected void onResume() {
        super.onResume();
        map.onResume();
        IMapController mapController = map.getController();
        mapController.setZoom(18.0);
        mapController.setCenter(this.startPoint);
        UiModeManager uiManager = (UiModeManager) getSystemService(Context.UI_MODE_SERVICE);
        if(uiManager.getNightMode() == UiModeManager.MODE_NIGHT_YES ) {
            map.getOverlayManager().getTilesOverlay().setColorFilter(TilesOverlay.INVERT_COLORS);
        }
    }


    @Override
    protected void onPause() {
        super.onPause();
        map.onPause();
    }


    private Marker createMarker(GeoPoint p, String title, String desc, int iconID) {
        Marker marker = null;
        if(map!=null) {
            marker = new Marker(map);
            if (title != null) {
                marker.setTitle(title);
            }
            if (desc != null) {
                marker.setSubDescription(desc);
            }
            if (iconID != 0) {
                Drawable myIcon = getResources().getDrawable(iconID, this.getTheme());
                marker.setIcon(myIcon);
            }
            marker.setPosition(p);
            marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
        }
        return marker;
    }


    private MapEventsOverlay createOverlayEvents() {
        MapEventsOverlay overlayEventos = new MapEventsOverlay(new MapEventsReceiver() {
            @Override
            public boolean singleTapConfirmedHelper(GeoPoint p) {
                return false;
            }
            @Override
            public boolean longPressHelper(GeoPoint p) {
                longPressOnMap(p);
                return true;
            }
        });
        return overlayEventos;
    }


    private void longPressOnMap(GeoPoint p) {
        if(longPressedMarker != null) {
            map.getOverlays().remove(longPressedMarker);
        }
        longPressedMarker = createMarker(p, "location", null, R.drawable.location_blue);
        map.getOverlays().add(longPressedMarker);
    }


    private void drawRoute(GeoPoint start, GeoPoint finish) {
        ArrayList<GeoPoint> routePoints = new ArrayList<>();
        routePoints.add(start);
        routePoints.add(finish);
        Road road = roadManager.getRoad(routePoints);
        Log.i(TAG, "Route length: "+road.mLength+" klm");
        Log.i(TAG, "Duration: "+road.mDuration/60+" min");
        if(map!=null) {
            if(roadOverlay!=null) {
                map.getOverlays().remove(roadOverlay);
            }
            roadOverlay = RoadManager.buildRoadOverlay(road);
            roadOverlay.getOutlinePaint().setColor(Color.RED);
            roadOverlay.getOutlinePaint().setStrokeWidth(10);
            map.getOverlays().add(roadOverlay);
        }
    }

}