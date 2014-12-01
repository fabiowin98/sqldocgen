package org.dgl.sqldocgen.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
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
import org.dgl.sqldocgen.db.TableColumn;

public class TableColumnCommentFrame extends JDialog implements ActionListener {

    private JPanel infoPanel, commentPanel, buttonPanel;
    private JLabel tableLabel, columnLabel, commentLabel;
    private JTextField tableField, columnField;
    private JTextArea commentField;
    private JButton okButton, cancelButton;
    private LangManager langManager;
    private final Dimension size = new Dimension(300, 200);
    private Table tableReference;
    private TableColumn tableColumnReference;

    public TableColumnCommentFrame(JFrame owner, Table tableReference, TableColumn tableColumnReference) throws Exception {
        super(owner, "", true);
        this.tableColumnReference = tableColumnReference;
        this.tableReference = tableReference;
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
        infoPanel = new JPanel(new GridLayout(2, 2));
        tableLabel = new JLabel();
        infoPanel.add(tableLabel);
        tableField = new JTextField();
        tableField.setEditable(false);
        infoPanel.add(tableField);
        columnLabel = new JLabel();
        infoPanel.add(columnLabel);
        columnField = new JTextField();
        columnField.setEditable(false);
        infoPanel.add(columnField);
        add(infoPanel, BorderLayout.NORTH);
        commentPanel = new JPanel(new BorderLayout());
        commentLabel = new JLabel();
        commentPanel.add(commentLabel, BorderLayout.NORTH);
        commentField = new JTextArea();
        commentPanel.add(new JScrollPane(commentField), BorderLayout.CENTER);
        add(commentPanel, BorderLayout.CENTER);
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
        this.setTitle(langManager.get("TABLECOLUMNCOMMENTFRAME_TITLE"));
        tableLabel.setText(langManager.get("TABLECOLUMNCOMMENTFRAME_TABLELABEL"));
        tableField.setText(tableReference.getName());
        columnLabel.setText(langManager.get("TABLECOLUMNCOMMENTFRAME_COLUMNLABEL"));
        columnField.setText(tableColumnReference.getName());
        commentLabel.setText(langManager.get("TABLECOLUMNCOMMENTFRAME_COMMENTLABEL"));
        commentField.setText(tableColumnReference.getComment());
        okButton.setText(langManager.get("TABLECOLUMNCOMMENTFRAME_OKBUTTON"));
        cancelButton.setText(langManager.get("TABLECOLUMNCOMMENTFRAME_CANCELBUTTON"));
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource().equals(okButton)) {
            tableColumnReference.setComment(commentField.getText());
            dispose();
        }
        if (e.getSource().equals(cancelButton)) {
            dispose();
        }
    }

}
