package bgu.spl.net.impl.BGRSServer.messages;

import bgu.spl.net.impl.BGRSServer.BGRSMessageProtocol;
import bgu.spl.net.impl.BGRSServer.Database;

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
        if(!dataBase.addNewAdmin(myUserName,myPassword,myClient)) // if the client is already logged in he cant register. if addNewAdmin returns false the user already exist
            return new ERROR(myOpCode);
        else
            return new ACK(myOpCode,"");
    }
}
