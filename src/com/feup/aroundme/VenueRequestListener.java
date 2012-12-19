package com.feup.aroundme;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.facebook.android.FacebookError;
import com.facebook.android.AsyncFacebookRunner.RequestListener;
import com.feup.aroundme.markers.CustomOverlayItem;
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
				while (i.hasNext()) {
						Marker m = i.next(); 
						if (m.getVenueID().equals(event.getVenueID())) { 
							markerFound = true;
							m.addEvent(event);
							m.titleToVenue(); // since we have more than one event, we change the display name of the marker
							//Log.w("events","added event:" + event.getTitle());
							//Log.w("events","existing events:" + m.getEventString());
							break;
						}
						else {
							Log.w("events","venueRequestListener not equal:" );
						}
					}
				if (!markerFound)
					Log.w("events","adding Marker for venue:" + event.getVenueID() + " markers.size()" + ShowMapActivity.markers.size() );
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
	
public void renderMarkers() {
	
	// TODO transform this to a function
	// flat markers
	Map<String, ArrayList<Marker>> map = new HashMap<String, ArrayList<Marker>>();
	
	synchronized(ShowMapActivity.markers) {
		Iterator<Marker> i = ShowMapActivity.markers.iterator();
		while (i.hasNext()) {
				Marker m = i.next(); 
				if (map.containsKey(m.getVenueID()))
					map.get(m.getVenueID()).add(m); // Venue X -> Marker1, Marker2
				else {
					ArrayList<Marker> markers = new ArrayList<Marker>();
					markers.add(m);
					map.put(m.getVenueID(), markers); // Venue X -> Marker
				}
			}
	}
	
	List<Marker> finalMarkers = Collections.synchronizedList(new ArrayList()); 
	Collection<ArrayList<Marker>> allMarkers = map.values();
	for (ArrayList<Marker> lMarker: allMarkers) {

		ArrayList<Event> events = new ArrayList<Event>();
		for (Marker m: lMarker)
			events.addAll(m.getEvents());
		
		Marker m = lMarker.get(0);
		m.setEvents(events);
		finalMarkers.add(m);
	}
	
	ShowMapActivity.markers = finalMarkers;
	// end flatting markers
		
	    synchronized (ShowMapActivity.markers) {
	    	Iterator<Marker> i = ShowMapActivity.markers.iterator();
	    	Log.w("markers", "markers: " + ShowMapActivity.markers.size());
			while (i.hasNext()) {
				Marker m = i.next();
				GeoPoint point = new GeoPoint((int) (m.getLat()* 1E6), (int) (m.getLog()* 1E6));
				
				// TODO transform eventList in a full link w/ description
				Event []events = new Event[m.getEvents().size()];
				m.getEvents().toArray(events);
				
				String eventList = "";
				for (Event e: events) {
					eventList += e.getTitle() + " - " + e.getStartTime() + "\n";
					Log.w("markers", "eventList: " + e.getTitle());
				}
				
				Log.w("markers", "renderMarkers" + m.getTitle() + "  with events: \n " + eventList);
				CustomOverlayItem overlayitem = new CustomOverlayItem(point, m.getTitle(), eventList, m.getEvents());
				
				ShowMapActivity.itemizedoverlay.addOverlay(overlayitem);
							
				GeoPoint point2 = new GeoPoint((int)(51.5174723*1E6),(int)(-0.0899537*1E6));
				CustomOverlayItem overlayItem = new CustomOverlayItem(point2, "Tomorrow Never Dies (1997)",
				"(M gives Bond his mission in Daimler car)",
				m.getEvents());
				ShowMapActivity.itemizedoverlay.addOverlay(overlayItem);
				
				ShowMapActivity.mapView.getOverlays().clear();
				ShowMapActivity.mapView.getOverlays().add(ShowMapActivity.itemizedoverlay);
				ShowMapActivity.mapView.postInvalidate();
				
				//final MapController mc = mapView.getController();
				//mc.animateTo(point2);
				//mc.setZoom(16);
				//mapView.getController.animate(center point)  
				//mapOverlays.add(itemizedoverlay);
			}
	    }
	}

}
