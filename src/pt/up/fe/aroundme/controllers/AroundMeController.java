package pt.up.fe.aroundme.controllers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import pt.up.fe.aroundme.android.MapManager;
import pt.up.fe.aroundme.connections.AroundMeConnection;
import pt.up.fe.aroundme.database.EventsDAO;
import pt.up.fe.aroundme.database.LandmarksDAO;
import pt.up.fe.aroundme.models.Event;
import pt.up.fe.aroundme.models.Landmark;
import android.location.Location;
import android.util.Log;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class AroundMeController {
	private final MapManager mapManager;

	private final AroundMeConnection aroundMeConnection;
	private final LandmarksDAO landmarksDAO;
	private final EventsDAO eventsDAO;

	private final Gson gson;

	private List<Landmark> loadedLandmarks;

	public AroundMeController(final MapManager mapManager) {

		this.mapManager = mapManager;

		this.aroundMeConnection = new AroundMeConnection(this);

		this.landmarksDAO = new LandmarksDAO(this.mapManager.getMapActivity());
		this.eventsDAO = new EventsDAO(this.mapManager.getMapActivity());
		this.gson =
				new GsonBuilder().setFieldNamingPolicy(
						FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES).create();

		this.loadedLandmarks =
				Collections.synchronizedList(new ArrayList<Landmark>());
	}

	// FIXME make this function prettier
	public void refreshLandmarks(final Location userLocation, final int radius) {
		this.loadedLandmarks =
				Collections.synchronizedList(new ArrayList<Landmark>());

		final List<Landmark> cachedLandmarks =
				this.landmarksDAO.getLandmarksByRadius(userLocation, radius);
		Log.d("refreshLandmarks(): cachedLandmarks", cachedLandmarks.size()
				+ " | " + this.loadedLandmarks.addAll(cachedLandmarks));

		if( this.mapManager.getMapActivity().isNetworkAvailable() ) {
			this.aroundMeConnection.fetchLandmarks(userLocation, radius);
		}

		Log.d("refreshLandmarks(): loadedLandmarks", this.loadedLandmarks
				.size()
				+ "");
	}

	// Callbacks for connections
	public void loadLandmarks(final String landmarksJSON) {
		if( !this.mapManager.getMapActivity().isNetworkAvailable() ) { return; }

		if( landmarksJSON == null ) {
			Log.d("loadLandmarks()", "landmarksJSON is null.");
			// TODO throw new DataDownloadErrorException()
		}

		final Landmark[] fetchedLandmarks =
				this.gson.fromJson(landmarksJSON, Landmark[].class);

		Log.d("loadLandmarks(): fetchedLandmarks", fetchedLandmarks.length + "");

		for(final Landmark landmark: fetchedLandmarks) {
			if( !this.loadedLandmarks.contains(landmark) ) {
				Log.d("loadLandmarks(): newLandmarks", landmark.getUsername()
						+ "");
				this.aroundMeConnection.fetchLandmark(landmark.getUsername()
						.toLowerCase(Locale.US));
			}
		}
	}

	public void addLandmark(final String landmarkJSON) {
		// landmarkJSON is never null
		final Landmark landmark =
				this.gson.fromJson(landmarkJSON, Landmark.class);

		Log.d("addLandmark(): landmark fetched", landmark.getUsername());

		this.landmarksDAO.createLandmark(landmark);
		synchronized(this.loadedLandmarks) {
			this.loadedLandmarks.add(landmark);
			this.mapManager.addMarker(landmark);
		}
	}

	public void loadEvents(final String eventsJSON) {
		if( !this.mapManager.getMapActivity().isNetworkAvailable() ) { return; }

		if( eventsJSON == null ) {
			Log.d("eventsJSON()", "eventsJSON is null.");
			// TODO throw new DataDownloadErrorException()
		}

		final Event[] fetchedEvents =
				this.gson.fromJson(eventsJSON, Event[].class);

		Log.d("loadEvents(): fetchedEvents", fetchedEvents.length + "");

		for(final Event event: fetchedEvents) {
			Log.d("loadEvents(): fetchedEvents", event.getName() + "");
			this.aroundMeConnection.fetchEvent(Integer.toString(event.getId()));
		}

	}

	public void addEvent(final String eventJSON) {
		// eventJSON is never null
		final Event event = this.gson.fromJson(eventJSON, Event.class);

		Log.d("addEvent(): event fetched", event.getName());

		this.eventsDAO.createEvent(event);
	}

	public List<Landmark> getLoadedLandmarks() {
		return this.loadedLandmarks;
	}
}
