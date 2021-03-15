# Polestart_SQL

# Overall :
The Program cal be run in 
Running the Application in different way : 
    - Using VSCode IDE Run Button.
    - Using the CLI ( Command Line Interface ).

# MRun the Code using VS Code :
    - Just hit Run, The default File in the data folder will be loaded and outputed to a 'output.txt' file.

# Implementation :
    - The Program uses the Decorator Design Pattern:
      . If one prefers not to execute the SQL Queries , the 'Element' Class will be executed and .
      . Element Class Description : 'Element' Class SQL Methods delivers the SQL Command as String.
      . ElementTransferToDB Class Description : The Strings provided by 'Element' Class are used in ElementTransferToDB Method using the Decorator Design Pattern to add the SQL Execution of those String SQL Commands.
      . Result : This way, one could choose whether to store the SQL Commands in a File , print those Commands in the 'Element' Class , or Executing them in the 'ElementTransferToDB Class.

## CLI Command :
Template : 
mvn exec:java -Dexec.mainClass=com.example.javasqlquery.JavaSqlQueryApplication -Dexec.args="File_1.txt File_2.txt"
The Command above provides the CLI Template to run the program.
It takes the following arguments :
 - File_1.txt : Input File ( input.json )
 - File_2.txt : ( Optional ) Output File if it is specified.

CLI Instructions.
    - If File_2.txt is specified , the SQL Commands will be stored int the specified file.
      Otherwise , the commands would just be printed in the screen.
    - The CLI Commands will not be executed to create the SQL Tbles or insert Entries

