package pt.up.fe.aroundme.android.activities;

import java.util.ArrayList;
import java.util.List;

import pt.up.fe.aroundme.R;
import pt.up.fe.aroundme.database.EventsDAO;
import pt.up.fe.aroundme.models.Event;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class ListEventsActivity extends Activity {
	EventsDAO eventsDAO;

	ListView listEventsView;

	public static final String EVENTS_LIST_KEY = "events_list_key";

	private List<Event> events;
	private int landmarkId;

	@Override
	protected void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		this.eventsDAO = new EventsDAO(this.getApplicationContext());

		final ViewGroup contentView =
				(ViewGroup) this.getLayoutInflater().inflate(
						R.layout.list_events, null);

		this.listEventsView =
				(ListView) contentView.findViewById(R.id.list_events_view);

		this.landmarkId = this.getIntent().getExtras().getInt(EVENTS_LIST_KEY);
		this.events = this.eventsDAO.getEventsByLandmark(this.landmarkId);

		this.setContentView(contentView);
	}

	@Override
	protected void onStart() {
		super.onStart();

		this.setupListView();
	}

	private void setupListView() {
		final List<String> eventTitles = new ArrayList<String>();

		for(final Event event: this.events) {
			eventTitles.add(event.getName());
		}

		final ArrayAdapter<String> adapter =
				new ArrayAdapter<String>(this,
						android.R.layout.simple_list_item_1, eventTitles);

		this.listEventsView.setAdapter(adapter);

		this.listEventsView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(final AdapterView<?> parent,
					final View view, final int position, final long id) {
				final Event event =
						ListEventsActivity.this.events.get(position);
				final Intent intent =
						new Intent(ListEventsActivity.this
								.getApplicationContext(), EventActivity.class);
				intent.putExtra(EventActivity.EVENT_ID_KEY, event.getId());
				ListEventsActivity.this.startActivity(intent);
			}
		});
	}
}
