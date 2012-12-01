package com.feup.aroundme;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Iterator;

import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.facebook.android.FacebookError;
import com.facebook.android.AsyncFacebookRunner.RequestListener;
import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.OverlayItem;

public class VenueRequestListener implements RequestListener {
	
	boolean markerFound = false;
	MapActivity map = null;
	Event event = null;
	
	public VenueRequestListener(Event wE, MapActivity map) {
		this.map = map;
		this.event = wE;
		Log.w("venues", "Processing VenueRequesListener " + event.getTitle());
	}

	@Override
	public void onComplete(String response, Object state) {
		JSONObject jsonData;
		try {
			jsonData = new JSONObject(response);
			JSONObject venues = new JSONObject(jsonData.getString("venue"));
			event.setVenueID(venues.getString("id"));
			
			synchronized(ShowMapActivity.markers) {
				Iterator<Marker> i = ShowMapActivity.markers.iterator();
				//Log.w("venues","Processing VenueRequesListener Marker: " + ((Marker) i).getVenue());
				while (i.hasNext()) {
						Marker m = i.next(); 
						Log.w("events","m.getVenueID:" + m.getVenueID() + " , event.getVenue:" + event.getVenueID());
						if (m.getVenueID().equals(event.getVenueID())) { // TODO this never happens
							markerFound = true;
							m.addEvent(event);
							m.titleToVenue(); // since we have more than one event, we change the display name of the marker
							Log.w("events","added event:" + event.getTitle());
							Log.w("events","existing events:" + m.getEventString());
							break;
						}
					}
				if (!markerFound)
					ShowMapActivity.addMarker(event, map);
				} 
			
			renderMarkers(); // render one at a time?
			
		} catch (JSONException e) {
			e.printStackTrace();
		}	
	}

	@Override
	public void onIOException(IOException e, Object state) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onFileNotFoundException(FileNotFoundException e, Object state) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onMalformedURLException(MalformedURLException e, Object state) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onFacebookError(FacebookError e, Object state) {
		// TODO Auto-generated method stub

	}
	
private void renderMarkers() {
		
	    synchronized (ShowMapActivity.markers) {
	    	Iterator<Marker> i = ShowMapActivity.markers.iterator();
			while (i.hasNext()) {
				Marker m = i.next();
				GeoPoint point = new GeoPoint((int) (m.getLat()* 1E6), (int) (m.getLog()* 1E6));
				
				// TODO transform eventlist in a full link w/ description and whatnot
				String eventList = "";
				for (Event e: m.getEvents())
					eventList = eventList + e.getTitle() + ":" + e.getStartTime() + "\n";
				
				Log.w("markers", "muitos renderMarkers: adding marker2: " + m.getTitle() + "  with events: \n " + eventList);
				//Log.w("markers", "muitos renderMarkers size: " + Integer.toString(mapOverlays.size()));
				OverlayItem overlayitem = new OverlayItem(point, m.getTitle(), eventList);
				ShowMapActivity.itemizedoverlay.addOverlay(overlayitem);
				
				GeoPoint point2 = new GeoPoint(35410000, 139460000);
				OverlayItem overlayitem2 = new OverlayItem(point2, "Sekai, konichiwa!", "I'm in Japan!");
				ShowMapActivity.itemizedoverlay.addOverlay(overlayitem2);
				
				ShowMapActivity.mapView.getOverlays().clear();
				
				ShowMapActivity.mapView.getOverlays().add(ShowMapActivity.itemizedoverlay);
				ShowMapActivity.mapView.postInvalidate();
				//mapView.getController.animate(center point)  
				//mapOverlays.add(itemizedoverlay);
			}
	    }
	}

}
