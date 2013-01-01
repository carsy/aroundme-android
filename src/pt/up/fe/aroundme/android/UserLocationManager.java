package pt.up.fe.aroundme.android;

import pt.up.fe.aroundme.android.exceptions.UserLocationIsNullException;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;

public class UserLocationManager implements LocationListener {
	private static final int LOCATION_UPDATE_MINDISTANCE = 1;
	private static final int LOCATION_UPDATES_MINTIME = 400;

	private String locationProvider;
	private Location userLocation;

	private final MapManager mapManager;

	private final LocationManager locationManager;

	UserLocationManager(final MapManager mapManager,
			final LocationManager locationManager) {
		this.mapManager = mapManager;

		this.locationManager = locationManager;
		this.locationProvider = this.updateLocationProvider();
		this.userLocation = this.updateLastKnownLocation();
	}

	@Override
	public void onLocationChanged(final Location location) {
		this.userLocation = new Location(location);
		try {
			this.mapManager.update();
		} catch (final UserLocationIsNullException e) {
			Log.d("onLocationChanged()", "wtf: location is null");
			e.printStackTrace();
		}
	}

	@Override
	public void onProviderDisabled(final String provider) {
		this.updateLocationProvider();
	}

	@Override
	public void onProviderEnabled(final String provider) {
		this.updateLocationProvider();
	}

	@Override
	public void onStatusChanged(final String provider, final int status,
			final Bundle extras) {
		Log.d(provider, "provider status changed to => " + status);
	}

	private Location updateLastKnownLocation() {
		return this.userLocation =
				this.locationManager
						.getLastKnownLocation(this.locationProvider);
	}

	private String updateLocationProvider() {
		final Criteria criteria = new Criteria();
		criteria.setCostAllowed(false);

		return this.locationProvider =
				this.locationManager.getBestProvider(criteria, true);
	}

	public void requestLocationUpdates() {
		this.locationManager.requestLocationUpdates(this.locationProvider,
				LOCATION_UPDATES_MINTIME, LOCATION_UPDATE_MINDISTANCE, this);
	}

	public void removeUpdates() {
		this.locationManager.removeUpdates(this);
	}

	// GETTERS

	public Location getLocation() throws UserLocationIsNullException {
		if( this.userLocation == null ) { throw new UserLocationIsNullException(); }

		return this.userLocation;
	}

	public double getLatitude() throws UserLocationIsNullException {
		if( this.userLocation == null ) { throw new UserLocationIsNullException(); }

		return this.userLocation.getLatitude();
	}

	public double getLongitude() throws UserLocationIsNullException {
		if( this.userLocation == null ) { throw new UserLocationIsNullException(); }

		return this.userLocation.getLongitude();
	}

}
