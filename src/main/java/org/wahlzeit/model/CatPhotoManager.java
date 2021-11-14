package org.wahlzeit.model;

import org.wahlzeit.services.SysLog;

import java.io.File;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class CatPhotoManager extends PhotoManager{

    protected static final CatPhotoManager instance = new CatPhotoManager();

    public static CatPhotoManager getInstance(){
        return instance;
    }

    public CatPhotoManager() {
        super();
    }

    @Override
    public CatPhoto getPhotoFromId(PhotoId id) {
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

        return result;
    }

    @Override
    protected CatPhoto doGetPhotoFromId(PhotoId id) {
        return (CatPhoto) super.doGetPhotoFromId(id);
    }

    @Override
    protected CatPhoto createObject(ResultSet rset) throws SQLException {
        return CatPhotoFactory.getInstance().createPhoto(rset);
    }

    @Override
    public CatPhoto createPhoto(File file) throws Exception {
        PhotoId id = PhotoId.getNextId();
        CatPhoto result = PhotoUtil.createCatPhoto(file, id);
        addPhoto(result);
        return result;
    }
}
