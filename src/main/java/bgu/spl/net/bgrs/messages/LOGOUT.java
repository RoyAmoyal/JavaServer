package bgu.spl.net.bgrs.messages;

import bgu.spl.net.bgrs.BGRSMessageProtocol;
import bgu.spl.net.bgrs.Database;

public class LOGOUT extends Message{
    public LOGOUT() {
        super.myOpCode = 4;
    }

    @Override
    public Message process(BGRSMessageProtocol myClient) {
        Database dataBase = Database.getInstance();
        if(!dataBase.isClientLoggedIn(myClient))
            return new ERROR(myOpCode); // if the client isn't logged in as a user.
        else {
            dataBase.logoutFromTheSystem(myClient);
            return new ACK(myOpCode, ""); //Success
        }
    }
}
