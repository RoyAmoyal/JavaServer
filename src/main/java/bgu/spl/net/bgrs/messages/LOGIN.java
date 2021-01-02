package bgu.spl.net.bgrs.messages;

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
    public Message process() {
        return null;
    }
}
