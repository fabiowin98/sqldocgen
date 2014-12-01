package org.dgl.sqldocgen.gui;

import java.awt.Dimension;
import java.awt.GridLayout;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import org.dgl.sqldocgen.Lang;

public class AboutFrame extends JDialog {

    private final Dimension size = new Dimension(350, 100);

    public AboutFrame(JFrame owner) {
        super(owner, "", true);
        gui();
        setVisible(true);
    }

    private void gui() {
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setLayout(new GridLayout(3, 1));
        this.setSize(size);
        this.add(new JLabel(Lang.NAME, JLabel.CENTER));
        this.add(new JLabel(Lang.VERSION, JLabel.CENTER));
        this.add(new JLabel(Lang.AUTHOR, JLabel.CENTER));
        this.setLocationRelativeTo(null);
        this.setResizable(false);
    }
}
