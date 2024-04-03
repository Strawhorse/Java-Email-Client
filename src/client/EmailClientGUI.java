package client;

import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMultipart;
import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.util.Arrays;


public class EmailClientGUI extends JFrame {

    private JTextArea emailMessageContent = new JTextArea();
    private Message[] messages;

    private JTextField usernameField = new JTextField();
    private JTextField passwordField = new JTextField();

    private DefaultListModel<String> emailListModel;  // model for the email list
    private JList<String> emailList;  // for displaying emails


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

//        Switched over to a JSplitPane so that users have the option to resize the email list and email reader panes within our client.
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        splitPane.setResizeWeight(0.5);
        splitPane.setOneTouchExpandable(true);

        JTextArea emailContent = new JTextArea();
        emailContent.setEditable(false);

        JButton composeButton = new JButton("Compose");

        emailList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        emailList.addListSelectionListener(this::emailListSelectionChanged);
        JScrollPane listScrollPane = new JScrollPane(emailList);

        emailContent.setEditable(false);
        JScrollPane contentScrollPane = new JScrollPane(emailContent);

        splitPane.setLeftComponent(listScrollPane);
        splitPane.setRightComponent(contentScrollPane);

        getContentPane().add(splitPane, BorderLayout.CENTER);

        bottomPanel.add(composeButton);
        bottomPanel.add(refreshInboxButton);
        add(bottomPanel, BorderLayout.SOUTH);
        // Prompt the user to log in when the application starts
        SwingUtilities.invokeLater(this::showLoginDialogBox);

//        compose new email button
        JButton composeEmailButton = new JButton("Compose");
        composeEmailButton.addActionListener(e -> showComposeDialog());
    }


    private void showComposeDialog(){
        JDialog composeDialog = new JDialog(this, "Compose Email", true);
        composeDialog.setLayout(new BorderLayout(5,5));
    }


//    method for refreshing the inbox
    private void refreshInbox() throws MessagingException {
        messages = SessionManager.getInstance().receiveEmail();
        emailListModel.clear();
        for (Message message: messages){
            emailListModel.addElement(message.getSubject() + " - From: " + InternetAddress.toString(message.getFrom()));
        }
    }



//    event listener for the email list selection changes.
//    When a user selects an email from the list, the application fetches and displays the email's subject, sender information, and body content in a dedicated reading area

    private void emailListSelectionChanged(ListSelectionEvent e) {

        if(!e.getValueIsAdjusting() && emailList.getSelectedIndex() !=-1){
            Message selectedMessage = messages[emailList.getSelectedIndex()];
            emailMessageContent.setText("");    // clear previous content
            emailMessageContent.append("Subject: " + selectedMessage.getSubject() + "\n\n");
            emailMessageContent.append("From: " + Arrays.toString(selectedMessage.getFrom()) + "\n\n");
            emailMessageContent.append(getTextFromMessage(selectedMessage));
        }
    }



    private String getTextFromMessage(Message message) throws MessagingException, IOException {
        if (message.isMimeType("text/plain")) {
            return (String) message.getContent();
        }
        else if (message.isMimeType("multipart/*")) {
            MimeMultipart mimeMultipart = (MimeMultipart) message.getContent();

            for(int i = 0; i < mimeMultipart.getCount(); i++) {
                BodyPart bodypart = mimeMultipart.getBodyPart(i);
                if(bodypart.isMimeType("text/plain")) {
                    return (String) bodypart.getContent();
                }
            }
        }
        return "No readable email message content found";
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

        else System.out.println("Sorry, login failed...");


    }




    public static void main(String[] args) {
        SwingUtilities.invokeLater(EmailClientGUI::new);
    }


}
