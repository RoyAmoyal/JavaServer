package bgu.spl.net.bgrs.messages;

import bgu.spl.net.bgrs.BGRSMessageProtocol;

public class ACK extends Message{
    private final short myReturnedMessageOpcode;
    private final String myStringReply;

    public ACK(short messageSentOpcode,String stringMessageReply) {
        super.myOpCode = 12;
        myReturnedMessageOpcode = messageSentOpcode;
        myStringReply = stringMessageReply;
    }

    public short getMyReturnedMessageOpcode() {
        return myReturnedMessageOpcode;
    }

    public String getMyStringReply(){
        return myStringReply;
    }

    @Override
    public Message process(BGRSMessageProtocol myCilent) {
        return null;
    }
}
