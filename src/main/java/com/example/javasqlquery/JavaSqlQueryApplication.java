package com.example.javasqlquery;

import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class JavaSqlQueryApplication {

	public static void main(String[] args) throws Exception {

		// ElementTransferToDB e = new ElementTransferToDB( );
		IElement e;
		String sChoice = "RUN_APPLICATION";

		if( sChoice.equals( "CLI" ) || sChoice.equals( "RUN_APPLICATION" ) ) {
			if( sChoice.equals( "CLI" ) )
				e = new Element( args );
			else {
				IElement inner = new Element();
				e = new ElementTransferToDB( inner );
				// e = new ElementTransferToDB( "out.txt" );
			}
				

			e.ConfigureDatabases();
			e.CreateTables();
			// e.PopulateTheTables();
			// e.applyForeignKeyToTheTables();
			// e.obtainResult();
			// e.removeForeignKeyFromTheTables();
		}
	}

}
