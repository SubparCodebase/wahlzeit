package org.wahlzeit.model;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.wahlzeit.agents.AgentManager;
import org.wahlzeit.main.ServiceMain;
import org.wahlzeit.services.EmailAddress;
import org.wahlzeit.services.Language;

import java.io.File;
import java.net.URL;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class PersistenceTest {

    //Start the service Main instance to test interactions with the database.
    @BeforeClass
    public static void initTest() throws Exception {
        try {
            ServiceMain.getInstance().startUp(false, System.getProperty("user.dir") + "/build/inplaceWebapp");
        }catch (IllegalStateException e){
            //Occurs when ServiceMain was already started previously.
            //Do nothing
        }catch (IllegalThreadStateException e){
            //Occurs when the agent manager tries to start all thread when they were previously started.
            //Also do nothing
        }catch (RuntimeException e){
            System.out.println("This Exception occurs if the database is not running.");
            System.out.println("Please make sure the database is running and restart the test.");
            throw e;
        }
    }

    //Inserts a location in the database, then clears the manager cache to check if the location can be retrieved from the database.
    @Test
    public void insertLocationTest(){
        LocationManager lm = LocationManager.getInstance();
        Location l = lm.createLocation("Earths core", 0,0,0);
        lm.saveLocation(l); //Location should be in the database now
        LocationId testLocationId = l.getId();
        lm.locationCache.clear(); //This prevents getLocationFromId() to just take the location from the cache without accessing the database.
        Location location = lm.getLocationFromId(testLocationId); //Location should now be read from the database.
        assertTrue(l.isEqual(location));
    }

    //It is not possible to Test Persistence over a restart as ServiceMain would need to be supplied with a new AgentManager as the stopped Threads cant be started.
    //As such this Test can be used to check if a location can be read from the database after it was added in a previous instance.
    //Commented out as this Test cannot be run without previous setup.
    //@Test
    public void retrieveLocationSepInstanceTest(){
        //Change value of this to change to Location to compare to. This Location needs to be added before this Test can be run.
        Location testLocation = new Location(LocationId.getIdFromInt(8), "The Void", -1, -1, -1);

        LocationManager lm = LocationManager.getInstance();
        Location location = lm.getLocationFromId(testLocation.getId());
        assertTrue(location.isEqual(testLocation));
    }

    //Inserts a photo in the database, then clears the manager cache to check if the photo can be retrieved from the database.
    @Test
    public void insertImageTest() throws Exception {
        CatPhotoManager pm = CatPhotoManager.getInstance();
        String pathname = System.getProperty("user.dir");
        File f = new File(pathname + "/src/testData/TESTPIC1.jpg"); //Get test picture
        CatPhoto p = pm.createPhoto(f); //Create Photo from picture

        //Add data
        p.setOwnerName("TEST NAME");
        p.setOwnerLanguage(Language.GERMAN);
        p.setOwnerHomePage(new URL("https:\\www.no.com"));
        p.setTags(new Tags("testTag"));
        p.setOwnerId(1);
        p.setOwnerEmailAddress(EmailAddress.getFromString("testname@default.net"));
        p.setCatCount(5);

        pm.savePhoto(p); //Photo should be in the database now
        PhotoId testPhotoId = p.getId();
        //pm.photoCache.clear(); //This prevents getPhotoFromId() to just take the photo from the cache without accessing the database.
        CatPhoto photo = pm.getPhotoFromId(testPhotoId); //Photo should now be read from the database.

        //Check if read photo data matches (just one check should suffice)
        assertEquals(photo.getOwnerName(), "TEST NAME");
        assertEquals(photo.getCatCount(), p.getCatCount());
    }

    //Inserts a photo with associated location in the database, then clears the manager caches to check if the photo and location can be retrieved from the database.
    @Test
    public void insertImageWithLocationTest() throws Exception {
        CatPhotoManager pm = CatPhotoManager.getInstance();
        String pathname = System.getProperty("user.dir");
        File f = new File(pathname + "/src/testData/TESTPIC2.jpg"); //Get test picture
        CatPhoto p = pm.createPhoto(f); //Create Photo from picture

        //Add data
        p.setOwnerName("LOCATION OWNER");
        p.setOwnerLanguage(Language.GERMAN);
        p.setOwnerHomePage(new URL("https:\\\\www.earths-core.com"));
        p.setTags(new Tags("earths_core"));
        p.setOwnerId(1);
        p.setOwnerEmailAddress(EmailAddress.getFromString("owner@earths-core.com"));
        p.setCatCount(5);

        //Create and add location
        LocationManager lm = LocationManager.getInstance();
        Location l = lm.createLocation("Earths core", 0,0,0);
        p.setLocation(l);

        pm.savePhoto(p); //Photo and location should be in the database now
        PhotoId testPhotoId = p.getId();

        //Clear caches for reasons as before
        pm.photoCache.clear();
        lm.locationCache.clear();

        CatPhoto photo = pm.getPhotoFromId(testPhotoId); //Photo with location should now be read from the database.

        //Check read photo and location
        assertEquals(photo.getOwnerName(), "LOCATION OWNER");
        assertTrue(photo.getLocation().isEqual(l));
        assertEquals(photo.getLocation().getId(), l.getId());
        assertEquals(photo.getCatCount(), p.getCatCount());
    }

    //Inserts a photo with associated already in the database existing location in the database,
    // then clears the manager caches to check if the photo can be retrieved from the database.
    @Test
    public void insertImageWithExistingLocationTest() throws Exception {
        //Everything as in insertImageWithLocationTest
        CatPhotoManager pm = CatPhotoManager.getInstance();
        String pathname = System.getProperty("user.dir");
        File f = new File(pathname + "/src/testData/TESTPIC3.jpg");
        CatPhoto p = pm.createPhoto(f);
        p.setOwnerName("LOCATION OWNER-IN-CHIEF");
        p.setOwnerLanguage(Language.GERMAN);
        p.setOwnerHomePage(new URL("https:\\\\www.earths-core.com"));
        p.setTags(new Tags("earths_core"));
        p.setOwnerId(1);
        p.setOwnerEmailAddress(EmailAddress.getFromString("owner-in-chief@earths-core.com"));
        p.setCatCount(5);

        //Add last added location to the photo (will be the location from insertImageWithLocationTest)
        LocationManager lm = LocationManager.getInstance();
        Location l = lm.getLocationFromId(LocationId.getIdFromInt(LocationId.getCurrentIdAsInt()));
        p.setLocation(l);

        pm.savePhoto(p); //Photo should be in the database now, no new location should be added to the database
        PhotoId testPhotoId = p.getId();

        //Clear caches for reasons as before
        pm.photoCache.clear();
        lm.locationCache.clear();

        CatPhoto photo = pm.getPhotoFromId(testPhotoId); //Photo with location should now be read from the database.

        //Check read photo and location
        assertEquals(photo.getOwnerName(), "LOCATION OWNER-IN-CHIEF");
        assertTrue(photo.getLocation().isEqual(l));
        assertEquals(photo.getLocation().getId(), l.getId());
        assertEquals(photo.getCatCount(), p.getCatCount());
    }

    //Creates both a normal Photo and a Cat Photo, saves them and attempts to read them as both CatPhoto and normal Photo
    @Test
    public void checkPhotoAndCatPhotoDifference() throws Exception{
        CatPhotoManager cpm = CatPhotoManager.getInstance();
        PhotoManager pm = PhotoManager.getInstance();
        String pathname = System.getProperty("user.dir");
        //Get test pictures
        File f1 = new File(pathname + "/src/testData/TESTPIC4.jpg");
        File f2 = new File(pathname + "/src/testData/TESTPIC5.jpg");
        //Create Photo from picture
        CatPhoto p1 = cpm.createPhoto(f1);
        Photo p2 = pm.createPhoto(f2);

        //Add data p1
        p1.setOwnerName("CAT OWNER");
        p1.setOwnerLanguage(Language.GERMAN);
        p1.setOwnerHomePage(new URL("https:\\\\www.cat.com"));
        p1.setTags(new Tags("cat"));
        p1.setOwnerId(1);
        p1.setOwnerEmailAddress(EmailAddress.getFromString("owner@cat.com"));
        p1.setCatCount(1);

        //Add data p2
        p2.setOwnerName("NOT CAT OWNER");
        p2.setOwnerLanguage(Language.GERMAN);
        p2.setOwnerHomePage(new URL("https:\\\\www.no-cat.com"));
        p2.setTags(new Tags("no_cat"));
        p2.setOwnerId(1);
        p2.setOwnerEmailAddress(EmailAddress.getFromString("owner@no-cat.com"));

        //Photos should be in the database now
        cpm.savePhoto(p1);
        pm.savePhoto(p2);
        PhotoId testPhotoId1 = p1.getId();
        PhotoId testPhotoId2 = p2.getId();

        //Clear caches for reasons as before
        cpm.photoCache.clear();
        pm.photoCache.clear();

        //Photos should now be read from the database.
        //Read saved CatPhoto as CatPhoto
        CatPhoto p1cat = cpm.getPhotoFromId(testPhotoId1);
        //Read saved Photo as CatPhoto (should not work, assertion of the class invariant should fail)
        try {
            CatPhoto p2cat = cpm.getPhotoFromId(testPhotoId2);
        }catch (AssertionError e){
            //Working as intended
        }

        //Read saved CatPhoto as Photo (should also work)
        Photo p1base = pm.getPhotoFromId(testPhotoId1);
        //Read saved Photo as Photo (definetly works)
        Photo p2base = pm.getPhotoFromId(testPhotoId2);

        //Check read photo and location
        assertEquals(p1cat.getOwnerName(), p1base.getOwnerName(), "CAT OWNER");
        assertEquals(p2base.getOwnerName(), "NOT CAT OWNER");
        assertEquals(p1cat.getCatCount(), 1);
    }

}
