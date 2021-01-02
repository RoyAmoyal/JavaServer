package bgu.spl.net.bgrs.messages;

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
    public Message process()  {
        Database dataBase = Database.getInstance();
        if(!dataBase.addNewAdmin(myUserName,myPassword))
            return new ERROR(myOpCode);
        else
            return new ACK(myOpCode,myUserName + " registered successfully as a new admin");
    }
}
