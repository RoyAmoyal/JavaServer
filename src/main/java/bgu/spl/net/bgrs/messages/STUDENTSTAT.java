package bgu.spl.net.bgrs.messages;

public class STUDENTSTAT extends Message{
    public STUDENTSTAT() {
        super.myOpCode = 8;
    }

    @Override
    public <T extends Message> T process() {
        return null;
    }
}
