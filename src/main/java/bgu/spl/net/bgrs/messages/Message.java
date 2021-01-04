package bgu.spl.net.bgrs.messages;

import bgu.spl.net.bgrs.BGRSMessageProtocol;

public abstract class Message {
    short myOpCode;

    public abstract Message process(BGRSMessageProtocol myCilent);

    public short getOpCode() {
        return myOpCode;
    }




}
