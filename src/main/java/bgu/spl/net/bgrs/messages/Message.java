package bgu.spl.net.bgrs.messages;

public abstract class Message {
    short myOpCode;

    public abstract Message process();

    public short getOpCode() {
        return myOpCode;
    }




}
