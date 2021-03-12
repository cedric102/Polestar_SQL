package com.example.javasqlquery;

public interface IElement {
    Object st = null;
    public int insertToRoot(int UniqueIndexId, int ElementId, int IndexId, String _Key, String _Value);
    public void ConfigureDatabases() throws Exception;
    public String [] CreateTables() throws Exception;
    public void applyForeignKeyToTheTables() throws Exception;
    public void PopulateTheTables() throws Exception;
    public void obtainResult() throws Exception;
    public void removeForeignKeyFromTheTables() throws Exception;
}
