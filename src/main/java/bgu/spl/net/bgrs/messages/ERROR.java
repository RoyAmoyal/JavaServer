package bgu.spl.net.bgrs.messages;

public class ERROR extends Message{
    private final short returnedMessageOpcode;

    public ERROR(short myOpCode,short messageSentOpcode) {
        super.myOpCode = 13;
        returnedMessageOpcode = messageSentOpcode;
    }

    @Override
    public <T extends Message> T process() {
        return null;
    }
}
