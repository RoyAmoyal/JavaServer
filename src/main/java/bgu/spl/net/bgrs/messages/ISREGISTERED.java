package bgu.spl.net.bgrs.messages;

public class ISREGISTERED extends Message{
    private final int myCourseNumber;

    public ISREGISTERED(int courseNumber) {
      super.myOpCode = 9;
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
