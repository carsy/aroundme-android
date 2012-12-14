package com.feup.aroundme.other;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.feup.aroundme.Event;
import com.feup.aroundme.Marker;
import com.feup.aroundme.ShowMapActivity;
import com.feup.aroundme.VenueRequestListener;
import com.feup.aroundme.markers.CustomOverlayItem;
import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapView;

import android.os.AsyncTask;
import android.util.Log;

public class DownloadEventTask extends AsyncTask<String, Integer, Void>{

	MapActivity m = null;
	
	public void setMap(MapActivity mapView) {
		m = mapView;
	}
	
	@Override
	protected void onPreExecute() {

	}

	@Override
	protected void onCancelled() {
	}

	@Override
	protected void onProgressUpdate(Integer... values) {

	}

	@Override
	protected void onPostExecute(Void result) {
		Log.v("Progress", "Finished");

		// Flatten markers
		/*
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
		
		List<Marker> finalMarkers = new ArrayList<Marker>();
		Collection<ArrayList<Marker>> allMarkers = map.values();
		for (ArrayList<Marker> lMarker: allMarkers) {

			ArrayList<Event> events = new ArrayList<Event>();
			for (Marker m: lMarker)
				events.addAll(m.getEvents());
			
			Marker m = lMarker.get(0);
			m.setEvents(events);
			finalMarkers.add(m);
		}
		
		renderMarkers(finalMarkers);
		*/
	}

	@Override
	protected Void doInBackground(String... params) {
		for(String s: params) {
    		ShowMapActivity.mAsyncRunner.request(s, new com.feup.aroundme.EventRequestListener(m));
	    }
		return null;
	}
	
private void renderMarkers(List<Marker> finalMarkers) {
		
    	Iterator<Marker> i = finalMarkers.iterator();
    	Log.w("markers", "finalMarkers: " + finalMarkers.size());
		while (i.hasNext()) {
			Marker m = i.next();
			GeoPoint point = new GeoPoint((int) (m.getLat()* 1E6), (int) (m.getLog()* 1E6));
			
			// TODO transform eventlist in a full link w/ description and whatnot
			String eventList = "teste\n t2";
			for (Event e: m.getEvents()) {
				eventList += e.getTitle() + ";";
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