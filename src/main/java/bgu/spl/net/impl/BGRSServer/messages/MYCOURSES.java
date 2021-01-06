package bgu.spl.net.impl.BGRSServer.messages;

import bgu.spl.net.impl.BGRSServer.BGRSMessageProtocol;
import bgu.spl.net.impl.BGRSServer.Database;

public class MYCOURSES extends Message{

    public MYCOURSES() {
        super.myOpCode = 11;
    }


    @Override
    public Message process(BGRSMessageProtocol myClient) {
        Database dataBase = Database.getInstance();
        if(!dataBase.isClientLoggedIn(myClient) || dataBase.isAdmin(myClient)) //if the client isn't logged in as a user or he isn't an student return error.
            return new ERROR(myOpCode);
        return new ACK(myOpCode,dataBase.getMyCoursesStr(myClient));
    }
}
