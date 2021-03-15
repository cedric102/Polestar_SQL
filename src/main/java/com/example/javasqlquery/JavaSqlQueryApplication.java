package com.example.javasqlquery;

import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class JavaSqlQueryApplication {

	public static void main(String[] args) throws Exception {

		// ElementTransferToDB e = new ElementTransferToDB( );
		String sChoice = "RUN_APPLICATION";

		if( sChoice.equals( "CLI" ) || sChoice.equals( "RUN_APPLICATION" ) ) {
			if( sChoice.equals( "CLI" ) ) {
				IElement e;
				e = new Element( args );
				e.ConfigureDatabases();
				e.CreateTables();
				e.PopulateTheTables();
				e.applyForeignKeyToTheTables();
				e.obtainResult();
				e.removeForeignKeyFromTheTables();
			}
			else {
				IElement inner = new Element();
				IElementTransferToDB e = new ElementTransferToDB( inner );
				e.ConfigureDatabases();
				e.CreateTables();
				e.PopulateTheTables();
				e.applyForeignKeyToTheTables();
				e.obtainResult();
				e.removeForeignKeyFromTheTables();
			}
				
		}
	}

}
