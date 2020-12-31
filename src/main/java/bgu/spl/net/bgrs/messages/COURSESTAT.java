package bgu.spl.net.bgrs.messages;

public class COURSESTAT extends Message{
    public COURSESTAT(short myOpCode) {
        super.myOpCode = 7;
    }

    @Override
    public <T extends Message> T process(Class<T> type) {
        return null;
    }
}
