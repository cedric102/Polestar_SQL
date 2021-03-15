package com.example.javasqlquery;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import org.json.simple.parser.JSONParser;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

/*
* Class to Convert from JSON to SQL String
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
* Returns : The String Representation of the SQL Database with 2 Tables ROOT and PARAMETERS :
*           - The ROOT takes all the fields from "Name" to "UniqueID"
*           - the PARAMETERS takes takes all the "Key" and "Values" 
*             related to the "Parameters" Field
*           - A Foreigh Key is used to link both databases
*             via the "Parameters" Field
*/
public class Element implements IElement {

	Connection conn = null;
	public Statement st = null;
	int resultSet = -1;
	
	// To be used to populate the file or printing the list of commands when it is requested
	// This Variable is built in each SQL Method Management
	// Used during obtainResult()
	StringBuilder outputRes;

	// File Variables
	int argLength = 0;
	String inputFilePath = "";
	String outputFilePath = "";
	String filePath = "src/main/java/com/example/javasqlquery/data/";
	FileWriter outputFile;

	Element( ) {
		inputFilePath = "src/main/java/com/example/javasqlquery/data/input.json";
		outputRes = new StringBuilder();
		argLength = 1;
	}

	Element( String[] args ) {
		for( int i=0 ; i<args.length ; i++ )
			System.out.println( args[i] );
		outputRes = new StringBuilder();

		// Store Input File Path
		inputFilePath = filePath + args[0];

		// Store Output File Path if requested
		argLength = args.length;
		if( args.length > 1 ) {
			outputFilePath = args[1];
		}
	}


	public void ConfigureDatabases() throws Exception {

		System.out.println();
	}

	public String insertToRoot(int UniqueIndexId, int ElementId, int IndexId, String _Key, String _Value) {

		String sInsert = "INSERT INTO PARAMETERS VALUES ( " 
					+ UniqueIndexId + " , " 
					+ ElementId + " , " 
					+ IndexId + " , \"" 
					+ _Key + "\" , \"" 
					+ _Value + "\" )";
		System.out.println( sInsert );
		return sInsert;

	}

	/**
	 * Create The Tables
	 * This Class Populates the SQL Query String to be used to create those tables
	 * @Author : C. Carteron
	 * @Params : None
	 * @Return : Strings[] -> "Create Table SQL Query String"
	 */
	public String [] CreateTables() throws Exception {

		String [] res = new String[2];

		String ElementDefine = "CREATE TABLE ROOT ( " 
						+ " UniqueIndexId int NOT NULL CHECK (UniqueIndexId >= 0) , " // Unique
						+ " Name varchar(20) , " // Name of the Item
						+ " AlarmColor int , " // AlarmColor of the Item
						+ " Id int , " // Id of the Item
						+ " ElementId int , " // ElementId , Provide the Parameters List Id
						+ " Parameters_Size int , " // Provide the Parameters list Size for the given ID
						+ " DatasourcesCount int , " // JSON Data
						+ " _alertIcon varchar(20) , " // JSON Data
						+ " ElementCount int , " // JSON Data
						+ " UniqueID varchar(50) , " // JSON Data
						+ " PRIMARY KEY (UniqueIndexId) )";
		res[0] = ElementDefine;
		outputRes.append( ElementDefine );
		outputRes.append( "\n" );

		String paramDefine = "CREATE TABLE PARAMETERS ( "
						+ " UniqueIndexId int NOT NULL CHECK (UniqueIndexId >= 0) , " // Unique
						+ " ElementId int , " // Link to ROOT Table with the Foreign Key. Refer to Alter Table below
						+ " IndexId int , " // Offset in the given ElementId
						+ " _Key1 varchar(200) , " // JSON Data
						+ " _Value varchar(50) , " // JSON Data
						+ " PRIMARY KEY (UniqueIndexId) )";
		res[1] = paramDefine;
		outputRes.append( paramDefine );
		outputRes.append( "\n" );

		return res;

	}

	/**
	 * Populate the SQL Insertion To Table List of String
	 * @Author : C. Carteron
	 * @Param : None
	 * @Return : List<String> containing the SQL Queries "INSERT INTO TABLE ..."
	 */
	public List<String> PopulateTheTables() throws Exception {
		// Process the JSON File
		List<String> res = new ArrayList<String>();

		JSONParser parse = new JSONParser();
		JSONArray jsonArray;
		int paramUniqueId = 0;

		// Process the JSON File
		FileReader reader = new FileReader( inputFilePath );
		jsonArray = (JSONArray) parse.parse(reader);

		for (int i = 0; i < jsonArray.size(); i++) {
			JSONObject obj = (JSONObject) jsonArray.get(i);

			JSONArray par = (JSONArray) obj.get("Parameters");
			String string_I_Index = String.valueOf(i);

			// Provide the "Key" and "Value" to be inserted to the PARAMETERS Table
			// corresponding to the list for the "Parameters" field;
			int j = 0;
			String string_J_Index = String.valueOf("0");
			for (j = 0; j < par.size(); j++) {
				JSONObject parObj = (JSONObject) par.get(j);
				string_J_Index = String.valueOf(j);
				String paramValues = "INSERT INTO PARAMETERS VALUES ( " + paramUniqueId++ + " , "
								+ string_I_Index + " , " 
								+ string_J_Index + " , \"" 
								+ parObj.get("Key") + "\" , \""
								+ parObj.get("Value") + "\" )";

				res.add( paramValues );
				outputRes.append( paramValues );
				outputRes.append( "\n" );
			}

			// Insert to ROOT Table all the required data from the JSON File
			String elemValues = "INSERT INTO ROOT VALUES ( " 
									+ string_I_Index + " , \"" 
									+ obj.get("Name") + "\" , "
									+ obj.get("AlarmColor") + " , " 
									+ obj.get("Id") + " , " 
									+ string_I_Index + " , "
									+ string_J_Index + " , " 
									+ obj.get("DatasourcesCount") + " , \"" 
									+ obj.get("_alertIcon")
									+ "\" , " + obj.get("ElementCount") + " , \"" 
									+ obj.get("UniqueID") + "\" )";

			res.add( elemValues );
			outputRes.append( elemValues );
			outputRes.append( "\n" );
		}
		return res;
	}
	
	/**
	 * Populate the SQL Foreign Key Addition To Table and return the String
	 * @Author : C. Carteron
	 * @Param : None
	 * @Return : String containing the SQL Queries "ALTER TABLE ..."
	 */
	public String applyForeignKeyToTheTables() throws Exception {

		// Implement the Forreign Key
		String alterTables = "ALTER TABLE PARAMETERS ADD CONSTRAINT FK_ElementId FOREIGN KEY (ElementId) REFERENCES ROOT (UniqueIndexId)";

		outputRes.append( alterTables );
		outputRes.append( "\n" );

		return alterTables;
	}

	/**
	 * Populate the SQL Foreign Key Removal To Table and return the String
	 * @Author : C. Carteron
	 * @Param : None
	 * @Return : String containing the SQL Queries "ALTER TABLE ..."
	 */
	public String removeForeignKeyFromTheTables() throws Exception {

		// Implement the Forreign Key
		String alterTables = "ALTER TABLE PARAMETERS DROP CONSTRAINT FK_ElementId";
		outputRes.append( alterTables );
		outputRes.append( "\n" );

		return alterTables;
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
			System.out.println( outputRes.toString() );
		else {
			try {
				outputFile = new FileWriter(filePath + outputFilePath);
			} catch( IOException e ) {
				System.out.println( e );
			}
			outputFile.write( outputRes.toString() );
			outputFile.close( );
		}
	}

}
