package pt.up.fe.aroundme.android;

import pt.up.fe.aroundme.android.exceptions.UserLocationIsNullException;
import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;

public class UserLocationManager implements LocationListener {
	private Location userLocation;

	private final MapManager mapManager;

	UserLocationManager(final MapManager mapManager,
			final LocationManager locationManager) {
		this.mapManager = mapManager;
		this.userLocation = this.mapManager.getMyLocation();
	}

	@Override
	public void onLocationChanged(final Location location) {
		this.userLocation = new Location(location);
		Log.d("onLocationChanged()", this.userLocation + "");
		try {
			this.mapManager.update();
		} catch (final UserLocationIsNullException e) {
			Log.d("onLocationChanged()", "wtf: location is null");
			e.printStackTrace();
		}
	}

	@Override
	public void onProviderDisabled(final String provider) {
		Log.d("onProviderDisabled()", provider);
	}

	@Override
	public void onProviderEnabled(final String provider) {
		Log.d("onProviderEnabled()", provider);
	}

	@Override
	public void onStatusChanged(final String provider, final int status,
			final Bundle extras) {
		Log.d(provider, "provider status changed to => " + status);
	}

	// GETTERS

	public Location getLocation() throws UserLocationIsNullException {
		if( this.userLocation == null ) {
			this.userLocation = this.mapManager.getMyLocation();
		}

		if( this.userLocation == null ) {
			final LocationManager locationManager =
					(LocationManager) this.mapManager.getMapActivity()
							.getSystemService(Context.LOCATION_SERVICE);
			this.userLocation =
					locationManager.getLastKnownLocation(locationManager
							.getBestProvider(new Criteria(), true));
		}

		if( this.userLocation == null ) { throw new UserLocationIsNullException(); }

		return this.userLocation;
	}

	public double getLatitude() throws UserLocationIsNullException {
		return this.getLocation().getLatitude();
	}

	public double getLongitude() throws UserLocationIsNullException {
		return this.getLocation().getLongitude();
	}

}
