package com.jbr.javaemailapp;

import javax.mail.*;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

public class EmailSender {

//    Number of tasks to do
//    configure the STMP properties of the gmail account, for sending
//    Create a session with authentication for the access
//    Compose and then send emails

    public static void sendEmail(String recipient, String subject, String messageBody) throws MessagingException {

        final String username = "s**********@gmail.com";
        final String password = "<password goes here>";


//        email account properties for sending email
        Properties properties = new Properties();

        properties.put("mail.smtp.host", "smtp.gmail.com");
        properties.put("mail.smtp.port", "587");
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");


//        create a new session
        Session session = Session.getInstance(properties, new javax.mail.Authenticator()
                {
                    @Override
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(username,password);
                    }
                }
        );

//       MIME uses headers and separators that tell a user agent (UA) how to re-create the message

        MimeMessage message = new MimeMessage(session);

        message.setFrom(new InternetAddress(username));
        message.addRecipient(Message.RecipientType.TO, new InternetAddress(recipient));
        message.setSubject(subject);
        message.setText(messageBody);

        Transport.send(message);

        System.out.println("Message sent successfully to: " + recipient);

    }


    public static void main(String[] args) throws MessagingException {

        String to = "strawhorse@gmail.com";
        String subject = "Test 1";
        String body = "This is a first test";

        sendEmail(to, subject, body);

    }



}
