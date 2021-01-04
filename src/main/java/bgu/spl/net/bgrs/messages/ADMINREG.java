package bgu.spl.net.bgrs.messages;

import bgu.spl.net.bgrs.BGRSMessageProtocol;
import bgu.spl.net.bgrs.Database;

public class ADMINREG extends Message {
    private final String myUserName;
    private final String myPassword;


    public ADMINREG(String username,String password) {
        super.myOpCode = 1;
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
    public Message process(BGRSMessageProtocol myClient)  {
        Database dataBase = Database.getInstance();
        if(dataBase.isClientLoggedIn(myClient) || !dataBase.addNewAdmin(myUserName,myPassword)) // if the client is already logged in he cant register. if addNewAdmin returns false the user already exist
            return new ERROR(myOpCode);
        else
            return new ACK(myOpCode,myUserName + " registered successfully as a new Admin");
    }
}
