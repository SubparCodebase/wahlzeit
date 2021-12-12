/*
 * SPDX-FileCopyrightText: 2006-2009 Dirk Riehle <dirk@riehle.org> https://dirkriehle.com
 * SPDX-License-Identifier: AGPL-3.0-or-later
 */

package org.wahlzeit.model;

import java.sql.*;
import java.net.*;

import org.wahlzeit.services.*;
import org.wahlzeit.utils.*;

/**
 * A photo represents a user-provided (uploaded) photo.
 */
public class Photo extends DataObject {

	/**
	 * 
	 */
	public static final String IMAGE = "image";
	public static final String THUMB = "thumb";
	public static final String LINK = "link";
	public static final String PRAISE = "praise";
	public static final String NO_VOTES = "noVotes";
	public static final String CAPTION = "caption";
	public static final String DESCRIPTION = "description";
	public static final String KEYWORDS = "keywords";

	public static final String TAGS = "tags";

	public static final String STATUS = "status";
	public static final String IS_INVISIBLE = "isInvisible";
	public static final String UPLOADED_ON = "uploadedOn";
	
	/**
	 * 
	 */
	public static final int MAX_PHOTO_WIDTH = 420;
	public static final int MAX_PHOTO_HEIGHT = 600;
	public static final int MAX_THUMB_PHOTO_WIDTH = 105;
	public static final int MAX_THUMB_PHOTO_HEIGHT = 150;
	
	/**
	 * 
	 */
	protected PhotoId id = null;
	
	/**
	 * 
	 */
	protected int ownerId = 0;
	protected String ownerName;

	/**
	 * 
	 */
	protected boolean ownerNotifyAboutPraise = false;
	protected EmailAddress ownerEmailAddress = EmailAddress.EMPTY;
	protected Language ownerLanguage = Language.ENGLISH;
	protected URL ownerHomePage;
	
	/**
	 * 
	 */
	protected int width;
	protected int height;
	protected PhotoSize maxPhotoSize = PhotoSize.MEDIUM; // derived
	
	/**
	 * 
	 */
	protected Tags tags = Tags.EMPTY_TAGS;

	/**
	 * 
	 */
	protected PhotoStatus status = PhotoStatus.VISIBLE;
	
	/**
	 * 
	 */
	protected int praiseSum = 10;
	protected int noVotes = 1;
	
	/**
	 * 
	 */
	protected long creationTime = System.currentTimeMillis();

	/**
	 *
	 */
	protected Location location;
	
	/**
	 * 
	 */
	public Photo() {
		//Preconditions: None
		id = PhotoId.getNextId();
		incWriteCount();
		//Postconditions: None
	}
	
	/**
	 * 
	 * @methodtype constructor
	 */
	public Photo(PhotoId myId) {
		//Preconditions: The argument myId is not null
		ContractEnforcerUtil.assertArgumentNonNull(myId);
		id = myId;
		
		incWriteCount();
		//Postconditions: None
	}
	
	/**
	 * 
	 * @methodtype constructor
	 */
	public Photo(ResultSet rset) throws SQLException {
		//Preconditions: The argument rset is not null, this is checked in readFrom
		readFrom(rset);
		//Postconditions: None
		//ClassInvariants are asserted in readFrom
	}

	/**
	 * 
	 * @methodtype get
	 */
	public String getIdAsString() {
		assertClassInvariants();
		//Preconditions: None
		//Postconditions: None
		return String.valueOf(id.asInt());
	}
	
	/**
	 * 
	 */
	public void readFrom(ResultSet rset) throws SQLException {
		//ClassInvariants are not checked here, since we override the Object anyway
		//Preconditions: The argument rset is not null
		ContractEnforcerUtil.assertArgumentNonNull(rset);
		//Id cant be missing
		id = PhotoId.getIdFromInt(rset.getInt("id"));

		ownerId = rset.getInt("owner_id");
		ownerName = rset.getString("owner_name");
		
		ownerNotifyAboutPraise = rset.getBoolean("owner_notify_about_praise");
		ownerEmailAddress = EmailAddress.getFromString(rset.getString("owner_email_address"));
		ownerLanguage = Language.getFromInt(rset.getInt("owner_language"));
		ownerHomePage = StringUtil.asUrl(rset.getString("owner_home_page"));

		width = rset.getInt("width");
		height = rset.getInt("height");

		tags = new Tags(rset.getString("tags"));

		status = PhotoStatus.getFromInt(rset.getInt("status"));
		praiseSum = rset.getInt("praise_sum");
		noVotes = rset.getInt("no_votes");

		creationTime = rset.getLong("creation_time");

		maxPhotoSize = PhotoSize.getFromWidthHeight(width, height);

		//Getting the location by retrieving the location with the id saved in the database.
		location = LocationManager.getLocation(LocationId.getIdFromInt(rset.getInt("location")));
		//Postconditions: None
	}
	
	/**
	 * 
	 */
	public void writeOn(ResultSet rset) throws SQLException {
		assertClassInvariants();
		//Preconditions: The argument rset is not null
		ContractEnforcerUtil.assertArgumentNonNull(rset);
		rset.updateInt("id", id.asInt());
		rset.updateInt("owner_id", ownerId);
		rset.updateString("owner_name", ownerName);
		rset.updateBoolean("owner_notify_about_praise", ownerNotifyAboutPraise);
		rset.updateString("owner_email_address", ownerEmailAddress.asString());
		rset.updateInt("owner_language", ownerLanguage.asInt());
		rset.updateString("owner_home_page", ownerHomePage.toString());
		rset.updateInt("width", width);
		rset.updateInt("height", height);
		rset.updateString("tags", tags.asString());
		rset.updateInt("status", status.asInt());
		rset.updateInt("praise_sum", praiseSum);
		rset.updateInt("no_votes", noVotes);
		rset.updateLong("creation_time", creationTime);
		if(location!=null){
			//Saving the location of the photo
			//By calling the saving of the location here, the functions to save photos did not need to be adapted.
			LocationManager.getInstance().saveLocation(location);
			//The id of the location will be added to the database for association.
			rset.updateInt("location", location.getId().asInt());
		}
		//Postconditions: We would need to check if the data is now present in the database,
		//but should we try to call the PhotoManager to read the now added photo from the database
		//it would just return it from the cache, and deleting the cache after every write operation is not reasonable
		assertClassInvariants();
	}

	/**
	 * 
	 */
	public void writeId(PreparedStatement stmt, int pos) throws SQLException {
		assertClassInvariants();
		//Preconditions: The argument stmt is not null
		ContractEnforcerUtil.assertArgumentNonNull(stmt);
		stmt.setInt(pos, id.asInt());
		//Postconditions: Same problems as in writeOn()
	}
	
	/**
	 * 
	 * @methodtype get
	 */
	public PhotoId getId() {
		assertClassInvariants();
		//Preconditions: None
		//Postconditions: None
		return id;
	}
	
	/**
	 * 
	 * @methodtype get
	 */
	public int getOwnerId() {
		assertClassInvariants();
		//Preconditions: None
		//Postconditions: None
		return ownerId;
	}
	
	/**
	 * 
	 * @methodtype set
	 */
	public void setOwnerId(int newId) {
		assertClassInvariants();
		//Preconditions: None
		ownerId = newId;
		incWriteCount();
		//Postconditions: None
		assertClassInvariants();
	}
	
	/**
	 * 
	 * @methodtype get
	 */
	public String getOwnerName() {
		assertClassInvariants();
		//Preconditions: None
		//Postconditions: None
		return ownerName;
	}
	
	/**
	 * 
	 * @methodtype set
	 */
	public void setOwnerName(String newName) {
		assertClassInvariants();
		//Preconditions: The argument newName is not null
		ContractEnforcerUtil.assertArgumentNonNull(newName);
		ownerName = newName;
		incWriteCount();
		//Postconditions: None
		assertClassInvariants();
	}
	
	/**
	 * 
	 * @methodtype get
	 */
	public String getSummary(ModelConfig cfg) {
		assertClassInvariants();
		//Preconditions: None
		//Postconditions: None
		return cfg.asPhotoSummary(ownerName);
	}

	/**
	 * 
	 * @methodtype get
	 */
	public String getCaption(ModelConfig cfg) {
		assertClassInvariants();
		//Preconditions: The argument cfg is not null
		ContractEnforcerUtil.assertArgumentNonNull(cfg);
		//Postconditions: None
		return cfg.asPhotoCaption(ownerName, ownerHomePage);
	}

	/**
	 * 
	 * @methodtype get
	 */
	public boolean getOwnerNotifyAboutPraise() {
		assertClassInvariants();
		//Preconditions: None
		//Postconditions: None
		return ownerNotifyAboutPraise;
	}
	
	/**
	 * 
	 * @methodtype set
	 */
	public void setOwnerNotifyAboutPraise(boolean newNotifyAboutPraise) {
		assertClassInvariants();
		//Preconditions: None
		ownerNotifyAboutPraise = newNotifyAboutPraise;
		incWriteCount();
		//Postconditions: None
		assertClassInvariants();
	}

	/**
	 * 
	 * @methodtype get
	 */
	public EmailAddress getOwnerEmailAddress() {
		assertClassInvariants();
		//Preconditions: None
		//Postconditions: None
		return ownerEmailAddress;
	}
	
	/**
	 * 
	 * @methodtype set
	 */
	public void setOwnerEmailAddress(EmailAddress newEmailAddress) {
		assertClassInvariants();
		//Preconditions: The argument newEmailAddress is not null
		ContractEnforcerUtil.assertArgumentNonNull(newEmailAddress);
		ownerEmailAddress = newEmailAddress;
		incWriteCount();
		//Postconditions: None
		assertClassInvariants();
	}

	/**
	 * 
	 */
	public Language getOwnerLanguage() {
		assertClassInvariants();
		//Preconditions: None
		//Postconditions: None
		return ownerLanguage;
	}
	
	/**
	 * 
	 */
	public void setOwnerLanguage(Language newLanguage) {
		assertClassInvariants();
		//Preconditions: The argument newLanguage is not null
		ContractEnforcerUtil.assertArgumentNonNull(newLanguage);
		ownerLanguage = newLanguage;
		incWriteCount();
		//Postconditions: None
		assertClassInvariants();
	}

	/**
	 * 
	 * @methodtype get
	 */
	public URL getOwnerHomePage() {
		assertClassInvariants();
		//Preconditions: None
		//Postconditions: None
		return ownerHomePage;
	}
	
	/**
	 * 
	 * @methodtype set
	 */
	public void setOwnerHomePage(URL newHomePage) {
		assertClassInvariants();
		//Preconditions: The argument newHomePage is not null
		ContractEnforcerUtil.assertArgumentNonNull(newHomePage);
		ownerHomePage = newHomePage;
		incWriteCount();
		//Postconditions: None
		assertClassInvariants();
	}
	
	/**
	 * 
	 * @methodtype boolean-query
	 */
	public boolean hasSameOwner(Photo photo) {
		assertClassInvariants();
		//Preconditions: The argument photo is not null
		ContractEnforcerUtil.assertArgumentNonNull(photo);
		//Postconditions: None
		return photo.getOwnerEmailAddress().equals(ownerEmailAddress);
	}

	/**
	 * 
	 * @methodtype boolean-query
	 */
	public boolean isWiderThanHigher() {
		assertClassInvariants();
		//Preconditions: None
		//Postconditions: None
		return (height * MAX_PHOTO_WIDTH) < (width * MAX_PHOTO_HEIGHT);
	}
	
	/**
	 * 
	 * @methodtype get
	 */
	public int getWidth() {
		assertClassInvariants();
		//Preconditions: None
		//Postconditions: None
		return width;
	}
	
	/**
	 * 
	 * @methodtype get
	 */
	public int getHeight() {
		assertClassInvariants();
		//Preconditions: None
		//Postconditions: None
		return height;
	}
	
	/**
	 * 
	 * @methodtype get
	 */
	public int getThumbWidth() {
		assertClassInvariants();
		//Preconditions: None
		//Postconditions: None
		return isWiderThanHigher() ? MAX_THUMB_PHOTO_WIDTH : (width * MAX_THUMB_PHOTO_HEIGHT / height);
	}
	
	/**
	 * 
	 * @methodtype get
	 */
	public int getThumbHeight() {
		assertClassInvariants();
		//Preconditions: None
		//Postconditions: None
		return isWiderThanHigher() ? (height * MAX_THUMB_PHOTO_WIDTH / width) : MAX_THUMB_PHOTO_HEIGHT;
	}
	
	/**
	 * 
	 * @methodtype set
	 */
	public void setWidthAndHeight(int newWidth, int newHeight) {
		assertClassInvariants();
		//Preconditions: None
		width = newWidth;
		height = newHeight;

		maxPhotoSize = PhotoSize.getFromWidthHeight(width, height);

		incWriteCount();
		//Postconditions: None
		assertClassInvariants();
	}
	
	/**
	 * Can this photo satisfy provided photo size?
	 * 
	 * @methodtype boolean-query
	 */
	public boolean hasPhotoSize(PhotoSize size) {
		assertClassInvariants();
		//Preconditions: The argument size is not null
		ContractEnforcerUtil.assertArgumentNonNull(size);
		//Postconditions: None
		return maxPhotoSize.asInt() >= size.asInt();
	}
	
	/**
	 * 
	 * @methodtype get
	 */
	public PhotoSize getMaxPhotoSize() {
		assertClassInvariants();
		//Preconditions: None
		//Postconditions: None
		return maxPhotoSize;
	}
	
	/**
	 * 
	 * @methodtype get
	 */
	public double getPraise() {
		assertClassInvariants();
		//Preconditions: None
		//Postconditions: None
		return (double) praiseSum / noVotes;
	}
	
	/**
	 * 
	 * @methodtype get
	 */
	public String getPraiseAsString(ModelConfig cfg) {
		assertClassInvariants();
		//Preconditions: The argument cfg is not null
		ContractEnforcerUtil.assertArgumentNonNull(cfg);
		//Postconditions: None
		return cfg.asPraiseString(getPraise());
	}
	
	/**
	 * 
	 */
	public void addToPraise(int value) {
		assertClassInvariants();
		//Preconditions: None
		praiseSum += value;
		noVotes += 1;
		incWriteCount();
		//Postconditions: None
		assertClassInvariants();
	}
	
	/**
	 * 
	 * @methodtype boolean-query
	 */
	public boolean isVisible() {
		assertClassInvariants();
		//Preconditions: None
		//Postconditions: None
		return status.isDisplayable();
	}
	
	/**
	 * 
	 * @methodtype get
	 */
	public PhotoStatus getStatus() {
		assertClassInvariants();
		//Preconditions: None
		//Postconditions: None
		return status;
	}
	
	/**
	 * 
	 * @methodtype set
	 */
	public void setStatus(PhotoStatus newStatus) {
		assertClassInvariants();
		//Preconditions: The argument newStatus is not null
		ContractEnforcerUtil.assertArgumentNonNull(newStatus);
		status = newStatus;
		incWriteCount();
		//Postconditions: None
		assertClassInvariants();
	}
	
	/**
	 * 
	 * @methodtype boolean-query
	 */
	public boolean hasTag(String tag) {
		assertClassInvariants();
		//Preconditions: The argument tag is not null
		ContractEnforcerUtil.assertArgumentNonNull(tag);
		//Postconditions: None
		return tags.hasTag(tag);
	}
	
	/**
	 * 
	 * @methodtype get
	 */
	public Tags getTags() {
		assertClassInvariants();
		//Preconditions: None
		//Postconditions: None
		return tags;
	}

	/**
	 * 
	 * @methodtype set
	 */
	public void setTags(Tags newTags) {
		assertClassInvariants();
		//Preconditions: The argument newTags is not null
		ContractEnforcerUtil.assertArgumentNonNull(newTags);
		tags = newTags;
		incWriteCount();
		assertClassInvariants();
		//Postconditions: None
		assertClassInvariants();
	}
	
	/**
	 * 
	 * @methodtype get
	 */
	public long getCreationTime() {
		assertClassInvariants();
		//Preconditions: None
		//Postconditions: None
		return creationTime;
	}

	/**
	 *
	 * @methodtype get
	 */
	public Location getLocation() {
		assertClassInvariants();
		//Preconditions: None
		//Postconditions: None
		return location;
	}

	/**
	 *
	 * @methodtype get
	 */
	public void setLocation(Location loc){
		assertClassInvariants();
		//Preconditions: The argument loc is not null
		ContractEnforcerUtil.assertArgumentNonNull(loc);
		location = loc;
		incWriteCount();
		//Postconditions: None
		assertClassInvariants();
	}

	public void assertClassInvariants(){
		//If the Photo has a Location, it also needs to assert its invariants
		if(location != null)location.assertClassInvariants();
	}
}
