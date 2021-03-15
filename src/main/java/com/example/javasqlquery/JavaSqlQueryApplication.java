package com.example.javasqlquery;

import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class JavaSqlQueryApplication {

	public static void main(String[] args) throws Exception {

		if( args.length > 0 ) {
			IElement e;
			e = new Element( args );
			e.ConfigureDatabases();
			e.CreateTables();
			e.PopulateTheTables();
			e.applyForeignKeyToTheTables();
			e.obtainResult();
			e.removeForeignKeyFromTheTables();
		} else {
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
