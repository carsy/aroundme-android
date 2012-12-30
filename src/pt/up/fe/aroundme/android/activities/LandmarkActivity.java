package pt.up.fe.aroundme.android.activities;

import pt.up.fe.aroundme.R;
import pt.up.fe.aroundme.controllers.LandmarksController;
import pt.up.fe.aroundme.models.Landmark;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class LandmarkActivity extends Activity {
	LandmarksController landmarksController;

	Landmark landmark;

	public static final String LANDMARK_ID_KEY = "landmark_id_key";

	@Override
	protected void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		this.landmarksController =
				new LandmarksController(this.getApplicationContext());

		final View contentView =
				this.getLayoutInflater().inflate(R.layout.landmark_view, null);

		final TextView landmarkNameTextView =
				(TextView) contentView
						.findViewById(R.id.landmark_name_text_view);

		this.landmark =
				this.landmarksController.getLandmarkById(this.getIntent()
						.getExtras().getInt(LandmarkActivity.LANDMARK_ID_KEY));

		landmarkNameTextView.setText(this.landmark.getName());

		this.setContentView(contentView);
	}
}
