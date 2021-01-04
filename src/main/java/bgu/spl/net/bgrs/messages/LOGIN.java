package bgu.spl.net.bgrs.messages;

import bgu.spl.net.bgrs.BGRSMessageProtocol;
import bgu.spl.net.bgrs.Database;

public class LOGIN extends Message{
    private final String myUserName;
    private final String myPassword;

    public LOGIN(String username,String password) {
        super.myOpCode = 3;
        myUserName = username;
        myPassword = password;
    }

    public String getAdminUserName(){
        return myUserName;
    }

    public String getAdminPassword(){
        return myPassword;
    }

    @Override
    public Message process(BGRSMessageProtocol myClient) {
        Database dataBase = Database.getInstance();
        if (dataBase.isClientLoggedIn(myClient) || dataBase.isUserLoggedIn(myUserName)) { //if the client is already logged in or someone is already logged in with that user.
            return new ERROR(myOpCode);
        }
        if (dataBase.loginToTheSystem(myUserName, myPassword, myClient)) { //The condition will fail if the username or the password are incorrect.
            return new ACK(myOpCode,"");
        }
        return new ERROR(myOpCode); //if the password or the username is incorrect.
    }
}
