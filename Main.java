package com.example.max.instamap;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
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
import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.models.Tweet;
import com.twitter.sdk.android.tweetui.TimelineResult;
import com.twitter.sdk.android.tweetui.UserTimeline;

import java.util.ArrayList;

import io.fabric.sdk.android.Fabric;


public class Main extends FragmentActivity implements

        OnMapReadyCallback, OnInfoWindowClickListener {

    private GoogleMap mMap;
    // Note: Consumer key and secret should be obfuscated in your source code before shipping.
    private static final String TWITTER_KEY = "KxWYzFPPDQMWAvZyvZTV9E1hO";
    private static final String TWITTER_SECRET = "ZFsoz9V8OO3M2RCNY1UNe7IsIc9vr1ZSvBMAvXmyNAXEyMvCqq";

    private ArrayList<Tweet> mTweets;
    private boolean mIsMapReady = false;
    private LatLngBounds.Builder bounds;
    private String twitterUser;
    private ArrayList<NamedLocation> LIST_LOCATIONS;
    DialogFragment dialog_noPoints = new DialogFragmentNoPoints();


    @Override
    public void onInfoWindowClick(Marker marker) {}

    /**  customizing the info window and/or its contents. */
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
        }

        private void render(Marker marker, View view) {
            int badge=0;
            // badge = R.drawable.brooklyn_bridge;
            /*for (int i=0;i<LIST_LOCATIONS.size();i++){ // gets the ImageID corresponding to the marker
                if (marker.getTitle().equals(LIST_LOCATIONS.get(i).name)){ // be sure to use .equals
                    badge = LIST_LOCATIONS.get(i).ImageID;
                }
            }*/
            ImageView imgView = (ImageView) view.findViewById(R.id.badge);
            imgView.setImageResource(badge);
            ScreenResolution screenRes = deviceDimensions();
            imgView.getLayoutParams().width = (screenRes.width)/3;
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

    public void onNewTwitterUser(String newTwitterUser) {
        twitterUser = newTwitterUser;  //the activity simply restarts with the new user, need to find a cleaner method of doing this though
                                        // I want the camera to smoothly animate like the clamping example
                                        // using this for now since, I am stuck on: java.lang.IllegalStateException: Fragment already added:
        Intent i = new Intent(this, Main.class);
        i.putExtra("TwitterUser",twitterUser);
        startActivity(i);
        finish();
        /*twitterUser = newTwitterUser;
        LIST_LOCATIONS.clear(); // resets the list of locations
        Log.d("onNewTwitterUser", twitterUser);

        // Start loading tweets
        mTweets = new ArrayList<>();
        loadTweetList(twitterUser);
        LIST_LOCATIONS = buildListLocations(mTweets);

        addMarkersToMap(LIST_LOCATIONS);
       // clampToTweets(LIST_LOCATIONS);*/
}

    public void loadTweetList(String twitterUser) { // gets a given users tweets and builds an arraylist out of them
        final ArrayList<Tweet> tweets = new ArrayList<>();
        mTweets.clear();
        //twitterUser = getIntent().getExtras().getString("TwitterUser");

        final UserTimeline userTimeline = new UserTimeline.Builder()
                .screenName(twitterUser)
                .build();
        Log.d("twitteruser for Tweet", String.valueOf(twitterUser));
        userTimeline.next(null, new Callback<TimelineResult<Tweet>>() {
            @Override
            public void success(Result<TimelineResult<Tweet>> result) {
                for(Tweet tweet : result.data.items){
                    tweets.add(tweet);
                }
                mTweets = tweets;
                Log.d("Finished Tweet List", String.valueOf(tweets));
                if (checkReady()) {// this is for race condition
                    //loadTweetList(twitterUser);
                    LIST_LOCATIONS = buildListLocations(mTweets);
                    addMarkersToMap(LIST_LOCATIONS);
                }
            }
            @Override
            public void failure(TwitterException exception) {
                exception.printStackTrace();
            }
        });
    }

    private static double getTweetLong (Tweet tweet){
        // be sure to check that tweet.place != null before passing to this function
        return tweet.place.boundingBox.coordinates.get(0).get(0).get(0);
    }
    private static double getTweetLat (Tweet tweet){
        // be sure to check that tweet.place != null before passing to this function
        return tweet.place.boundingBox.coordinates.get(0).get(0).get(1);
    }

    private ArrayList<NamedLocation> buildListLocations(ArrayList<Tweet> tweets){
        final ArrayList<NamedLocation> list_locations = new ArrayList<>();
        //Log.d("Tweet Location?",String.valueOf(mTweets.get(0).place.boundingBox.coordinates.get(0)));
        for (int i = 0; i<tweets.size(); i++){
            if(tweets.get(i).place != null){
                list_locations.add(new NamedLocation(tweets.get(i).place.name,
                        new LatLng(getTweetLat(tweets.get(i)),
                                getTweetLong(tweets.get(i))),
                        0,
                        tweets.get(i).text));
            }
        }
        return list_locations;
    }

    private void addMarkersToMap(ArrayList<NamedLocation> _LIST_LOCATIONS) {
        for (int i = 0; i < _LIST_LOCATIONS.size(); i++) {
            mMap.addMarker(new MarkerOptions()
                    .position(_LIST_LOCATIONS.get(i).location)
                    .title((_LIST_LOCATIONS.get(i).name))
                    .snippet((_LIST_LOCATIONS.get(i).tweetString))
            );
        }
        clampToTweets(_LIST_LOCATIONS);
    }

    private LatLngBounds buildMapBounds(ArrayList<NamedLocation> _LIST_LOCATIONS){
        // learned how to make bounds this way from
        // https://stackoverflow.com/questions/14636118/android-set-goolgemap-bounds-from-from-database-of-points
        //  final LatLngBounds.Builder bounds= new LatLngBounds.Builder();// when map opens, all points are in view
        final LatLngBounds.Builder _bounds= new LatLngBounds.Builder();
        for (int i = 0; i < _LIST_LOCATIONS.size(); i++) {
            Log.d("List locations at",String.valueOf(_LIST_LOCATIONS.get(i).location));
            _bounds.include(_LIST_LOCATIONS.get(i).location);
        }
        Log.d("Returning Bounds",String.valueOf(bounds));
            return _bounds.build();
    }


    public void clampToTweets(ArrayList<NamedLocation> _LIST_LOCATIONS) {
        if (!checkReady() || _LIST_LOCATIONS.isEmpty()) {
            Log.d("Gonna show dialog","");
            dialog_noPoints.show(getSupportFragmentManager(), "No Points in Tweets");
            return;
        }
        final LatLngBounds clampBounds = buildMapBounds(_LIST_LOCATIONS);
        final View mapView = getSupportFragmentManager().findFragmentById(R.id.map).getView();
        if (mapView.getViewTreeObserver().isAlive()) {
            mapView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @SuppressWarnings("deprecation") // We use the new method when supported
                @SuppressLint("NewApi") // We check which build version we are using.
                @Override
                public void onGlobalLayout() {
                    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
                        mapView.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                    } else {
                        mapView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    }
                    try { // i think I want to move this try.catch somewhere else, probably somewhere before map is added
                        mMap.setLatLngBoundsForCameraTarget(clampBounds);
                        mMap.animateCamera(CameraUpdateFactory.newLatLngBounds(clampBounds, 50));
                    } catch (java.lang.IllegalStateException e) {
                        Log.d("noPoints", twitterUser);
                        //dialog_noPoints.show(getSupportFragmentManager(), "No Points in Tweets");
                        /*if(!mDialogAdded) {
                            Log.d("noPoints", twitterUser);
                            dialog_noPoints.show(getSupportFragmentManager(), "No Points in Tweets");
                        }
                        mDialogAdded = true;*/
                    }
                }
            });}
    }

    private class ScreenResolution {
        int width;
        int height;
        public ScreenResolution(int width, int height) {
            this.width = width;
            this.height = height;
        }
    }
    @SuppressLint("NewApi")
    @SuppressWarnings("deprecation")
    ScreenResolution deviceDimensions() {
        int currentapiVersion = android.os.Build.VERSION.SDK_INT;
        // getsize() is available from API 13
        if (currentapiVersion >= android.os.Build.VERSION_CODES.HONEYCOMB_MR2) {
            Display display = getWindowManager().getDefaultDisplay();
            Point size = new Point();
            display.getSize(size);
            return new ScreenResolution(size.x, size.y);
        }
        else {
            Display display = getWindowManager().getDefaultDisplay();
            // getWidth() & getHeight() are deprecated
            return new ScreenResolution(display.getWidth(), display.getHeight());
        }
    }

    /**
     * Before the map is ready many calls will fail.
     * This should be called on all entry points that call methods on the Google Maps API.
     */
    private boolean checkReady() {
        if (mMap == null) {
            //Toast.makeText(this, R.string.map_not_ready, Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TwitterAuthConfig authConfig = new TwitterAuthConfig(TWITTER_KEY, TWITTER_SECRET);
        Fabric.with(this, new Twitter(authConfig));
        setContentView(R.layout.activity_main);

        // Start loading tweets
        mTweets = new ArrayList<>();
        twitterUser = getIntent().getExtras().getString("TwitterUser");
        loadTweetList(twitterUser);
        LIST_LOCATIONS = buildListLocations(mTweets);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);

        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        //mIsMapReady = true;  <-- don't need this anymore, use checkReady() instead
        mMap = googleMap;
        if (!LIST_LOCATIONS.isEmpty()) { // this is for race condition
            addMarkersToMap(LIST_LOCATIONS);
            //moveMap(buildMapBounds(LIST_LOCATIONS()));
       }
        // Setting an info window adapter allows us to change the both the contents and look of the
        // info window.
        mMap.setInfoWindowAdapter(new CustomInfoWindowAdapter());
        //moveMap();
       /*// Set listeners for marker events.  See the bottom of this class for their behavior.
        mMap.setOnMarkerClickListener(this);
        mMap.setOnInfoWindowClickListener(this);
        mMap.setOnMarkerDragListener(this);
        mMap.setOnInfoWindowCloseListener(this);
        mMap.setOnInfoWindowLongClickListener(this);*/

    }


}
