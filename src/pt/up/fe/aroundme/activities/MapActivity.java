package pt.up.fe.aroundme.activities;

import pt.up.fe.aroundme.R;
import pt.up.fe.aroundme.controllers.MapManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.google.android.gms.maps.SupportMapFragment;

public class MapActivity extends FragmentActivity {
	private MapManager mapManager;

	// Activity Life Cycle

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		this.mapManager = new MapManager(this,
				((SupportMapFragment) this.getSupportFragmentManager()
						.findFragmentById(R.id.map)).getMap());

		this.mapManager.snapUsersPosition(this.getCurrentFocus());

		Log.d(this + "", "onCreate()");
	}

	/* Request updates at startup */
	@Override
	protected void onStart() {
		super.onStart();
		this.mapManager.update();
		this.mapManager.requestLocationUpdates();

		Log.d(this + "", "onStart()");
	}

	@Override
	protected void onResume() {
		super.onResume();

		Log.d(this + "", "onResume()");
	}

	@Override
	protected void onPause() {
		super.onPause();

		Log.d(this + "", "onPause()");
	}

	/* Remove the locationlistener updates when Activity is stopped */
	@Override
	protected void onStop() {
		super.onStop();
		this.mapManager.removeUpdates();
		Log.d(this + "", "onStop()");
	}

	@Override
	protected void onRestart() {
		super.onRestart();
		Log.d(this + "", "onRestart()");
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		Log.d(this + "", "onDestroy()");
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
	}

	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		super.onRestoreInstanceState(savedInstanceState);
	}

	// MENU Related

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.menu_snap_user_position:
			this.onClickSnapUsersPositionButton(this.getCurrentFocus());
			return true;
		case R.id.menu_refresh:
			menuRefresh();
			return true;
		case R.id.menu_settings:
			menuSettings();
			return true;
		case R.id.menu_about:
			menuAbout();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	public void onClickSnapUsersPositionButton(View view) {
		this.mapManager.snapUsersPosition(view);
	}

	private void menuRefresh() {
		this.mapManager.update();
	}

	private void menuSettings() {
		startActivity(new Intent(getApplicationContext(),
				SettingsActivity.class));
	}

	private void menuAbout() {

	}

}
