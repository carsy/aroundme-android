package pt.up.fe.aroundme.activities;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import pt.up.fe.aroundme.R;
import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.maps.MapController;

public class MainActivity extends FragmentActivity implements LocationListener {
	private static final int SNAP_ANIMATION_DURATION = 650;

	private static final LatLng FEUP = new LatLng(41.1781788, -8.5947517);

	private GoogleMap map;
	private LocationManager locationManager;
	private String provider;
	private MapController mControl;

	private Location lastKnownUserLocation;
	private Marker userMarker;
	private CameraPosition userCameraPosition;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        map = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map)).getMap();

        LocationManager service = (LocationManager) getSystemService(LOCATION_SERVICE);
        boolean enabledGPS = service
                .isProviderEnabled(LocationManager.GPS_PROVIDER);
        boolean enabledWiFi = service
                .isProviderEnabled(LocationManager.NETWORK_PROVIDER);

		putUserOnMap();
	}

	private void putUserOnMap() { // TODO handle providers availability
		
		locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		// Define the criteria how to select the location provider -> use
		// default
		Criteria criteria = new Criteria();
		provider = locationManager.getBestProvider(criteria, false);
		lastKnownUserLocation = locationManager
.getLastKnownLocation(provider);

		// Initialize the location fields
		if (lastKnownUserLocation != null) {
			onLocationChanged(lastKnownUserLocation);

			userCameraPosition = new CameraPosition.Builder()
					.target(new LatLng(lastKnownUserLocation.getLatitude(),
							lastKnownUserLocation.getLongitude())).zoom(16) // Sets
																		// the
																		// zoom
					.tilt(30) // Sets the tilt of the camera to 30 degrees
					.build();

			this.snapUserPosition();

		} else {
			Log.d("putUserOnMap()", "lastKnownLocation null!");
		}
	}

	/* Request updates at startup */
	@Override
	protected void onResume() {
		super.onResume();
		locationManager.requestLocationUpdates(provider, 400, 1, this);
	}

	/* Remove the locationlistener updates when Activity is paused */
	@Override
	protected void onPause() {
		super.onPause();
		locationManager.removeUpdates(this);
	}

	@Override
	public void onLocationChanged(Location location) {
		double lat = location.getLatitude();
		double lng = location.getLongitude();

		Log.d("onLocationChanged", "Location = (" + lat + "," + lng + ")");

		if (userMarker != null)
			userMarker.remove();

		userMarker = map.addMarker(new MarkerOptions()
				.position(
						new LatLng(lastKnownUserLocation.getLatitude(),
								lastKnownUserLocation.getLongitude()))
				.title("Start")
				.icon(BitmapDescriptorFactory
						.fromResource(R.drawable.location_user)));
	}

	@Override
	public void onProviderDisabled(String provider) {
		Toast.makeText(this, "Enabled new provider " + provider,
				Toast.LENGTH_SHORT).show();

	}

	@Override
	public void onProviderEnabled(String provider) {
		Toast.makeText(this, "Disabled provider " + provider,
				Toast.LENGTH_SHORT).show();
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.menu_snap_user_position:
			snapUserPosition();
			return true;
		case R.id.menu_refresh:
			menuRefresh();
			return true;
		case R.id.menu_settings:
			menuSettings();
			return true;
		case R.id.menu_about:
			menuAbout();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	private void snapUserPosition() {
		map.animateCamera(
				CameraUpdateFactory.newCameraPosition(userCameraPosition),
				MainActivity.SNAP_ANIMATION_DURATION, null);
	}

	private void menuRefresh() {
		new RequestTask()
				.execute("http://around-me.herokuapp.com/landmarks.json");
	}

	private void menuSettings() {
		// TODO Auto-generated method stub

	}

	private void menuAbout() {
		// TODO Auto-generated method stub

	}

	class RequestTask extends AsyncTask<String, String, String> {

		@Override
		protected String doInBackground(String... uri) {
			HttpClient httpclient = new DefaultHttpClient();
			HttpResponse response;
			String responseString = null;
			try {
				response = httpclient.execute(new HttpGet(uri[0]));
				StatusLine statusLine = response.getStatusLine();
				if (statusLine.getStatusCode() == HttpStatus.SC_OK) {
					ByteArrayOutputStream out = new ByteArrayOutputStream();
					response.getEntity().writeTo(out);
					out.close();
					responseString = out.toString();
				} else {
					// Closes the connection.
					response.getEntity().getContent().close();
					throw new IOException(statusLine.getReasonPhrase());
				}
			} catch (ClientProtocolException e) {
				// TODO Handle problems..
			} catch (IOException e) {
				// TODO Handle problems..
			}
			return responseString;
		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);

			Toast.makeText(getApplicationContext(), result, Toast.LENGTH_SHORT)
					.show();
			Log.d("response", result);
		}
	}
}
