package pt.up.fe.aroundme.controllers;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import pt.up.fe.aroundme.database.DBManager;
import pt.up.fe.aroundme.models.Landmark;
import android.content.Context;

import com.j256.ormlite.dao.Dao;

public class LandmarksController {

	private final DBManager dbManager;
	private final Dao<Landmark, Integer> landmarkDao;

	public LandmarksController(final Context context) {
		this.dbManager = DBManager.getInstance(context);

		this.landmarkDao = this.dbManager.getHelper().getLandmarkDao();
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

	public List<Landmark> getLandmarksByRadius(final double latitude,
			final double longitude, final Integer radius) {
		// TODO query db for landmarks around user
		return this.getAllLandmarks();
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

}
