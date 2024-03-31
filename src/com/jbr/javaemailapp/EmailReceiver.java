package com.jbr.javaemailapp;

import javax.mail.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;



public class EmailReceiver {

//    To receive emails from a Gmail account, you'll need to configure your application to use IMAP with Gmail's servers.

    /*
    1. Configure IMAP properties for gmail (server address, port, SSL)
    2. Create session to connect to IMAP server
    3. Access and process emails received.
     */

    private static String username = "";
    private static String password = "";


//    Added a setCredentials method to dynamically set these credentials so that they can be called with the user's input from the GUI after login.
    public static void setLoginCredentials(String user, String pw){
        username = user;
        password = pw;
    }



//   Info from: https://www.tabnine.com/code/java/classes/javax.mail.Store

    public static Message[] receiveEmail() throws MessagingException {

        Properties properties = new Properties();
        properties.put("mail.store.protocol", "imaps");
        properties.put("mail.imaps.host", "imap.gmail.com");
        properties.put("mail.imaps.port", "993");
        properties.put("mail.imaps.ssl.enable", "true");

        List<Message> messageListFromInbox = new ArrayList<>();

        Session emailSession = Session.getInstance(properties);

//        A Store mirrors an inbox structure with similar retrieval

        Store store = emailSession.getStore("imaps");

//        Adjusted the receiveEmail method to use the stored credentials instead of passing them as parameters.
        store.connect("imap.gmail.com", username,password);


//        create a folder class and open as Read Only
        Folder inbox = store.getFolder("INBOX");
        inbox.open(Folder.READ_ONLY);

        Message[] messages = inbox.getMessages();

        for(Message message: messages) {

            messageListFromInbox.add(message);
        }


        inbox.close();
        store.close();

        return messageListFromInbox.toArray(new Message[0]);
    }

    public static void main(String[] args) throws MessagingException {

        receiveEmail();
    }

}
