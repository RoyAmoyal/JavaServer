package bgu.spl.net.impl.BGRSServer;

import bgu.spl.net.srv.Server;

public class ReactorMain {
    public static void main(String[] args){
        if(args.length < 2){
            System.out.println("Please Enter a port and the number of threads");
            return;
        }
       Database.getInstance();
        Server.reactor(
                Integer.parseInt(args[1]),
                Integer.parseInt(args[0]),
                () -> new BGRSMessageProtocol(), () -> new BGRSMessageEncoderDecoder()
        ).serve();
    }
}
