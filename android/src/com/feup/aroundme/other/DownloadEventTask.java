package com.feup.aroundme.other;

import com.feup.aroundme.ShowMapActivity;
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
	}

	@Override
	protected Void doInBackground(String... params) {
		// TODO Auto-generated method stub
		for(String s: params) {
    		ShowMapActivity.mAsyncRunner.request(s, new com.feup.aroundme.EventRequestListener(m));
	    }
		return null;
	}
	
}