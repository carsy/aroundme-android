package pt.up.fe.aroundme.android;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import pt.up.fe.aroundme.R;
import pt.up.fe.aroundme.android.activities.LandmarkActivity;
import pt.up.fe.aroundme.android.activities.MapActivity;
import pt.up.fe.aroundme.android.exceptions.UserLocationIsNullException;
import pt.up.fe.aroundme.controllers.AroundMeController;
import pt.up.fe.aroundme.models.Landmark;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationManager;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnInfoWindowClickListener;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.PolygonOptions;

public class MapManager {

	private static final int DEFAULT_RADIUS = 25;

	private final MapActivity mapActivity;

	private final SharedPreferences sharedPreferences;
	private final GoogleMap map;
	private final UserLocationManager userlocationManager;
	private final AroundMeController aroundmeController;

	private Integer radiusValue = DEFAULT_RADIUS;
	private Polygon radiusPolygon;
	private HashMap<Marker, Landmark> loadedMarkers;

	public MapManager(final MapActivity mapActivity) {
		this.mapActivity = mapActivity;

		this.map =
				((SupportMapFragment) this.getMapActivity()
						.getSupportFragmentManager().findFragmentById(R.id.map))
						.getMap();

		this.map.setIndoorEnabled(true);
		this.map.setMyLocationEnabled(true);
		this.map.setOnInfoWindowClickListener(new OnInfoWindowClickListener() {

			@Override
			public void onInfoWindowClick(final Marker marker) {
				final Intent intent =
						new Intent(MapManager.this.mapActivity
								.getApplicationContext(),
								LandmarkActivity.class);
				intent.putExtra(LandmarkActivity.LANDMARK_ID_KEY,
						MapManager.this.loadedMarkers.get(marker).getId());
				MapManager.this.mapActivity.startActivity(intent);

			}
		});
		this.map.animateCamera(CameraUpdateFactory
				.newCameraPosition(new CameraPosition.Builder().target(
						new LatLng(41.1782628, -8.5947189)).zoom(12).build()),
				650, null);

		this.userlocationManager =
				new UserLocationManager(this, (LocationManager) this
						.getMapActivity().getSystemService(
								Context.LOCATION_SERVICE));

		this.aroundmeController = new AroundMeController(this);

		this.sharedPreferences =
				PreferenceManager.getDefaultSharedPreferences(this
						.getMapActivity().getApplicationContext());
		this.loadedMarkers = new HashMap<Marker, Landmark>();

	}

	// MAP UPDATE METHODS

	public void update() throws UserLocationIsNullException {
		this.updateRadius();
		this.updateLandmarksMarkers();
	}

	public void updateRadius() throws UserLocationIsNullException {
		// TODO refactor radius_checkbox_key string
		if( !this.sharedPreferences.getBoolean("radius_checkbox_key", true) ) { return; }

		this.radiusValue =
				this.sharedPreferences.getInt("radius",
						R.integer.radius_default);
		Log.d("updateRadius()", this.radiusValue + "");

		final PolygonOptions options = new PolygonOptions();

		// TODO refactor: create class with this method as static...dunno...
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

		if( this.radiusPolygon != null ) {
			this.radiusPolygon.remove();
		}

		this.radiusPolygon =
				this.map.addPolygon(options.strokeColor(Color.LTGRAY)
						.strokeWidth(2).fillColor(Color.LTGRAY));
	}

	public void updateLandmarksMarkers() {
		this.clearLandmarks();

		try {
			this.aroundmeController.refreshLandmarks(this.userlocationManager
					.getLocation(), this.radiusValue);
		} catch (final UserLocationIsNullException e) {
			e.printStackTrace();
		}

		final List<Landmark> loadedLandmarks =
				this.aroundmeController.getLoadedLandmarks();
		Log.d("updateLandmarksMarkers()", this.loadedMarkers.size() + "");
		for(final Landmark landmark: loadedLandmarks) {
			this.addMarker(landmark);
		}
	}

	private void clearLandmarks() {

		final Set<Marker> loadedMarkers = this.loadedMarkers.keySet();

		for(final Marker marker: loadedMarkers) {
			Log.d("clearLandmarkMarkers()", "clearing...");
			marker.remove();
		}

		this.loadedMarkers = new HashMap<Marker, Landmark>();
	}

	public void toggleMapType(final View view) {
		this.map.setMapType(this.map.getMapType() == GoogleMap.MAP_TYPE_NORMAL
				? GoogleMap.MAP_TYPE_HYBRID : GoogleMap.MAP_TYPE_NORMAL);
	}

	public void addMarker(final Landmark landmark) {
		if( this.loadedMarkers.containsValue(landmark) ) { return; }

		final MarkerOptions markerOptions =
				new MarkerOptions().position(
						new LatLng(landmark.getLocationLatitude(), landmark
								.getLocationLongitude())).title(
						landmark.getName()).icon(
						BitmapDescriptorFactory
								.fromResource(R.drawable.location_place));
		final Marker marker = this.map.addMarker(markerOptions);

		this.loadedMarkers.put(marker, landmark);
	}

	// Getters
	public Integer getRadiusValue() {
		return this.radiusValue;
	}

	public String[] getLoadedLandmarksUsername() {
		final Collection<Landmark> landmarks = this.loadedMarkers.values();
		final String[] landmarksUsername = new String[landmarks.size()];
		int i = 0;

		for(final Landmark landmark: landmarks) {
			landmarksUsername[i++] = landmark.getUsername();
		}
		return landmarksUsername;
	}

	public MapActivity getMapActivity() {
		return this.mapActivity;
	}

	public int getLoadedLandmarksSize() {
		return this.loadedMarkers.size();
	}

	public Location getMyLocation() {
		return this.map.getMyLocation();
	}

}
