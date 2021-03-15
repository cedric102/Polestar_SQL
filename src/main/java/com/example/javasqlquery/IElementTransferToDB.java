package com.example.javasqlquery;

import java.util.List;

public interface IElementTransferToDB {
    Object st = null;
    public int insertToRoot(int UniqueIndexId, int ElementId, int IndexId, String _Key, String _Value);
    public void ConfigureDatabases() throws Exception;
    public int CreateTables() throws Exception;
    public int PopulateTheTables() throws Exception;
    public void obtainResult() throws Exception;
    public int applyForeignKeyToTheTables() throws Exception;
    public int removeForeignKeyFromTheTables() throws Exception;
    public String getOutputFile();
    
}
