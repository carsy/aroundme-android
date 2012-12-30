package pt.up.fe.aroundme.database;

import java.util.ArrayList;
import java.util.List;

import pt.up.fe.aroundme.models.Event;
import pt.up.fe.aroundme.models.Landmark;
import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

public class DBHelper extends OrmLiteSqliteOpenHelper {
	private static final String DATABASE_NAME = "around-db.sqlite";

	private static final int DATABASE_VERSION = 1;

	// the DAO objects
	private Dao<Landmark, Integer> landmarkDao = null;
	private Dao<Event, Integer> eventDAO = null;

	public DBHelper(final Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(final SQLiteDatabase database,
			final ConnectionSource connectionSource) {
		try {
			TableUtils.createTable(connectionSource, Landmark.class);
			TableUtils.createTable(connectionSource, Event.class);
		} catch (final SQLException e) {
			Log.e(DBHelper.class.getName(), "Can't create database", e);
			throw new RuntimeException(e);
		} catch (final java.sql.SQLException e) {
			e.printStackTrace();
		}

	}

	@Override
	public void onUpgrade(final SQLiteDatabase db,
			final ConnectionSource connectionSource, final int oldVersion,
			final int newVersion) {
		try {
			final List<String> allSql = new ArrayList<String>();
			switch (oldVersion) {
				case 1:
					// allSql.add("alter table AdData add column `new_col` VARCHAR");
					// allSql.add("alter table AdData add column `new_col2` VARCHAR");
			}
			for(final String sql: allSql) {
				db.execSQL(sql);
			}
		} catch (final SQLException e) {
			Log.e(DBHelper.class.getName(), "exception during onUpgrade", e);
			throw new RuntimeException(e);
		}

	}

	public Dao<Landmark, Integer> getLandmarkDao() {
		if( this.landmarkDao == null ) {
			try {
				this.landmarkDao = this.getDao(Landmark.class);
			} catch (final java.sql.SQLException e) {
				e.printStackTrace();
			}
		}

		return this.landmarkDao;
	}

	public Dao<Event, Integer> getEventDao() {
		if( this.eventDAO == null ) {
			try {
				this.eventDAO = this.getDao(Event.class);
			} catch (final java.sql.SQLException e) {
				e.printStackTrace();
			}
		}

		return this.eventDAO;
	}

}
