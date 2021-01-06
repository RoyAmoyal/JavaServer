package bgu.spl.net.impl.BGRSServer.messages;

import bgu.spl.net.impl.BGRSServer.BGRSMessageProtocol;

public class ERROR extends Message{
    private final short myReturnedMessageOpcode;

    public ERROR(short messageSentOpcode) {
        super.myOpCode = 13;
        myReturnedMessageOpcode = messageSentOpcode;
    }



    @Override
    public Message process(BGRSMessageProtocol myClient) {
        return null;
    }

    public short getMyReturnedMessageOpcode() {
        return myReturnedMessageOpcode;
    }
}
