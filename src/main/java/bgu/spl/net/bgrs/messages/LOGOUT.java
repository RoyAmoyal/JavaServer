package bgu.spl.net.bgrs.messages;

public class LOGOUT extends Message{
    public LOGOUT() {
        super.myOpCode = 4;
    }

    @Override
    public <T extends Message> T process() {
        return null;
    }
}
