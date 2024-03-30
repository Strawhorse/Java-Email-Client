package com.jbr.javaemailapp;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.io.File;
import java.io.IOException;


import java.util.Properties;

public class EmailSender {

//    Number of tasks to do
//    configure the STMP properties of the gmail account, for sending
//    Create a session with authentication for the access
//    Compose and then send emails


    public static void sendEmailsWithAttachment(String recipient, String subject, String body, File[] attachments) throws MessagingException, IOException {


        final String username = "s****b******@gmail.com";
        final String password = "";


//        email account properties for sending email
        Properties properties = new Properties();

        properties.put("mail.smtp.host", "smtp.gmail.com");
        properties.put("mail.smtp.port", "587");
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");


//        create a new session
//        Info found at: https://www.tabnine.com/code/java/methods/javax.mail.Session/getInstance

        Session session = Session.getInstance(properties, new Authenticator()
                {
                    @Override
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(username,password);
                    }
                }
        );

//       MIME uses headers and separators that tell a user agent (UA) how to re-create the message

        MimeMessage message = new MimeMessage(session);


//        Would just message.setFrom(username); work?
        message.setFrom(new InternetAddress(username));
        message.addRecipient(Message.RecipientType.TO, new InternetAddress(recipient));
        message.setSubject(subject);


//        javax.mail
//        Multipart is a container that holds multiple body parts. Multipart provides methods to retrieve and set its subparts.
        Multipart mailMultipart = new MimeMultipart();

        MimeBodyPart textBody = new MimeBodyPart();
        textBody.setText(body);
        mailMultipart.addBodyPart(textBody);

        boolean hasAttachments = false;
        for(File file: attachments){
            MimeBodyPart attachmentPart = new MimeBodyPart();
            attachmentPart.attachFile(file);
            mailMultipart.addBodyPart(attachmentPart);
            hasAttachments = true;
        }

        message.setContent(mailMultipart);
        Transport.send(message);

        if(hasAttachments) {
            System.out.println("Message sent successfully with attachments to: " + recipient);
        }
        else {
            System.out.println("Message sent successfully to: " + recipient);
        }


        System.out.println("Message sent successfully to: " + recipient);


    }


    public static void main(String[] args) {

            System.out.println("Hello, test");
    }



}
