package com.example.max.instamap;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.ViewTreeObserver;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnInfoWindowClickListener;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;


public class Main extends FragmentActivity implements

        OnMapReadyCallback, OnInfoWindowClickListener {
    private LatLngBounds.Builder bounds;



    @Override
    public void onInfoWindowClick(Marker marker) {

    }

    private static class NamedLocation {

        public final String name;
        public final LatLng location;

        NamedLocation(String name, LatLng location) {
            this.name = name;
            this.location = location;
        }
    }

    private static final NamedLocation[] LIST_LOCATIONS = new NamedLocation[]{
            new NamedLocation("Brooklyn Bridge", new LatLng(40.706086, -73.996864)),
            new NamedLocation("Times Square", new LatLng(40.7583595, -73.9864889)),
            new NamedLocation("Staten Island Ferry", new LatLng(40.6719458, -74.0424948)),
            new NamedLocation("Koneko Cat Cafe", new LatLng(40.7204578, -73.9841388)),
            new NamedLocation("Oculus WTC", new LatLng(40.71137299999999, -74.01227299999999)),
            new NamedLocation("Driftwood Martial Arts", new LatLng(43.42177299999999, -80.55811799999999)),
    };

    private static final int[] ImageID = new int[]{
            R.drawable.brooklyn_bridge,
            R.drawable.times_square,
            R.drawable.staten_ferry,
            R.drawable.koneko,
            R.drawable.oculus_wtc,
            0,
    };


    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

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
        LatLng manhattan = new LatLng(40.783060, -73.971249);
        addMarkersToMap();

        // Setting an info window adapter allows us to change the both the contents and look of the
        // info window.
        mMap.setOnInfoWindowClickListener(this);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(manhattan, 10f));

        /*// Set listeners for marker events.  See the bottom of this class for their behavior.
        mMap.setOnMarkerClickListener(this);
        mMap.setOnInfoWindowClickListener(this);
        mMap.setOnMarkerDragListener(this);
        mMap.setOnInfoWindowCloseListener(this);
        mMap.setOnInfoWindowLongClickListener(this);*/

        final View mapView = getSupportFragmentManager().findFragmentById(R.id.map).getView();
        if (mapView.getViewTreeObserver().isAlive()) {
            mapView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @SuppressWarnings("deprecation") // We use the new method when supported
                @SuppressLint("NewApi") // We check which build version we are using.
                @Override
                public void onGlobalLayout() {
                    /*LatLngBounds bounds = new LatLngBounds.Builder()
                            .include(LIST_LOCATIONS[0].location)
                            .include(LIST_LOCATIONS[1].location)
                            .include(LIST_LOCATIONS[2].location)
                            .include(LIST_LOCATIONS[3].location)
                            .include(LIST_LOCATIONS[4].location)
                            .include(LIST_LOCATIONS[5].location)
                            .build();*/
                    bounds = new LatLngBounds.Builder();
                    for (int i = 0; i < LIST_LOCATIONS.length; i++) {
                        bounds.include(LIST_LOCATIONS[i].location);
                    }

                    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
                        mapView.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                    } else {
                        mapView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    }
                    mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds.build(), 50));
                }
            });
        }
    }

    private void addMarkersToMap() {
        for (int i = 0; i < LIST_LOCATIONS.length; i++) {
            mMap.addMarker(new MarkerOptions()
                    .position(LIST_LOCATIONS[i].location)
                    .title((LIST_LOCATIONS[i].name))
            );
        }
    }


}
