package client;

import javax.swing.*;
import java.awt.*;


public class EmailClientGUI extends JFrame {


    public EmailClientGUI(){
        setTitle("Java Email App");
        setSize(600,400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);


//    a UI initialisation method where all UI components will be initialised and added to the JFrame.
        initUserInterface();
        setVisible(true);
    }


    public void initUserInterface(){

//        INBOX PANEL

//      JList with a DefaultListModel to list email subjects, making it scrollable by adding it to a JScrollPane.
//        http://www.java2s.com/Code/Java/Swing-JFC/AnexampleofJListwithaDefaultListModel.htm#google_vignette

        DefaultListModel<String> emailListModel = new DefaultListModel<>();
        JList<String> emailList = new JList<>(emailListModel);
        add(new JScrollPane(emailList), BorderLayout.WEST);


//        READING PANEL
//        Added a JTextArea for displaying the content of the selected email, also within a JScrollPane to enable scrolling

        JTextArea readingPanel = new JTextArea();
        readingPanel.setEditable(false);
        add(new JScrollPane(readingPanel), BorderLayout.CENTER);


//        COMPOSE BUTTON

        JButton composeEmailButton = new JButton("Compose new email");
        add(composeEmailButton, BorderLayout.SOUTH);

    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(()-> new EmailClientGUI());
    }


}
