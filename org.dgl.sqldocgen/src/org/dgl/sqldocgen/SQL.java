package org.dgl.sqldocgen;

public class SQL {

    public static final String QUERY_TABLES = "SELECT OBJECT_ID,NAME FROM SYS.OBJECTS WHERE TYPE = 'U' ORDER BY NAME";
    public static final String QUERY_COLUMNS = "SELECT [COLUMNS].COLUMN_ID,[COLUMNS].NAME,[TYPES].NAME AS TYPENAME, [SCHEMAS].CHARACTER_MAXIMUM_LENGTH AS TYPELEN FROM SYS.TABLES AS [TABLES] INNER JOIN SYS.COLUMNS AS [COLUMNS] ON [TABLES].OBJECT_ID = [COLUMNS].OBJECT_ID INNER JOIN SYS.TYPES AS [TYPES] ON [COLUMNS].SYSTEM_TYPE_ID =  [TYPES].SYSTEM_TYPE_ID INNER JOIN INFORMATION_SCHEMA.COLUMNS as [SCHEMAS] ON [COLUMNS].NAME = [SCHEMAS].COLUMN_NAME AND [TABLES].NAME = [SCHEMAS].TABLE_NAME WHERE [TABLES].OBJECT_ID = ? AND [TYPES].NAME <> 'sysname' ORDER BY COLUMN_ID";
    public static final String QUERY_TRIGGERS = "select T.object_id,T.name,C.text from sys.triggers as T inner join sys.syscomments as C on T.object_id = C.id where parent_id = ? ORDER BY T.NAME";
    public static final String QUERY_STOREDPROCEDURES = "select sp.object_id,sp.name,c.text from sys.procedures as sp inner join sys.syscomments as c on sp.object_id = c.id ORDER BY SP.NAME";
    public static final String QUERY_VIEWS = "select v.object_id,v.name,c.text from sys.views as v inner join sys.syscomments as c on v.object_id = c.id ORDER BY V.NAME";
    public static final String SP_DROPCOMMENT_TABLE = "{CALL SP_DROPEXTENDEDPROPERTY('MS_Description','Schema','dbo','Table','?')}";
    public static final String SP_DROPCOMMENT_COLUMN = "{CALL SP_DROPEXTENDEDPROPERTY('MS_Description','Schema','dbo','Table','?','Column','?')}";
    public static final String SP_DROPCOMMENT_TRIGGER = "{CALL SP_DROPEXTENDEDPROPERTY('MS_Description','Schema','dbo','Table','?','Trigger','?')}";
    public static final String SP_DROPCOMMENT_VIEW = "{CALL SP_DROPEXTENDEDPROPERTY('MS_Description','Schema','dbo','View','?')}";
    public static final String SP_DROPCOMMENT_STOREDPROCEDURE = "{CALL SP_DROPEXTENDEDPROPERTY('MS_Description','Schema','dbo','Procedure','?')}";
    public static final String SP_ADDCOMMENT_TABLE = "{CALL SP_ADDEXTENDEDPROPERTY('MS_Description',N'?','Schema','dbo','Table','?')}";
    public static final String SP_ADDCOMMENT_COLUMN = "{CALL SP_ADDEXTENDEDPROPERTY('MS_Description',N'?','Schema','dbo','Table','?','Column','?')}";
    public static final String SP_ADDCOMMENT_TRIGGER = "{CALL SP_ADDEXTENDEDPROPERTY('MS_Description',N'?','Schema','dbo','Table','?','Trigger',N'?')}";
    public static final String SP_ADDCOMMENT_VIEW = "{CALL SP_ADDEXTENDEDPROPERTY('MS_Description',N'?','Schema','dbo','View','?')}";
    public static final String SP_ADDCOMMENT_STOREDPROCEDURE = "{CALL SP_ADDEXTENDEDPROPERTY('MS_Description',N'?','Schema','dbo','Procedure','?')}";
    public static final String QUERY_GETCOMMENT_TABLE = "select * from sys.extended_properties where major_id = ? and minor_id = 0 and name = 'MS_Description'";
    public static final String QUERY_GETCOMMENT_COLUMN = "select * from sys.extended_properties where major_id = ? and minor_id = ? and name = 'MS_Description'";
    public static final String QUERY_GETCOMMENT_TRIGGER = "select * from sys.extended_properties where major_id = ? and minor_id = 0 and name = 'MS_Description'";
    public static final String QUERY_GETCOMMENT_VIEW = "select * from sys.extended_properties where major_id = ? and minor_id = 0 and name = 'MS_Description'";
    public static final String QUERY_GETCOMMENT_STOREDPROCEDURE = "select * from sys.extended_properties where major_id = ? and minor_id = 0 and name = 'MS_Description'";

}
