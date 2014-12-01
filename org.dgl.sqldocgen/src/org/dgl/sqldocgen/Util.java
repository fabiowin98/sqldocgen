package org.dgl.sqldocgen;

import java.awt.Component;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.filechooser.FileNameExtensionFilter;
import org.dgl.manager.ConfigurationManager;
import org.dgl.manager.DBManager;
import org.dgl.manager.LangManager;
import org.dgl.sqldocgen.db.StoredProcedure;
import org.dgl.sqldocgen.db.Table;
import org.dgl.sqldocgen.db.TableColumn;
import org.dgl.sqldocgen.db.TableTrigger;
import org.dgl.sqldocgen.db.View;

public class Util {

    public static final FileNameExtensionFilter SQLDOCGENERATOR_FILENAMEEXTENSIONFILTER = new FileNameExtensionFilter("SqlDocGen Files", "sdgf");
    public static final FileNameExtensionFilter HTML_FILENAMEEXTENSIONFILTER = new FileNameExtensionFilter("HTML", "html");
    public static final String DEFAULT_OUTPUT_FONT_FAMILY = "Arial";
    public static final String DEFAULT_OUTPUT_FONT_SIZE = "12";

    public static DBManager createDBManagerFromSQLConfiguration(SqlConfiguration sqlConfiguration) throws Exception {
        String driver, url, username, password;
        DBManager output;
        driver = "net.sourceforge.jtds.jdbc.Driver";
        url = "jdbc:jtds:sqlserver://?;databasename=?";
        username = ";user=?";
        password = ";password=?";
        url = url.replaceFirst("\\?", sqlConfiguration.getServer());
        url = url.replaceFirst("\\?", sqlConfiguration.getDatabase());
        username = username.replaceFirst("\\?", sqlConfiguration.getUsername());
        password = password.replaceFirst("\\?", sqlConfiguration.getPassword());
        output = new DBManager(driver, url, username, password);
        return output;
    }

    public static void waitFor(JFrame popup) {
        while (popup.isVisible()) {
            try {
                Thread.sleep(100);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    public static void sleep(int ms) {
        try {
            Thread.sleep(ms);
        } catch (Exception ignored) {
            ignored.printStackTrace();
        }
    }

    public static void saveSqlDocGeneratorToDB(SqlDocGenerator what) throws Exception {
        String sql;
        for (Table t : what.getTables()) {
            for (TableColumn tc : t.getColumns()) {
                try {
                    sql = SQL.SP_DROPCOMMENT_COLUMN;
                    sql = sql.replaceFirst("\\?", t.getName());
                    sql = sql.replaceFirst("\\?", tc.getName());
                    what.getDatabaseManager().call(sql);
                } catch (Exception ignored) {
                }
                sql = SQL.SP_ADDCOMMENT_COLUMN;
                sql = sql.replaceFirst("\\?", tc.getComment().replaceAll("'", "''"));
                sql = sql.replaceFirst("\\?", t.getName());
                sql = sql.replaceFirst("\\?", tc.getName());
                what.getDatabaseManager().call(sql);
            }
            for (TableTrigger tr : t.getTriggers()) {
                try {
                    sql = SQL.SP_DROPCOMMENT_TRIGGER;
                    sql = sql.replaceFirst("\\?", t.getName());
                    sql = sql.replaceFirst("\\?", tr.getName());
                    what.getDatabaseManager().call(sql);
                } catch (Exception ignored) {
                }
                sql = SQL.SP_ADDCOMMENT_TRIGGER;
                sql = sql.replaceFirst("\\?", tr.getComment().replaceAll("'", "''"));
                sql = sql.replaceFirst("\\?", t.getName());
                sql = sql.replaceFirst("\\?", tr.getName());
                what.getDatabaseManager().call(sql);
            }
            try {
                sql = SQL.SP_DROPCOMMENT_TABLE;
                sql = sql.replaceFirst("\\?", t.getName());
                what.getDatabaseManager().call(sql);
            } catch (Exception ignored) {
            }
            sql = SQL.SP_ADDCOMMENT_TABLE;
            sql = sql.replaceFirst("\\?", t.getComment().replaceAll("'", "''"));
            sql = sql.replaceFirst("\\?", t.getName());
            what.getDatabaseManager().call(sql);
        }
        for (View v : what.getViews()) {
            try {
                sql = SQL.SP_DROPCOMMENT_VIEW;
                sql = sql.replaceFirst("\\?", v.getName());
                what.getDatabaseManager().call(sql);
            } catch (Exception ignored) {
            }
            sql = SQL.SP_ADDCOMMENT_VIEW;
            sql = sql.replaceFirst("\\?", v.getComment().replaceAll("'", "''"));
            sql = sql.replaceFirst("\\?", v.getName());
            what.getDatabaseManager().call(sql);
        }
        for (StoredProcedure sp : what.getStoredProcedures()) {
            try {
                sql = SQL.SP_DROPCOMMENT_STOREDPROCEDURE;
                sql = sql.replaceFirst("\\?", sp.getName());
                what.getDatabaseManager().call(sql);
            } catch (Exception ignored) {
            }
            sql = SQL.SP_ADDCOMMENT_STOREDPROCEDURE;
            sql = sql.replaceFirst("\\?", sp.getComment().replaceAll("'", "''"));
            sql = sql.replaceFirst("\\?", sp.getName());
            what.getDatabaseManager().call(sql);
        }
    }

    public static void saveSqlDocGeneratorToFile(SqlDocGenerator what, String where) throws Exception {
        ObjectOutputStream output;
        output = new ObjectOutputStream(new FileOutputStream(where));
        output.writeObject(what.getDatabaseManager().getDriver());
        output.writeObject(what.getDatabaseManager().getUrl());
        output.writeObject(what.getDatabaseManager().getUsername());
        output.writeObject(what.getDatabaseManager().getPassword());
        output.writeInt(what.getTables().size());
        for (Table t : what.getTables()) {
            output.writeObject(t);
        }
        output.writeInt(what.getViews().size());
        for (View v : what.getViews()) {
            output.writeObject(v);
        }
        output.writeInt(what.getStoredProcedures().size());
        for (StoredProcedure sp : what.getStoredProcedures()) {
            output.writeObject(sp);
        }
        output.flush();
        output.close();
    }

    public static SqlDocGenerator loadSqlDocGenerator(String where) throws Exception {
        ObjectInputStream input;
        SqlDocGenerator what;
        String driver, url, username, password;
        int howmany;
        input = new ObjectInputStream(new FileInputStream(where));
        driver = (String) input.readObject();
        url = (String) input.readObject();
        username = (String) input.readObject();
        password = (String) input.readObject();
        what = new SqlDocGenerator(new DBManager(driver, url, username, password));
        howmany = (int) input.readInt();
        for (int i = 0; i < howmany; i++) {
            what.getTables().add((Table) input.readObject());
        }
        howmany = (int) input.readInt();
        for (int i = 0; i < howmany; i++) {
            what.getViews().add((View) input.readObject());
        }
        howmany = (int) input.readInt();
        for (int i = 0; i < howmany; i++) {
            what.getStoredProcedures().add((StoredProcedure) input.readObject());
        }
        input.close();
        return what;
    }

    public static void generateHtmlOutputFile(SqlDocGenerator what, String where) throws Exception {
        PrintWriter output;
        output = new PrintWriter(new FileWriter(where));
        output.print(generateOutputFile(what));
        output.flush();
        output.close();
    }

    private static String generateOutputFile(SqlDocGenerator what) throws Exception {
        StringBuffer output;
        String aux, country;
        ConfigurationManager configManager;
        LangManager langManager;
        String fontFamily, fontSize;
        configManager = new ConfigurationManager();
        fontFamily = configManager.get("OUTPUT_FONT_FAMILY");
        if (fontFamily == null) {
            fontFamily = Util.DEFAULT_OUTPUT_FONT_FAMILY;
        }
        fontSize = configManager.get("OUTPUT_FONT_SIZE");
        if (fontSize == null) {
            fontSize = Util.DEFAULT_OUTPUT_FONT_SIZE;
        }
        country = configManager.get("LANG");
        if (country == null) {
            throw new Exception("LANG property not defined");
        }
        output = new StringBuffer();
        langManager = new LangManager(country);
        output.append("<html><body style=\"font-family:" + fontFamily + ";font-size:" + fontSize + "px;\">");
        output.append("<h1>?</h1>".replaceFirst("\\?", langManager.get("GENERATEOUTPUTFILE_DATABASE")));
        output.append("<h2>?</h2>".replaceFirst("\\?", langManager.get("GENERATEOUTPUTFILE_TABLES")));
        if (what.getTables().isEmpty()) {
            output.append(langManager.get("GENERATEOUTPUTFILE_EMPTY"));
        }
        for (Table t : what.getTables()) {
            aux = "<h3>?</h3>";
            aux = aux.replaceFirst("\\?", langManager.get("GENERATEOUTPUTFILE_TABLE") + " " + t.getName());
            output.append(aux);
            aux = "<p align=\"justify\" style=\"word-wrap: break-word;\">?</p>";
            aux = aux.replaceFirst("\\?", t.getComment());
            output.append(aux);
            output.append("<table style=\"table-layout:fixed;\" width=100% cellspacing=\"0\" cellpadding=\"0\">");
            aux = "<tr><td width=100% colspan=3  style=\"background-color:?;border-style:solid;border-width:1px;\">?</td></tr>";
            aux = aux.replaceFirst("\\?", configManager.get("HEADERS_BGCOLOR"));
            aux = aux.replaceFirst("\\?", langManager.get("GENERATEOUTPUTFILE_TABLECOLUMNS"));
            output.append(aux);
            aux = "<tr><td width=30% style=\"background-color:?;border-style:solid;border-width:1px;\">?</td><td width=30% style=\"background-color:?;border-style:solid;border-width:1px;\">?</td><td width=40% style=\"background-color:?;border-style:solid;border-width:1px;\">?</td></tr>";
            aux = aux.replaceFirst("\\?", configManager.get("HEADERS_BGCOLOR"));
            aux = aux.replaceFirst("\\?", langManager.get("GENERATEOUTPUTFILE_TABLECOLUMNNAME"));
            aux = aux.replaceFirst("\\?", configManager.get("HEADERS_BGCOLOR"));
            aux = aux.replaceFirst("\\?", langManager.get("GENERATEOUTPUTFILE_TABLECOLUMNTYPE"));
            aux = aux.replaceFirst("\\?", configManager.get("HEADERS_BGCOLOR"));
            aux = aux.replaceFirst("\\?", langManager.get("GENERATEOUTPUTFILE_COMMENT"));
            output.append(aux);
            for (TableColumn tc : t.getColumns()) {
                aux = "<tr><td width=30% style=\"border-style:solid;border-width:1px;\">?</td><td width=30% style=\"border-style:solid;border-width:1px;\">?</td><td width=40% style=\"word-wrap: break-word;border-style:solid;border-width:1px;\">?</td></tr>";
                aux = aux.replaceFirst("\\?", tc.getName());
                aux = aux.replaceFirst("\\?", tc.getType());
                aux = aux.replaceFirst("\\?", tc.getComment());
                output.append(aux);
            }
            output.append("</table>");
            for (TableTrigger tr : t.getTriggers()) {
                aux = "<h4>?</h4>";
                aux = aux.replaceFirst("\\?", langManager.get("GENERATEOUTPUTFILE_TABLETRIGGER") + " " + tr.getName());
                output.append(aux);
                aux = "<p align=\"justify\">?</p>";
                aux = aux.replaceFirst("\\?", tr.getComment());
                output.append(aux);
                output.append("<table style=\"table-layout:fixed;\" width=100% cellspacing=\"0\" cellpadding=\"0\">");
                aux = "<tr><td width=100% style=\"background-color:?;border-style:solid;border-width:1px;\">?</td></tr>";
                aux = aux.replaceFirst("\\?", configManager.get("HEADERS_BGCOLOR"));
                aux = aux.replaceFirst("\\?", langManager.get("GENERATEOUTPUTFILE_CODE"));
                output.append(aux);
                aux = "<tr><td width=100% style=\"word-wrap: break-word;font-familiy:courier;font-size:8px;border-style:solid;border-width:1px;\"><pre>?</pre></td></tr>";
                aux = aux.replaceFirst("\\?", tr.getCode());
                output.append(aux);
            }
            output.append("</table><br>");
        }
        output.append("<h2>?</h2>".replaceFirst("\\?", langManager.get("GENERATEOUTPUTFILE_VIEWS")));
        if (what.getViews().isEmpty()) {
            output.append(langManager.get("GENERATEOUTPUTFILE_EMPTY"));
        }
        for (View v : what.getViews()) {
            aux = "<h3>?</h3>";
            aux = aux.replaceFirst("\\?", langManager.get("GENERATEOUTPUTFILE_VIEW") + " " + v.getName());
            output.append(aux);
            aux = "<p align=\"justify\" style=\"word-wrap: break-word;\">?</p>";
            aux = aux.replaceFirst("\\?", v.getComment());
            output.append(aux);
            output.append("<table style=\"table-layout:fixed;\" width=100% cellspacing=\"0\" cellpadding=\"0\">");
            aux = "<tr><td width=100% colspan=3 style=\"background-color:?;border-style:solid;border-width:1px;\">?</td></tr>";
            aux = aux.replaceFirst("\\?", configManager.get("HEADERS_BGCOLOR"));
            aux = aux.replaceFirst("\\?", langManager.get("GENERATEOUTPUTFILE_CODE"));
            output.append(aux);
            aux = "<tr><td width=100% colspan=3 style=\"word-wrap: break-word;font-familiy:courier;font-size:8px;border-style:solid;border-width:1px;\"><pre>?</pre></td></tr>";
            aux = aux.replaceFirst("\\?", v.getCode());
            output.append(aux);
            output.append("</table><br>");
        }
        output.append("<h2>?</h2>".replaceFirst("\\?", langManager.get("GENERATEOUTPUTFILE_STOREDPROCEDURES")));
        if (what.getStoredProcedures().isEmpty()) {
            output.append(langManager.get("GENERATEOUTPUTFILE_EMPTY"));
        }
        for (StoredProcedure sp : what.getStoredProcedures()) {
            aux = "<h3>?</h3>";
            aux = aux.replaceFirst("\\?", langManager.get("GENERATEOUTPUTFILE_STOREDPROCEDURE") + " " + sp.getName());
            output.append(aux);
            aux = "<p align=\"justify\" style=\"word-wrap: break-word;\">?</p>";
            aux = aux.replaceFirst("\\?", sp.getComment());
            output.append(aux);
            output.append("<table style=\"table-layout:fixed;\" width=100% cellspacing=\"0\" cellpadding=\"0\">");
            aux = "<tr><td width=100% colspan=3 style=\"background-color:?;border-style:solid;border-width:1px;\">?</td></tr>";
            aux = aux.replaceFirst("\\?", configManager.get("HEADERS_BGCOLOR"));
            aux = aux.replaceFirst("\\?", langManager.get("GENERATEOUTPUTFILE_CODE"));
            output.append(aux);
            aux = "<tr><td width=100% colspan=3 style=\"word-wrap: break-word;font-familiy:courier;font-size:8px;border-style:solid;border-width:1px;\"><pre>?</pre></td></tr>";
            aux = aux.replaceFirst("\\?", sp.getCode() == null ? langManager.get("GENERATEOUTPUTFILE_STOREDPROCEDUREENCRYPTED") : sp.getCode());
            output.append(aux);
            output.append("</table><br>");
        }
        output.append("</body></html>");
        return output.toString();
    }

    public static final void importComments(SqlDocGenerator from, SqlDocGenerator to) {
        for (Table t : from.getTables()) {
            if (to.getTables().contains(t)) {
                Table aux = to.getTables().get(to.getTables().indexOf(t));
                aux.setComment(t.getComment());
                for (TableColumn tc : t.getColumns()) {
                    if (aux.getColumns().contains(tc)) {
                        aux.getColumns().get(aux.getColumns().indexOf(tc)).setComment(tc.getComment());
                    }
                }
                for (TableTrigger tr : t.getTriggers()) {
                    if (aux.getTriggers().contains(tr)) {
                        aux.getTriggers().get(aux.getTriggers().indexOf(tr)).setComment(tr.getComment());
                    }
                }
            }
        }
        for (View v : from.getViews()) {
            if (to.getViews().contains(v)) {
                to.getViews().get(to.getViews().indexOf(v)).setComment(v.getComment());
            }
        }
        for (StoredProcedure sp : from.getStoredProcedures()) {
            if (to.getStoredProcedures().contains(sp)) {
                to.getStoredProcedures().get(to.getStoredProcedures().indexOf(sp)).setComment(sp.getComment());
            }
        }
    }

    public static final void setFocusOn(Component component) {
        final Component reference = component;
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                reference.requestFocus();
            }
        });
    }
}
