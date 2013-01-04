package pt.up.fe.aroundme.android.activities;

import java.util.ArrayList;
import java.util.List;

import pt.up.fe.aroundme.R;
import pt.up.fe.aroundme.database.EventsDAO;
import pt.up.fe.aroundme.database.LandmarksDAO;
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
	LandmarksDAO landmarksDAO;

	ListView listLandmarksView;

	public static final String LANDMARKS_LIST_KEY = "landmarks_list_key";

	private List<Landmark> landmarks;

	@Override
	protected void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		this.landmarksDAO = new LandmarksDAO(this.getApplicationContext());

		final ViewGroup contentView =
				(ViewGroup) this.getLayoutInflater().inflate(
						R.layout.list_landmarks, null);

		this.listLandmarksView =
				(ListView) contentView.findViewById(R.id.list_landmarks_view);

		this.landmarks =
				this.landmarksDAO.getLandmarksByUsername(this.getIntent()
						.getExtras().getStringArray(LANDMARKS_LIST_KEY));

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
			final int eventsSize =
					new EventsDAO(this.getApplicationContext())
							.getEventsByLandmark(landmark.getId()).size();
			final String title =
					" ("
						+ (eventsSize > 0 ? eventsSize + " event"
							+ (eventsSize > 1 ? "s" : "") : "no events") + ")";
			landmarkTitles.add(landmark.getName() + title);
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
