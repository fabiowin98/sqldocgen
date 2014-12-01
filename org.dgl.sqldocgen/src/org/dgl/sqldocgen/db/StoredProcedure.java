package org.dgl.sqldocgen.db;

import java.io.Serializable;
import java.sql.ResultSet;

public class StoredProcedure extends Commentable implements Serializable {

    private String code;

    public StoredProcedure() {
        super();
        code = "";
    }

    public static StoredProcedure parse(ResultSet result) throws Exception {
        StoredProcedure output;
        output = new StoredProcedure();
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
