package pt.up.fe.aroundme.connections;

import java.net.URI;

import pt.up.fe.aroundme.controllers.AroundMeController;
import android.location.Location;
import android.util.Log;

public class AroundMeConnection {
	private static final String HTTP = "http";

	private static final String HOST = "around-me.herokuapp.com";

	private static final String FORMAT_JSON = ".json";
	private static final String FORMAT_XML = ".xml";

	private static final String GET_LANDMARKS = "/landmarks";
	private static final String GET_LANDMARKS_JSON = GET_LANDMARKS
			+ FORMAT_JSON;
	private static final String GET_LANDMARKS_XML = GET_LANDMARKS + FORMAT_XML;

	private final AroundMeController aroundMeController;

	public AroundMeConnection(AroundMeController aroundMeController) {
		this.aroundMeController = aroundMeController;
	}

	public void fetchLandmarks(Location userLocation, Integer radius) {
		try {
			URI uri = new URI(HTTP, HOST, GET_LANDMARKS_JSON,
					this.getLandmarksRadiusQuery(userLocation, radius), null);
			new DownloadJSONTask(aroundMeController) {
				@Override
				protected void onPostExecute(String landmarksJSON) {
					this.aroundMeController.loadLandmarks(landmarksJSON);
				}
			}.execute(uri);
		} catch (Exception e) {
			e.printStackTrace();
			Log.e(this.toString(), "URL error. Landmarks fetching aborted.");
		}

	}

	public void fetchLandmark(String username) {
		URI uri;
		try {
			uri = new URI(HTTP, HOST, this.getLandmarkPath(username), null);
			new DownloadJSONTask(aroundMeController) {
				@Override
				protected void onPostExecute(String landmarksJSON) {
					this.aroundMeController.addLandmark(landmarksJSON);
				}
			}.execute(uri);
		} catch (Exception e) {
			e.printStackTrace();
			Log.e(this.toString(), "URL error. Landmark " + username
					+ " fetching aborted.");
		}
	}

	private String getLandmarksRadiusQuery(Location userLocation, Integer radius) {
		return "radius=" + radius + "&longitude=" + userLocation.getLongitude()
				+ "&latitude=" + userLocation.getLatitude();
	}

	private String getLandmarkPath(String username) {
		return GET_LANDMARKS + "/" + username + FORMAT_JSON;
	}
}
