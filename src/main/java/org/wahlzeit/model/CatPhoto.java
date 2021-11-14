package org.wahlzeit.model;

import java.sql.ResultSet;
import java.sql.SQLException;

public class CatPhoto extends Photo{

    protected int catCount;

    public CatPhoto() {
        this(1);
    }

    public CatPhoto(PhotoId myId) {
        this(myId, 1);
    }

    public CatPhoto(int count){
        super();
        catCount = count;
    }

    public CatPhoto(PhotoId myId, int count){
        super(myId);
        catCount = count;
    }

    public CatPhoto(ResultSet rset) throws SQLException {
        readFrom(rset);
    }

    @Override
    public void readFrom(ResultSet rset) throws SQLException {
        super.readFrom(rset);
        catCount = rset.getInt("cat_count");
    }

    @Override
    public void writeOn(ResultSet rset) throws SQLException {
        super.writeOn(rset);
        rset.updateInt("cat_count", catCount);
    }

    public int getCatCount(){
        return catCount;
    }

    public void setCatCount(int count){
        //A CatPhoto needs to have atleast one cat.
        if(count >= 1) catCount = count;
    }
}
