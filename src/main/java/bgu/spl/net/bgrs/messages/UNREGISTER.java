package bgu.spl.net.bgrs.messages;

public class UNREGISTER extends Message{
    private final int myCourseNumber;
    public UNREGISTER(short myOpCode,int courseNumber) {
        super.myOpCode = 10;
        myCourseNumber = courseNumber;
    }

    public int getMyCourseNumber() {
        return myCourseNumber;
    }


    @Override
    public <T extends Message> T process(Class<T> type) {
        return null;
    }
}
