package client;

import javax.mail.MessagingException;
import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;


public class EmailClientGUI extends JFrame {

    private JTextField usernameField = new JTextField(20);
    private JTextField passwordField = new JTextField(20);


    public EmailClientGUI(){
        setTitle("Java Email App");
        setSize(800,600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

//    a UI initialisation method where all UI components will be initialised and added to the JFrame.
        initUserInterface();
        setVisible(true);


//        Add a listener to check when we are closing the program down
//        Check if the instance is null, and if not shut the instance down

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                if(SessionManager.getInstance() != null){
                    try {
                        SessionManager.getInstance().closeInbox();
                    } catch (MessagingException ex) {
                        ex.printStackTrace();
                    }
                }
            }
        });

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


//        Login dialogue screen; Ensured the login dialog is invoked immediately after the UI initialises
        SwingUtilities.invokeLater(this::showLoginDialogBox);

    }


//    Login dialogue section
    private void showLoginDialogBox(){

//        1 column for login
        JPanel panel = new JPanel(new GridLayout(0, 1));
        panel.add(new JLabel("Email address"));
        panel.add(usernameField);
        panel.add(new JLabel("Password"));
        panel.add(passwordField);


        int resultOfLogin = JOptionPane.showConfirmDialog(null, panel, "Login please..", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

//        loop to check choice which was made
        if(resultOfLogin == JOptionPane.OK_OPTION){
            String username = usernameField.getText();
            String password = passwordField.getText();

            try{

//                Initialise the session manager here
                SessionManager.getInstance(username, password);

//                method to refresh the inbox
                refreshInbox();

            } catch (MessagingException e) {
                JOptionPane.showMessageDialog(this, "Failed to access email account: " + e.getMessage(), "Login error", JOptionPane.ERROR_MESSAGE);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        else {
            System.out.println("Sorry, login failed...");
        }


    }


    public static void main(String[] args) {
        SwingUtilities.invokeLater(EmailClientGUI::new);
    }


}
