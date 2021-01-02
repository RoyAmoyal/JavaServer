package bgu.spl.net.bgrs.messages;

import bgu.spl.net.bgrs.Database;

public class ERROR extends Message{
    private final short returnedMessageOpcode;

    public ERROR(short messageSentOpcode) {
        super.myOpCode = 13;
        returnedMessageOpcode = messageSentOpcode;
    }

    @Override
    public <T extends Message> T process() {

    }
}
