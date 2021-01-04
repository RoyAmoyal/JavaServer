package bgu.spl.net.bgrs.messages;

import bgu.spl.net.bgrs.BGRSMessageProtocol;
import bgu.spl.net.bgrs.Database;

public class COURSEREG extends Message{
    private final short myCourseNumber;

    public COURSEREG(short courseNumber) {
        super.myOpCode = 5;
        myCourseNumber = courseNumber;
    }

    public short getMyCourseNumber() {
        return myCourseNumber;
    }

    @Override
    public Message process(BGRSMessageProtocol myClient) {
        Database database = Database.getInstance();
        if(!database.isClientLoggedIn(myClient) || !database.isCourseExist(myCourseNumber) || )
            return new ERROR(myOpCode);

    }
}
