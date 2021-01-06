package bgu.spl.net.bgrs.messages;

import bgu.spl.net.bgrs.BGRSMessageProtocol;
import bgu.spl.net.bgrs.Database;

public class ISREGISTERED extends Message{
    private final short myCourseNumber;

    public ISREGISTERED(short courseNumber) {
      super.myOpCode = 9;
      myCourseNumber = courseNumber;
    }



    public short getMyCourseNumber() {
        return myCourseNumber;
    }

    @Override
    public Message process(BGRSMessageProtocol myClient) {
        Database dataBase = Database.getInstance();
        if(!dataBase.isClientLoggedIn(myClient) || dataBase.isAdmin(myClient)) //if the client isn't logged in as a user or he is an admin return error.
            return new ERROR(myOpCode);
        if(dataBase.isRegisteredToCourse(myClient,myCourseNumber))
            return new ACK(myOpCode,"REGISTERED");
        else
            return new ACK(myOpCode,"NOT REGISTERED");


    }
}
