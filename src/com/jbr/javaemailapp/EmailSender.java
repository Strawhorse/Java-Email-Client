package com.jbr.javaemailapp;

import java.util.Properties;

public class EmailSender {

//    Number of tasks to do
//    configure the STMP properties of the gmail account, for sending
//    Create a session with authentication for the access
//    Compose and then send emails

    public static void sendEmail(String recipient, String subject, String messageBody) {

        final String account = "seanobrachain@gmail.com";
        final String password = "than uklw rpbt ioha";


//        email account properties for sending email
        Properties properties = new Properties();

        properties.put("mail.smtp.host", "smtp.gmail.com");
        properties.put("mail.smtp.port", "587");
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");




    }



}
