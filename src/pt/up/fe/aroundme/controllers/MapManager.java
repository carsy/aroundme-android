package pt.up.fe.aroundme.controllers;

import pt.up.fe.aroundme.R;
import pt.up.fe.aroundme.activities.MapActivity;
import pt.up.fe.aroundme.models.Landmark;
import android.content.Context;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationManager;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

public class MapManager {

	private static final int DEFAULT_RADIUS = 25;
	private static final int SNAP_ANIMATION_DURATION = 650;

	private Integer radiusValue = DEFAULT_RADIUS;
	private Marker userMarker;
	private CameraPosition userCameraPosition;
	private Polyline radiusPolyline;

	private final MapActivity parentActivity;
	private final GoogleMap map;

	private final UserLocationManager userlocationManager;
	private final AroundMeController aroundmeController;

	public MapManager(MapActivity mainActivity, GoogleMap map) {
		this.map = map;
		this.parentActivity = mainActivity;

		this.aroundmeController = new AroundMeController(this);
		this.userlocationManager = new UserLocationManager(this,
				(LocationManager) this.parentActivity
						.getSystemService(Context.LOCATION_SERVICE));

	}

	// MAP UPDATE METHODS

	public void update() {
		this.map.clear();
		this.updateUserMarker();
		this.updateRadius();
		this.updateLandmarksMarkers();
	}

	public void updateUserMarker() {
		Log.d("updateUserMarker()",
				"Location = (" + this.userlocationManager.getLatitude() + ","
						+ this.userlocationManager.getLongitude() + ")");

		if (this.userMarker != null)
			this.userMarker.remove();

		this.userMarker = this.map.addMarker(new MarkerOptions()
				.position(
						new LatLng(this.userlocationManager.getLatitude(),
								this.userlocationManager.getLongitude()))
				.title("You")
				.icon(BitmapDescriptorFactory
						.fromResource(R.drawable.location_user)));
	}

	public void updateLandmarksMarkers() {
		this.aroundmeController.refreshLandmarks(
				this.userlocationManager.getLocation(), this.radiusValue);
	}

	public void updateRadius() {

		this.radiusValue = PreferenceManager.getDefaultSharedPreferences(
				this.parentActivity.getApplicationContext()).getInt("radius",
				R.integer.radius_default);

		double R = 6371d; // earth's mean radius in km
		double d = this.radiusValue / R; // radius given in km
		double lat1 = Math.toRadians(this.userlocationManager.getLatitude());
		double lon1 = Math.toRadians(this.userlocationManager.getLongitude());
		PolylineOptions options = new PolylineOptions();

		for (int x = 0; x <= 720; x++) {
			double brng = Math.toRadians(x);
			double latitudeRad = Math.asin(Math.sin(lat1) * Math.cos(d)
					+ Math.cos(lat1) * Math.sin(d) * Math.cos(brng));
			double longitudeRad = (lon1 + Math.atan2(
					Math.sin(brng) * Math.sin(d) * Math.cos(lat1), Math.cos(d)
							- Math.sin(lat1) * Math.sin(latitudeRad)));
			options.add(new LatLng(Math.toDegrees(latitudeRad), Math
					.toDegrees(longitudeRad)));
		}

		if (this.radiusPolyline != null)
			this.radiusPolyline.remove();

		this.radiusPolyline = this.map.addPolyline(options.color(Color.BLUE)
				.width(1));
	}

	public void snapUsersPosition(View view) {
		if (this.userCameraPosition == null) {
			this.userCameraPosition = new CameraPosition.Builder()
					.target(new LatLng(this.userlocationManager.getLatitude(),
							this.userlocationManager.getLongitude())).zoom(10)
					.tilt(30).build();
		}

		this.map.animateCamera(
				CameraUpdateFactory.newCameraPosition(this.userCameraPosition),
				MapManager.SNAP_ANIMATION_DURATION, null);
	}

	void addLandmarkMarker(Landmark landmark) {
		this.map.addMarker(new MarkerOptions()
				.position(
						new LatLng(landmark.getLocationLatitude(), landmark
								.getLocationLongitude()))
				.title(landmark.getName())
				.icon(BitmapDescriptorFactory
						.fromResource(R.drawable.location_place)));
	}

	// Getters

	public Location getUserLocation() {
		return this.userlocationManager.getLocation();
	}

	public Integer getRadiusValue() {
		return this.radiusValue;
	}

	public void requestLocationUpdates() {
		this.userlocationManager.requestLocationUpdates();
	}

	public void removeUpdates() {
		this.userlocationManager.removeUpdates();
	}

}
