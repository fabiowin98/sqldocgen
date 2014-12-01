package org.dgl.sqldocgen;

import java.awt.Window;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JOptionPane;
import org.dgl.manager.LangManager;

public class FormClosingPreventer extends WindowAdapter {

    LangManager langManager;

    public FormClosingPreventer(LangManager langManager) {
        this.langManager = langManager;
    }

    public void preventFormClosing(Window window) {
        String dialogMessage;
        int userChoice;
        dialogMessage = langManager.get("SQLDOCGENERATORFRAME_CONFIRMEXIT");
        userChoice = JOptionPane.showConfirmDialog(window, dialogMessage, "", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
        if (userChoice == JOptionPane.NO_OPTION) {
            return;
        }
        window.dispose();
    }

    @Override
    public void windowClosing(WindowEvent e) {
        preventFormClosing(e.getWindow());
    }

}
