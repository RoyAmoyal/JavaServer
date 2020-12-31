package bgu.spl.net.bgrs.messages;

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
    public <T extends Message> T process() {
        return null;
    }
}
