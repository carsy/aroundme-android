package pt.up.fe.aroundme.android.activities;

import java.util.ArrayList;
import java.util.List;

import pt.up.fe.aroundme.R;
import pt.up.fe.aroundme.controllers.LandmarksController;
import pt.up.fe.aroundme.models.Landmark;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class ListLandmarksActivity extends Activity {
	LandmarksController landmarksController;

	ListView listLandmarksView;

	public static final String LANDMARKS_LIST_KEY = "landmarks_list_key";

	private List<Landmark> landmarks;

	@Override
	protected void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		this.landmarksController =
				new LandmarksController(this.getApplicationContext());

		final ViewGroup contentView =
				(ViewGroup) this.getLayoutInflater().inflate(
						R.layout.list_landmarks, null);

		this.listLandmarksView =
				(ListView) contentView.findViewById(R.id.list_landmarks_view);

		this.landmarks =
				this.landmarksController.getLandmarksById(this.getIntent()
						.getExtras().getIntArray(LANDMARKS_LIST_KEY));

		this.setContentView(contentView);
	}

	@Override
	protected void onStart() {
		super.onStart();

		this.setupListView();
	}

	private void setupListView() {
		final List<String> landmarkTitles = new ArrayList<String>();

		for(final Landmark landmark: this.landmarks) {
			landmarkTitles.add(landmark.getName());
		}

		final ArrayAdapter<String> adapter =
				new ArrayAdapter<String>(this,
						android.R.layout.simple_list_item_1, landmarkTitles);

		this.listLandmarksView.setAdapter(adapter);

		this.listLandmarksView
				.setOnItemClickListener(new OnItemClickListener() {

					@Override
					public void onItemClick(final AdapterView<?> parent,
							final View view, final int position, final long id) {
						final Landmark landmark =
								ListLandmarksActivity.this.landmarks
										.get(position);
						final Intent intent =
								new Intent(ListLandmarksActivity.this
										.getApplicationContext(),
										LandmarkActivity.class);
						intent.putExtra(LandmarkActivity.LANDMARK_ID_KEY,
								landmark.getId());
						ListLandmarksActivity.this.startActivity(intent);
					}
				});
	}
}
