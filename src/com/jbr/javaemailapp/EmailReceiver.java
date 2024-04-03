package com.jbr.javaemailapp;

import client.SessionManager;

import javax.mail.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;



public class EmailReceiver {

    public static Message[] receiveEmail() throws MessagingException {
        SessionManager manager = SessionManager.getInstance();
        return manager.receiveEmail();
    }


    public static void main(String[] args) throws MessagingException {

        receiveEmail();
    }

}
