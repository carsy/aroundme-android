package pt.up.fe.aroundme.controllers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import pt.up.fe.aroundme.android.MapManager;
import pt.up.fe.aroundme.connections.AroundMeConnection;
import pt.up.fe.aroundme.models.Landmark;
import android.location.Location;
import android.util.Log;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class AroundMeController {
	private final MapManager mapManager;

	private final AroundMeConnection aroundMeConnection;

	private final LandmarksController landmarksController;
	private final Gson gson;

	private final List<Landmark> loadedLandmarks;

	public AroundMeController(final MapManager mapManager) {
		this.mapManager = mapManager;

		this.aroundMeConnection = new AroundMeConnection(this);

		this.landmarksController =
				new LandmarksController(this.mapManager.getMapActivity());
		this.gson =
				new GsonBuilder().setFieldNamingPolicy(
						FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES).create();

		this.loadedLandmarks =
				Collections.synchronizedList(new ArrayList<Landmark>());
	}

	// FIXME make this func prettier
	public void refreshLandmarks(final Location userLocation, final int radius) {
		final List<Landmark> landmarks2Remove = new ArrayList<Landmark>();

		for(final Landmark landmark: this.loadedLandmarks) {
			if( !this.isInRadius(landmark, userLocation, radius) ) {
				landmarks2Remove.add(landmark);
				Log.d("refreshLandmarks(): landmark2remove added", landmark
						.getUsername()
						+ "");
			}
		}
		for(final Landmark landmark: landmarks2Remove) {
			this.loadedLandmarks.remove(landmark);
			this.mapManager.removeMarker(landmark.getId());

			Log.d("refreshLandmarks(): landmark2remove removed", landmark
					.getUsername()
					+ "");
		}

		final List<Landmark> cachedLandmarks =
				this.landmarksController.getLandmarksByRadius(userLocation
						.getLatitude(), userLocation.getLongitude(), radius);
		Log.d("refreshLandmarks(): cachedLandmarks", cachedLandmarks.size()
				+ " | " + this.loadedLandmarks.addAll(cachedLandmarks));

		if( this.mapManager.getMapActivity().isNetworkAvailable() ) {
			this.aroundMeConnection.fetchLandmarks(userLocation, radius);
		}
		else {
			// TODO warn mapActivity
		}

		for(final Landmark landmark: this.loadedLandmarks) {
			if( !landmarks2Remove.contains(landmark) ) {
				this.mapManager.addMarker(landmark.getId(), landmark
						.getLocationLatitude(),
						landmark.getLocationLongitude(), landmark.getName());
			}
		}
	}

	private boolean isInRadius(final Landmark landmark,
			final Location userLocation, final Integer radius) {
		// TODO distance between landmark pos and userlocation < radius ?
		return true;
	}

	// Callback for connections

	public void loadLandmarks(final String landmarksJSON) {

		if( landmarksJSON == null ) {
			Log.d("loadLandmarks()", "landmarksJSON is null.");
			// TODO throw new DataDownloadErrorException()
		}

		final Landmark[] fetchedLandmarks =
				this.gson.fromJson(landmarksJSON, Landmark[].class);
		final ArrayList<Landmark> newLandmarks = new ArrayList<Landmark>();

		Log.d("loadLandmarks(): fetchedLandmarks", fetchedLandmarks.length + "");

		for(final Landmark landmark: fetchedLandmarks) {
			if( !this.loadedLandmarks.contains(landmark) ) {
				newLandmarks.add(landmark);
				Log.d("loadLandmarks(): newLandmarks", landmark.getUsername()
						+ "");
			}
		}

		if( !this.mapManager.getMapActivity().isNetworkAvailable() ) {
			// TODO warn mapActivity
			return;
		}

		for(final Landmark landmark: newLandmarks) {
			this.aroundMeConnection.fetchLandmark(landmark.getUsername()
					.toLowerCase(Locale.US));
		}
	}

	// landmarkJSON is never null
	public void addLandmark(final String landmarkJSON) {
		final Landmark landmark =
				this.gson.fromJson(landmarkJSON, Landmark.class);

		Log.d("addLandmark(): landmark fetched", landmark.getUsername());

		this.landmarksController.createLandmark(landmark);
		synchronized(this.loadedLandmarks) {
			this.loadedLandmarks.add(landmark);
		}

		this.mapManager.addMarker(landmark.getId(), landmark
				.getLocationLatitude(), landmark.getLocationLongitude(),
				landmark.getName());
	}
}
