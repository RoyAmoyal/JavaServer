package bgu.spl.net.impl.BGRSServer.messages;

import bgu.spl.net.impl.BGRSServer.BGRSMessageProtocol;
import bgu.spl.net.impl.BGRSServer.Database;

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
        if(!database.registerToNewCourse(myCourseNumber,myClient)) //if the client failed to register to the course
            return new ERROR(myOpCode);
        else
            return new ACK(myOpCode,"");

    }
}
