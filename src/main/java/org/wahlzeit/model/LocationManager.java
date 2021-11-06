/*
 * SPDX-FileCopyrightText: 2006-2009 Dirk Riehle <dirk@riehle.org> https://dirkriehle.com
 * SPDX-License-Identifier: AGPL-3.0-or-later
 */

package org.wahlzeit.model;

import java.io.*;
import java.sql.*;
import java.util.*;

import org.wahlzeit.main.*;
import org.wahlzeit.services.*;

/**
 * Adapted Copy of PhotoManager
 */
public class LocationManager extends ObjectManager {

    protected static final LocationManager instance = new LocationManager();

    protected Map<LocationId, Location> locationCache = new HashMap<LocationId, Location>();

    public static final LocationManager getInstance() {
        return instance;
    }

    public static final boolean hasLocation(String id) {
        return hasLocation(LocationId.getIdFromString(id));
    }

    public static final boolean hasLocation(LocationId id) {
        return getLocation(id) != null;
    }

    public static final Location getLocation(String id) {
        return getLocation(LocationId.getIdFromString(id));
    }

    public static final Location getLocation(LocationId id) {
        return instance.getLocationFromId(id);
    }

    public LocationManager() {
        //Do nothing
    }

    protected boolean doHasLocation(LocationId id) {
        return locationCache.containsKey(id);
    }

    public Location getLocationFromId(LocationId id) {
        if (id.isNullId()) {
            return null;
        }

        Location result = doGetLocationFromId(id);

        if (result == null) {
            try {
                PreparedStatement stmt = getReadingStatement("SELECT * FROM locations WHERE id = ?");
                result = (Location) readObject(stmt, id.asInt());
            } catch (SQLException sex) {
                SysLog.logThrowable(sex);
            }
            if (result != null) {
                doAddLocation(result);
            }
        }

        return result;
    }

    protected Location doGetLocationFromId(LocationId id) {
        return locationCache.get(id);
    }

    protected Location createObject(ResultSet rset) throws SQLException {
        return LocationFactory.getInstance().createLocation(rset);
    }

    public void addLocation(Location location) {
        LocationId id = location.getId();
        assertIsNewLocation(id);
        doAddLocation(location);

        try {
            PreparedStatement stmt = getReadingStatement("INSERT INTO locations(id) VALUES(?)");
            createObject(location, stmt, id.asInt());
            ServiceMain.getInstance().saveGlobals();
        } catch (SQLException sex) {
            SysLog.logThrowable(sex);
        }
    }

    protected void doAddLocation(Location myLocation) {
        locationCache.put(myLocation.getId(), myLocation);
    }

    public void loadLocations(Collection<Location> result) {
        try {
            PreparedStatement stmt = getReadingStatement("SELECT * FROM locations");
            readObjects(result, stmt);
            for (Iterator<Location> i = result.iterator(); i.hasNext(); ) {
                Location location = i.next();
                if (!doHasLocation(location.getId())) {
                    doAddLocation(location);
                } else {
                    SysLog.logSysInfo("location", location.getId().asString(), "location had already been loaded");
                }
            }
        } catch (SQLException sex) {
            SysLog.logThrowable(sex);
        }

        SysLog.logSysInfo("loaded all locations");
    }

    public void saveLocation(Location location) {
        try {
            PreparedStatement stmt = getUpdatingStatement("SELECT * FROM locations WHERE id = ?");
            updateObject(location, stmt);
        } catch (SQLException sex) {
            SysLog.logThrowable(sex);
        }
    }

    public void saveLocations() {
        try {
            PreparedStatement stmt = getUpdatingStatement("SELECT * FROM locations WHERE id = ?");
            updateObjects(locationCache.values(), stmt);
        } catch (SQLException sex) {
            SysLog.logThrowable(sex);
        }
    }

    protected PreparedStatement getUpdatingStatementFromConditions(int no) throws SQLException {
        String query = "SELECT * FROM tags";
        if (no > 0) {
            query += " WHERE";
        }

        for (int i = 0; i < no; i++) {
            if (i > 0) {
                query += " OR";
            }
            query += " (tag = ?)";
        }

        return getUpdatingStatement(query);
    }

    public Location createLocation(String name, double x, double y, double z) {
        LocationId id = LocationId.getNextId();
        Location result = LocationFactory.getInstance().createLocation(id, name, x, y, z);
        addLocation(result);
        return result;
    }

    public Location createLocation(String name, Coordinate c) {
        LocationId id = LocationId.getNextId();
        Location result = LocationFactory.getInstance().createLocation(id, name, c);
        addLocation(result);
        return result;
    }

    /**
     * @methodtype assertion
     */
    protected void assertIsNewLocation(LocationId id) {
        if (hasLocation(id)) {
            throw new IllegalStateException("Location already exists!");
        }
    }

}
