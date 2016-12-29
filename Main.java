package com.example.max.instamap;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class Main extends FragmentActivity implements OnMapReadyCallback {

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
        for (int i=0; i<LIST_LOCATIONS.length; i++){
            mMap.addMarker(new MarkerOptions().position(LIST_LOCATIONS[i].location)
                    .title((LIST_LOCATIONS[i].name))
                    );
        }
        mMap.moveCamera(CameraUpdateFactory.newLatLng(manhattan));

       // mMap.addMarker(new MarkerOptions().position(manhattan).title("Marker in Sydney"));
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
            new NamedLocation("Occulus WTC", new LatLng(40.71137299999999, -74.01227299999999)),
            new NamedLocation("Driftwood Martial Arts", new LatLng(43.42177299999999, -80.55811799999999)),
    };
}
