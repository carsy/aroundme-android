package com.feup.aroundme;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import ca.odell.glazedlists.AbstractEventList;
import ca.odell.glazedlists.BasicEventList;
import ca.odell.glazedlists.EventList;
import ca.odell.glazedlists.event.ListEvent;
import ca.odell.glazedlists.event.ListEventListener;

import com.facebook.android.AsyncFacebookRunner;
import com.facebook.android.AsyncFacebookRunner.RequestListener;
import com.facebook.android.Facebook;
import com.facebook.android.FacebookError;
import com.feup.aroundme.R;
import com.feup.aroundme.markers.CustomItemizedOverlay;
import com.feup.aroundme.markers.CustomOverlayItem;
import com.feup.aroundme.other.DownloadEventTask;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import com.google.android.maps.OverlayItem;

@SuppressWarnings("deprecation")
public class ShowMapActivity extends MapActivity {
	
	public static final String APP_ID = "your_id_here"; 
	public static final String TAG = "FACEBOOK CONNECT";
	private static final String[] PERMS = new String[] { "user_events" };
	private static final String[] PAGES = new String[] { "me/events", "me/events/not_replied", "casadamusica/events", "ColiseuPorto/events", "fundacaoserralves/events" };
	private Facebook mFacebook;
	public static AsyncFacebookRunner mAsyncRunner;

	public static List<Marker> markers = Collections.synchronizedList(new ArrayList()); 
	
	// Google Maps
	public static MapView mapView = null;
	List<Overlay> mapOverlays;
	Drawable drawable;
	public static CustomItemizedOverlay<CustomOverlayItem> itemizedoverlay;

	private SharedPreferences mPrefs;
	Random randomGenerator = new Random();
	
	@Override
	protected boolean isRouteDisplayed() {
	    return false;
	}

	@SuppressWarnings("deprecation")
	@Override
	public void onCreate(Bundle savedInstanceState) {
			
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.activity_show_map);
	    mapView = (MapView) findViewById(R.id.mapview);
	    mapView.setBuiltInZoomControls(true);
	    mapOverlays = mapView.getOverlays();
	    
	    drawable = this.getResources().getDrawable(R.drawable.marker);
	    itemizedoverlay = new CustomItemizedOverlay<CustomOverlayItem>(drawable, mapView);
	    
	    mapOverlays.add(itemizedoverlay);
	    
	    // TODO get current location and center map
	    
	    // TODO move this to a class / static members
	    // Initialize Facebook session
	    mFacebook = new Facebook(APP_ID);
	    mAsyncRunner = new AsyncFacebookRunner(mFacebook);
	    
	    mPrefs = this.getSharedPreferences("appPrefs", MODE_WORLD_READABLE);
        String access_token = mPrefs.getString("access_token", null);
        long expires = mPrefs.getLong("access_expires", 0);
        if(access_token != null) {
        	mFacebook.setAccessToken(access_token);
        	Log.d(TAG, "Acess token is: " + access_token);
        }
        else {Log.d(TAG, "No access token");}
        if(expires != 0) {
        	mFacebook.setAccessExpires(expires);
        }
	   	 
		DownloadEventTask task = new DownloadEventTask();
		task.setMap(this);
		task.execute(PAGES);
	    // Check if FB is on
	    // Check if Internet on
	    // Check if data stored sql
	}
		
	/**
	 * Add a new marker to the markers list
	 */
	public static void addMarker(Event e2, MapActivity map) {
		
		GeoLocationRequestListener geoListener = new GeoLocationRequestListener(e2);
		mAsyncRunner.request(e2.getVenueID(), geoListener);
	}
	


} // end class
