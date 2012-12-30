package pt.up.fe.aroundme.controllers;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import pt.up.fe.aroundme.database.DBHelper;
import pt.up.fe.aroundme.models.Landmark;
import android.content.Context;
import android.location.Location;

import com.j256.ormlite.dao.Dao;

public class LandmarksController {

	private final Dao<Landmark, Integer> landmarkDao;

	public LandmarksController(final Context context) {
		this.landmarkDao = DBHelper.getInstance(context).getLandmarkDao();
	}

	// CRUD METHODS

	public void createLandmark(final Landmark landmark) {
		try {
			this.landmarkDao.createIfNotExists(landmark);
		} catch (final SQLException e) {
			e.printStackTrace();
		}
	}

	public List<Landmark> getAllLandmarks() {
		List<Landmark> landmarks = null;
		try {
			landmarks = this.landmarkDao.queryForAll();
		} catch (final SQLException e) {
			e.printStackTrace();
		}
		return landmarks;
	}

	public Landmark getLandmarkById(final int landmarkId) {
		Landmark landmark = null;

		try {
			landmark = this.landmarkDao.queryForId(landmarkId);
		} catch (final SQLException e) {
			e.printStackTrace();
		}

		return landmark;
	}

	public void refreshLandmark(final Landmark landmark) {
		try {
			this.landmarkDao.refresh(landmark);
		} catch (final SQLException e) {
			e.printStackTrace();
		}
	}

	public void updateLandmark(final Landmark landmark) {
		try {
			this.landmarkDao.update(landmark);
		} catch (final SQLException e) {
			e.printStackTrace();
		}
	}

	public void deleteLandmark(final Landmark landmark) {
		try {
			this.landmarkDao.delete(landmark);
		} catch (final SQLException e) {
			e.printStackTrace();
		}
	}

	public List<Landmark> getLandmarksByRadius(final Location userLocation,
			final Integer radius) {
		final List<Landmark> landmarksInRadius = new ArrayList<Landmark>();
		final List<Landmark> allLandmarks = this.getAllLandmarks();

		for(final Landmark landmark: allLandmarks) {
			if( this.isInRadius(landmark, userLocation, radius) ) {
				landmarksInRadius.add(landmark);
			}
		}

		return landmarksInRadius;
	}

	public List<Landmark> getLandmarksById(final int[] landmarksId) {
		final ArrayList<Landmark> landmarks = new ArrayList<Landmark>();

		for(final Integer landmarkId: landmarksId) {
			try {
				landmarks.add(this.landmarkDao.queryForId(landmarkId));
			} catch (final SQLException e) {
				e.printStackTrace();
			}
		}
		return landmarks;
	}

	protected boolean isInRadius(final Landmark landmark,
			final Location userLocation, final Integer radius) {
		final double R = 6371.0; // earth's mean radius in km

		final double landmarkLatitude = landmark.getLocationLatitude(), landmarkLongitude =
				landmark.getLocationLongitude();
		final double userLatitude = userLocation.getLatitude(), userLongitude =
				userLocation.getLongitude();

		final double diffLatitude =
				Math.toRadians(landmarkLatitude - userLatitude), diffLongitude =
				Math.toRadians(landmarkLongitude - userLongitude);

		final double a =
				Math.pow(Math.sin(diffLatitude / 2.0), 2.0)
						+ Math.cos(Math.toRadians(userLatitude))
						* Math.cos(Math.toRadians(landmarkLatitude))
						* Math.pow(Math.sin(diffLongitude / 2.0), 2.0);
		final double distance =
				R * 2.0 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

		return distance <= radius;
	}
}
