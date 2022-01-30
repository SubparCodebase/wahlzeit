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

    //Client-Service-Collaboration with Cat; Bound role: Client
    protected Cat depictedCat;

    //Documentation for Homework cw11 - object creation of CatPhoto
    //Call to CatPhoto() => call to Photo()
    public CatPhoto() {
        //Preconditions: None
        super();
        //Postconditions: None
        assertClassInvariants();
    }

    public CatPhoto(PhotoId myId) {
        //Preconditions: The argument myId is not null, this will be check in the called constructor
        super(myId);
        //Postconditions: None
        assertClassInvariants();
    }

    public CatPhoto(Cat cat){
        //Preconditions: None
        super();
        depictedCat = cat;
        //Postconditions: None
        assertClassInvariants();
    }

    public CatPhoto(PhotoId myId, Cat cat){
        //Preconditions: The argument myId is not null, this will be checked in the superclass
        super(myId);
        depictedCat = cat;
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
        //Reading the Cat Object would go here if persistence would be needed
        //Postconditions: None
        assertClassInvariants();
    }

    @Override
    public void writeOn(ResultSet rset) throws SQLException {
        assertClassInvariants();
        //Preconditions: The argument rset is not null, this is checked in the superclass call of readFrom
        super.writeOn(rset);
        //Writing the Cat Object would go here if persistence would be needed
        //Postconditions: None
        assertClassInvariants();
    }

    public Cat getDepictedCat(){
        assertClassInvariants();
        //Preconditions: None
        //Postconditions: None
        return depictedCat;
    }

    public void setDepictedCat(Cat cat){
        assertClassInvariants();
        //Preconditions: count >= 1
        depictedCat = cat;
        //Postconditions: None
        assertClassInvariants();
    }

    @Override
    public void assertClassInvariants(){
        //If the Photo has a Cat, it also needs to assert its invariants
        //Not done here, as its not part of the homework
        //If the Photo has a Location, it also needs to assert its invariants
        if(location != null)location.assertClassInvariants();
    }
}
