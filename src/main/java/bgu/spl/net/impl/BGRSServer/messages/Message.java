package bgu.spl.net.impl.BGRSServer.messages;

import bgu.spl.net.impl.BGRSServer.BGRSMessageProtocol;

public abstract class Message {
    short myOpCode;

    public abstract Message process(BGRSMessageProtocol myCilent);

    public short getOpCode() {
        return myOpCode;
    }




}
