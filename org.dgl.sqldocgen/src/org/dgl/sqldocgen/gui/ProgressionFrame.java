package org.dgl.sqldocgen.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import org.dgl.manager.ConfigurationManager;
import org.dgl.manager.LangManager;

public class ProgressionFrame extends JFrame implements ActionListener {

    private JLabel textLabel;
    private JProgressBar valueBar;
    private JButton cancelButton;
    private JPanel buttonPanel;
    private LangManager langManager;
    private final Dimension size = new Dimension(300, 115);

    public ProgressionFrame() throws Exception {
        super("");
        gui();
        translate();
    }

    private void gui() {
        this.setSize(size);
        this.setLayout(new BorderLayout());
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        textLabel = new JLabel();
        add(textLabel, BorderLayout.NORTH);
        valueBar = new JProgressBar(0, 100);
        valueBar.setStringPainted(true);
        add(valueBar, BorderLayout.CENTER);
        buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        cancelButton = new JButton();
        cancelButton.addActionListener(this);
        buttonPanel.add(cancelButton);
        add(buttonPanel, BorderLayout.SOUTH);
        this.setVisible(true);
        this.setLocationRelativeTo(null);
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
        setTitle(langManager.get("PROGRESSIONFRAME_TITLE"));
        textLabel.setText(getTitle());
        cancelButton.setText(langManager.get("PROGRESSIONFRAME_CANCELBUTTON"));
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        System.exit(0);
    }

    public String getText() {
        return textLabel.getText();
    }

    public void setText(String text) {
        textLabel.setText(langManager.get(text));
    }

    public int getValue() {
        return valueBar.getValue();
    }

    public void setValue(int progression) {
        valueBar.setValue(progression);
    }

}
