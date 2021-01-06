package bgu.spl.net.impl.BGRSServer.messages;

import bgu.spl.net.impl.BGRSServer.BGRSMessageProtocol;
import bgu.spl.net.impl.BGRSServer.Database;

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
