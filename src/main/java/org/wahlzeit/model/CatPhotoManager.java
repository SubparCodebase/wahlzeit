package org.wahlzeit.model;

import org.wahlzeit.services.SysLog;

import java.io.File;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

@PatternInstance(
        patternName = "Singleton",
        participants = {
                "Singleton"
        }
)
//My reasoning behind this, is that methods like getPhotoFromId call doGetPhotoFromId, which could be overridden in a
//subclass to change the behavior, while the part that reads from the database stays intact.
//This seems very similar to the "Template Method" design pattern, with the only problem being that there is currently
//no subclass which makes use of this pattern.
@PatternInstance(
        patternName = "Template Method",
        participants = {
                "AbstractClass", "ConcreteClass"
        }
)
public class CatPhotoManager extends PhotoManager{

    protected static final CatPhotoManager instance = new CatPhotoManager();

    public static CatPhotoManager getInstance(){
        //Preconditions: None
        //Postconditions: None
        return instance;
    }

    public CatPhotoManager() {
        //Preconditions: None
        //Postconditions: None
        super();
    }

    @Override
    public CatPhoto getPhotoFromId(PhotoId id) {
        //Preconditions: The argument id is not null
        ContractEnforcerUtil.assertArgumentNonNull(id);
        if (id.isNullId()) {
            return null;
        }

        CatPhoto result = doGetPhotoFromId(id);

        if (result == null) {
            try {
                PreparedStatement stmt = getReadingStatement("SELECT * FROM photos WHERE id = ?");
                result = (CatPhoto) readObject(stmt, id.asInt());
            } catch (SQLException sex) {
                SysLog.logThrowable(sex);
            }
            if (result != null) {
                doAddPhoto(result);
            }
        }

        //Postconditions: Location is now added to the Cache, if the result was not null
        if(result!=null)assert photoCache.get(id) != null;
        return result;
    }

    @Override
    protected CatPhoto doGetPhotoFromId(PhotoId id) {
        //Preconditions: None, null check for id is done in calling method
        //Postconditions: None
        return (CatPhoto) super.doGetPhotoFromId(id);
    }

    @Override
    protected CatPhoto createObject(ResultSet rset) throws SQLException {
        //Preconditions: None, null check for rset is done in createPhoto
        //Postconditions: None
        return CatPhotoFactory.getInstance().createPhoto(rset);
    }

    @Override
    public CatPhoto createPhoto(File file) throws Exception {
        //Preconditions: The argument file is not null, this is checked in PhotoUtil.createCatPhoto
        PhotoId id = PhotoId.getNextId();
        CatPhoto result = PhotoUtil.createCatPhoto(file, id);
        addPhoto(result);
        //Postconditions: Location is now added to the Cache, this is checked in addPhoto
        return result;
    }
}
