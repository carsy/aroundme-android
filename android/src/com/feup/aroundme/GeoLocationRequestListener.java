package com.feup.aroundme;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Iterator;

import org.json.JSONException;
import org.json.JSONObject;

import com.facebook.android.AsyncFacebookRunner.RequestListener;
import com.facebook.android.FacebookError;
import com.google.android.maps.GeoPoint;

public class GeoLocationRequestListener implements RequestListener {

	Event e = null;
	
	GeoLocationRequestListener(Event ev) {
		e = ev;
	}
	
	@Override
	public void onComplete(String r, Object state) {
		try {
			JSONObject json = new JSONObject(r);
			JSONObject json2 = new JSONObject(json.getString("location"));
			
			synchronized (ShowMapActivity.markers) {
				Marker m = new Marker(Double.parseDouble(json2.getString("latitude")), 
						Double.parseDouble(json2.getString("longitude")),
						json.getString("name"),
						e.getTitle(),
						json.getString("id"));
				ShowMapActivity.markers.add(m);
				
				// with e.getTitle we rename marker to 
				// display the name of the event
				// in the beginning a marker is a single event
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
	}

	public Event getE() {
		return e;
	}

	public void setE(Event e) {
		this.e = e;
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

}
