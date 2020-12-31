package bgu.spl.net.bgrs.messages;

public class MYCOURSES extends Message{

    public MYCOURSES() {
        super.myOpCode = 11;
    }


    @Override
    public <T extends Message> T process() {
        return null;
    }
}
