package bgu.spl.net.bgrs.messages;

import bgu.spl.net.bgrs.Database;

public class STUDENTREG extends Message{
    private final String myUserName;
    private final String myPassword;

    public STUDENTREG(String username,String password) {
        super.myOpCode = 2;
        myUserName = username;
        myPassword = password;
    }


    public String getStudentUserName(){
        return myUserName;
    }

    public String getStudentPassword(){
        return myPassword;
    }

    @Override
    public Message process() {
        Database dataBase = Database.getInstance();
        if(!dataBase.addNewAdmin(myUserName,myPassword))
            return new ERROR(myOpCode);
        else
            return new ACK(myOpCode,myUserName + " registered successfully as a new Student");
    }
}
