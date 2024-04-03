package com.jbr.javaemailapp;

import client.SessionManager;

import javax.mail.*;


public class EmailReceiver {

    public static void receiveEmail() throws MessagingException {
        SessionManager manager = SessionManager.getInstance();
        manager.receiveEmail();
    }


    public static void main(String[] args) throws MessagingException {

        receiveEmail();
    }

}
