package org.dgl.sqldocgen.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import org.dgl.manager.ConfigurationManager;
import org.dgl.manager.LangManager;
import org.dgl.manager.LogManager;
import org.dgl.sqldocgen.SqlConfiguration;
import org.dgl.sqldocgen.SqlDocGenerator;
import org.dgl.sqldocgen.Util;

public class ConfigurationFrame extends JFrame implements ActionListener {

    private JLabel serverLabel, databaseLabel, usernameLabel, passwordLabel;
    private JTextField serverField, databaseField, usernameField;
    private JPasswordField passwordField;
    private LangManager langManager;
    private JPanel loadPanel, dataPanel, buttonPanel;
    private JPanel loadCenterPanel;
    private JPanel dataLeftPanel, dataCenterPanel;
    private JButton loadButton, okButton, cancelButton;
    private JRadioButton loadRadio, dataRadio;
    private final Dimension size = new Dimension(350, 200);
    private SqlDocGenerator sqlDocGeneratorFromFile, sqlDocGeneratorFrameFromDatabase;
    private boolean loadCommentsFromDB;

    public ConfigurationFrame() throws Exception {
        super("");
        gui();
        translate();
    }

    private void gui() throws Exception {
        this.setSize(size);
        this.setLayout(new BorderLayout());
        loadPanel = new JPanel(new BorderLayout());
        loadCenterPanel = new JPanel();
        loadCenterPanel.setLayout(new GridLayout(2, 2));
        loadRadio = new JRadioButton();
        loadRadio.addActionListener(this);
        loadCenterPanel.add(loadRadio);
        loadButton = new JButton();
        loadButton.addActionListener(this);
        loadCenterPanel.add(loadButton);
        dataRadio = new JRadioButton();
        dataRadio.addActionListener(this);
        loadCenterPanel.add(dataRadio);
        loadCenterPanel.add(new JLabel());
        loadPanel.add(loadCenterPanel, BorderLayout.CENTER);
        ButtonGroup radioGroup = new ButtonGroup();
        radioGroup.add(loadRadio);
        radioGroup.add(dataRadio);
        this.add(loadPanel, BorderLayout.NORTH);
        dataPanel = new JPanel();
        dataPanel.setLayout(new BorderLayout(10, 10));
        dataLeftPanel = new JPanel(new GridLayout(4, 1));
        serverLabel = new JLabel();
        dataLeftPanel.add(serverLabel);
        databaseLabel = new JLabel();
        dataLeftPanel.add(databaseLabel);
        usernameLabel = new JLabel();
        dataLeftPanel.add(usernameLabel);
        passwordLabel = new JLabel();
        dataLeftPanel.add(passwordLabel);
        dataPanel.add(dataLeftPanel, BorderLayout.WEST);
        dataCenterPanel = new JPanel(new GridLayout(4, 1));
        serverField = new JTextField();
        dataCenterPanel.add(serverField);
        databaseField = new JTextField();
        dataCenterPanel.add(databaseField);
        usernameField = new JTextField();
        dataCenterPanel.add(usernameField);
        passwordField = new JPasswordField();
        dataCenterPanel.add(passwordField);
        dataPanel.add(dataCenterPanel, BorderLayout.CENTER);
        this.add(dataPanel, BorderLayout.CENTER);
        buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        okButton = new JButton();
        okButton.addActionListener(this);
        buttonPanel.add(okButton);
        cancelButton = new JButton();
        cancelButton.addActionListener(this);
        buttonPanel.add(cancelButton);
        this.add(buttonPanel, BorderLayout.SOUTH);
        this.setLocationRelativeTo(null);
        this.setVisible(true);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.getRootPane().setDefaultButton(okButton);
        this.setResizable(false);
        loadRadio.setSelected(true);
        setDataPanelComponentsEnabled(false);
    }

    private void translate() throws Exception {
        ConfigurationManager configManager;
        String country;
        configManager = new ConfigurationManager();
        country = configManager.get("LANG");
        if (country == null) {
            throw new Exception("LANG property not defined");
        }
        langManager = new LangManager(country);
        this.setTitle(langManager.get("CONFIGURATIONFRAME_TITLE"));
        serverLabel.setText(langManager.get("CONFIGURATIONFRAME_SERVERLABEL"));
        databaseLabel.setText(langManager.get("CONFIGURATIONFRAME_DBLABEL"));
        cancelButton.setText(langManager.get("CONFIGURATIONFRAME_CANCELBUTTON"));
        usernameLabel.setText(langManager.get("CONFIGURATIONFRAME_USERNAMELABEL"));
        passwordLabel.setText(langManager.get("CONFIGURATIONFRAME_PASSWORDLABEL"));
        okButton.setText(langManager.get("CONFIGURATIONFRAME_OKBUTTON"));
        loadRadio.setText(langManager.get("CONFIGURATIONFRAME_LOADLABEL"));
        loadButton.setText(langManager.get("CONFIGURATIONFRAME_LOADBUTTON"));
        dataRadio.setText(langManager.get("CONFIGURATIONFRAME_DATALABEL"));
    }

    private boolean validateInput() {
        String message;
        message = null;
        if (serverField.getText().trim().equals("")) {
            message = langManager.get("CONFIGURATIONFRAME_EMPTYSERVER");
        }
        if (databaseField.getText().trim().equals("")) {
            message = langManager.get("CONFIGURATIONFRAME_EMPTYDATABASE");
        }
        if (usernameField.getText().trim().equals("")) {
            message = langManager.get("CONFIGURATIONFRAME_EMPTYUSERNAME");
        }
        if (passwordField.getText().trim().equals("")) {
            message = langManager.get("CONFIGURATIONFRAME_EMPTYPASSWORD");
        }
        if (message != null) {
            JOptionPane.showMessageDialog(this, message, "", JOptionPane.WARNING_MESSAGE);
            return false;
        } else {
            return true;
        }
    }

    private void setDataPanelComponentsEnabled(boolean value) {
        serverField.setEnabled(value);
        databaseField.setEnabled(value);
        usernameField.setEnabled(value);
        passwordField.setEnabled(value);
        loadButton.setEnabled(!value);
        okButton.setEnabled(value);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        int userChoice;
        if ((e.getSource().equals(okButton)) || (e.getSource().equals(loadButton))) {
            userChoice = JOptionPane.showConfirmDialog(this, langManager.get("CONFIGURATIONFRAME_ASKLOADFROMDB"), "", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
            loadCommentsFromDB = (userChoice == JOptionPane.YES_OPTION);
        }
        if (e.getSource().equals(okButton)) {
            if (validateInput()) {
                SqlConfiguration sqlConfiguration;
                sqlConfiguration = new SqlConfiguration();
                sqlConfiguration.setServer(serverField.getText());
                sqlConfiguration.setDatabase(databaseField.getText());
                sqlConfiguration.setUsername(usernameField.getText());
                sqlConfiguration.setPassword(passwordField.getText());
                try {
                    setSqlDocGeneratorFrameFromDatabase(new SqlDocGenerator(sqlConfiguration));
                } catch (Exception ex) {
                    LogManager.error(LogManager.describeException(ex));
                    JOptionPane.showMessageDialog(this, ex.toString(), "", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                dispose();
            }
        }
        if (e.getSource().equals(cancelButton)) {
            System.exit(0);
        }
        if (e.getSource().equals(loadRadio)) {
            setDataPanelComponentsEnabled(false);
        }
        if (e.getSource().equals(dataRadio)) {
            setDataPanelComponentsEnabled(true);
        }
        if (e.getSource().equals(loadButton)) {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setFileFilter(Util.SQLDOCGENERATOR_FILENAMEEXTENSIONFILTER);
            fileChooser.setMultiSelectionEnabled(false);
            fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
            userChoice = fileChooser.showOpenDialog(this);
            if (userChoice != JFileChooser.APPROVE_OPTION) {
                return;
            }
            try {
                setSqlDocGeneratorFromFile(Util.loadSqlDocGenerator(fileChooser.getSelectedFile().getAbsolutePath()));
                setSqlDocGeneratorFrameFromDatabase(new SqlDocGenerator(getSqlDocGeneratorFromFile().getDatabaseManager()));
            } catch (Exception ex) {
                ex.printStackTrace();
                LogManager.error(LogManager.describeException(ex));
                JOptionPane.showMessageDialog(this, ex.toString(), "", JOptionPane.ERROR_MESSAGE);
                return;
            }
            dispose();
        }
    }

    public SqlDocGenerator getSqlDocGeneratorFromFile() {
        return sqlDocGeneratorFromFile;
    }

    public void setSqlDocGeneratorFromFile(SqlDocGenerator sqlDocGeneratorFromFile) {
        this.sqlDocGeneratorFromFile = sqlDocGeneratorFromFile;
    }

    public SqlDocGenerator getSqlDocGeneratorFrameFromDatabase() {
        return sqlDocGeneratorFrameFromDatabase;
    }

    public void setSqlDocGeneratorFrameFromDatabase(SqlDocGenerator sqlDocGeneratorFrameFromDatabase) {
        this.sqlDocGeneratorFrameFromDatabase = sqlDocGeneratorFrameFromDatabase;
    }

    public boolean isLoadCommentsFromDB() {
        return loadCommentsFromDB;
    }

}
