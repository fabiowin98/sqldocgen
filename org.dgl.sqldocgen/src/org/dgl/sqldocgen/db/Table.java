package org.dgl.sqldocgen.db;

import java.io.Serializable;
import java.sql.ResultSet;
import java.util.ArrayList;

public class Table extends Commentable implements Serializable {

    private ArrayList<TableColumn> columns;
    private ArrayList<TableTrigger> triggers;

    public Table() {
        super();
        columns = new ArrayList<>();
        triggers = new ArrayList<>();
    }

    public static Table parse(ResultSet result) throws Exception {
        Table output;
        output = new Table();
        output.setId(result.getInt("object_id"));
        output.setName(result.getString("name"));
        return output;
    }

    public ArrayList<TableColumn> getColumns() {
        return columns;
    }

    public void setColumns(ArrayList<TableColumn> columns) {
        this.columns = columns;
    }

    public ArrayList<TableTrigger> getTriggers() {
        return triggers;
    }

    public void setTriggers(ArrayList<TableTrigger> triggers) {
        this.triggers = triggers;
    }

}
