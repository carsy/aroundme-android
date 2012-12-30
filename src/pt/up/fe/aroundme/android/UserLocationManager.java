package pt.up.fe.aroundme.android;

import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;

public class UserLocationManager implements LocationListener {
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
		this.mapManager.update();
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
		this.locationManager.requestLocationUpdates(this.locationProvider, 400,
				1, this);
	}

	public void removeUpdates() {
		this.locationManager.removeUpdates(this);
	}

	// GETTERS

	public Location getLocation() {
		return this.userLocation;
	}

	public double getLatitude() {
		return this.userLocation.getLatitude();
	}

	public double getLongitude() {
		return this.userLocation.getLongitude();
	}

}
