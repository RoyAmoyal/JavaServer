package bgu.spl.net.bgrs.messages;

public abstract class Message {
    short myOpCode;

    public abstract <T extends Message> T process();

    public short getOpCode() {
        return myOpCode;
    }




}
