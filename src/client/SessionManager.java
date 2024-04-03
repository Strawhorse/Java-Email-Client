package client;

/*
* By encapsulating email session management in this singleton class,
* your app can maintain a clean and efficient connection lifecycle, improve resource management,
* and simplifying the process of accessing and displaying emails.
*/


import javax.mail.*;
import java.util.Properties;

public class SessionManager {

    private Session emailSession;
    private Store store;
    private Folder inbox;
    private static SessionManager instance;

    // Add static fields to store username and password
    private static String currentUsername = "";
    private static String currentPassword = "";


//    constructor

    private SessionManager(String username, String password) throws Exception {
        Properties properties = new Properties();
        properties.put("mail.store.protocol", "imaps");
        properties.put("mail.imaps.host", "imap.gmail.com");
        properties.put("mail.imaps.port", "993");
        properties.put("mail.imaps.ssl.enable", "true");

        this.emailSession = Session.getInstance(properties, null);
        this.store = emailSession.getStore("imaps");
        this.store.connect(username, password);

        currentUsername = username;
        currentPassword = password;


    }


//    static getInstance method to provide a global access point to the singleton instance of EmailSessionManager
//    SessionManager = Store a global session, allow the storage of alternative sessions as well.

    public static void getInstance(String username, String password) throws Exception {
        if (instance == null) {
            instance = new SessionManager(username, password);
        }
    }

//Overloaded the getInstance method with a parameterless version for accessing the instance after a login has occurred
    public static SessionManager getInstance() throws IllegalStateException{
        if(instance == null) {
            throw new IllegalStateException("EmailSessionManager is not started. Please log in and try again.");
        }
        return instance;
    }


    public static String getUsername(){
        return currentUsername;
    }

    public static String getPassword(){
        return currentPassword;
    }



    public Message[] receiveEmail() throws MessagingException {
        if (inbox == null || inbox.isOpen()) {
            inbox = store.getFolder("INBOX");
            inbox.open(Folder.READ_ONLY);
        }
        return inbox.getMessages();
    }


    public void closeInbox() throws MessagingException {
        if(inbox != null) {
            inbox.close(false);
            inbox = null;
        }
        if(store != null){
            store.close();
            store = null;
        }

        instance = null;

        currentUsername = "";
        currentPassword = "";
    }


}
