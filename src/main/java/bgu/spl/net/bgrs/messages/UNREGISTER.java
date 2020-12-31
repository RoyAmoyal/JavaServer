package bgu.spl.net.bgrs.messages;

public class UNREGISTER extends Message{
    private final int myCourseNumber;
    public UNREGISTER(int courseNumber) {
        super.myOpCode = 10;
        myCourseNumber = courseNumber;
    }

    public int getMyCourseNumber() {
        return myCourseNumber;
    }


    @Override
    public <T extends Message> T process() {
        return null;
    }
}
