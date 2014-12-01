package org.dgl.sqldocgen.db;

import java.io.Serializable;
import java.sql.ResultSet;

public class View extends Commentable implements Serializable {

    private String code;

    public View() throws Exception {
        super();
        code = "";
    }

    public static View parse(ResultSet result) throws Exception {
        View output;
        output = new View();
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
