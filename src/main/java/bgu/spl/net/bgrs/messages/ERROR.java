package bgu.spl.net.bgrs.messages;

import bgu.spl.net.bgrs.BGRSMessageProtocol;
import bgu.spl.net.bgrs.Database;

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
