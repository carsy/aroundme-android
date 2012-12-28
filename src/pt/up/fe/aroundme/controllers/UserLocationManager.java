package pt.up.fe.aroundme.controllers;

import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;

public class UserLocationManager implements LocationListener {
	private final LocationManager locationManager;
	private String locationProvider;
	private Location userLocation;

	private final MapManager mapManager;

	UserLocationManager(MapManager mapManager, LocationManager locationManager) {
		this.mapManager = mapManager;

		this.locationManager = locationManager;
		this.locationProvider = this.updateLocationProvider();
		this.userLocation = this.updateLastKnownLocation();
	}

	@Override
	public void onLocationChanged(Location location) {
		this.userLocation = new Location(location);
		this.mapManager.update();
	}

	@Override
	public void onProviderDisabled(String provider) {
		this.updateLocationProvider();

	}

	@Override
	public void onProviderEnabled(String provider) {
		this.updateLocationProvider();
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		Log.d(provider, "provider status changed to => " + status);
	}

	private Location updateLastKnownLocation() {
		return this.userLocation = this.locationManager
				.getLastKnownLocation(this.locationProvider);
	}

	private String updateLocationProvider() {
		Criteria criteria = new Criteria();
		criteria.setCostAllowed(false);

		return this.locationProvider = this.locationManager.getBestProvider(
				criteria, true);
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
		return userLocation;
	}

	public double getLatitude() {
		return userLocation.getLatitude();
	}

	public double getLongitude() {
		return userLocation.getLongitude();
	}

}
