package com.feup.aroundme;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.facebook.android.AsyncFacebookRunner;
import com.facebook.android.Facebook;
import com.feup.aroundme.markers.CustomItemizedOverlay;
import com.feup.aroundme.markers.CustomOverlayItem;
import com.feup.aroundme.other.DownloadEventTask;
import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;

public class ShowMapActivity extends MapActivity {

	public static final String APP_ID = "your_id_here";
	public static final String TAG = "FACEBOOK CONNECT";
	private static final String[] PERMS = new String[] { "user_events" };
	private static final String[] PAGES = new String[] { "me/events",
			"me/events/not_replied", "casadamusica/events",
			"ColiseuPorto/events", "fundacaoserralves/events" };
	private Facebook mFacebook;
	public static AsyncFacebookRunner mAsyncRunner;

	public static List<Marker> markers = Collections
			.synchronizedList(new ArrayList());

	// Google Maps
	public static MapView mapView = null;
	List<Overlay> mapOverlays;
	Drawable drawable;
	public static CustomItemizedOverlay<CustomOverlayItem> itemizedoverlay;

	private SharedPreferences mPrefs;
	Random randomGenerator = new Random();

	MapController mc;

	@Override
	protected boolean isRouteDisplayed() {
		return false;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_show_map);

		mapView = (MapView) findViewById(R.id.mapview);

		mc = mapView.getController();

		locat();

		mc.setZoom(15);
		mapView.invalidate();

		mapView = (MapView) findViewById(R.id.mapview);
		mapView.setBuiltInZoomControls(true);
		mapOverlays = mapView.getOverlays();

		drawable = this.getResources().getDrawable(R.drawable.marker);
		itemizedoverlay = new CustomItemizedOverlay<CustomOverlayItem>(
				drawable, mapView);

		mapOverlays.add(itemizedoverlay);

		// TODO get current location and center map

		// TODO move this to a class / static members // Initialize Facebook
		mFacebook = new Facebook(APP_ID);
		mAsyncRunner = new AsyncFacebookRunner(mFacebook);

		mPrefs = this.getSharedPreferences("appPrefs", MODE_WORLD_READABLE);
		String access_token = mPrefs.getString("access_token", null);
		long expires = mPrefs.getLong("access_expires", 0);
		if (access_token != null) {
			mFacebook.setAccessToken(access_token);
			Log.d(TAG, "Acess token is: " + access_token);
		} else {
			Log.d(TAG, "No access token");
		}
		if (expires != 0) {
			mFacebook.setAccessExpires(expires);
		}

		DownloadEventTask task = new DownloadEventTask();
		task.setMap(this);
		task.execute(PAGES);
		// Check if FB is on
		// Check if Internet on
		// Check if data stored sql
	}

	/*
	 * Add a new marker to the markers list
	 */
	public static void addMarker(Event e2, MapActivity map) {

		GeoLocationRequestListener geoListener = new GeoLocationRequestListener(
				e2);
		mAsyncRunner.request(e2.getVenueID(), geoListener);
	}

	public void locat() {
		// Use the LocationManager class to obtain GPS locations

		LocationManager mlocManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

		LocationListener mlocListener = new MyLocationListener();

		mlocManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0,
				0, mlocListener);
	}

	// Class My Location Listener

	public class MyLocationListener implements LocationListener
	{

		@Override
		public void onLocationChanged(Location loc) {
			GeoPoint initialPoint = new GeoPoint(
					(int) (loc.getLatitude() * 1E6),
					(int) (loc.getLongitude() * 1E6));

			mc.animateTo(initialPoint);
			mc.setZoom(15);

			String Text = "My current location is: " + "Latitude = "
					+ loc.getLatitude() + "Longitude = " + loc.getLongitude();

			Toast.makeText(getApplicationContext(), Text, Toast.LENGTH_SHORT)
					.show();
		}

		@Override
		public void onProviderDisabled(String provider)
		{
			Toast.makeText(getApplicationContext(), "WIFI Disabled",
					Toast.LENGTH_SHORT).show();
		}

		@Override
		public void onProviderEnabled(String provider)
		{
			Toast.makeText(getApplicationContext(), "WIFI Enabled",
					Toast.LENGTH_SHORT).show();
		}

		@Override
		public void onStatusChanged(String provider, int status, Bundle extras)
		{
		}

	}// End of Class MyLocationListener */

} // end class
