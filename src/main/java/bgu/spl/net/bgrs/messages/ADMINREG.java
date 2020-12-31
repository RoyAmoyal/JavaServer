package bgu.spl.net.bgrs.messages;

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
    public <T extends Message> T process() {
        return null;
    }
}
