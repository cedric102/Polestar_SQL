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

	// Composition on IElement in order to build on the to
	// of what those methods return using the Decorator Design PAttern
	IElement innerElem;
	
	// To be used to populate the file or printing the list of commands when it is requested
	// This Variable is built in each SQL Method Management
	// Used during obtainResult()
	StringBuilder outputRes;

	// File Variables
	String inputFilePath;
	String outputFilePath;
	String filePath = "src/main/java/com/example/javasqlquery/data/";
	FileWriter outputFile;

	/**
	 * Create the Database Connection
	 * @Author : C. Carteron
	 * @Param : None
	 * @Return : None
	 */
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

	/**
	 * Initialize the File Path Variables
	 * Using the Default Values
	 */
	ElementTransferToDB( ) {
		outputRes = new StringBuilder();
		inputFilePath = filePath + "input.json";
		outputFilePath = filePath + "output.txt";
	}

	/**
	 * Initialize the File Path Variables
	 * and makes use of the Decorator Pattern
	 * Using the Default Values
	 */
	ElementTransferToDB( IElement e ) {
		this.innerElem = e;
		outputRes = new StringBuilder();
		inputFilePath = filePath + "input.json";
		outputFilePath = filePath + "output.txt";
	}

	/**
	 * Initialize the File Path Variables
	 * Using the Default Value of the input file
	 * Override the output file
	 */
	ElementTransferToDB( String outputFile ) {
		outputRes = new StringBuilder();
		inputFilePath = filePath + "input.json";
		this.outputFilePath = filePath + outputFile;
	}

	/**
	 * Initialize the File Path Variables
	 * Override the input file and output file
	 */
	ElementTransferToDB( String inputFileName , String outputFile ) {
		outputRes = new StringBuilder();
		this.inputFilePath = filePath + inputFileName;
		this.outputFilePath = filePath + outputFile;
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

	/**
	 * Execute the SQL Table using the Array of String provided by the innerElem
	 * @Author : C. Carteron
	 * @Param : None
	 * @Return : -1 if a Query fails
	 */
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
	
	/**
	 * Execute the SQL Insertion To Table using the List of String provided by the innerElem
	 * @Author : C. Carteron
	 * @Param : None
	 * @Return : -1 if a Query fails
	 */
	public int PopulateTheTables() throws Exception {

		List<String> res = innerElem.PopulateTheTables();
		for( String r : res ) {
			int temp = this.st.executeUpdate( r );
			if( temp != 1 )
				return -1;
		}
		return 1;
	}

	/**
	 * Execute the SQL Foreign Key Addition To Table using the String provided by the innerElem
	 * @Author : C. Carteron
	 * @Param : None
	 * @Return : 1 if a Query succeed
	 */
	public int applyForeignKeyToTheTables() throws Exception {

		// Implement the Forreign Key
		String alterTables = innerElem.applyForeignKeyToTheTables();
		
		this.resultSet = this.st.executeUpdate( alterTables );
		outputRes.append( alterTables );
		outputRes.append( "\n" );

		return this.resultSet;

	}

	/**
	 * Execute the SQL Foreign Key Removal To Table using the String provided by the innerElem
	 * @Author : C. Carteron
	 * @Param : None
	 * @Return : 1 if a Query succeed
	 */
	public int removeForeignKeyFromTheTables() throws Exception {

		// Implement the Forreign Key
		String alterTables = innerElem.removeForeignKeyFromTheTables();
		
		this.resultSet = this.st.executeUpdate( alterTables );
		outputRes.append( alterTables );
		outputRes.append( "\n" );
		
		return this.resultSet;
	}

	public String getOutputFile() {
		return outputRes.toString();
	}

	/**
	 * Print / Store on the provided File the outputRes Content
	 * @Author : C. Carteron
	 * @Param : None
	 * @Return : 1 if a Query succeed
	 */
	public void obtainResult() throws Exception {

		if( outputFilePath.equals("") )
			innerElem.obtainResult();
		else {
			try {
				outputFile = new FileWriter(outputFilePath);
			} catch( IOException e ) {
				System.out.println( e );
			}
			outputFile.write( innerElem.getOutputFile() );
			outputFile.close( );
		}
		
	}

}
