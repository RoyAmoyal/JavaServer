package bgu.spl.net.bgrs.messages;

public class COURSEREG extends Message{
    private final int myCourseNumber;

    public COURSEREG(short myOpCode,int courseNumber) {
        super.myOpCode = 5;
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
