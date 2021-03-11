package com.example.javasqlquery;


import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class JavaSqlQueryApplication {


	public static void main(String[] args) throws Exception {
		// SpringApplication.run(JavaSqlQueryApplication.class, args);

		Element e = new Element();
		e.CreateTables();
		e.PopulateTheTables();
		
	}

}
