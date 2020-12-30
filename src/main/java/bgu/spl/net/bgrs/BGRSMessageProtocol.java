package bgu.spl.net.bgrs;
import bgu.spl.net.api.*;
import bgu.spl.net.bgrs.*;
import bgu.spl.net.bgrs.users.*;

public class BGRSMessageProtocol implements MessagingProtocol<String> {
    private User MyUser;
    private boolean shouldTerminate = false;

    private void commandIdenticator(String msg){

    }

    @Override
    public String process(String msg) {
        String messageOPCODE = msg.substring(0,1);

        switch(messageOPCODE){
            case "01": //ADMINREG
            {
                String messageUserName = msg.substring(2,msg.indexOf('\0') - 1); // Don't forget to check about the index
                String messagePassword = msg.substring(msg.indexOf('\0') + 1, msg.length() - 2);
                Database dataBase = Database.getInstance();
                if(!dataBase.addNewAdmin(messageUserName,messagePassword)){
                    // if we get false its mean there is already a registered user with that username so we should send an error message.
                    // ERROR(opcode 01) MESSAGE should be send over here.
                    break;
                }
                //ACK-MESSAGE SHOULD BE SEND OVER HERE
                break;
            }
            case "02": //STUDENTREG
            {
                String messageUserName = msg.substring(2,msg.indexOf('\0') - 1); // Don't forget to check about the index
                String messagePassword = msg.substring(msg.indexOf('\0') + 1, msg.length() - 2);
                Database dataBase = Database.getInstance();
                if(!dataBase.addNewStudent(messageUserName,messagePassword)){
                    // if we get false its mean there is already a registered user with that username so we should send an error message.
                    // ERROR(opcode 01) MESSAGE should be send over here.
                    break;
                }
                //ACK-MESSAGE SHOULD BE SEND OVER HERE
                break;
            }
            case "03": //LOGIN
            {
                String messageUserName = msg.substring(2,msg.indexOf('\0') - 1); // Don't forget to check about the index
                String messagePassword = msg.substring(msg.indexOf('\0') + 1, msg.length() - 2);
                Database dataBase = Database.getInstance();
                this.MyUser = dataBase.loginToTheSystem(messageUserName,messagePassword);
                //if we get null its mean that the user may already be logged-in or there is a problem with the username or the password .
                if(this.MyUser==null){
                    // ERROR(opcode 01) MESSAGE should be send over here.
                    break;
                }
                //The client manage to login. MyUser = to that user for future logout.
                break; // No need to send ACK message to the client
            }
            case "04": //LOGOUT
            {
                if(MyUser==null){
                    // ERROR, THIS CLIENT ISN'T LOGGED IN AS A USER
                    break;
                }
                Database dataBase = Database.getInstance();
                dataBase.logoutFromTheSystem(this.MyUser);
                MyUser=null; //The client isn't logged-in as a user anymore.
            }
            case "05": //COURSEREG
            {


            }
            case "KDAMCHECK":{

            }

        }




        return " ";
    }

    @Override
    public boolean shouldTerminate() {
        return shouldTerminate;
    }

}
