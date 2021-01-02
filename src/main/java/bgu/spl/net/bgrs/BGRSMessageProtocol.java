package bgu.spl.net.bgrs;
import bgu.spl.net.api.*;
import bgu.spl.net.bgrs.*;
import bgu.spl.net.bgrs.messages.ADMINREG;
import bgu.spl.net.bgrs.messages.*;
import bgu.spl.net.bgrs.users.*;

public class BGRSMessageProtocol implements MessagingProtocol<Message> {
    private String MyUser=null;
    private boolean shouldTerminate = false;




    @Override
    public Message process(Message msg) {


        return msg.process();




        switch(messageOPCODE) {
                //Example message for cases 1,2,3: "02Amit\01234\0" , opcode = 02 , Username = Amit , Password = 1234;
                case 1: //ADMINREG
                {
                    ADMINREG adminRegMessage = (ADMINREG) msg;
                    String messageUserName = adminRegMessage.getAdminUserName();
                    String messagePassword = adminRegMessage.getAdminPassword();
                    Database dataBase = Database.getInstance();
                    Message response = msg.process();
                    if (!dataBase.addNewAdmin(messageUserName, messagePassword)) {
                        return new
                        break;
                    }
                    //ACK-MESSAGE SHOULD BE SEND OVER HERE
                }
                case "02": //STUDENTREG
                {
                    String messageUserName = msg.substring(2, msg.indexOf('\0') - 1); // Don't forget to check about the index
                    String messagePassword = msg.substring(msg.indexOf('\0') + 1, msg.length() - 1);
                    Database dataBase = Database.getInstance();
                    if (!dataBase.addNewStudent(messageUserName, messagePassword)) {
                        // if we get false its mean there is already a registered user with that username so we should send an error message.
                        // ERROR(opcode 01) MESSAGE should be send over here.
                        break;
                    }
                    //ACK-MESSAGE SHOULD BE SEND OVER HERE

                }
                case "03": //LOGIN
                {
                    String messageUserName = msg.substring(2, msg.indexOf('\0') - 1); // Don't forget to check about the index
                    String messagePassword = msg.substring(msg.indexOf('\0') + 1, msg.length() - 1);
                    Database dataBase = Database.getInstance();
                    if (dataBase.isloggedIn(messageUserName)) {
                        //ERROR MESSAGE BECAUSE HE ALREADY LOGGED IN
                    }
                    if (dataBase.loginToTheSystem(messageUserName, messagePassword)) {//The condition will fail if the username or the password are incorrect.
                        //The client manage to login. MyUser - is for client to manage in the future to logout.
                        this.MyUser = messageUserName;
                    } else {
                        //SEND ERROR
                    }
                    // No need to send ACK message to the client about successful login
                }
                case "04": //LOGOUT
                {
                    if (MyUser == null) {
                        // ERROR, THIS CLIENT ISN'T LOGGED IN AS A USER
                        break;
                    }
                    Database dataBase = Database.getInstance();
                    dataBase.logoutFromTheSystem(MyUser);
                    MyUser = null; //The client isn't logged-in as a user anymore.
                }
                case "05": //COURSEREG
                {


                }
                case "KDAMCHECK": {

                }
                default: {
                    break;
                }
            }




        return " ";
    }

    @Override
    public boolean shouldTerminate() {
        return shouldTerminate;
    }

}
