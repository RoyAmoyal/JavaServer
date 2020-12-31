package bgu.spl.net.bgrs.messages;

public class LOGOUT extends Message{
    public LOGOUT(short myOpCode) {
        super.myOpCode = 4;
    }

    @Override
    public <T extends Message> T process(Class<T> type) {
        return null;
    }
}
