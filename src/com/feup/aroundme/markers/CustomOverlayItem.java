package com.feup.aroundme.markers;

import java.util.ArrayList;
import com.feup.aroundme.Event;
import com.google.android.maps.GeoPoint;
import com.google.android.maps.OverlayItem;

public class CustomOverlayItem extends OverlayItem {

	ArrayList<Event> events;
	
	public CustomOverlayItem(GeoPoint point, String title, String snippet, ArrayList<Event> evs) {
		super(point, title, snippet);
		events = evs;
	}
	
	public ArrayList<Event> getEvents() {
		return events;
	}
	
	public void setEvents(ArrayList<Event> events) {
		this.events = events;
	}
}