package com.example.javasqlquery;

import java.io.FileWriter;
import java.io.IOException;
import java.sql.*;
import java.util.List;

/*
* Class to Convert from JSON to SQL
*
* @Author : Cedric Carteron
*
* Description : The class takes an array of objects
* and one of its parameters aso takes an array of objects
* ie: [{
* 	"Name": "Site 1",
* 	"AlarmColor": -1671296,
* 	"Id": 8,
* 	"Parameters": [{
* 		"Key": "Name",
* 		"Value": "Site 1"
* 		}, ...
*  ],
* 	"DatasourcesCount": 0,
* 	"_alertIcon": "Communications",
* 	"ElementCount": 640,
* 	"UniqueID": "87111c51-08df-4b29-85c5-43803a994bdd"
* }, ...
* ]
*
* Result : SQL Database with 2 Tables ROOT and PARAMETERS :
*           - The ROOT takes all the fields from "Name" to "UniqueID"
*           - the PARAMETERS takes takes all the "Key" and "Values" 
*             related to the "Parameters" Field
*           - A Foreigh Key is used to link both databases
*             via the "Parameters" Field
*/
public class ElementTransferToDB implements IElementTransferToDB {

	Connection conn = null;
	Statement st = null;
	int resultSet = -1;
	String inputFile;
	String outputFile;
	StringBuilder outputRes;
	FileWriter outFile;
	IElement innerElem;

	public void ConfigureDatabases() throws Exception {

		String myUrl = "jdbc:mysql://localhost/JAVA_SQL";

		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			this.conn = DriverManager.getConnection(myUrl, "root", "passwordKi1s");
			this.st = conn.createStatement();
		} catch ( Exception e ) {
			System.out.println( e );
		}
	}

	ElementTransferToDB( ) {
		outputRes = new StringBuilder();
		inputFile = "/Users/cedric/View_Boot/java-sql-query/src/main/java/com/example/javasqlquery/input.json";
		outputFile = "output.txt";
	}

	ElementTransferToDB( IElement e ) {
		this.innerElem = e;
		outputRes = new StringBuilder();
		inputFile = "/Users/cedric/View_Boot/java-sql-query/src/main/java/com/example/javasqlquery/input.json";
		outputFile = "output.txt";
	}

	ElementTransferToDB( String outputFile ) {
		outputRes = new StringBuilder();
		inputFile = "/Users/cedric/View_Boot/java-sql-query/src/main/java/com/example/javasqlquery/input.json";
		this.outputFile = outputFile;
	}

	ElementTransferToDB( String inputFile , String outputFile ) {
		outputRes = new StringBuilder();
		this.inputFile = inputFile;
		this.outputFile = outputFile;
	}

	public int insertToRoot(int UniqueIndexId, int ElementId, int IndexId, String _Key, String _Value) {

		String sInsert = this.innerElem.insertToRoot( UniqueIndexId , ElementId , IndexId , _Key , _Value);

		try {
			return this.st.executeUpdate(sInsert);
		} catch ( Exception e ) {
			System.out.println();
		}

		return -1;

	}

	public int CreateTables() throws Exception {

		// Create the Tables ( Two of them )
		String [] res = this.innerElem.CreateTables();

		try {
			int r1 = this.st.executeUpdate( res[0] );
			int r2 = this.st.executeUpdate( res[1] );
			if( r1 == 0 && r2 == 0 ) {
				return 1;
			} else
				return -1;
		} catch ( Exception e ) {
			System.out.println(e);
			return -1;
		}

	}

	public int applyForeignKeyToTheTables() throws Exception {

		// Implement the Forreign Key
		String alterTables = innerElem.applyForeignKeyToTheTables();
		
		this.resultSet = this.st.executeUpdate( alterTables );
		outputRes.append( alterTables );
		outputRes.append( "\n" );

		return this.resultSet;

	}

	public int removeForeignKeyFromTheTables() throws Exception {

		// Implement the Forreign Key
		String alterTables = innerElem.removeForeignKeyFromTheTables();
		
		this.resultSet = this.st.executeUpdate( alterTables );
		outputRes.append( alterTables );
		outputRes.append( "\n" );
		
		return this.resultSet;
	}
	
	public int PopulateTheTables() throws Exception {

		List<String> res = innerElem.PopulateTheTables();
		for( String r : res ) {
			int temp = this.st.executeUpdate( r );
			if( temp != 1 )
				return -1;
		}
		return 1;
	}


	public String getOutputFile() {
		return outputRes.toString();
	}

	public void obtainResult() throws Exception {
		if( outputFile.equals("") )
			System.out.println( innerElem.getOutputFile() );
		else {
			try {
				outFile = new FileWriter(outputFile);
			} catch( IOException e ) {
				System.out.println( e );
			}
			outFile.write( innerElem.getOutputFile() );
			outFile.close( );
		}
	}

}
