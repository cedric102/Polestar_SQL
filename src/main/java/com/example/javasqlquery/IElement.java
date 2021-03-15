package com.example.javasqlquery;

import java.util.List;

public interface IElement {
    Object st = null;
    public String insertToRoot(int UniqueIndexId, int ElementId, int IndexId, String _Key, String _Value);
    public void ConfigureDatabases() throws Exception;
    public String [] CreateTables() throws Exception;
    public List<String> PopulateTheTables() throws Exception;
    public void obtainResult() throws Exception;
    public String applyForeignKeyToTheTables() throws Exception;
    public String removeForeignKeyFromTheTables() throws Exception;
    public String getOutputFile();
}
