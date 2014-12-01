package org.dgl.sqldocgen;

import javax.swing.JOptionPane;
import org.dgl.manager.LogManager;
import org.dgl.sqldocgen.gui.ConfigurationFrame;
import org.dgl.sqldocgen.gui.SqlDocGeneratorFrame;

public class Main implements Runnable {

    public static void main(String[] args) {
        new Thread(new Main()).start();
    }

    @Override
    public void run() {
        ConfigurationFrame configurationFrame;
        SqlDocGenerator sqlDocGeneratorFromFile, sqlDocGeneratorFromDatabase;
        boolean isLoading;
        try {
            configurationFrame = new ConfigurationFrame();
            Util.waitFor(configurationFrame);
            sqlDocGeneratorFromDatabase = configurationFrame.getSqlDocGeneratorFrameFromDatabase();
            sqlDocGeneratorFromFile = configurationFrame.getSqlDocGeneratorFromFile();
            isLoading = sqlDocGeneratorFromFile != null;
            sqlDocGeneratorFromDatabase.read(configurationFrame.isLoadCommentsFromDB());
            if (isLoading) {
                Util.importComments(sqlDocGeneratorFromFile, sqlDocGeneratorFromDatabase);
            }
            Util.waitFor(new SqlDocGeneratorFrame(sqlDocGeneratorFromDatabase));
            sqlDocGeneratorFromDatabase.dispose();
        } catch (Exception ex) {
            ex.printStackTrace();
            LogManager.error(LogManager.describeException(ex));
            JOptionPane.showMessageDialog(null, ex.toString(), "", JOptionPane.ERROR_MESSAGE);
        }
    }

}
