package bgu.spl.net.impl.BGRSServer;

import bgu.spl.net.srv.Server;

public class TPCMain {
    public static void main(String[] args){
        if(args.length < 1){
            System.out.println("Please Enter a port and the number of threads");
            return;
        }
        Database.getInstance();
        Server.threadPerClient(
                Integer.parseInt(args[0]),
                () -> new BGRSMessageProtocol(), () -> new BGRSMessageEncoderDecoder()
        ).serve();
    }
}
