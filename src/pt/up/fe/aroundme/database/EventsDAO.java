package pt.up.fe.aroundme.database;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pt.up.fe.aroundme.models.Event;
import android.content.Context;

import com.j256.ormlite.dao.Dao;

public class EventsDAO {
	private final Dao<Event, Integer> eventDao;

	public EventsDAO(final Context context) {
		this.eventDao = DBHelper.getInstance(context).getEventDao();
	}

	// CRUD METHODS

	public void createEvent(final Event event) {
		try {
			this.eventDao.create(event);
		} catch (final SQLException e) {
			e.printStackTrace();
		}
	}

	public List<Event> getAllEvents() {
		List<Event> events = null;
		try {
			events = this.eventDao.queryForAll();
		} catch (final SQLException e) {
			e.printStackTrace();
		}
		return events;
	}

	public Event getEventById(final int eventId) {
		Event event = null;

		try {
			event = this.eventDao.queryForId(eventId);
		} catch (final SQLException e) {
			e.printStackTrace();
		}

		return event;
	}

	public void refreshEvent(final Event event) {
		try {
			this.eventDao.refresh(event);
		} catch (final SQLException e) {
			e.printStackTrace();
		}
	}

	public void updateEvent(final Event event) {
		try {
			this.eventDao.update(event);
		} catch (final SQLException e) {
			e.printStackTrace();
		}
	}

	public void deleteEvent(final Event event) {
		try {
			this.eventDao.delete(event);
		} catch (final SQLException e) {
			e.printStackTrace();
		}
	}

	public List<Event> getEventsByLandmark(final Integer landmarkId) {
		List<Event> events = new ArrayList<Event>();

		try {
			final Map<String, Object> values = new HashMap<String, Object>();
			values.put("landmarkId", landmarkId);
			events = this.eventDao.queryForFieldValues(values);
		} catch (final SQLException e) {
			e.printStackTrace();
		}

		return events;
	}
}
