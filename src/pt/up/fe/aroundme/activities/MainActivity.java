package pt.up.fe.aroundme.activities;

import pt.up.fe.aroundme.R;
import pt.up.fe.aroundme.models.Landmark;
import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnCameraChangeListener;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class MainActivity extends FragmentActivity implements LocationListener {
	private static final int SNAP_ANIMATION_DURATION = 650;

	private static final LatLng FEUP = new LatLng(41.1781788, -8.5947517);

	private GoogleMap map;
	private LocationManager locationManager;
	private String provider;

	private Location lastUserLocation;
	private Integer radius = 100; // TODO
	private Marker userMarker;

	private CameraPosition lastCameraPosition; // TODO
	private CameraPosition userCameraPosition;

	private AroundMeController aroundmeController;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		this.aroundmeController = new AroundMeController(this);
		this.map = ((SupportMapFragment) getSupportFragmentManager()
				.findFragmentById(R.id.map)).getMap();
		// this.map.setOnCameraChangeListener(this.onCameraChange());

		this.putUserOnMap();

		Log.d("MainActivity", "onCreate()");
	}

	/* Request updates at startup */
	@Override
	protected void onResume() {
		super.onResume();
		locationManager.requestLocationUpdates(provider, 400, 1, this);
		Log.d("MainActivity", "onResume()");
	}

	/* Remove the locationlistener updates when Activity is paused */
	@Override
	protected void onPause() {
		super.onPause();
		locationManager.removeUpdates(this);

		Log.d("MainActivity", "onPause()");
	}

	@Override
	public void onLocationChanged(Location location) {
		this.lastUserLocation = new Location(location);
		this.updateUserMarker();
	}

	@Override
	public void onProviderDisabled(String provider) {
		Toast.makeText(this, "Disabled new provider " + provider,
				Toast.LENGTH_SHORT).show();

	}

	@Override
	public void onProviderEnabled(String provider) {
		Toast.makeText(this, "Enabled provider " + provider, Toast.LENGTH_SHORT)
				.show();
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO handle providers availability
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
			snapUsersPosition(this.getCurrentFocus());
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

	private void menuRefresh() {
		this.aroundmeController.refreshLandmarks(this.lastUserLocation,
				this.radius);
	}

	private void menuSettings() {
		// TODO Auto-generated method stub

	}

	private void menuAbout() {
		// TODO Auto-generated method stub

	}

	private OnCameraChangeListener onCameraChange() {
		return new OnCameraChangeListener() {

			@Override
			public void onCameraChange(CameraPosition position) {
			}
		};
	}

	private void putUserOnMap() { // TODO handle providers availability

		this.locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		// Define the criteria how to select the location provider -> use
		// default
		Criteria criteria = new Criteria();
		this.provider = this.locationManager.getBestProvider(criteria, false);
		this.lastUserLocation = this.locationManager
				.getLastKnownLocation(this.provider);

		// Initialize the location fields
		if (this.lastUserLocation != null) {
			this.updateUserMarker();

			this.lastCameraPosition = this.userCameraPosition;

			this.snapUsersPosition(this.getCurrentFocus());

		} else {
			Log.d("putUserOnMap()", "lastKnownLocation null!");
		}
	}

	private void updateUserMarker() {
		Log.d("updateUserMarker()",
				"Location = (" + this.lastUserLocation.getLatitude() + ","
						+ this.lastUserLocation.getLongitude() + ")");

		if (this.userMarker != null)
			this.userMarker.remove();

		this.userMarker = this.map.addMarker(new MarkerOptions()
				.position(
						new LatLng(this.lastUserLocation.getLatitude(),
								this.lastUserLocation.getLongitude()))
				.title("you")
				.icon(BitmapDescriptorFactory
						.fromResource(R.drawable.location_user)));
	}

	public void snapUsersPosition(View view) {
		if (this.userCameraPosition == null) {
			this.userCameraPosition = new CameraPosition.Builder()
					.target(new LatLng(lastUserLocation.getLatitude(),
							lastUserLocation.getLongitude())).zoom(16).tilt(30)
					.build();
		}

		this.map.animateCamera(
				CameraUpdateFactory.newCameraPosition(this.userCameraPosition),
				MainActivity.SNAP_ANIMATION_DURATION, null);
	}

	public void addLandmarkMarker(Landmark landmark) {
		this.map.addMarker(new MarkerOptions()
				.position(
						new LatLng(landmark.getLocationLatitude(), landmark
								.getLocationLongitude()))
				.title(landmark.getName())
				.icon(BitmapDescriptorFactory
						.fromResource(R.drawable.location_place)));
	}
}
