/*
 * SPDX-FileCopyrightText: 2006-2009 Dirk Riehle <dirk@riehle.org> https://dirkriehle.com
 * SPDX-License-Identifier: AGPL-3.0-or-later
 */

package org.wahlzeit.model;

import java.util.Random;

/**
 * A photo id identifies a photo with a unique number.
 * The number has an equivalent string for web access. 
 * This class also hands out the ids.
 */
public class PhotoId {
	
	/**
	 * 0 is never returned from nextValue; first value is 1
	 */
	protected static int currentId = 0;
	
	/**
	 * 
	 */
	public static final int BUFFER_SIZE_INCREMENT = 64;
	
	/**
	 * 
	 */
	public static final PhotoId NULL_ID = new PhotoId(0);
	
	/**
	 * 
	 */
	protected static PhotoId[] ids = new PhotoId[BUFFER_SIZE_INCREMENT];
	
	/**
	 * What a hack :-)
	 */
	public static final int ID_START = getFromString("x1abz") + 1 ;
	
	/**
	 * 
	 */
	protected static Random randomNumber = new Random(System.currentTimeMillis());
			
	/**
	 * 
	 */
	public static int getCurrentIdAsInt() {
		//Preconditions: None
		//Postconditions: None
		return currentId;
	}
	
	/**
	 * 
	 */
	public static synchronized void setCurrentIdFromInt(int id) {
		//Preconditions: None
		currentId = id;
		ids = new PhotoId[currentId + BUFFER_SIZE_INCREMENT];
		ids[0] = NULL_ID;
		//Postconditions: None
	}
	
	/**
	 * 
	 */
	public static synchronized int getNextIdAsInt() {
		//Preconditions: None
		currentId += 1;
		if (currentId >= ids.length) {
			PhotoId[] nids = new PhotoId[currentId + BUFFER_SIZE_INCREMENT];
			System.arraycopy(ids, 0, nids, 0, currentId);
			ids = nids;
		}
		//Postconditions: None
		return currentId;
	}

	/**
	 * 
	 */
	public static PhotoId getIdFromInt(int id) {
		//Preconditions: None
		if ((id < 0) || (id > currentId)) {
			return NULL_ID;
		}
		
		// @FIXME http://en.wikipedia.org/wiki/Double-checked_locking
		PhotoId result = ids[id];
		if (result == null) {
			synchronized(ids) {
				result = ids[id];
				if (result == null) {
					result = new PhotoId(id);
					ids[id] = result;
				}
			}
		}

		//Postconditions: None
		return result;
	}
	
	/**
	 * 
	 */
	public static PhotoId getIdFromString(String id) {
		//Preconditions: None, null check for id is done in getFromString
		//Postconditions: None
		return getIdFromInt(getFromString(id));
	}
	
	/**
	 * 
	 */
	public static PhotoId getNextId() {
		//Preconditions: None
		//Postconditions: None
		return getIdFromInt(getNextIdAsInt());
	}
	
	/**
	 * 
	 */
	public static PhotoId getRandomId() {
		//Preconditions: None
		int max = getCurrentIdAsInt() - 1;
		int id = randomNumber.nextInt();
		id = (id == Integer.MIN_VALUE) ? id ++ : id;
		id = (Math.abs(id) % max) + 1;
		//Postconditions: None
		return getIdFromInt(id);
	}
	
	/**
	 * 
	 */
	protected int value = 0;
	protected String stringValue = null;

	/**
	 * 
	 */
	protected PhotoId(int myValue) {
		//Preconditions: None
		value = myValue;
		stringValue = getFromInt(myValue);
		//Postconditions: None
	}
	
	/**
	 * 
	 */
	public boolean equals(Object o) {
		// Fixed :)
		//Preconditions: Object must not be null, Object Class must match own Class, Class Invariants of the Object must hold (not checked here)
		if (o == null) {
			return false;
		}
		
		if (!(o instanceof PhotoId)) {
			return false;
		}
		
		PhotoId pid = (PhotoId) o;
		//Postconditions: None
		return isEqual(pid);
	}
	
	/**
	 * 
	 */
	public boolean isEqual(PhotoId other) {
		//Preconditions: None
		//Postconditions: None
		return other.value == value;
	}
	
	/**
	 * @methodtype get
	 */
	public int hashCode() {
		//Preconditions: None
		//Postconditions: None
		return value;
	}
	
	/**
	 * 
	 */
	public boolean isNullId() {
		//Preconditions: None
		//Postconditions: None
		return this == NULL_ID;
	}
	
	/**
	 * 
	 */
	public int asInt() {
		//Preconditions: None
		//Postconditions: None
		return value;
	}
	
	/**
	 * 
	 */
	public String asString() {
		//Preconditions: None
		//Postconditions: None
		return stringValue;
	}

	public void assertClassInvariants() {
		//There is nothing to check here, method added for completeness, will not be called
	}
	
	/**
	 * 
	 */
	public static String getFromInt(int id) {
		//Preconditions: None
		StringBuffer result = new StringBuffer(10);
		
		id += ID_START;
		for ( ; id > 0;	id = id / 36 ) {
			char letterOrDigit;
			int modulus = id % 36;
			if (modulus < 10) {
				letterOrDigit = (char) ((int) '0' + modulus);
			} else {
				letterOrDigit = (char) ((int) 'a' - 10 + modulus);
			}
			result.insert(0, letterOrDigit);

		}

		//Postconditions: None
		return "x" + result.toString();
	}
	
	/**
	 * 
	 */
	public static int getFromString(String value) {
		//Preconditions: The argument value is not null
		ContractEnforcerUtil.assertArgumentNonNull(value);
		int result = 0;		
		for (int i = 1; i < value.length(); i ++ ) {
			int temp = 0;
			char letterOrDigit = value.charAt(i);
			if (letterOrDigit < 'a') {
				temp = (int) letterOrDigit - '0';
			} else {
				temp = 10 + (int) letterOrDigit - 'a';
			}
			result = result * 36 + temp; 
		}
			
		result -= ID_START;
		if (result < 0) {
			result = 0;
		}

		//Postconditions: None
		return result;
	}
	
}
