package client;

import com.jbr.javaemailapp.AttachmentPicker;
import com.jbr.javaemailapp.EmailSender;

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
import java.io.File;
import java.io.IOException;
import java.sql.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class EmailClientGUI extends JFrame {

    private final JTextArea emailMessageContent = new JTextArea();
    private Message[] messages;

    private final JTextField usernameField = new JTextField();
    private final JTextField passwordField = new JTextField();

    DefaultListModel<String> emailListModel = new DefaultListModel<>();
    JList<String> emailList = new JList<>(emailListModel);


    public EmailClientGUI(){
        setTitle("Java Email App");
        setSize(800,600);


//    a UI initialisation method where all UI components will be initialised and added to the JFrame.
        initUserInterface();
        setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

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

        // Prompt the user to log in when the application starts
        SwingUtilities.invokeLater(this::showLoginDialogBox);

//        compose new email button
        JButton composeEmailButton = new JButton("Compose");
        composeEmailButton.addActionListener(e -> showComposeDialog());
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
            try {
                emailMessageContent.append("Subject: " + selectedMessage.getSubject() + "\n\n");
            } catch (MessagingException ex) {
                throw new RuntimeException(ex);
            }
            try {
                emailMessageContent.append("From: " + Arrays.toString(selectedMessage.getFrom()) + "\n\n");
            } catch (MessagingException ex) {
                throw new RuntimeException(ex);
            }
            try {
                emailMessageContent.append(getTextFromMessage(selectedMessage));
            } catch (MessagingException | IOException ex) {
                throw new RuntimeException(ex);
            }
        }
    }


//getTextFromMessage extracts and present the email body as plain text. This handles both simple text emails and more complex multipart messages.
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

        else {
            System.out.println("Sorry, login failed...");
        }}



        private void showComposeDialog(){

            JDialog composeDialog = new JDialog(this, "Compose Email", true);
            composeDialog.setLayout(new BorderLayout(5,5));

            Box fieldsPanel = Box.createVerticalBox();
            JTextField recipientField = new JTextField();
            JTextField subjectField = new JTextField();
            JTextArea bodyText = new JTextArea(10, 20);

            bodyText.setLineWrap(true);
            bodyText.setWrapStyleWord(true);

            fieldsPanel.add(new JLabel("To"));
            fieldsPanel.add(recipientField);
            fieldsPanel.add(new JLabel("Subject"));
            fieldsPanel.add(subjectField);

            JPanel bottomPanel = new JPanel();
            JButton attachFilesButton = new JButton("Attach Files");
            JButton sendButton = new JButton("Send");
            JLabel attachFilesLabel = new JLabel("No files attached..");


//            event listener to check for attached files
//        Good info here re: storing files in arraylists -> https://www.tutorialspoint.com/how-to-read-a-file-into-an-arraylist-in-java
            List<File> attachedFiles = new ArrayList<File>();
            attachFilesButton.addActionListener(e -> {
                File[] files = AttachmentPicker.pickAttachments();
                attachedFiles.addAll(Arrays.asList(files));
                attachFilesLabel.setText(attachedFiles.size() + " files attached.");
            });


            sendButton.addActionListener(e->{
                String recipient = recipientField.getText();
                String subject = subjectField.getText();
                String body = bodyText.getText();

                File[] attachments = attachedFiles.toArray(new File[0]);
                try {
                    EmailSender.sendEmailsWithAttachment(recipient,subject,body,attachments);
                    composeDialog.dispose();
                } catch (MessagingException | IOException ex) {
                    throw new RuntimeException(ex);
                }
            });

//            adhere buttons to the bottom panel
            bottomPanel.add(attachFilesButton);
            bottomPanel.add(sendButton);

            composeDialog.add(fieldsPanel, BorderLayout.NORTH);
            composeDialog.add(new JScrollPane(bodyText), BorderLayout.CENTER);
            composeDialog.add(bottomPanel, BorderLayout.SOUTH);


            composeDialog.pack();
            composeDialog.setLocationRelativeTo(this);
            composeDialog.setVisible(true);
        }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(EmailClientGUI::new);
    }

    }







