package org.dgl.sqldocgen.db;

import java.io.Serializable;
import java.sql.ResultSet;

public class TableTrigger extends Commentable implements Serializable {

    private String code;

    public TableTrigger() {
        super();
        code = "";
    }

    public static TableTrigger parse(ResultSet result) throws Exception {
        TableTrigger output;
        output = new TableTrigger();
        output.setId(result.getInt("object_id"));
        output.setName(result.getString("name"));
        output.setCode(result.getString("text"));
        return output;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

}
