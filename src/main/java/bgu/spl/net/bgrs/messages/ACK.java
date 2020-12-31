package bgu.spl.net.bgrs.messages;

public class ACK extends Message {
    private final short returnedMessageOpcode;

    public ACK(short messageSentOpcode) {
        super.myOpCode = 12;
        returnedMessageOpcode = messageSentOpcode;
    }

    public short getReturnedMessageOpcode() {
        return returnedMessageOpcode;
    }

    @Override
    public <T extends Message> T process() {
        return null;
    }
}
