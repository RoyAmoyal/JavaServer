package bgu.spl.net.bgrs.messages;

import bgu.spl.net.bgrs.BGRSMessageProtocol;
import bgu.spl.net.bgrs.Database;

public class UNREGISTER extends Message{
    private final short myCourseNumber;
    public UNREGISTER(short courseNumber) {
        super.myOpCode = 10;
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
        if(dataBase.unRegisterToCourse(myClient,myCourseNumber)) //if he manage to unregister successfully.
            return new ACK(myOpCode,"");
        else
            return new ERROR(myOpCode);
    }
}
