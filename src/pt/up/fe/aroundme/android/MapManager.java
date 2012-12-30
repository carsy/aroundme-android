package pt.up.fe.aroundme.android;

import pt.up.fe.aroundme.R;
import pt.up.fe.aroundme.android.activities.MapActivity;
import pt.up.fe.aroundme.controllers.AroundMeController;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.location.LocationManager;
import android.preference.PreferenceManager;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

public class MapManager {

	private static final int DEFAULT_RADIUS = 25;
	private static final int SNAP_ANIMATION_DURATION = 650;

	private final MapActivity mapActivity;

	private final SharedPreferences sharedPreferences;
	private final GoogleMap map;
	private final UserLocationManager userlocationManager;
	private final AroundMeController aroundmeController;

	private Integer radiusValue = DEFAULT_RADIUS;
	private CameraPosition userCameraPosition;
	private Polyline radiusPolyline;
	private final SparseArray<MarkerOptions> loadedMarkers;

	public MapManager(final MapActivity mapActivity) {
		this.mapActivity = mapActivity;

		this.map =
				((SupportMapFragment) this.getMapActivity()
						.getSupportFragmentManager().findFragmentById(R.id.map))
						.getMap();
		this.userlocationManager =
				new UserLocationManager(this, (LocationManager) this
						.getMapActivity().getSystemService(
								Context.LOCATION_SERVICE));
		this.aroundmeController = new AroundMeController(this);

		this.sharedPreferences =
				PreferenceManager.getDefaultSharedPreferences(this
						.getMapActivity().getApplicationContext());
		this.loadedMarkers = new SparseArray<MarkerOptions>();

		this.update();
	}

	// MAP UPDATE METHODS

	public void update() { // order of updates IS RELEVANT
		this.map.clear();
		this.updateUserMarker();
		this.updateRadius();
		this.updateLandmarksMarkers();
	}

	public void updateUserMarker() {
		Log.d("updateUserMarker()", "Location = ("
				+ this.userlocationManager.getLatitude() + ","
				+ this.userlocationManager.getLongitude() + ")");

		this.map.addMarker(new MarkerOptions().position(
				new LatLng(this.userlocationManager.getLatitude(),
						this.userlocationManager.getLongitude())).title("You")
				.icon(BitmapDescriptorFactory
						.fromResource(R.drawable.location_user)));
	}

	public void updateLandmarksMarkers() {
		Log.d("updateLandmarksMarkers()", this.loadedMarkers.size() + "");
		this.aroundmeController.refreshLandmarks(this.userlocationManager
				.getLocation(), this.radiusValue);
	}

	public void updateRadius() {
		this.radiusValue =
				this.sharedPreferences.getInt("radius",
						R.integer.radius_default);
		Log.d("updateRadius()", this.radiusValue + "");

		// FIXME refactor radius_checkbox_key string
		if( !this.sharedPreferences.getBoolean("radius_checkbox_key", true) ) { return; }

		final PolylineOptions options = new PolylineOptions();

		// FIXME refactor: create class with this method as static...dunno...
		final double R = 6371d; // earth's mean radius in km
		final double d = this.radiusValue / R; // radius given in km
		final double lat1 =
				Math.toRadians(this.userlocationManager.getLatitude());
		final double lon1 =
				Math.toRadians(this.userlocationManager.getLongitude());

		for(int x = 0; x <= 720; x++) {
			final double brng = Math.toRadians(x);
			final double latitudeRad =
					Math.asin(Math.sin(lat1) * Math.cos(d) + Math.cos(lat1)
							* Math.sin(d) * Math.cos(brng));
			final double longitudeRad =
					(lon1 + Math.atan2(Math.sin(brng) * Math.sin(d)
							* Math.cos(lat1), Math.cos(d) - Math.sin(lat1)
							* Math.sin(latitudeRad)));
			options.add(new LatLng(Math.toDegrees(latitudeRad), Math
					.toDegrees(longitudeRad)));
		}

		if( this.radiusPolyline != null ) {
			this.radiusPolyline.remove();
		}

		this.radiusPolyline =
				this.map.addPolyline(options.color(Color.LTGRAY).width(2));
	}

	// Buttons event handlers

	public void snapUsersPosition(final View view) {
		if( this.userCameraPosition == null ) {
			this.userCameraPosition =
					new CameraPosition.Builder().target(
							new LatLng(this.userlocationManager.getLatitude(),
									this.userlocationManager.getLongitude()))
							.zoom(10).tilt(30).build();
		}

		this.map.animateCamera(CameraUpdateFactory
				.newCameraPosition(this.userCameraPosition),
				MapManager.SNAP_ANIMATION_DURATION, null);
	}

	public void toggleMapType(final View view) {
		this.map.setMapType(this.map.getMapType() == GoogleMap.MAP_TYPE_NORMAL
				? GoogleMap.MAP_TYPE_HYBRID : GoogleMap.MAP_TYPE_NORMAL);
	}

	// Callback for controllers
	public void addMarker(final Integer markerId, final Double latitude,
			final Double longitude, final String markerTitle) {

		final MarkerOptions markerOptions =
				new MarkerOptions().position(new LatLng(latitude, longitude))
						.title(markerTitle)
						.icon(BitmapDescriptorFactory
								.fromResource(R.drawable.location_place));
		this.loadedMarkers.put(markerId, markerOptions);
		this.map.addMarker(markerOptions);
	}

	// Getters

	public Integer getRadiusValue() {
		return this.radiusValue;
	}

	public void requestLocationUpdates() {
		this.userlocationManager.requestLocationUpdates();
	}

	public void removeUpdates() {
		this.userlocationManager.removeUpdates();
	}

	public int[] getLoadedLandmarksIds() {
		final int[] landmarksId = new int[this.loadedMarkers.size()];
		final int loadedMarkersSize = this.loadedMarkers.size();
		int i = 0;

		for(; i < loadedMarkersSize; i++) {
			if( this.loadedMarkers.keyAt(i) != 0 ) {
				landmarksId[i] = this.loadedMarkers.keyAt(i);
			}
		}

		Log.d("checks", "size = " + loadedMarkersSize + " | i = " + --i);

		return landmarksId;
	}

	public MapActivity getMapActivity() {
		return this.mapActivity;
	}

	public void removeMarker(final Integer markerId) {
		this.loadedMarkers.remove(markerId);
	}

}
