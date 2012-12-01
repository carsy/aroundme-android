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
	// TODO add other events that user was invited
	private static final String[] PAGES = new String[] { "me/events", "me/events/not_replied", "casadamusica/events" };
	private Facebook mFacebook;
	public static AsyncFacebookRunner mAsyncRunner;

	//static List<Event> events = Collections.synchronizedList(new ArrayList()); 
	static List<Marker> markers = Collections.synchronizedList(new ArrayList()); 
	
	// Google Maps
	public static MapView mapView = null;
	List<Overlay> mapOverlays;
	Drawable drawable;
	static MapItemOverlay itemizedoverlay;

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
	    
	    drawable = this.getResources().getDrawable(R.drawable.marker); // TODO renamethis
	    itemizedoverlay = new MapItemOverlay(drawable, this);
	    
	    GeoPoint point = new GeoPoint(19240000,-99120000);
	    OverlayItem overlayitem = new OverlayItem(point, "Hola, Mundo!", "I'm in Mexico City!");
	    itemizedoverlay.addOverlay(overlayitem);
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
	
	private static void renderMarker(Marker m, MapActivity map) {
		
        	List<Overlay> mapOverlays = mapView.getOverlays();
		    Drawable drawable = map.getResources().getDrawable(R.drawable.marker);
		    
		    MapItemOverlay itemizedoverlay = new MapItemOverlay(drawable, map);
		    
			GeoPoint point = new GeoPoint((int) (m.getLat()* 1E6), (int) (m.getLog()* 1E6));
			
			// TODO transform eventlist in a full link w/ description and whatnot
			String eventList = "";
			for (Event e: m.getEvents()) // TODO possible exception
				eventList = eventList + e.getTitle() + ":" + e.getStartTime() + "\n";
			
			Log.w("markers", "solo renderMarkers: adding marker: " + m.getTitle() + "  with events: \n " + eventList);
			OverlayItem overlayitem = new OverlayItem(point, m.getTitle(), eventList);
			itemizedoverlay.addOverlay(overlayitem);
			mapOverlays.add(itemizedoverlay);
	}
	
	// TODO render only when changed
	// Renders every marker in the map
	
	
	
	/**
	 * Add a new marker to the markers list
	 */
	public static void addMarker(Event e2, MapActivity map) {
		
		mAsyncRunner.request(e2.getVenueID(), new GeoLocationRequestListener());
		
		// Rename marker display title to the name of the event
		synchronized (markers) {
			Iterator<Marker> i = ShowMapActivity.markers.iterator();
			while (i.hasNext()) {
				Marker m = i.next();
				if (m.getId().equals(e2.getVenueID())) {
						m.setTitle(e2.getTitle());
				}
			}
		}	
	}
	


} // end class
