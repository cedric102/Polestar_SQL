package com.example.javasqlquery;

import java.io.FileReader;
import java.sql.*;

import org.json.simple.parser.JSONParser;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

////////////////////////////////
// Class to Convert from JSON to SQL
////////////
// Author : Cedric Carteron
//
// Description : The class takes an array of objects
// and one of its parameters aso takes an array of objects
// ie: [{
// 	"Name": "Site 1",
// 	"AlarmColor": -1671296,
// 	"Id": 8,
// 	"Parameters": [{
// 		"Key": "Name",
// 		"Value": "Site 1"
// 		}, ...
//  ],
// 	"DatasourcesCount": 0,
// 	"_alertIcon": "Communications",
// 	"ElementCount": 640,
// 	"UniqueID": "87111c51-08df-4b29-85c5-43803a994bdd"
// }, ...
// ]
//
// Result : SQL Database with 2 Tables ROOT and PARAMETERS :
//           - The ROOT takes all the fields from "Name" to "UniqueID"
//           - the PARAMETERS takes takes all the "Key" and "Values" 
//             related to the "Parameters" Field
//           - A Foreigh Key is used to link both databases
//             via the "Parameters" Field
////////////////////////////////
public class Element {

	Connection conn = null;
	Statement st = null;
	int resultSet = -1;

	Element() {

	}

	public int insertToRoot(int UniqueIndexId, int ElementId, int IndexId, String _Key, String _Value) {

		int Result = -1;
		String myUrl = "jdbc:mysql://localhost/JAVA_SQL";

		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			this.conn = DriverManager.getConnection(myUrl, "root", "passwordKi1s");

			this.st = conn.createStatement();
			Result = this.st.executeUpdate("INSERT INTO PARAMETERS VALUES ( " + UniqueIndexId + " , " + ElementId
					+ " , " + IndexId + " , \"" + _Key + "\" , \"" + _Value + "\" )");

		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return Result;

	}

	public void CreateTables() throws Exception {

		String myUrl = "jdbc:mysql://localhost/JAVA_SQL";

		// Create the Tables ( Two of them )
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			this.conn = DriverManager.getConnection(myUrl, "root", "passwordKi1s");

			this.st = conn.createStatement();

			this.resultSet = this.st.executeUpdate("CREATE TABLE ROOT ( " + " UniqueIndexId int NOT NULL , " // Unique
					+ " Name varchar(20) , " // Name of the Item
					+ " AlarmColor int , " // AlarmColor of the Item
					+ " Id int , " // Id of the Item
					+ " ElementId int , " // ElementId , Provide the Parameters List Id
					+ " Parameters_Size int , " // Provide the Parameters list Size for the given ID
					+ " DatasourcesCount int , " // JSON Data
					+ " _alertIcon varchar(20) , " // JSON Data
					+ " ElementCount int , " // JSON Data
					+ " UniqueID varchar(50) , " // JSON Data
					+ " PRIMARY KEY (UniqueIndexId) )");

			this.resultSet = this.st.executeUpdate("CREATE TABLE PARAMETERS ( " + " UniqueIndexId int NOT NULL , " // Unique
					+ " ElementId int , " // Link to ROOT Table with the Foreign Key. Refer to Alter Table below
					+ " IndexId int , " // Offset in the given ElementId
					+ " _Key1 varchar(200) , " // JSON Data
					+ " _Value varchar(50) , " // JSON Data
					+ " PRIMARY KEY (UniqueIndexId) )");

		} catch (Exception e) {
			System.out.println(e.getMessage());
			System.out.println("CREATE ERROR");
		}

		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			this.conn = DriverManager.getConnection(myUrl, "root", "passwordKi1s");

			this.st = conn.createStatement();

			// Implement the Forreign Key
			this.resultSet = this.st.executeUpdate(
					"ALTER TABLE PARAMETERS (" + " ADD FOREIGN KEY (ElementId) REFERENCES ROOT (UniqueIndexId) )");

		} catch (Exception e) {
			System.out.println(e.getMessage());
			System.out.println("ADD FOREIGN KEY ERROR");
		}

	}

	public void PopulateTheTables() throws Exception {

		String myUrl = "jdbc:mysql://localhost/JAVA_SQL";

		// Process the JSON File
		JSONParser parse = new JSONParser();
		JSONArray jsonArray;
		int paramUniqueId = 0;

		// Process the JSON File
		try {

			Class.forName("com.mysql.cj.jdbc.Driver");
			this.conn = DriverManager.getConnection(myUrl, "root", "passwordKi1s");
			this.st = conn.createStatement();

			FileReader reader = new FileReader(
					"/Users/cedric/View_Boot/java-sql-query/src/main/java/com/example/javasqlquery/input.json");
			jsonArray = (JSONArray) parse.parse(reader);

			for (int i = 0; i < jsonArray.size(); i++) {
				JSONObject obj = (JSONObject) jsonArray.get(i);

				JSONArray par = (JSONArray) obj.get("Parameters");
				String string_I_Index = String.valueOf(i);

				// Provide the "Key" and "Value" to be inserted to the PARAMETERS Table
				// corresponding to the list for the "Parameters" field;
				int j = 0;
				String string_J_Index = new String("0");
				for (j = 0; j < par.size(); j++) {
					JSONObject parObj = (JSONObject) par.get(j);
					string_J_Index = String.valueOf(j);
					this.resultSet = this.st.executeUpdate("INSERT INTO PARAMETERS VALUES ( " + paramUniqueId++ + " , "
							+ string_I_Index + " , " + string_J_Index + " , \"" + parObj.get("Key") + "\" , \""
							+ parObj.get("Value") + "\" )");
				}

				// Insert to ROOT Table all the required data from the JSON File
				this.resultSet = this.st.executeUpdate(
						"INSERT INTO ROOT VALUES ( " + string_I_Index + " , \"" + obj.get("Name") + "\" , "
								+ obj.get("AlarmColor") + " , " + obj.get("Id") + " , " + string_I_Index + " , "
								+ string_J_Index + " , " + obj.get("DatasourcesCount") + " , \"" + obj.get("_alertIcon")
								+ "\" , " + obj.get("ElementCount") + " , \"" + obj.get("UniqueID") + "\" )");

			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}

	}

}
