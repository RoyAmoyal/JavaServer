package bgu.spl.net.bgrs;
import bgu.spl.net.api.*;
import bgu.spl.net.bgrs.*;
public class BGRSMessageProtocol implements MessagingProtocol<String> {

    private boolean shouldTerminate = false;

    private void commandIdenticator(String msg){

    }

    @Override
    public String process(String msg) {
        String messageOPCODE = msg.substring(0,1);

        switch(messageOPCODE){
            case "01": // ADMINREG
            {
                String messageUserName = msg.substring(2,msg.indexOf('\0') - 1); // Don't forget to check about the index
                String messagePassword = msg.substring(msg.indexOf('\0') + 1, msg.length() - 2);
                Database dataBase = Database.getInstance();
                if(dataBase.isUserRegistered(messageUserName)){
                    // RETURN ERROR(opcode 01) MESSAGE
                }
                else{
                    dataBase.addNewAdmin(messageUserName,messagePassword);
                }
            }
            case "02": //STUDENTREG
            {

            }
            case "LOGIN":
            {}
            case "LOGOUT":
            {}
            case "COURSEREG":
            {}
            case "KDAMCHECK"

        }




        return " ";
    }

    @Override
    public boolean shouldTerminate() {
        return shouldTerminate;
    }

}
