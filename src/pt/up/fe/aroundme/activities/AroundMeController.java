package pt.up.fe.aroundme.activities;

import java.util.ArrayList;
import java.util.Locale;

import pt.up.fe.aroundme.connections.AroundMeConnection;
import pt.up.fe.aroundme.models.Landmark;
import android.location.Location;
import android.util.Log;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class AroundMeController {
	private AroundMeConnection aroundMeConnection;
	ArrayList<Landmark> loadedLandmarks;
	private MainActivity mainActivity;
	private Gson gson;

	public AroundMeController(MainActivity mainActivity) {
		this.loadedLandmarks = new ArrayList<Landmark>();
		this.aroundMeConnection = new AroundMeConnection(this);
		this.mainActivity = mainActivity;
		this.gson = new GsonBuilder().setFieldNamingPolicy(
				FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES).create();
	}

	public void refreshLandmarks(Location userLocation, Integer radius) {
		aroundMeConnection.fetchLandmarks(userLocation, radius);
	}

	public void loadLandmarks(String landmarksJSON) {
		Landmark[] landmarks = this.gson.fromJson(landmarksJSON,
				Landmark[].class);

		for (Landmark landmark : landmarks) {
			aroundMeConnection.fetchLandmark(landmark.getUsername()
					.toLowerCase(Locale.US));
		}
	}

	public void addLandmark(String landmarkJSON) {
		Landmark landmark = this.gson.fromJson(landmarkJSON,
				Landmark.class);

		Log.d("addLandmark", landmark.getUsername());

		this.loadedLandmarks.add(landmark);
		mainActivity.addLandmarkMarker(landmark);
	}

}
