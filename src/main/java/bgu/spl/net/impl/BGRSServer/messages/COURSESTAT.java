package bgu.spl.net.impl.BGRSServer.messages;

import bgu.spl.net.impl.BGRSServer.BGRSMessageProtocol;
import bgu.spl.net.impl.BGRSServer.Database;

public class COURSESTAT extends Message{
    private final short myCourseNumber;

    public COURSESTAT(short courseNumber) {
        super.myOpCode = 7;
        myCourseNumber = courseNumber;
    }

    @Override
    public Message process(BGRSMessageProtocol myClient) {
        Database dataBase = Database.getInstance();
        if (!dataBase.isCourseExist(myCourseNumber) || !dataBase.isClientLoggedIn(myClient) || !dataBase.isAdmin(myClient))
        //if the client isn't logged in or he *isn't a admin* or the course doesn't exist in the system return error
            return new ERROR(myOpCode);
        else
            return new ACK(myOpCode, dataBase.getCourseStatString(myCourseNumber));
    }


}
