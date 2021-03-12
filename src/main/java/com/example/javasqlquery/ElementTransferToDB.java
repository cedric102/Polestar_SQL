package com.example.javasqlquery;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.*;
import java.util.List;

import org.json.simple.parser.JSONParser;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

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
public class ElementTransferToDB implements IElement {

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
			System.out.println( this.st );
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

		int Result = -1;
		String sInsert = "INSERT INTO PARAMETERS VALUES ( " 
					+ UniqueIndexId + " , " 
					+ ElementId + " , " 
					+ IndexId + " , \"" 
					+ _Key + "\" , \"" 
					+ _Value + "\" )";

		try {
			Result = this.st.executeUpdate(sInsert);
		} catch ( Exception e ) {
			System.out.println();
		}

		return Result;

	}

	public String [] CreateTables() throws Exception {

		// Create the Tables ( Two of them )

		// String ElementDefine = "CREATE TABLE ROOT ( " 
		// 				+ " UniqueIndexId int NOT NULL CHECK (UniqueIndexId >= 0) , " // Unique
		// 				+ " Name varchar(20) , " // Name of the Item
		// 				+ " AlarmColor int , " // AlarmColor of the Item
		// 				+ " Id int , " // Id of the Item
		// 				+ " ElementId int , " // ElementId , Provide the Parameters List Id
		// 				+ " Parameters_Size int , " // Provide the Parameters list Size for the given ID
		// 				+ " DatasourcesCount int , " // JSON Data
		// 				+ " _alertIcon varchar(20) , " // JSON Data
		// 				+ " ElementCount int , " // JSON Data
		// 				+ " UniqueID varchar(50) , " // JSON Data
		// 				+ " PRIMARY KEY (UniqueIndexId) )";
		String [] res = this.innerElem.CreateTables();
		// System.out.println( ElementDefine );
		System.out.println( res[0] );
		System.out.println( res[1] );

		this.st.executeUpdate( res[0] );
		// outputRes.append( res[0] );
		// // this.resultSet = this.st.executeUpdate( ElementDefine );
		// outputRes.append( ElementDefine );
		// outputRes.append( "\n" );

		// String paramDefine = "CREATE TABLE PARAMETERS ( "
		// 				+ " UniqueIndexId int NOT NULL CHECK (UniqueIndexId >= 0) , " // Unique
		// 				+ " ElementId int , " // Link to ROOT Table with the Foreign Key. Refer to Alter Table below
		// 				+ " IndexId int , " // Offset in the given ElementId
		// 				+ " _Key1 varchar(200) , " // JSON Data
		// 				+ " _Value varchar(50) , " // JSON Data
		// 				+ " PRIMARY KEY (UniqueIndexId) )";
		this.st.executeUpdate( res[1] );
		// outputRes.append( res[1] );
		// // this.resultSet = this.st.executeUpdate( paramDefine );
		// outputRes.append( paramDefine );
		// outputRes.append( "\n" );

		return res;

	}


	public String applyForeignKeyToTheTables() throws Exception {

		// Implement the Forreign Key
		String alterTables = innerElem.applyForeignKeyToTheTables();
		// String alterTables = "ALTER TABLE PARAMETERS ADD CONSTRAINT FK_ElementId FOREIGN KEY (ElementId) REFERENCES ROOT (UniqueIndexId)";
		this.resultSet = this.st.executeUpdate( alterTables );
		outputRes.append( alterTables );
		outputRes.append( "\n" );

		return "";

	}

	public String removeForeignKeyFromTheTables() throws Exception {

		// Implement the Forreign Key
		String alterTables = innerElem.removeForeignKeyFromTheTables();
		this.resultSet = this.st.executeUpdate( alterTables );
		outputRes.append( alterTables );
		outputRes.append( "\n" );
		
		return "";
	}
	
	public List<String> PopulateTheTables() throws Exception {

		// // Process the JSON File
		// JSONParser parse = new JSONParser();
		// JSONArray jsonArray;
		// int paramUniqueId = 0;

		// // Process the JSON File

		// FileReader reader = new FileReader( "/Users/cedric/View_Boot/java-sql-query/src/main/java/com/example/javasqlquery/input.json" );
		// jsonArray = (JSONArray) parse.parse(reader);

		// for (int i = 0; i < jsonArray.size(); i++) {
		// 	JSONObject obj = (JSONObject) jsonArray.get(i);

		// 	JSONArray par = (JSONArray) obj.get("Parameters");
		// 	String string_I_Index = String.valueOf(i);

		// 	// Provide the "Key" and "Value" to be inserted to the PARAMETERS Table
		// 	// corresponding to the list for the "Parameters" field;
		// 	int j = 0;
		// 	String string_J_Index = String.valueOf("0");
		// 	for (j = 0; j < par.size(); j++) {
		// 		JSONObject parObj = (JSONObject) par.get(j);
		// 		string_J_Index = String.valueOf(j);
		// 		String paramValues = "INSERT INTO PARAMETERS VALUES ( " + paramUniqueId++ + " , "
		// 						+ string_I_Index + " , " 
		// 						+ string_J_Index + " , \"" 
		// 						+ parObj.get("Key") + "\" , \""
		// 						+ parObj.get("Value") + "\" )";
		// 		this.resultSet = this.st.executeUpdate( paramValues );
		// 		outputRes.append( paramValues );
		// 		outputRes.append( "\n" );
		// 	}

		// 	// Insert to ROOT Table all the required data from the JSON File
		// 	String elemValues = "INSERT INTO ROOT VALUES ( " 
		// 							+ string_I_Index + " , \"" 
		// 							+ obj.get("Name") + "\" , "
		// 							+ obj.get("AlarmColor") + " , " 
		// 							+ obj.get("Id") + " , " 
		// 							+ string_I_Index + " , "
		// 							+ string_J_Index + " , " 
		// 							+ obj.get("DatasourcesCount") + " , \"" 
		// 							+ obj.get("_alertIcon")
		// 							+ "\" , " + obj.get("ElementCount") + " , \"" 
		// 							+ obj.get("UniqueID") + "\" )";
		// 	this.resultSet = this.st.executeUpdate( elemValues );
		// 	outputRes.append( elemValues );
		// 	outputRes.append( "\n" );
		// }

		List<String> res = innerElem.PopulateTheTables();
		for( String r : res )
			this.st.executeUpdate( r );
		
		return null;
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
