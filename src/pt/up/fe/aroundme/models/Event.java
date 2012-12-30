package pt.up.fe.aroundme.models;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable
public class Event {

	@DatabaseField
	private String createdAt;

	@DatabaseField
	private String description;

	@DatabaseField
	private String endTime;

	@DatabaseField
	private String fbId;

	@DatabaseField(id = true)
	private Integer id;

	@DatabaseField
	private Boolean isDateOnly;

	@DatabaseField
	private Integer landmarkId;

	@DatabaseField
	private String location;

	@DatabaseField
	private String name;

	@DatabaseField
	private String ownerCategory;

	@DatabaseField
	private String ownerName;

	@DatabaseField
	private String pictureUrl;

	@DatabaseField
	private String privacy;

	@DatabaseField
	private String startTime;

	@DatabaseField
	private String timeZone;

	@DatabaseField
	private String updatedAt;

	@DatabaseField
	private String updatedTime;

	@DatabaseField(foreign = true, foreignAutoRefresh = true)
	private Landmark landmark;

	public String getCreatedAt() {
		return this.createdAt;
	}

	public String getDescription() {
		return this.description;
	}

	public String getEndTime() {
		return this.endTime;
	}

	public String getFbId() {
		return this.fbId;
	}

	public Integer getId() {
		return this.id;
	}

	public Boolean getIsDateOnly() {
		return this.isDateOnly;
	}

	public Integer getLandmarkId() {
		return this.landmarkId;
	}

	public String getLocation() {
		return this.location;
	}

	public String getName() {
		return this.name;
	}

	public String getOwnerCategory() {
		return this.ownerCategory;
	}

	public String getOwnerName() {
		return this.ownerName;
	}

	public String getPictureUrl() {
		return this.pictureUrl;
	}

	public String getPrivacy() {
		return this.privacy;
	}

	public String getStartTime() {
		return this.startTime;
	}

	public String getTimeZone() {
		return this.timeZone;
	}

	public String getUpdatedAt() {
		return this.updatedAt;
	}

	public String getUpdatedTime() {
		return this.updatedTime;
	}

	public Landmark getLandmark() {
		return this.landmark;
	}
}
