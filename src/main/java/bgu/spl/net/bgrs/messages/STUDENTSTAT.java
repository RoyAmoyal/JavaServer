package bgu.spl.net.bgrs.messages;

import bgu.spl.net.bgrs.BGRSMessageProtocol;
import bgu.spl.net.bgrs.Database;

public class STUDENTSTAT extends Message{
    private final String myRequestedStatsUserName; // The username of the student the client wants the stats about.
    public STUDENTSTAT(String studentUserName) {
        super.myOpCode = 8;
        myRequestedStatsUserName = studentUserName;
    }

    @Override
    public Message process(BGRSMessageProtocol myClient) {
        Database dataBase = Database.getInstance();
        if (!dataBase.isClientLoggedIn(myClient) || !dataBase.isAdmin(myClient) || !dataBase.isUserExist(myRequestedStatsUserName) ||dataBase.isAdmin(myRequestedStatsUserName))
            //if the client isn't logged in or he *isn't a admin* or the requested User doesn't exist on the system or the requested user is not an student (means he is a admin).
            return new ERROR(myOpCode);
        else
            return new ACK(myOpCode, dataBase.getStudentStatString(myRequestedStatsUserName));

    }
}
