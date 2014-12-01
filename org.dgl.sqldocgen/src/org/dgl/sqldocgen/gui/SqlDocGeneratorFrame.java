package org.dgl.sqldocgen.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowListener;
import java.io.File;
import java.util.ArrayList;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.ListSelectionModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import org.dgl.manager.ConfigurationManager;
import org.dgl.manager.LangManager;
import org.dgl.manager.LogManager;
import org.dgl.sqldocgen.FormClosingPreventer;
import org.dgl.sqldocgen.SqlDocGenerator;
import org.dgl.sqldocgen.Util;
import org.dgl.sqldocgen.db.Commentable;
import org.dgl.sqldocgen.db.StoredProcedure;
import org.dgl.sqldocgen.db.Table;
import org.dgl.sqldocgen.db.TableColumn;
import org.dgl.sqldocgen.db.TableTrigger;
import org.dgl.sqldocgen.db.View;
import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.fife.ui.rsyntaxtextarea.SyntaxConstants;
import org.fife.ui.rtextarea.RTextScrollPane;

public class SqlDocGeneratorFrame extends JFrame implements ActionListener, ListSelectionListener, ChangeListener, MouseListener {

    private final SqlDocGenerator sqlDocGenerator;
    private LangManager langManager;
    private JPanel tablesPanel, viewsPanel, storedProceduresPanel;
    private JPanel tablesRightPanel, viewsRightPanel, storedProceduresRightPanel;
    private JPanel tablesLeftPanel, viewsLeftPanel, storedProceduresLeftPanel;
    private JPanel tableCommentPanel, tableColumnsPanel, tableTriggersPanel;
    private JPanel viewCommentPanel, viewCodePanel;
    private JPanel storedProcedureCommentPanel, storedProcedureCodePanel;
    private JList tablesList, tableColumnsList, tableTriggersList, viewsList, storedProceduresList;
    private JLabel tablesListLabel, tableColumnsListLabel, tableTriggersListLabel, viewsListLabel, storedProceduresListLabel;
    private JLabel tableCommentLabel, tableColumnsLabel, tableTriggersLabel, viewCommentLabel, viewCodeLabel, storedProcedureCommentLabel, storedProcedureCodeLabel;
    private JTextArea tableCommentField, viewCommentField, storedProcedureCommentField;
    private RSyntaxTextArea viewCodeField, storedProcedureCodeField;
    private JTable tableColumnsTable, tableTriggersTable;
    private JTabbedPane tabPanel;
    private JMenuBar menuBar;
    private JMenu fileMenu, aboutMenu;
    private JMenuItem saveToFileMenuItem, saveToDBMenuItem, generateMenuItem, exitMenuItem, aboutMenuItem;
    private ArrayList<String> tables, tableColumns, tableTriggers, views, storedProcedures;
    private final Dimension size = new Dimension(800, 600);
    private Commentable selectedObject;
    private FormClosingPreventer formClosingPresenter;

    public SqlDocGeneratorFrame(SqlDocGenerator sqlDocGenerator) throws Exception {
        super("");
        this.sqlDocGenerator = sqlDocGenerator;
        gui();
        translate();
        setVisible(true);
        formClosingPresenter = new FormClosingPreventer(langManager);
        addWindowListener(formClosingPresenter);
    }

    private void gui() throws Exception {
        this.setSize(size);
        this.setLayout(new BorderLayout());
        //TAB PANEL
        tabPanel = new JTabbedPane(JTabbedPane.TOP);
        tabPanel.addChangeListener(this);
        guiTablesPanel();
        tabPanel.addTab("", tablesPanel);
        guiViewsPanel();
        tabPanel.addTab("", viewsPanel);
        guiStoredProcedurePanel();
        tabPanel.addTab("", storedProceduresPanel);
        this.add(tabPanel, BorderLayout.CENTER);
        //MENU
        menuBar = new JMenuBar();
        fileMenu = new JMenu();
        menuBar.add(fileMenu);
        aboutMenu = new JMenu();
        menuBar.add(aboutMenu);
        saveToFileMenuItem = new JMenuItem();
        saveToFileMenuItem.addActionListener(this);
        saveToDBMenuItem = new JMenuItem();
        saveToDBMenuItem.addActionListener(this);
        generateMenuItem = new JMenuItem();
        generateMenuItem.addActionListener(this);
        exitMenuItem = new JMenuItem();
        exitMenuItem.addActionListener(this);
        fileMenu.add(saveToFileMenuItem);
        fileMenu.add(saveToDBMenuItem);
        fileMenu.add(generateMenuItem);
        fileMenu.add(new JSeparator());
        fileMenu.add(exitMenuItem);
        aboutMenuItem = new JMenuItem();
        aboutMenuItem.addActionListener(this);
        aboutMenu.add(aboutMenuItem);
        setJMenuBar(menuBar);
        //END
        this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        this.setLocationRelativeTo(null);
        tablesList.setPrototypeCellValue(tablesList.getPrototypeCellValue());
    }

    private void guiTablesPanel() {
        tablesPanel = new JPanel(new BorderLayout());
        //TABLES PANEL - LEFT
        tablesLeftPanel = new JPanel(new BorderLayout());
        tablesListLabel = new JLabel();
        tablesLeftPanel.add(tablesListLabel, BorderLayout.NORTH);
        tables = new ArrayList<>();
        for (Table table : sqlDocGenerator.getTables()) {
            tables.add(table.getName());
        }
        tablesList = new JList(tables.toArray());
        tablesList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tablesList.setLayoutOrientation(JList.VERTICAL);
        tablesList.setFixedCellWidth(getWidth() * 30 / 100);
        tablesList.addListSelectionListener(this);
        tablesLeftPanel.add(new JScrollPane(tablesList));
        tablesPanel.add(tablesLeftPanel, BorderLayout.WEST);
        //TABLES PANEL - RIGHT
        tablesRightPanel = new JPanel(new GridLayout(3, 1));
        tablesPanel.add(tablesRightPanel, BorderLayout.CENTER);
        tableCommentPanel = new JPanel(new BorderLayout());
        tableCommentLabel = new JLabel();
        tableCommentPanel.add(tableCommentLabel, BorderLayout.NORTH);
        tableCommentField = new JTextArea();
        tableCommentField.setEditable(false);
        tableCommentPanel.add(new JScrollPane(tableCommentField), BorderLayout.CENTER);
        tablesRightPanel.add(tableCommentPanel);
        tableColumnsPanel = new JPanel(new BorderLayout());
        tableColumnsLabel = new JLabel();
        tableColumnsPanel.add(tableColumnsLabel, BorderLayout.NORTH);
        tableColumnsTable = new JTable();
        tableColumnsTable.addMouseListener(this);
        tableColumnsPanel.add(new JScrollPane(tableColumnsTable), BorderLayout.CENTER);
        tablesRightPanel.add(tableColumnsPanel);
        tableTriggersPanel = new JPanel(new BorderLayout());
        tableTriggersLabel = new JLabel();
        tableTriggersPanel.add(tableTriggersLabel, BorderLayout.NORTH);
        tableTriggersTable = new JTable();
        tableTriggersTable.addMouseListener(this);
        tableTriggersPanel.add(new JScrollPane(tableTriggersTable), BorderLayout.CENTER);
        tablesRightPanel.add(tableTriggersPanel);
    }

    private void guiViewsPanel() {
        GridBagConstraints c = new GridBagConstraints();
        viewsPanel = new JPanel(new BorderLayout());
        //LEFT
        viewsLeftPanel = new JPanel(new BorderLayout());
        viewsListLabel = new JLabel();
        viewsLeftPanel.add(viewsListLabel, BorderLayout.NORTH);
        views = new ArrayList<>();
        for (View view : sqlDocGenerator.getViews()) {
            views.add(view.getName());
        }
        viewsList = new JList(views.toArray());
        viewsList.setFixedCellWidth(this.getWidth() * 30 / 100);
        viewsList.addListSelectionListener(this);
        viewsLeftPanel.add(new JScrollPane(viewsList), BorderLayout.CENTER);
        viewsPanel.add(viewsLeftPanel, BorderLayout.WEST);
        //RIGHT
        viewsRightPanel = new JPanel(new GridBagLayout());
        viewCommentPanel = new JPanel(new BorderLayout());
        viewCommentLabel = new JLabel();
        viewCommentPanel.add(viewCommentLabel, BorderLayout.NORTH);
        viewCommentField = new JTextArea();
        viewCommentField.setEditable(false);
        viewCommentPanel.add(new JScrollPane(viewCommentField), BorderLayout.CENTER);
        c.gridx = 0;
        c.gridy = 0;
        c.gridwidth = 1;
        c.gridheight = 1;
        c.weightx = 100.0;
        c.weighty = 30.0;
        c.anchor = GridBagConstraints.CENTER;
        c.fill = GridBagConstraints.BOTH;
        viewsRightPanel.add(viewCommentPanel, c);
        viewCodePanel = new JPanel(new BorderLayout());
        viewCodeLabel = new JLabel();
        viewCodePanel.add(viewCodeLabel, BorderLayout.NORTH);
        viewCodeField = new RSyntaxTextArea();
        viewCodeField.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_SQL);
        viewCodeField.setCodeFoldingEnabled(true);
        viewCodeField.setAntiAliasingEnabled(true);
        viewCodeField.setEditable(false);
        viewCodeField.setHighlightCurrentLine(false);
        viewCodePanel.add(new RTextScrollPane(viewCodeField), BorderLayout.CENTER);
        c.gridx = 0;
        c.gridy = 1;
        c.gridwidth = 1;
        c.gridheight = 1;
        c.weightx = 100.0;
        c.weighty = 70.0;
        c.anchor = GridBagConstraints.CENTER;
        c.fill = GridBagConstraints.BOTH;
        viewsRightPanel.add(viewCodePanel, c);
        viewsPanel.add(viewsRightPanel, BorderLayout.CENTER);
    }

    private void guiStoredProcedurePanel() {
        GridBagConstraints c = new GridBagConstraints();
        storedProceduresPanel = new JPanel(new BorderLayout());
        //LEFT
        storedProceduresLeftPanel = new JPanel(new BorderLayout());
        storedProceduresListLabel = new JLabel();
        storedProceduresLeftPanel.add(storedProceduresListLabel, BorderLayout.NORTH);
        storedProcedures = new ArrayList<>();
        for (StoredProcedure sp : sqlDocGenerator.getStoredProcedures()) {
            storedProcedures.add(sp.getName());
        }
        storedProceduresList = new JList(storedProcedures.toArray());
        storedProceduresList.setFixedCellWidth(this.getWidth() * 30 / 100);
        storedProceduresList.addListSelectionListener(this);
        storedProceduresLeftPanel.add(new JScrollPane(storedProceduresList), BorderLayout.CENTER);
        storedProceduresPanel.add(storedProceduresLeftPanel, BorderLayout.WEST);
        //RIGHT
        storedProceduresRightPanel = new JPanel(new GridBagLayout());
        storedProcedureCommentPanel = new JPanel(new BorderLayout());
        storedProcedureCommentLabel = new JLabel();
        storedProcedureCommentPanel.add(storedProcedureCommentLabel, BorderLayout.NORTH);
        storedProcedureCommentField = new JTextArea();
        storedProcedureCommentField.setEditable(false);
        storedProcedureCommentPanel.add(new JScrollPane(storedProcedureCommentField), BorderLayout.CENTER);
        c.gridx = 0;
        c.gridy = 0;
        c.anchor = GridBagConstraints.CENTER;
        c.fill = GridBagConstraints.BOTH;
        c.gridwidth = 1;
        c.gridheight = 1;
        c.weightx = 100.0;
        c.weighty = 30.0;
        storedProceduresRightPanel.add(storedProcedureCommentPanel, c);
        storedProcedureCodePanel = new JPanel(new BorderLayout());
        storedProcedureCodeLabel = new JLabel();
        storedProcedureCodePanel.add(storedProcedureCodeLabel, BorderLayout.NORTH);
        storedProcedureCodeField = new RSyntaxTextArea();
        storedProcedureCodeField.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_SQL);
        storedProcedureCodeField.setCodeFoldingEnabled(true);
        storedProcedureCodeField.setAntiAliasingEnabled(true);
        storedProcedureCodeField.setEditable(false);
        storedProcedureCodeField.removeAllLineHighlights();
        storedProcedureCodeField.setHighlightCurrentLine(false);
        storedProcedureCodePanel.add(new RTextScrollPane(storedProcedureCodeField), BorderLayout.CENTER);
        c.gridx = 0;
        c.gridy = 1;
        c.anchor = GridBagConstraints.CENTER;
        c.fill = GridBagConstraints.BOTH;
        c.gridwidth = 1;
        c.gridheight = 1;
        c.weightx = 100.0;
        c.weighty = 70.0;
        storedProceduresRightPanel.add(storedProcedureCodePanel, c);
        storedProceduresPanel.add(storedProceduresRightPanel, BorderLayout.CENTER);
    }

    private void translate() throws Exception {
        ConfigurationManager configManager;
        String country, saveMenuItemText;
        configManager = new ConfigurationManager();
        country = configManager.get("LANG");
        if (country == null) {
            throw new Exception("LANG property not defined");
        }
        langManager = new LangManager(country);
        tabPanel.setTitleAt(0, langManager.get("SQLDOCGENERATORFRAME_TABLESPANELTITLE"));
        tabPanel.setTitleAt(1, langManager.get("SQLDOCGENERATORFRAME_VIEWSPANELTITLE"));
        tabPanel.setTitleAt(2, langManager.get("SQLDOCGENERATORFRAME_STOREDPROCEDURESPANELTITLE"));
        tabPanel.updateUI();
        tablesListLabel.setText(langManager.get("SQLDOCGENERATORFRAME_TABLELISTLABEL"));
        tableCommentLabel.setText(langManager.get("SQLDOCGENERATORFRAME_TABLECOMMENTLABEL"));
        tableColumnsLabel.setText(langManager.get("SQLDOCGENERATORFRAME_TABLECOLUMNSLABEL"));
        tableTriggersLabel.setText(langManager.get("SQLDOCGENERATORFRAME_TABLETRIGGERSLABEL"));
        viewsListLabel.setText(langManager.get("SQLDOCGENERATORFRAME_VIEWSLISTLABEL"));
        viewCommentLabel.setText(langManager.get("SQLDOCGENERATORFRAME_VIEWCOMMENTLABEL"));
        viewCodeLabel.setText(langManager.get("SQLDOCGENERATORFRAME_VIEWCODELABEL"));
        storedProceduresListLabel.setText(langManager.get("SQLDOCGENERATORFRAME_STOREDPROCEDURESLISTLABEL"));
        storedProcedureCommentLabel.setText(langManager.get("SQLDOCGENERATORFRAME_STOREDPROCEDURECOMMENTLABEL"));
        storedProcedureCodeLabel.setText(langManager.get("SQLDOCGENERATORFRAME_STOREDPROCEDURECODELABEL"));
        fileMenu.setText(langManager.get("SQLDOCGENERATORFRAME_FILEMENU"));
        aboutMenu.setText(langManager.get("SQLDOCGENERATORFRAME_ABOUTMENU"));
        generateMenuItem.setText(langManager.get("SQLDOCGENERATORFRAME_GENERATEMENUITEM"));
        aboutMenuItem.setText(langManager.get("SQLDOCGENERATORFRAME_ABOUTMENUITEM"));
        exitMenuItem.setText(langManager.get("SQLDOCGENERATORFRAME_EXITMENUITEM"));
        saveMenuItemText = langManager.get("SQLDOCGENERATORFRAME_SAVETOFILEMENUITEM");
        if ((saveMenuItemText == null) || (saveMenuItemText.equals(""))) {
            saveToFileMenuItem.setText(langManager.get("SQLDOCGENERATORFRAME_SAVEMENUITEM"));
        } else {
            saveToFileMenuItem.setText(saveMenuItemText);
        }
        saveToDBMenuItem.setText(langManager.get("SQLDOCGENERATORFRAME_SAVETODBMENUITEM"));
    }

    private void updateTablePanel(String tableName) {
        Table table;
        TableColumn column;
        TableTrigger trigger;
        String[][] data;
        String[] headers;
        int i = -1;
        //get the selected table
        table = null;
        for (Table t : sqlDocGenerator.getTables()) {
            if (t.getName().equals(tableName)) {
                table = t;
                break;
            }
        }
        if (table == null) {
            tableCommentField.setText("");
            tableColumnsTable.setModel(new DefaultTableModel());
            tableTriggersTable.setModel(new DefaultTableModel());
            return;
        }
        selectedObject = table;
        //table comment
        tableCommentField.setText(table.getComment());
        tableCommentField.setEditable(true);
        //table columns table
        headers = langManager.get("SQLDOCGENERATORFRAME_TABLECOLUMNSTABLEHEADERS").split("\\,");
        data = new String[table.getColumns().size()][];
        for (i = 0; i < data.length; i++) {
            data[i] = new String[3];
        }
        i = -1;
        for (String[] row : data) {
            column = table.getColumns().get(++i);
            row[0] = column.getName();
            row[1] = column.getType();
            row[2] = column.getComment();
        }
        tableColumnsTable.setModel(new DefaultTableModel(data, headers));
        for (i = 0; i < tableColumnsTable.getColumnCount(); i++) {
            tableColumnsTable.setDefaultEditor(tableColumnsTable.getColumnClass(i), null);
        }
        //table triggers
        headers = langManager.get("SQLDOCGENERATORFRAME_TABLETRIGGERSTABLEHEADERS").split("\\,");
        data = new String[table.getTriggers().size()][];
        for (i = 0; i < data.length; i++) {
            data[i] = new String[2];
        }
        i = -1;
        for (String[] row : data) {
            trigger = table.getTriggers().get(++i);
            row[0] = trigger.getName();
            row[1] = trigger.getComment();
        }
        tableTriggersTable.setModel(new DefaultTableModel(data, headers));
        for (i = 0; i < tableTriggersTable.getColumnCount(); i++) {
            tableTriggersTable.setDefaultEditor(tableTriggersTable.getColumnClass(i), null);
        }
    }

    private void updateViewPanel(String viewName) {
        View view;
        //get selected
        view = null;
        for (View v : sqlDocGenerator.getViews()) {
            if (v.getName().equals(viewName)) {
                view = v;
                break;
            }
        }
        if (view == null) {
            viewCommentField.setText("");
            viewCodeField.setText("");
            return;
        }
        //comment
        viewCommentField.setText(view.getComment());
        viewCommentField.setEditable(true);
        //code
        viewCodeField.setText((view.getCode()));
        selectedObject = view;
    }

    private void updateStoredProcedurePanel(String storedProcedureName) {
        StoredProcedure storedProcedure;
        //get selected
        storedProcedure = null;
        for (StoredProcedure sp : sqlDocGenerator.getStoredProcedures()) {
            if (sp.getName().equals(storedProcedureName)) {
                storedProcedure = sp;
                break;
            }
        }
        if (storedProcedure == null) {
            storedProcedureCommentField.setText("");
            storedProcedureCodeField.setText("");
            return;
        }
        //comment
        storedProcedureCommentField.setText(storedProcedure.getComment());
        storedProcedureCommentField.setEditable(true);
        //code
        storedProcedureCodeField.setText((storedProcedure.getCode()) == null ? langManager.get("GENERATEOUTPUTFILE_STOREDPROCEDUREENCRYPTED") : storedProcedure.getCode());
        selectedObject = storedProcedure;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String dialogMessage;
        int userChoice;
        if ((e.getSource().equals(saveToFileMenuItem)) || (e.getSource().equals(saveToDBMenuItem))) {
            JFileChooser fileChooser = new JFileChooser();
            boolean saveToFile = e.getSource().equals(saveToFileMenuItem);
            dialogMessage = langManager.get("SQLDOCGENERATORFRAME_CONFIRMSAVE");
            userChoice = JOptionPane.showConfirmDialog(this, dialogMessage, "", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
            if (userChoice == JOptionPane.NO_OPTION) {
                return;
            }
            if (saveToFile) {
                fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
                fileChooser.setFileFilter(Util.SQLDOCGENERATOR_FILENAMEEXTENSIONFILTER);
                userChoice = fileChooser.showSaveDialog(this);
                if (userChoice == JFileChooser.APPROVE_OPTION) {
                    File saveFile = fileChooser.getSelectedFile();
                    if (saveFile.exists()) {
                        dialogMessage = langManager.get("SQLDOCGENERATORFRAME_CONFIRMOVERWRITE");
                        userChoice = JOptionPane.showConfirmDialog(this, dialogMessage, "", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                        if (userChoice == JOptionPane.NO_OPTION) {
                            return;
                        }
                    }
                    try {
                        Util.saveSqlDocGeneratorToFile(sqlDocGenerator, saveFile.getAbsolutePath());
                    } catch (Exception ex) {
                        LogManager.error(LogManager.describeException(ex));
                        JOptionPane.showMessageDialog(this, ex.toString(), "", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    dialogMessage = langManager.get("SQLDOCGENERATORFRAME_SUCCESSSAVE");
                    JOptionPane.showMessageDialog(this, dialogMessage, "", JOptionPane.INFORMATION_MESSAGE);
                }
            } else {
                try {
                    Util.saveSqlDocGeneratorToDB(sqlDocGenerator);
                } catch (Exception ex) {
                    LogManager.error(LogManager.describeException(ex));
                    JOptionPane.showMessageDialog(this, ex.toString(), "", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                dialogMessage = langManager.get("SQLDOCGENERATORFRAME_SUCCESSSAVE");
                JOptionPane.showMessageDialog(this, dialogMessage, "", JOptionPane.INFORMATION_MESSAGE);
            }
        }
        if (e.getSource().equals(exitMenuItem)) {
            formClosingPresenter.preventFormClosing(this);
        }
        if (e.getSource().equals(aboutMenuItem)) {
            new AboutFrame(this);
        }
        if (e.getSource().equals(generateMenuItem)) {
            JFileChooser fileChooser;
            fileChooser = new JFileChooser();
            dialogMessage = langManager.get("SQLDOCGENERATORFRAME_CONFIRMGENERATION");
            userChoice = JOptionPane.showConfirmDialog(this, dialogMessage, "", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
            if (userChoice == JOptionPane.NO_OPTION) {
                return;
            }
            fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
            fileChooser.setFileFilter(Util.HTML_FILENAMEEXTENSIONFILTER);
            userChoice = fileChooser.showSaveDialog(this);
            if (userChoice == JFileChooser.APPROVE_OPTION) {
                File outputFile = fileChooser.getSelectedFile();
                if (outputFile.exists()) {
                    dialogMessage = langManager.get("SQLDOCGENERATORFRAME_CONFIRMOVERWRITE");
                    userChoice = JOptionPane.showConfirmDialog(this, dialogMessage, "", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                    if (userChoice == JOptionPane.NO_OPTION) {
                        return;
                    }
                }
                try {
                    Util.generateHtmlOutputFile(sqlDocGenerator, outputFile.getAbsolutePath());
                } catch (Exception ex) {
                    ex.printStackTrace();
                    LogManager.error(LogManager.describeException(ex));
                    JOptionPane.showMessageDialog(this, ex.toString(), "", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                dialogMessage = langManager.get("SQLDOCGENERATORFRAME_SUCCESSGENERATION");
                JOptionPane.showMessageDialog(this, dialogMessage, "", JOptionPane.INFORMATION_MESSAGE);
            }
        }
    }

    @Override
    public void valueChanged(ListSelectionEvent e) {
        if (selectedObject != null) {
            if (selectedObject instanceof Table) {
                selectedObject.setComment(tableCommentField.getText());
            }
            if (selectedObject instanceof View) {
                selectedObject.setComment(viewCommentField.getText());
            }
            if (selectedObject instanceof StoredProcedure) {
                selectedObject.setComment(storedProcedureCommentField.getText());
            }
        }
        selectedObject = null;
        if (e.getSource().equals(tablesList)) {
            String tableName;
            tableName = (String) tablesList.getSelectedValue();
            updateTablePanel(tableName);
        }
        if (e.getSource().equals(viewsList)) {
            String viewName;
            viewName = (String) viewsList.getSelectedValue();
            updateViewPanel(viewName);
        }
        if (e.getSource().equals(storedProceduresList)) {
            String storedProcedure;
            storedProcedure = (String) storedProceduresList.getSelectedValue();
            updateStoredProcedurePanel(storedProcedure);
        }
    }

    @Override
    public void stateChanged(ChangeEvent e) {
        if (selectedObject != null) {
            if (selectedObject instanceof Table) {
                selectedObject.setComment(tableCommentField.getText());
            }
            if (selectedObject instanceof View) {
                selectedObject.setComment(viewCommentField.getText());
            }
            if (selectedObject instanceof StoredProcedure) {
                selectedObject.setComment(storedProcedureCommentField.getText());
            }
        }
        selectedObject = null;
        if (tablesList != null) {
            tablesList.clearSelection();
        }
        if (viewsList != null) {
            viewsList.clearSelection();
        }
        if (storedProceduresList != null) {
            storedProceduresList.clearSelection();
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        //unused
    }

    @Override
    public void mousePressed(MouseEvent e) {
        if (e.getClickCount() != 2) {
            return;
        }
        ((Table) selectedObject).setComment(tableCommentField.getText());
        if (e.getSource().equals(tableColumnsTable)) {
            final TableColumn selectedTableColumn;
            selectedTableColumn = ((Table) selectedObject).getColumns().get(tableColumnsTable.getSelectedRow());
            if (selectedTableColumn == null) {
                return;
            }
            try {
                TableColumnCommentFrame popup = new TableColumnCommentFrame(this, (Table) selectedObject, selectedTableColumn);
                updateTablePanel(((Table) selectedObject).getName());
            } catch (Exception ex) {
                LogManager.error(LogManager.describeException(ex));
                JOptionPane.showMessageDialog(null, ex.toString(), "", JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
            }
        }
        if (e.getSource().equals(tableTriggersTable)) {
            final TableTrigger selectedTableTrigger;
            selectedTableTrigger = ((Table) selectedObject).getTriggers().get(tableTriggersTable.getSelectedRow());
            if (selectedTableTrigger == null) {
                return;
            }
            try {
                TableTriggerCommentFrame popup = new TableTriggerCommentFrame(this, (Table) selectedObject, selectedTableTrigger);
                updateTablePanel(((Table) selectedObject).getName());
            } catch (Exception ex) {
                LogManager.error(LogManager.describeException(ex));
                JOptionPane.showMessageDialog(null, ex.toString(), "", JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
            }
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        //unused
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        //unused
    }

    @Override
    public void mouseExited(MouseEvent e) {
        //unused
    }

}
