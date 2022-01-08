package org.wahlzeit.model;

import java.sql.ResultSet;
import java.sql.SQLException;

@PatternInstance(
        patternName = "Abstract Factory",
        participants = {
                "AbstractProduct", "ConcreteProduct"
        }
)
@PatternInstance(
        patternName = "Factory Method",
        participants = {
                "Product", "ConcreteProduct"
        }
)
public class CatPhoto extends Photo{

    protected int catCount;

    public CatPhoto() {
        //Preconditions: None
        this(1);
        //Postconditions: None
        //ClassInvariants are checked in the called constructor
    }

    public CatPhoto(PhotoId myId) {
        //Preconditions: The argument myId is not null, this will be check in the called constructor
        this(myId, 1);
        //Postconditions: None
        //ClassInvariants are checked in the called constructor
    }

    public CatPhoto(int count){
        //Preconditions: None
        super();
        catCount = count;
        //Postconditions: None
        assertClassInvariants();
    }

    public CatPhoto(PhotoId myId, int count){
        //Preconditions: The argument myId is not null, this will be checked in the superclass
        super(myId);
        catCount = count;
        //Postconditions: None
        assertClassInvariants();
    }

    public CatPhoto(ResultSet rset) throws SQLException {
        //Preconditions: The argument rset is not null, this is checked in readFrom
        readFrom(rset);
        //Postconditions: None
        assertClassInvariants();
    }

    @Override
    public void readFrom(ResultSet rset) throws SQLException {
        //ClassInvariants are not checked here, since we override the Object anyway
        //Preconditions: The argument rset is not null, this is checked in the superclass call of readFrom
        super.readFrom(rset);
        catCount = rset.getInt("cat_count");
        //Postconditions: None
        assertClassInvariants();
    }

    @Override
    public void writeOn(ResultSet rset) throws SQLException {
        assertClassInvariants();
        //Preconditions: The argument rset is not null, this is checked in the superclass call of readFrom
        super.writeOn(rset);
        rset.updateInt("cat_count", catCount);
        //Postconditions: None
        assertClassInvariants();
    }

    public int getCatCount(){
        assertClassInvariants();
        //Preconditions: None
        //Postconditions: None
        return catCount;
    }

    public void setCatCount(int count){
        assertClassInvariants();
        //Preconditions: count >= 1
        //A CatPhoto needs to have atleast one cat.
        if(count >= 1) catCount = count;
        //Postconditions: None
        assertClassInvariants();
    }

    @Override
    public void assertClassInvariants(){
        //Invariant: A CatPhoto needs to have atleast one cat.
        assert catCount > 0;
        //If the Photo has a Location, it also needs to assert its invariants
        if(location != null)location.assertClassInvariants();
    }
}
