/*
 * SPDX-FileCopyrightText: 2006-2009 Dirk Riehle <dirk@riehle.org> https://dirkriehle.com
 * SPDX-License-Identifier: AGPL-3.0-or-later
 */

package org.wahlzeit.model;

import java.sql.*;

import org.wahlzeit.services.*;

/**
 * An Abstract Factory for creating photos and related objects.
 */
@PatternInstance(
		patternName = "Abstract Factory",
		participants = {
			"AbstractFactory", "ConcreteFactory"
		}
)
@PatternInstance(
		patternName = "Factory Method",
		participants = {
				"Creator", "ConcreteCreator"
		}
)
@PatternInstance(
		patternName = "Singleton",
		participants = {
				"Singleton"
		}
)
public class PhotoFactory {
	
	/**
	 * Hidden singleton instance; needs to be initialized from the outside.
	 */
	private static PhotoFactory instance = null;
	
	/**
	 * Public singleton access method.
	 */
	public static synchronized PhotoFactory getInstance() {
		//Preconditions: None
		if (instance == null) {
			SysLog.logSysInfo("setting generic PhotoFactory");
			setInstance(new PhotoFactory());
		}

		//Postconditions: None
		return instance;
	}
	
	/**
	 * Method to set the singleton instance of PhotoFactory.
	 */
	protected static synchronized void setInstance(PhotoFactory photoFactory) {
		if (instance != null) {
			throw new IllegalStateException("attempt to initialize PhotoFactory twice");
		}
		
		instance = photoFactory;
	}
	
	/**
	 * Hidden singleton instance; needs to be initialized from the outside.
	 */
	public static void initialize() {
		//Preconditions: None
		getInstance(); // drops result due to getInstance() side-effects
		//Postconditions: None
	}
	
	/**
	 * 
	 */
	protected PhotoFactory() {
		// do nothing
	}

	/**
	 * @methodtype factory
	 */
	//One create Photo per Photo Constructor, all checks are done in the Photo class itself
	public Photo createPhoto() {
		//Preconditions: None
		return new Photo();
		//Postconditions: None
	}
	
	/**
	 * 
	 */
	public Photo createPhoto(PhotoId id) {
		//Preconditions: None
		return new Photo(id);
		//Postconditions: None
	}
	
	/**
	 * 
	 */
	public Photo createPhoto(ResultSet rs) throws SQLException {
		//Preconditions: None
		return new Photo(rs);
		//Postconditions: None
	}
	
	/**
	 * 
	 */
	public PhotoFilter createPhotoFilter() {
		//Preconditions: None
		return new PhotoFilter();
		//Postconditions: None
	}
	
	/**
	 * 
	 */
	public PhotoTagCollector createPhotoTagCollector() {
		//Preconditions: None
		return new PhotoTagCollector();
		//Postconditions: None
	}

	public void assertClassInvariants() {
		//There is nothing to check here, method added for completeness, will not be called
	}

}
