package com.feup.aroundme;

import java.util.ArrayList;
import java.util.Iterator;

import org.apache.http.impl.conn.tsccm.WaitingThread;

import android.util.Log;

public class Marker {

	
	private String venue; // name of the location (e.g. city, name of the theater, etc)
	private String id;    // id of the venue
	private String title; // title of the window to be displayed on the map
	private ArrayList<Event> events;
	private double lat;
	private double log;
	
	public Marker(double parseDouble, double parseDouble2, String v, String t, String id) {
		Log.w("marker", "lat: " + Double.toString(parseDouble) + " log: " + Double.toString(parseDouble2));
		lat = parseDouble;
		log = parseDouble2;
		this.title = t;
		this.venue = v;
		this.id = id;
		this.events = new ArrayList<Event>();
	}
	public String getVenue() {
		return venue;
	}
	public String getVenueID() {
		return id;
	}
	
	
	public String getTitle() {
		return title;
	}
	public ArrayList<Event> getEvents() {
		return events;
	}
	
	public String getEventString() {
		String eventList = "";
		Iterator<Event> i = events.iterator();
		while (i.hasNext()) {
			Event e = i.next();
			eventList = eventList + e.getTitle() + ":" + e.getStartTime() + "\n";
			Log.w("events", "getEventString: e.getTitle()" + e.getTitle());
		}
		Log.w("events", "getEventString: " + eventList);
		return eventList;
	}
	
	public void setVenue(String venue) {
		this.venue = venue;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public void setEvents(ArrayList<Event> events) {
		this.events = events;
	}

	public void addEvent(Event e) {
		Log.w("events", "addEvent: " + e.getTitle());
		events.add(e);
		//events.
		Log.w("events", "totalEvents" + this.getEventString());
	}
	
	public void AddEvent(Event e) {
		events.add(e);
	}
	public String getId() {
		return this.id;
	}
	public void titleToVenue() {
		this.title = this.venue;
	}
	
	public double getLat() {
		return this.lat;
	}
	public double getLog() {
		return this.log;
	}

	
	
}
