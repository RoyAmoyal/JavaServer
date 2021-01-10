package bgu.spl.net.impl.BGRSServer.messages;

import bgu.spl.net.impl.BGRSServer.BGRSMessageProtocol;
import bgu.spl.net.impl.BGRSServer.Database;

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
        if (dataBase.loginToTheSystem(myUserName, myPassword, myClient)) { //The condition will fail if the username or the password are incorrect.
            return new ACK(myOpCode,"");
        }
        return new ERROR(myOpCode); //if failed return error
    }
}
