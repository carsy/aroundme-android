package pt.up.fe.aroundme.android.activities;

import pt.up.fe.aroundme.R;
import pt.up.fe.aroundme.database.LandmarksDAO;
import pt.up.fe.aroundme.models.Landmark;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class LandmarkActivity extends Activity {
	LandmarksDAO landmarksDAO;

	Landmark landmark;

	public static final String LANDMARK_ID_KEY = "landmark_id_key";

	@Override
	protected void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		this.landmarksDAO = new LandmarksDAO(this.getApplicationContext());

		final View contentView =
				this.getLayoutInflater().inflate(R.layout.landmark_view, null);

		final TextView landmarkNameTextView =
				(TextView) contentView
						.findViewById(R.id.landmark_name_text_view);

		this.landmark =
				this.landmarksDAO.getLandmarkById(this.getIntent().getExtras()
						.getInt(LandmarkActivity.LANDMARK_ID_KEY));

		landmarkNameTextView.setText(this.landmark.getName());

		this.setContentView(contentView);
	}

	public void onClickEvents(final View view) {
		final Intent intent =
				new Intent(this.getApplicationContext(),
						ListEventsActivity.class);

		intent.putExtra(ListEventsActivity.EVENTS_LIST_KEY, this.landmark
				.getId());

		this.startActivity(intent);
	}
}
