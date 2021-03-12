package com.example.javasqlquery;

import org.junit.jupiter.api.Test;

import static org.junit.Assert.assertEquals;

class Test_Element {

    IElement e;
        
    public Test_Element () {
        e = new ElementTransferToDB();
    }

	@Test
	public void setup() throws Exception {
        // Ensure the tables do not exist first
        e.CreateTables();
        e.PopulateTheTables();
        assertEquals( -1 , -1 );
    }

	@Test
	public void invalidEntries1() throws Exception {
        
        int res = (int)e.insertToRoot( 100 , 1 , 1 , "ELEM" , "VALUE" );
        assertEquals( 1 , res );

        res = (int)e.insertToRoot( 100 , 1 , 1 , "ELEM" , "VALUE" );
        assertEquals( -1 , res );

        res = (int)e.insertToRoot( -1 , 1 , 1 , "ELEM" , "VALUE" );
        assertEquals( -1 , res );
    }

	@Test
	public void alreadyExistingValue() throws Exception {
        e = new Element();

        int res = (int)e.insertToRoot( 1 , 1 , 1 , "ELEM" , "VALUE" );
        assertEquals( -1 , res );
        
    }
    
}