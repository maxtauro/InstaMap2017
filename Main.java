package com.example.max.instamap;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.InfoWindowAdapter;
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
    private SeekBar mRotationBar;



    @Override
    public void onInfoWindowClick(Marker marker) {

    }

    /** Demonstrates customizing the info window and/or its contents. */
    class CustomInfoWindowAdapter implements InfoWindowAdapter {

        // These are both viewgroups containing an ImageView with id "badge" and two TextViews with id
        // "title" and "snippet".
        private final View mWindow;
        //private final View mContents;

        CustomInfoWindowAdapter() {
            mWindow = getLayoutInflater().inflate(R.layout.custom_info_window, null);
           // mContents = getLayoutInflater().inflate(R.layout.custom_info_contents, null);
        }

        @Override
        public View getInfoWindow(Marker marker) {
            render(marker, mWindow);
            return mWindow;
        }

        @Override
        public View getInfoContents(Marker marker) {
            return null;                                // <-- dont know why i need this but i do
            //render(marker, mContents);
            //return mContents;
        }


        private void render(Marker marker, View view) {
            int badge=0;
           // badge = R.drawable.brooklyn_bridge;
            for (int i=0;i<LIST_LOCATIONS.length;i++){ // gets the ImageID corresponding to the marker
                if (marker.getTitle().equals(LIST_LOCATIONS[i].name)){ // be sure to use .equals
                    badge = LIST_LOCATIONS[i].ImageID;
                }
            }
            ImageView imgView = (ImageView) view.findViewById(R.id.badge);
            imgView.setImageResource(badge);
            imgView.getLayoutParams().width = 150;
            //((ImageView) view.findViewById(R.id.badge)).setImageResource(badge);  this works
            String title = marker.getTitle();
            TextView titleUi = ((TextView) view.findViewById(R.id.title));
            if (title != null) {
                // Spannable string allows us to edit the formatting of the text.
                SpannableString titleText = new SpannableString(title);
                titleText.setSpan(new ForegroundColorSpan(Color.RED), 0, titleText.length(), 0);
                titleUi.setText(titleText);
            } else {
                titleUi.setText("");
            }

            String snippet = marker.getSnippet();
            TextView snippetUi = ((TextView) view.findViewById(R.id.snippet));
            if (snippet != null && snippet.length() > 12) {
                SpannableString snippetText = new SpannableString(snippet);
                snippetText.setSpan(new ForegroundColorSpan(Color.MAGENTA), 0, 10, 0);
                snippetText.setSpan(new ForegroundColorSpan(Color.BLUE), 12, snippet.length(), 0);
                snippetUi.setText(snippetText);
            } else {
                snippetUi.setText("");
            }
        }
    }

    private static class NamedLocation {
    // what NamedLocation is now will essentially be what Tweets are once twitter is included
    // in the future this will need to be:
    // NamedLocation (String name, LatLng location, int ImageID, String tweetString, int nlikes, (listof users) retweets)
        public final String name;
        public final LatLng location;
        public final int ImageID;
        public final String tweetString;

        NamedLocation(String name, LatLng location, int ImageID, String tweetString) {
            this.name = name;
            this.location = location;
            this.ImageID = ImageID;// if there is no picture ImageID will be 0,
            this.tweetString = tweetString;
        }
    }

    private static final NamedLocation[] LIST_LOCATIONS = new NamedLocation[]{
            new NamedLocation("Brooklyn Bridge", new LatLng(40.706086, -73.996864),R.drawable.brooklyn_bridge,"typical Brooklyn Bridge photo"),
            new NamedLocation("Times Square", new LatLng(40.7583595, -73.9864889),R.drawable.times_square,null),
            new NamedLocation("Staten Island Ferry", new LatLng(40.6719458, -74.0424948),R.drawable.staten_ferry,null),
            new NamedLocation("Koneko Cat Cafe", new LatLng(40.7204578, -73.9841388),R.drawable.koneko,"Cat Cafes are 10/10"),
            new NamedLocation("Oculus WTC", new LatLng(40.71137299999999, -74.01227299999999),R.drawable.oculus_wtc,null),
            new NamedLocation("Jenna's Apartment", new LatLng(40.763402, -73.963768),0,null),
            //new NamedLocation("Driftwood Martial Arts", new LatLng(43.42177299999999, -80.55811799999999), 0),
    };

    /*private static final int[] ImageID = new int[]{
            R.drawable.brooklyn_bridge,
            R.drawable.times_square,
            R.drawable.staten_ferry,
            R.drawable.koneko,
            R.drawable.oculus_wtc,
            //0,
    };*/


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


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        LatLng manhattan = new LatLng(40.783060, -73.971249);
        addMarkersToMap();

        // Setting an info window adapter allows us to change the both the contents and look of the
        // info window.
        mMap.setInfoWindowAdapter(new CustomInfoWindowAdapter());
       // mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(manhattan, 10f));  <-- I dont need this since i set bounds with the markers

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
                    // learneds how to make bounds this way from
                    // https://stackoverflow.com/questions/14636118/android-set-goolgemap-bounds-from-from-database-of-points
                    bounds = new LatLngBounds.Builder();// when map opens, all points are in view
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
                    .snippet((LIST_LOCATIONS[i].tweetString))

            );
        }
    }


}
