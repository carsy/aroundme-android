package pt.up.fe.aroundme.database;

import android.content.Context;

public class DBManager {

	private static DBManager instance;

	private final DBHelper helper;

	private DBManager(final Context context) {
		this.helper = new DBHelper(context);
	}

	public static DBManager getInstance(final Context context) {
		return instance == null ? instance = new DBManager(context) : instance;
	}

	public DBHelper getHelper() {
		return this.helper;
	}

}
