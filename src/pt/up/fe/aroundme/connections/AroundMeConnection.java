package pt.up.fe.aroundme.connections;

import java.net.URI;

import pt.up.fe.aroundme.controllers.AroundMeController;
import android.location.Location;
import android.util.Log;

public class AroundMeConnection {
	private static final String HTTP = "http";

	private static final String HOST = "around-me.herokuapp.com";

	private static final String FORMAT_JSON = ".json";

	private static final String GET_LANDMARKS = "/landmarks";
	private static final String GET_LANDMARKS_JSON = GET_LANDMARKS
			+ FORMAT_JSON;
	private static final String GET_EVENTS = "/events";
	private static final String GET_EVENTS_JSON = GET_EVENTS + FORMAT_JSON;

	private final AroundMeController aroundMeController;

	public AroundMeConnection(final AroundMeController aroundMeController) {
		this.aroundMeController = aroundMeController;
	}

	public void fetchLandmarks(final Location userLocation, final Integer radius) {
		try {
			final URI uri =
					new URI(HTTP, HOST, GET_LANDMARKS_JSON, this
							.getLandmarksRadiusQuery(userLocation, radius),
							null);
			new DownloadJSONTask() {
				@Override
				protected void onPostExecute(final String landmarksJSON) {
					AroundMeConnection.this.aroundMeController
							.loadLandmarks(landmarksJSON);
				}
			}.execute(uri);
		} catch (final Exception e) {
			e.printStackTrace();
			Log.e(this.toString(), "URL error. Landmarks fetching aborted.");
		}

	}

	public void fetchLandmark(final String landmarkUsername) {
		try {
			final URI uri =
					new URI(HTTP, HOST, this.getLandmarkPath(landmarkUsername),
							null);
			new DownloadJSONTask() {
				@Override
				protected void onPostExecute(final String landmarkJSON) {
					AroundMeConnection.this.aroundMeController
							.addLandmark(landmarkJSON);
				}
			}.execute(uri);

			this.fetchEvents(landmarkUsername);

		} catch (final Exception e) {
			e.printStackTrace();
			Log.e(this.toString(), "URL error. Landmark " + landmarkUsername
					+ " fetching aborted.");
		}

	}

	private void fetchEvents(final String landmarkUsername) {
		try {
			final URI uri =
					new URI(HTTP, HOST, this.getEventsPath(landmarkUsername),
							null);
			Log.d("fetchEvents()", uri.toString());
			new DownloadJSONTask() {
				@Override
				protected void onPostExecute(final String eventsJSON) {
					AroundMeConnection.this.aroundMeController
							.loadEvents(eventsJSON);
				};
			}.execute(uri);
		} catch (final Exception e) {
			e.printStackTrace();
			Log.e(this.toString(), "Error. fetching events for Landmark "
					+ landmarkUsername + " aborted.");
		}
	}

	public void fetchEvent(final String eventId) {
		try {
			final URI uri =
					new URI(HTTP, HOST, this.getEventPath(eventId), null);
			Log.d("fetchEvents()", uri.toString());
			new DownloadJSONTask() {
				@Override
				protected void onPostExecute(final String eventJSON) {
					AroundMeConnection.this.aroundMeController
							.addEvent(eventJSON);
				}
			}.execute(uri);

		} catch (final Exception e) {
			e.printStackTrace();
			Log.e(this.toString(), "Error. fetching event " + eventId
					+ " aborted.");
		}
	}

	// URL creation utils
	private String getLandmarksRadiusQuery(final Location userLocation,
			final Integer radius) {
		return "radius=" + radius + "&longitude=" + userLocation.getLongitude()
				+ "&latitude=" + userLocation.getLatitude();
	}

	private String getLandmarkPath(final String username) {
		return GET_LANDMARKS + "/" + username + FORMAT_JSON;
	}

	private String getEventsPath(final String landmarkUsername) {
		return GET_LANDMARKS + "/" + landmarkUsername + GET_EVENTS_JSON;
	}

	private String getEventPath(final String eventId) {
		return GET_EVENTS + "/" + eventId + FORMAT_JSON;
	}
}
