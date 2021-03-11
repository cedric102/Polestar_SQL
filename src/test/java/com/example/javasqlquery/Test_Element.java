package com.example.javasqlquery;

import org.junit.jupiter.api.Test;

import static org.junit.Assert.assertEquals;

class Test_Element {

    Element e;

    public Test_Element () {
        e = new Element();
    }

	@Test
	public void test() throws Exception {
        e = new Element();
        int res = (int)e.insertToRoot( 100 , 1 , 1 , "ELEM" , "VALUE" );
        assertEquals( 1 , res );

        res = (int)e.insertToRoot( -1 , 1 , 1 , "ELEM" , "VALUE" );
        assertEquals( -1 , res );

        res = (int)e.insertToRoot( 100 , 1 , 1 , "ELEM" , "VALUE" );
        assertEquals( -1 , res );
    }

	@Test
	public void test2() throws Exception {
        e = new Element();

        int res = (int)e.insertToRoot( -1 , 1 , 1 , "ELEM" , "VALUE" );
        assertEquals( -1 , res );
        
    }
    
}