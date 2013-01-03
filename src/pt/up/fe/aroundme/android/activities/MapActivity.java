package pt.up.fe.aroundme.android.activities;

import pt.up.fe.aroundme.R;
import pt.up.fe.aroundme.android.MapManager;
import pt.up.fe.aroundme.android.exceptions.UserLocationIsNullException;
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
import android.widget.Toast;

public class MapActivity extends FragmentActivity {
	private final String CLASS_NAME = this.getClass().getSimpleName();

	private MapManager mapManager;

	// Activity Life Cycle

	// TODO set layout for first run with tut or something.
	// TODO create first run var in sharedpreferences.
	// TODO warn user: first run must have network connection available
	@Override
	protected void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.activity_main);
		PreferenceManager.setDefaultValues(this, R.xml.preferences, false);

		this.mapManager = new MapManager(this);

		Log.d(this.CLASS_NAME, "onCreate()");
	}

	@Override
	protected void onStart() {
		super.onStart();

		Log.d(this.CLASS_NAME, "onStart()");
	}

	@Override
	protected void onResume() {
		super.onResume();
		this.menuRefresh();

		Log.d(this.CLASS_NAME, "onResume()");
	}

	@Override
	protected void onPause() {
		super.onPause();

		Log.d(this.CLASS_NAME, "onPause()");
	}

	@Override
	protected void onStop() {
		super.onStop();

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
		if( this.mapManager.getLoadedLandmarksSize() == 0 ) {
			this.showToast("No Landmarks within radius");
			return;
		}

		final Intent intent =
				new Intent(this.getApplicationContext(),
						ListLandmarksActivity.class);
		intent.putExtra(ListLandmarksActivity.LANDMARKS_LIST_KEY,
				this.mapManager.getLoadedLandmarksUsername());

		this.startActivity(intent);
	}

	private void menuRefresh() {
		try {
			this.mapManager.update();
		} catch (final UserLocationIsNullException e) {
			this.showToast("Waiting for location...");
			e.printStackTrace();
		}

		if( !this.isNetworkAvailable() ) {
			this.showToast("Network connection not available");
		}
	}

	private void menuSettings() {
		this.startActivity(new Intent(this.getApplicationContext(),
				SettingsActivity.class));
	}

	// TODO create about activity
	private void menuAbout() {}

	// onClick Buttons' Handlers

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

	public void showToast(final String message) {
		Toast.makeText(this.getApplicationContext(), message,
				Toast.LENGTH_SHORT).show();
	}
}
