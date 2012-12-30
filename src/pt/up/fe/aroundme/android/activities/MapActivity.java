package pt.up.fe.aroundme.android.activities;

import pt.up.fe.aroundme.R;
import pt.up.fe.aroundme.android.MapManager;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

public class MapActivity extends FragmentActivity {
	private final String CLASS_NAME = this.getClass().getSimpleName();

	private MapManager mapManager;

	// Activity Life Cycle

	@Override
	protected void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.activity_main);
		PreferenceManager.setDefaultValues(this, R.xml.preferences, false);

		this.mapManager = new MapManager(this);
		this.mapManager.snapUsersPosition(this.getCurrentFocus());

		Log.d(this.CLASS_NAME, "onCreate()");
	}

	/* Request updates at startup */
	@Override
	protected void onStart() {
		super.onStart();
		this.mapManager.requestLocationUpdates();

		Log.d(this.CLASS_NAME, "onStart()");
	}

	@Override
	protected void onResume() {
		super.onResume();

		Log.d(this.CLASS_NAME, "onResume()");
	}

	@Override
	protected void onPause() {
		super.onPause();

		Log.d(this.CLASS_NAME, "onPause()");
	}

	/*
	 * Remove the location listener updates from mapManager when Activity is
	 * stopped
	 */
	@Override
	protected void onStop() {
		super.onStop();
		this.mapManager.removeUpdates();

		Log.d(this.CLASS_NAME, "onStop()");
	}

	@Override
	protected void onRestart() {
		super.onRestart();
		Log.d(this + "", "onRestart()");
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();

		Log.d(this.CLASS_NAME, "onDestroy()");
	}

	@Override
	public boolean onCreateOptionsMenu(final Menu menu) {
		this.getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

	@Override
	protected void onSaveInstanceState(final Bundle outState) {
		super.onSaveInstanceState(outState);
	}

	@Override
	protected void onRestoreInstanceState(final Bundle savedInstanceState) {
		super.onRestoreInstanceState(savedInstanceState);
	}

	// MENU Related

	@Override
	public boolean onOptionsItemSelected(final MenuItem item) {
		switch (item.getItemId()) {
			case R.id.menu_list_landmarks:
				this.menuListLandmarks();
				return true;
			case R.id.menu_refresh:
				this.menuRefresh();
				return true;
			case R.id.menu_settings:
				this.menuSettings();
				return true;
			case R.id.menu_about:
				this.menuAbout();
				return true;
			default:
				return super.onOptionsItemSelected(item);
		}
	}

	private void menuListLandmarks() {
		final Intent intent =
				new Intent(this.getApplicationContext(),
						ListLandmarksActivity.class);
		intent.putExtra(ListLandmarksActivity.LANDMARKS_LIST_KEY,
				this.mapManager.getLoadedLandmarksIds());

		this.startActivity(intent);
	}

	private void menuRefresh() {
		this.mapManager.update();
	}

	private void menuSettings() {
		this.startActivity(new Intent(this.getApplicationContext(),
				SettingsActivity.class));
	}

	private void menuAbout() {}

	// onClick Buttons' Handlers

	public void onClickSnapUsersPositionButton(final View view) {
		this.mapManager.snapUsersPosition(view);
	}

	public void onClickMapTypeButton(final View view) {
		this.mapManager.toggleMapType(view);
	}

	// Device Network State
	public boolean isNetworkAvailable() {
		final NetworkInfo networkInfo =
				((ConnectivityManager) this
						.getSystemService(Context.CONNECTIVITY_SERVICE))
						.getActiveNetworkInfo();

		return networkInfo != null && networkInfo.isConnected();
	}
}
