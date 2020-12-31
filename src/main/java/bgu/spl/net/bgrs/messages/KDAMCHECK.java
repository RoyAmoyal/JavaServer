package bgu.spl.net.bgrs.messages;

public class KDAMCHECK extends Message{
    private final int myCourseNumber;
    public KDAMCHECK(short myOpCode,int courseNumber) {
        super.myOpCode = 6;
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
