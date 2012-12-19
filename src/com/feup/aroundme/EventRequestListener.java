package com.feup.aroundme;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.facebook.android.FacebookError;
import com.facebook.android.AsyncFacebookRunner.RequestListener;
import com.google.android.maps.MapActivity;

public class EventRequestListener implements RequestListener {
			
		MapActivity map = null;
		
		public EventRequestListener(MapActivity map) {
			this.map = map;
		}
		
		public void onComplete(String response, Object state) {
			try {
				final JSONObject json = new JSONObject(response);
				JSONArray d = json.getJSONArray("data");
	 
				for (int i = 0; i < d.length(); i++) {
					JSONObject event = d.getJSONObject(i);
					Event newEvent = new Event(event.getString("id"),
							event.getString("name"),
							event.getString("start_time"),
							event.getString("location"));
					
					Log.d("events", "Requesting event: " + newEvent.getTitle());
					ShowMapActivity.mAsyncRunner.request(newEvent.getId(), new VenueRequestListener(newEvent, map));					
				}
				
			} catch (JSONException e) {
				Log.w("EventRequestListener", e.getMessage());
			}
		}
	 
		public void onIOException(IOException e, Object state) {
			// TODO Auto-generated method stub
	 
		}				// then post the processed result back to the UI thread
		// if we do not do this, an runtime exception will be generated
		// e.g. "CalledFromWrongThreadException: Only the ore2iginal
		// thread that created a view hierarchy can touch its views."
	 
		public void onFileNotFoundException(FileNotFoundException e,
				Object state) {
			// TODO Auto-generated method stub
	 
		}
	 
		public void onMalformedURLException(MalformedURLException e,
				Object state) {
			// TODO Auto-generated method stub
	 
		}
	 
		public void onFacebookError(FacebookError e, Object state) {
			// TODO Auto-generated method stub
			
		}
	}
