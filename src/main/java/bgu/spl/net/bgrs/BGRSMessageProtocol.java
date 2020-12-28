package bgu.spl.net.bgrs;
import bgu.spl.net.api.*;
public class BGRSMessageProtocol implements MessagingProtocol<String> {

    private boolean shouldTerminate = false;

    private void commandIdenticator(String msg){

    }



    @Override
    public String process(String msg) {

        return " ";
    }

    @Override
    public boolean shouldTerminate() {
        return shouldTerminate;
    }

}
