package com.jbr.javaemailapp;

import javax.swing.*;
import java.io.File;

public class AttachmentPicker {

//    JFileChooser provides a simple mechanism for the user to choose a file.
//JFileChooser to create a dialog that allows users to navigate their filesystem and select one or more files to attach to an email.

    public static File[] pickAttachments() {
        JFileChooser chooser = new JFileChooser();
        chooser.setMultiSelectionEnabled(true);
        int option = chooser.showOpenDialog(null);

        if(option == JFileChooser.APPROVE_OPTION){
            return chooser.getSelectedFiles();
        }
        return new File[] {};
    }
}
