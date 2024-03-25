package com.jbr.javaemailapp;

import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import java.util.Properties;



public class EmailReceiver {

//    To receive emails from a Gmail account, you'll need to configure your application to use IMAP with Gmail's servers.

    /*
    1. Configure IMAP properties for gmail (server address, port, SSL)
    2. Create session to connect to IMAP server
    3. Access and process emails received.
     */


//   Info from: https://www.tabnine.com/code/java/classes/javax.mail.Store

    public static void receiveEmails(String username, String password) throws MessagingException {

        Properties properties = new Properties();
        properties.put("mail.store.protocol", "imaps");
        properties.put("mail.imaps.host", "imap.gmail.com");
        properties.put("mail.imaps.port", "993");
        properties.put("mail.imaps.ssl.enable", "true");


//        now create session

        Session session = Session.getDefaultInstance(properties);

//        A Store mirrors an inbox structure with similar retrieval

        javax.mail.Store store = session.getStore("imaps");
        store.connect("imap.gmail.com", username,password);


//        create a folder class
        Folder inbox = store.getFolder("INBOX");
        inbox.open(Folder.READ_ONLY);

        Message[] messages = inbox.getMessages();
        System.out.println("Number of emails currently in Inbox: " + messages.length);

        for(Message message: messages) {
            System.out.println("Email subject: " + message.getSubject());
        }


        /*
        * This loops through everything in the inbox which takes ages
        * Could be improved with a pause function
        * */



        inbox.close();
        store.close();
    }

    public static void main(String[] args) throws MessagingException {


        String username = "s*****b*******.com";
        String password = "<password goes here>";

        receiveEmails(username,password);
    }

}
