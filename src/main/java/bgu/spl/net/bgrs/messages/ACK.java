package bgu.spl.net.bgrs.messages;

import bgu.spl.net.bgrs.BGRSMessageProtocol;

public class ACK extends Message {
    private final short myReturnedMessageOpcode;
    private final String myMessageReply;

    public ACK(short messageSentOpcode,String messageReply) {
        super.myOpCode = 12;
        myReturnedMessageOpcode = messageSentOpcode;
        myMessageReply = messageReply;
    }

    public short getMyReturnedMessageOpcode() {
        return myReturnedMessageOpcode;
    }

    public String getMyMessageReply(){
        return myMessageReply;
    }

    @Override
    public Message process(BGRSMessageProtocol myCilent) {
        return null;
    }
}
