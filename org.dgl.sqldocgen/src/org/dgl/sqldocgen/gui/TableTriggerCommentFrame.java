package org.dgl.sqldocgen.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import static javax.swing.WindowConstants.DISPOSE_ON_CLOSE;
import org.dgl.manager.ConfigurationManager;
import org.dgl.manager.LangManager;
import org.dgl.sqldocgen.Util;
import org.dgl.sqldocgen.db.Table;
import org.dgl.sqldocgen.db.TableTrigger;
import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.fife.ui.rsyntaxtextarea.SyntaxConstants;
import org.fife.ui.rtextarea.RTextScrollPane;

public class TableTriggerCommentFrame extends JDialog implements ActionListener {

    private JPanel infoPanel, commentPanel, centerPanel, northPanel, southPanel, buttonPanel;
    private JLabel tableLabel, triggerLabel, commentLabel, codeLabel;
    private JTextField tableField, triggerField;
    private RSyntaxTextArea codeField;
    private JTextArea commentField;
    private JButton okButton, cancelButton;
    private LangManager langManager;
    private final Dimension size = new Dimension(750, 550);
    private Table tableReference;
    private TableTrigger tableTriggerReference;

    public TableTriggerCommentFrame(JFrame owner, Table tableReference, TableTrigger tableTriggerReference) throws Exception {
        super(owner, "", true);
        this.tableReference = tableReference;
        this.tableTriggerReference = tableTriggerReference;
        gui();
        translate();
        Util.setFocusOn(commentField);
        setVisible(true);
    }

    private void gui() {
        setLayout(new BorderLayout());
        setSize(size);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        centerPanel = new JPanel(new GridBagLayout());
        northPanel = new JPanel(new BorderLayout());
        infoPanel = new JPanel(new GridLayout(2, 2));
        tableLabel = new JLabel();
        infoPanel.add(tableLabel);
        tableField = new JTextField();
        tableField.setEditable(false);
        infoPanel.add(tableField);
        triggerLabel = new JLabel();
        infoPanel.add(triggerLabel);
        triggerField = new JTextField();
        triggerField.setEditable(false);
        infoPanel.add(triggerField);
        northPanel.add(infoPanel, BorderLayout.NORTH);
        commentPanel = new JPanel(new BorderLayout());
        commentLabel = new JLabel();
        commentPanel.add(commentLabel, BorderLayout.NORTH);
        commentField = new JTextArea();
        commentPanel.add(new JScrollPane(commentField), BorderLayout.CENTER);
        northPanel.add(commentPanel, BorderLayout.CENTER);
        centerPanel.add(northPanel, new GridBagConstraints(0, 0, 1, 1, 100, 20, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
        southPanel = new JPanel(new BorderLayout());
        codeLabel = new JLabel();
        southPanel.add(codeLabel, BorderLayout.NORTH);
        codeField = new RSyntaxTextArea();
        codeField.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_SQL);
        codeField.setCodeFoldingEnabled(true);
        codeField.setAntiAliasingEnabled(true);
        codeField.setEditable(false);
        codeField.setHighlightCurrentLine(false);
        southPanel.add(new RTextScrollPane(codeField), BorderLayout.CENTER);
        centerPanel.add(southPanel, new GridBagConstraints(0, 1, 1, 1, 100, 80, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
        add(centerPanel, BorderLayout.CENTER);
        buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        okButton = new JButton();
        okButton.addActionListener(this);
        buttonPanel.add(okButton);
        cancelButton = new JButton();
        cancelButton.addActionListener(this);
        buttonPanel.add(cancelButton);
        add(buttonPanel, BorderLayout.SOUTH);
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
        this.setTitle(langManager.get("TABLETRIGGERCOMMENTFRAME_TITLE"));
        tableLabel.setText(langManager.get("TABLETRIGGERCOMMENTFRAME_TABLELABEL"));
        tableField.setText(tableReference.getName());
        triggerLabel.setText(langManager.get("TABLETRIGGERCOMMENTFRAME_COLUMNLABEL"));
        triggerField.setText(tableTriggerReference.getName());
        commentLabel.setText(langManager.get("TABLETRIGGERCOMMENTFRAME_COMMENTLABEL"));
        commentField.setText(tableTriggerReference.getComment());
        okButton.setText(langManager.get("TABLETRIGGERCOMMENTFRAME_OKBUTTON"));
        cancelButton.setText(langManager.get("TABLETRIGGERCOMMENTFRAME_CANCELBUTTON"));
        codeLabel.setText(langManager.get("TABLETRIGGERCOMMENTFRAME_CODELABEL"));
        codeField.setText(tableTriggerReference.getCode());
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource().equals(okButton)) {
            tableTriggerReference.setComment(commentField.getText());
            dispose();
        }
        if (e.getSource().equals(cancelButton)) {
            dispose();
        }
    }

}
