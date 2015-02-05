package org.dgl.sqldocgen;

import java.sql.ResultSet;
import java.util.ArrayList;
import javax.swing.JOptionPane;
import org.dgl.manager.DBManager;
import org.dgl.manager.LogManager;
import org.dgl.sqldocgen.db.StoredProcedure;
import org.dgl.sqldocgen.db.Table;
import org.dgl.sqldocgen.db.TableColumn;
import org.dgl.sqldocgen.db.TableTrigger;
import org.dgl.sqldocgen.db.View;
import org.dgl.sqldocgen.gui.ProgressionFrame;

public class SqlDocGenerator {

    private DBManager databaseManager;
    private ArrayList<Table> tables;
    private ArrayList<StoredProcedure> storedProcedures;
    private ArrayList<View> views;
    private ProgressionFrame progressionFrame;

    public SqlDocGenerator(SqlConfiguration sqlConfiguration) throws Exception {
        this(Util.createDBManagerFromSQLConfiguration(sqlConfiguration));
    }

    public SqlDocGenerator(DBManager databaseManager) throws Exception {
        this.databaseManager = databaseManager;
        tables = new ArrayList<>();
        storedProcedures = new ArrayList<>();
        views = new ArrayList<>();
    }

    public void read(boolean loadCommentsFromDB) {
        try {
            progressionFrame = new ProgressionFrame();
            progressionFrame.setText("PROGRESSIONFRAME_LOADINGTABLES");
            progressionFrame.setValue(25);
            readTables(loadCommentsFromDB);
            progressionFrame.setText("PROGRESSIONFRAME_LOADINGVIEWS");
            progressionFrame.setValue(50);
            readViews(loadCommentsFromDB);
            progressionFrame.setText("PROGRESSIONFRAME_LOADINGSTOREDPROCEDURES");
            progressionFrame.setValue(75);
            readStoredProcedures(loadCommentsFromDB);
            progressionFrame.setText("PROGRESSIONFRAME_LOADINGDONE");
            progressionFrame.setValue(100);
            progressionFrame.dispose();
        } catch (Exception ex) {
            ex.printStackTrace();
            LogManager.error(LogManager.describeException(ex));
            JOptionPane.showMessageDialog(null, ex.toString(), "", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void dispose() throws Exception {
        getDatabaseManager().close();
        for (Table t : getTables()) {
            t.getColumns().clear();
            t.getTriggers().clear();
        }
        getTables().clear();
        getStoredProcedures().clear();
        getViews().clear();
    }

    private void readTables(boolean loadCommentsFromDB) throws Exception {
        String sql;
        ResultSet tableResult, columnResult, triggerResult, commentResult;
        sql = SQL.QUERY_TABLES;
        tableResult = getDatabaseManager().query(sql);
        while (tableResult.next()) {
            Table table;
            table = Table.parse(tableResult);
            if (loadCommentsFromDB) {
                sql = SQL.QUERY_GETCOMMENT_TABLE;
                sql = sql.replaceFirst("\\?", "" + table.getId());
                commentResult = getDatabaseManager().query(sql);
                if (commentResult.next()) {
                    table.setComment(commentResult.getString("value"));
                }
                commentResult.close();
            }
            sql = SQL.QUERY_COLUMNS;
            sql = sql.replaceFirst("\\?", "" + table.getId());
            columnResult = getDatabaseManager().query(sql);
            while (columnResult.next()) {
                TableColumn tc = TableColumn.parse(columnResult);
                if (loadCommentsFromDB) {
                    sql = SQL.QUERY_GETCOMMENT_COLUMN;
                    sql = sql.replaceFirst("\\?", "" + table.getId());
                    sql = sql.replaceFirst("\\?", "" + tc.getId());
                    commentResult = getDatabaseManager().query(sql);
                    if (commentResult.next()) {
                        tc.setComment(commentResult.getString("value"));
                    }
                    commentResult.close();
                }
                table.getColumns().add(tc);
            }
            columnResult.close();
            sql = SQL.QUERY_TRIGGERS;
            sql = sql.replaceFirst("\\?", "" + table.getId());
            triggerResult = getDatabaseManager().query(sql);
            while (triggerResult.next()) {
                TableTrigger trigger;
                trigger = TableTrigger.parse(triggerResult);
                if (loadCommentsFromDB) {
                    sql = SQL.QUERY_GETCOMMENT_TRIGGER;
                    sql = sql.replaceFirst("\\?", "" + trigger.getId());
                    commentResult = getDatabaseManager().query(sql);
                    if (commentResult.next()) {
                        trigger.setComment(commentResult.getString("value"));
                    }
                    commentResult.close();
                }
                if (table.getTriggers().contains(trigger)) {
                    TableTrigger original = table.getTriggers().get(table.getTriggers().indexOf(trigger));
                    original.setCode(original.getCode() + trigger.getCode());
                } else {
                    table.getTriggers().add(trigger);
                }
            }
            triggerResult.close();
            getTables().add(table);
        }
        tableResult.close();
    }

    private void readStoredProcedures(boolean loadCommentsFromDB) throws Exception {
        String sql;
        ResultSet storedProceduresResult, commentResult;
        sql = SQL.QUERY_STOREDPROCEDURES;
        storedProceduresResult = getDatabaseManager().query(sql);
        while (storedProceduresResult.next()) {
            StoredProcedure storedProcedure;
            storedProcedure = StoredProcedure.parse(storedProceduresResult);
            if (loadCommentsFromDB) {
                sql = SQL.QUERY_GETCOMMENT_STOREDPROCEDURE;
                sql = sql.replaceFirst("\\?", "" + storedProcedure.getId());
                commentResult = getDatabaseManager().query(sql);
                if (commentResult.next()) {
                    storedProcedure.setComment(commentResult.getString("value"));
                }
                commentResult.close();
            }
            getStoredProcedures().add(storedProcedure);
        }
        storedProceduresResult.close();
    }

    private void readViews(boolean loadCommentsFromDB) throws Exception {
        String sql;
        ResultSet viewsResult, commentResult;
        sql = SQL.QUERY_VIEWS;
        viewsResult = getDatabaseManager().query(sql);
        while (viewsResult.next()) {
            View view;
            view = View.parse(viewsResult);
            if (loadCommentsFromDB) {
                sql = SQL.QUERY_GETCOMMENT_VIEW;
                sql = sql.replaceFirst("\\?", "" + view.getId());
                commentResult = getDatabaseManager().query(sql);
                if (commentResult.next()) {
                    view.setComment(commentResult.getString("value"));
                }
                commentResult.close();
            }
            getViews().add(view);
        }
        viewsResult.close();
    }

    public ArrayList<Table> getTables() {
        return tables;
    }

    public ArrayList<StoredProcedure> getStoredProcedures() {
        return storedProcedures;
    }

    public ArrayList<View> getViews() {
        return views;
    }

    public DBManager getDatabaseManager() throws Exception{
        if (!databaseManager.isConnected()) {
            databaseManager.connect();
        }
        return databaseManager;
    }

}
