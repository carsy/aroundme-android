package com.feup.aroundme;

public class Event {
	
	private String id;
	private String title;
	private String startTime;
	private String endTime;
	private String location;
	private String lat;
	private String log;
	private String venueID; // id of the venue
	private String venue;   // venue name

	public Event(String id, String t, String time, String location) {
		this.id = id;
		this.title = t;
		this.startTime = time;
		this.location = location;
		this.venueID = "";
	}

	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getStartTime() {
		return startTime;
	}
	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}
	public String getEndTime() {
		return endTime;
	}
	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}
	public String getLat() {
		return lat;
	}
	public void setLat(String lat) {
		this.lat = lat;
	}
	public String getLog() {
		return log;
	}
	public void setLog(String log) {
		this.log = log;
	}

	public void setVenueID(String venue) {
		this.venueID = venue;
	}
	public String getVenueID() {
		return venueID;
	}
	
	public void setVenue(String venue) {
		this.venue = venue;
	}
	public String getVenue() {
		return venue;
	}


}
