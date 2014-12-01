package org.dgl.sqldocgen.db;

import java.io.Serializable;
import java.sql.ResultSet;

public class TableColumn extends Commentable implements Serializable {

    private String type;

    public TableColumn() {
        super();
        type = "";
    }

    public static TableColumn parse(ResultSet result) throws Exception {
        TableColumn output;
        String typelen;
        output = new TableColumn();
        output.setId(result.getInt("column_id"));
        output.setName(result.getString("name"));
        output.setType(result.getString("typename"));
        typelen = result.getString("typelen");
        if (typelen != null) {
            output.setType(output.getType() + " (" + (typelen.equals("-1") ? "max" : typelen) + ")");
        }
        return output;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

}
