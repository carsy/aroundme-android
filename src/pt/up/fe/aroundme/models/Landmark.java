package pt.up.fe.aroundme.models;

import java.util.ArrayList;
import java.util.List;

import com.j256.ormlite.dao.ForeignCollection;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable
public class Landmark {

	@DatabaseField
	private String about;

	@DatabaseField
	private String category;

	@DatabaseField
	private Integer checkins;

	@DatabaseField
	private String coverId;

	@DatabaseField
	private String coverSource;

	@DatabaseField
	private String createdAt;

	@DatabaseField
	private String description;

	@DatabaseField
	private String fbId;

	@DatabaseField
	private String generalInfo;

	@DatabaseField(id = true)
	private Integer id;

	@DatabaseField
	private Boolean isPublished;

	@DatabaseField
	private Integer likes;

	@DatabaseField
	private String link;

	@DatabaseField
	private String locationCity;

	@DatabaseField
	private String locationCountry;

	@DatabaseField
	private Double locationLatitude;

	@DatabaseField
	private Double locationLongitude;

	@DatabaseField
	private String locationStreet;

	@DatabaseField
	private String locationZip;

	@DatabaseField
	private String name;

	@DatabaseField
	private String phone;

	@DatabaseField
	private String publicTransit;

	@DatabaseField
	private Integer talkingAboutCount;

	@DatabaseField
	private String updatedAt;

	@DatabaseField
	private String username;

	@DatabaseField
	private String website;

	@DatabaseField
	private Integer wereHereCount;

	@ForeignCollectionField
	private ForeignCollection<Event> events;

	public String getAbout() {
		return this.about;
	}

	public String getCategory() {
		return this.category;
	}

	public Integer getCheckins() {
		return this.checkins;
	}

	public String getCoverId() {
		return this.coverId;
	}

	public String getCoverSource() {
		return this.coverSource;
	}

	public String getCreatedAt() {
		return this.createdAt;
	}

	public String getDescription() {
		return this.description;
	}

	public String getFbId() {
		return this.fbId;
	}

	public String getGeneralInfo() {
		return this.generalInfo;
	}

	public Integer getId() {
		return this.id;
	}

	public Boolean getIsPublished() {
		return this.isPublished;
	}

	public Integer getLikes() {
		return this.likes;
	}

	public String getLink() {
		return this.link;
	}

	public String getLocationCity() {
		return this.locationCity;
	}

	public String getLocationCountry() {
		return this.locationCountry;
	}

	public Double getLocationLatitude() {
		return this.locationLatitude;
	}

	public Double getLocationLongitude() {
		return this.locationLongitude;
	}

	public String getLocationStreet() {
		return this.locationStreet;
	}

	public String getLocationZip() {
		return this.locationZip;
	}

	public String getName() {
		return this.name;
	}

	public String getPhone() {
		return this.phone;
	}

	public String getPublicTransit() {
		return this.publicTransit;
	}

	public Integer getTalkingAboutCount() {
		return this.talkingAboutCount;
	}

	public String getUpdatedAt() {
		return this.updatedAt;
	}

	public String getUsername() {
		return this.username;
	}

	public String getWebsite() {
		return this.website;
	}

	public Integer getWereHereCount() {
		return this.wereHereCount;
	}

	public List<Event> getEvents() {
		final ArrayList<Event> eventsList = new ArrayList<Event>();
		for(final Event item: this.events) {
			eventsList.add(item);
		}
		return eventsList;
	}

	@Override
	public boolean equals(final Object otherLandmark) {
		return this.username.equals(((Landmark) otherLandmark).username);
	}
}
