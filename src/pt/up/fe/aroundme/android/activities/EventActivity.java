package pt.up.fe.aroundme.android.activities;

import pt.up.fe.aroundme.R;
import pt.up.fe.aroundme.database.EventsDAO;
import pt.up.fe.aroundme.models.Event;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class EventActivity extends Activity {

	protected static final String EVENT_ID_KEY = "event_id_key";
	private EventsDAO eventsDAO;
	private Event event;

	@Override
	protected void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		this.eventsDAO = new EventsDAO(this.getApplicationContext());

		final View contentView =
				this.getLayoutInflater().inflate(R.layout.event_view, null);

		final TextView eventNameTextView =
				(TextView) contentView.findViewById(R.id.event_name_text_view);

		this.event =
				this.eventsDAO.getEventById(this.getIntent().getExtras()
						.getInt(EventActivity.EVENT_ID_KEY));

		eventNameTextView.setText(this.event.getName());

		this.setContentView(contentView);
	}
}
