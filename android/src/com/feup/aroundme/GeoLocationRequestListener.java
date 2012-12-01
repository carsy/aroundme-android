package com.feup.aroundme;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;

import org.json.JSONException;
import org.json.JSONObject;

import com.facebook.android.AsyncFacebookRunner.RequestListener;
import com.facebook.android.FacebookError;
import com.google.android.maps.GeoPoint;

public class GeoLocationRequestListener implements RequestListener {

	@Override
	public void onComplete(String r, Object state) {
		try {
			JSONObject json = new JSONObject(r);
			JSONObject json2 = new JSONObject(json.getString("location"));
			
			synchronized (ShowMapActivity.markers) {
				ShowMapActivity.markers.add(new Marker(Double.parseDouble(json2.getString("latitude")), 
						Double.parseDouble(json2.getString("longitude")),
						json.getString("name"),
						json.getString("name"), // nameo of the venue
						json.getString("id")));
			}
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

}
