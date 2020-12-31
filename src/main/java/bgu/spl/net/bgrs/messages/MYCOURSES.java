package bgu.spl.net.bgrs.messages;

public class MYCOURSES extends Message{

    public MYCOURSES(short myOpCode) {
        super.myOpCode = 11;
    }


    @Override
    public <T extends Message> T process(Class<T> type) {
        return null;
    }
}
