package pt.up.fe.aroundme.android.activities;

import pt.up.fe.aroundme.R;
import pt.up.fe.aroundme.database.LandmarksDAO;
import pt.up.fe.aroundme.models.Landmark;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class LandmarkActivity extends Activity {
	LandmarksDAO landmarksDAO;

	Landmark landmark;

	public static final String LANDMARK_ID_KEY = "landmark_id_key";

	@Override
	protected void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		this.landmarksDAO = new LandmarksDAO(this.getApplicationContext());

		this.landmark =
				this.landmarksDAO.getLandmarkById(this.getIntent().getExtras()
						.getInt(LandmarkActivity.LANDMARK_ID_KEY));

		final View contentView =
				this.getLayoutInflater().inflate(R.layout.landmark_view, null);

		((TextView) contentView.findViewById(R.id.landmark_name_text_view))
				.setText(this.landmark.getName());

		((TextView) contentView.findViewById(R.id.landmark_category))
				.setText(this.landmark.getCategory());

		((TextView) contentView.findViewById(R.id.landmark_description))
				.setText(this.landmark.getDescription());

		if( this.landmark.getPhone() != null ) {
			final Button callButton =
					(Button) contentView
							.findViewById(R.id.landmark_call_button);
			callButton.setText(this.landmark.getPhone().split("/")[0]);
		}

		((TextView) contentView.findViewById(R.id.landmark_location_text_view))
				.setText(this.landmark.getLocationCity() + ", "
					+ this.landmark.getLocationStreet());

		this.setContentView(contentView);
	}

	public void onClickCallPhone(final View view) {
		final String number =
				"tel:"
					+ LandmarkActivity.this.landmark.getPhone().split("/")[0]
							.trim();
		final Intent callIntent =
				new Intent(Intent.ACTION_CALL, Uri.parse(number)) {};

		LandmarkActivity.this.startActivity(callIntent);
	}

	public void onClickFacebook(final View view) {
		this.startActivity(new Intent(Intent.ACTION_VIEW, Uri
				.parse(this.landmark.getLink())));
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
