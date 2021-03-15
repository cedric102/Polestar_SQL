package com.example.javasqlquery;

import org.junit.jupiter.api.Test;
import org.mockito.AdditionalMatchers;
import org.mockito.Matchers;
import org.mockito.Mockito;

import java.sql.*;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;

class Test_Element {

    IElement e;

    Statement stat = mock(Statement.class);
    IElement inner;
    ElementTransferToDB eDB;
        
    public Test_Element () throws SQLException {
        e = new Element();

        inner = new Element();
        eDB = new ElementTransferToDB( inner );
        eDB.st = stat;
        Mockito.when(stat.executeUpdate("0")).thenReturn(1);
    }

	@Test
	public void createTables() throws Exception {

        String [] res2 = e.CreateTables();
        assertEquals( 2 , res2.length );

        Mockito.when(stat.executeUpdate(res2[0])).thenReturn(0);
        Mockito.when(stat.executeUpdate(res2[1])).thenReturn(-1);
        int iRes = eDB.CreateTables();
        assertEquals( -1 , iRes );

        Mockito.when(stat.executeUpdate(res2[1])).thenReturn(0);
        iRes = eDB.CreateTables();
        assertEquals( 1 , iRes );

    }

	@Test
	public void insertToTable() throws Exception {
        
        String res = e.insertToRoot( 100 , 1 , 1 , "ELEM" , "VALUE" );
        assertEquals( "INSERT INTO PARAMETERS VALUES ( 100 , 1 , 1 , \"ELEM\" , \"VALUE\" )"
        , res );

        // List<String> pop = e.PopulateTheTables();
        String elem = "INSERT INTO PARAMETERS VALUES ( 14 , 2 , 2 , \"Nominal Power\" , \"5250\" )";
        Mockito.when(stat.executeUpdate(elem)).thenReturn(1); // Send Valid Input
        Mockito.when(stat.executeUpdate(AdditionalMatchers.not(Matchers.eq(elem)))).thenReturn(1); // Send Valid Input
        int iRes = eDB.PopulateTheTables();
        assertEquals( 1 , iRes );

        Mockito.when(stat.executeUpdate(elem)).thenReturn(-1); // Send Invalid Input
        iRes = eDB.PopulateTheTables();
        assertEquals( -1 , iRes );

    }
    
}