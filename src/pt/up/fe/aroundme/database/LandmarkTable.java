package pt.up.fe.aroundme.database;

public class LandmarkTable {

	public static final String TABLE_LANDMARK = "landmark";

	public static final String COLUMN_ABOUT = "about";
	public static final String COLUMN_CATEGORY = "category";
	public static final String COLUMN_CHECKINS = "checkins";
	public static final String COLUMN_COVER_ID = "cover_id";
	public static final String COLUMN_COVER_SOURCE = "cover_source";
	public static final String COLUMN_CREATER_AT = "creater_at";
	public static final String COLUMN_DESCRIPTION = "description";
	public static final String COLUMN_FB_ID = "fb_id";
	public static final String COLUMN_GENERAL_INFO = "general_info";
	public static final String COLUMN_ID = "_id";
	public static final String COLUMN_IS_PUBLISHED = "is_published";
	public static final String COLUMN_LIKES = "likes";
	public static final String COLUMN_LINK = "link";
	public static final String COLUMN_LOCATION_CITY = "location_city";
	public static final String COLUMN_LOCATION_COUNTRY = "location_country";
	public static final String COLUMN_LOCATION_LATITUDE = "location_latitude";
	public static final String COLUMN_LOCATION_LONGITUDE = "location_longitude";
	public static final String COLUMN_LOCATION_STREET = "location_street";
	public static final String COLUMN_LOCATION_ZIP = "location_zip";
	public static final String COLUMN_NAME = "name";
	public static final String COLUMN_PHONE = "phone";
	public static final String COLUMN_PUBLIC_TRANSIT = "public_transit";
	public static final String COLUMN_TALKING_ABOUT = "talking_about";
	public static final String COLUMN_UPDATED_AT = "updated_at";
	public static final String COLUMN_USERNAME = "username";
	public static final String COLUMN_WEBSITE = "website";
	public static final String COLUMN_WERE_HERE_COUNT = "were_here_count";

	/** @formatter:off */
	public static final String DATABASE_CREATE = 
			"create table " + TABLE_LANDMARK 
			+ "(" 
			+ COLUMN_ID	+ " integer primary key autoincrement, " 
			+ COLUMN_ABOUT + " text, "
			+ COLUMN_CATEGORY + " text, "
			+ ");";
	/** @formatter:on */

}
