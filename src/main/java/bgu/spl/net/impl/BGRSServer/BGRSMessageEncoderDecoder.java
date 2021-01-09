package bgu.spl.net.impl.BGRSServer;
import bgu.spl.net.api.*;
import bgu.spl.net.impl.BGRSServer.messages.*;
import bgu.spl.net.impl.BGRSServer.messages.*;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;

public class BGRSMessageEncoderDecoder implements MessageEncoderDecoder<Message>{

    private byte[] bytes = new byte[1 << 10]; //start with 1k
    private int len = 0;
    private boolean secondZeroByte = false;
    private int endMessageZeroBytes=-2; // -2 as default state.
    private short opcode;
    private String userName;
    private String password;
    private Message messageFromClient;
    private int beginPointerForPassword;

    @Override
    public Message decodeNextByte(byte nextByte) {
        //notice that the top 128 ascii characters have the same representation as their utf-8 counterparts
        //this allow us to do the following comparison

        /*if (nextByte == '\0' && counterZeroBytes==endMessageZeroBytes ) { //END MESSAGE
            return popString();
        }*/

        //End message per case without including the zero byte that indicates about the end of the message
        if (nextByte == '\0' && endMessageZeroBytes == 1) { //opcode must be 8 here.
            userName = new String(bytes, 2 , len, StandardCharsets.US_ASCII);
            messageFromClient = new STUDENTSTAT(userName);
            return popMessage();
        }

        if(endMessageZeroBytes == 2 && nextByte == '\0'){
            if(secondZeroByte) { //if secondZeroByte=false its mean its the first time we encounter a zero byte so in the next zerobyte encounter we want to popString the message.
                password = new String(bytes, beginPointerForPassword, len, StandardCharsets.US_ASCII);
                if(opcode==1) { //If its ADMINREG Message
                    messageFromClient = new ADMINREG(userName, password);
                    System.out.println("testing: ADMINREG RECIVED: Username: " + userName + " Passowrd: " + password);
                }
                else if(opcode==2)
                    messageFromClient = new STUDENTREG(userName,password);
                else
                    messageFromClient = new LOGIN(userName,password);
                return popMessage();
            }
            userName = new String(bytes, 2 , len-1, StandardCharsets.US_ASCII);
            secondZeroByte = true;
            beginPointerForPassword = len + 1; //this is the first zero byte so from the next byte the password begin.
        }
        System.out.println("testing: Pushing byte num: " + len+1);
        pushByte(nextByte);

        //End Message: for the cases we don't have a terminate byte.
        if(len==2) { //we finished to read the opcode on the previous byte
            opcode = opcodeDecoder();
            System.out.println("test: opcode recived is: " + opcode);
            setEndMessageZeroBytesByOpcode(opcode);
            if(endMessageZeroBytes==-1) { // if the message doesn't contain '\0' for end message and it contains only the opcode.
                if (opcode == 4)
                    messageFromClient = new LOGOUT();
                else
                    messageFromClient = new MYCOURSES();
                return popMessage();
            }
        }

        if(len==4 && endMessageZeroBytes==0){
            byte[] courseNumBytes = Arrays.copyOfRange(bytes,2,4); // not including the 4th cell.
            short courseNum = bytesToShort(courseNumBytes);
            if(opcode==5)
                messageFromClient = new COURSEREG(courseNum);
            else if(opcode==6)
                messageFromClient = new KDAMCHECK(courseNum);
            else if(opcode==7)
                messageFromClient = new COURSESTAT(courseNum);
            else if(opcode==9)
                messageFromClient = new ISREGISTERED(courseNum);
            else if(opcode==10)
                messageFromClient = new UNREGISTER(courseNum);
            // finally
            return popMessage();
        }





        return null; //not a line yet
    }


    @Override
    public byte[] encode(Message message) {
        if(message.getClass().equals(ERROR.class)){
            ERROR currError = (ERROR)message;
            short opCodeMessageReply = currError.getMyReturnedMessageOpcode();
            short errorOpCode = currError.getOpCode();
            byte[] opCodeMessageReplyBytes = shortToBytes(opCodeMessageReply);
            byte[] errorOpCodeBytes = shortToBytes(errorOpCode);
            byte[] finalErrorMessageBytes = new byte[opCodeMessageReplyBytes.length+errorOpCodeBytes.length];
            System.arraycopy(errorOpCodeBytes,0,finalErrorMessageBytes,0,errorOpCodeBytes.length);
            System.arraycopy(opCodeMessageReplyBytes,0,finalErrorMessageBytes,errorOpCodeBytes.length,opCodeMessageReplyBytes.length);
            return finalErrorMessageBytes; //
        }
        //if its not an error message then its ack --->
        ACK currACK = (ACK)message;
        short opCodeMessageReply = currACK.getMyReturnedMessageOpcode();
        short ackOpCode = currACK.getOpCode();
        byte[] opCodeMessageReplyBytes = shortToBytes(opCodeMessageReply);
        byte[] ackOpCodeBytes = shortToBytes(ackOpCode);
        byte[] ackShortsMessageBytes = new byte[opCodeMessageReplyBytes.length + ackOpCodeBytes.length];
        System.arraycopy(ackOpCodeBytes,0,ackShortsMessageBytes,0,ackOpCodeBytes.length);
        System.arraycopy(opCodeMessageReplyBytes,0,ackShortsMessageBytes,ackOpCodeBytes.length,opCodeMessageReplyBytes.length);
        // we got the 4 bytes of the shorts ready.
        if(currACK.getMyStringReply().equals("")){ //if the string is empty
            byte[] finalACKMessageBytes = new byte[ackShortsMessageBytes.length + 1]; // plus one for the zeroByte
            System.arraycopy(ackShortsMessageBytes,0,finalACKMessageBytes,0,ackShortsMessageBytes.length); // we got the shorts in the array
            finalACKMessageBytes[4] = '\0';
            return finalACKMessageBytes;
        }
        //if the string isn't empty.
        String strReply = currACK.getMyStringReply();
        byte[] strBytes = strReply.getBytes(StandardCharsets.US_ASCII);
        byte[] finalACKMessageBytes = new byte[ackShortsMessageBytes.length + strBytes.length + 1]; // plus one for the zeroByte
        System.arraycopy(ackShortsMessageBytes,0,finalACKMessageBytes,0,finalACKMessageBytes.length); //now we got shorts in the array
        System.arraycopy(strBytes,0,finalACKMessageBytes,finalACKMessageBytes.length,strBytes.length); //now we got shorts + String in the array
        finalACKMessageBytes[finalACKMessageBytes.length-1] = '\0'; //now the Final ACK MESSAGE bytes are ready.
        return finalACKMessageBytes;
    }

    private void pushByte(byte nextByte) {
        if (len >= bytes.length) {
            bytes = Arrays.copyOf(bytes, len * 2);
        }
        bytes[len++] = nextByte;
    }

    private Message popMessage() {
        len = 0;
        secondZeroByte = false;
        endMessageZeroBytes = -2;
        opcode = -1;
        return messageFromClient;
    }


    //The 2 bytes of the opcode are encoding to string
    private short opcodeDecoder(){
        byte[] opcodeBytes = new byte[2];
        opcodeBytes[0] = bytes[0];
        opcodeBytes[1] = bytes[1];
        return bytesToShort(opcodeBytes);
    }

    //This method defines how much zero bytes we should expect for that message to identify the end of the message.
    private void setEndMessageZeroBytesByOpcode(short opcode){
        /* int endMessageZeroBytes=
        -2 as a default state. it should be changed after this method.
        -1 if the message contains only opcode.
         0 if the message contains opcode and string(of 2 bytes) --without-- '\0' char for the end of the message.
         1 if the message contains opcode and string --with a single-- '\0' char for the end of the message.
         2 if the message contains opcode and string (that splits by '\0' char to 2 substrings) --with two-- '\0' chars
           and second appear of the '\0' char represents the end of the message.
        */
        switch(opcode) {
            case 1:
            case 2:
            case 3: { // On this cases the the next bytes should contains '\0' when the second appear of '\0' its the end message char.
                endMessageZeroBytes = 2;
                break;
            }
            case 8: {
                endMessageZeroBytes = 1;
                break;
            }
            case 5:
            case 6:
            case 7:
            case 9:
            case 10: {//message contains only opcode and courseName in the total exactly  4 bytes.
                endMessageZeroBytes = 0;
                break;
            }
            case 4: // message contains only opcode
            case 11: {
                endMessageZeroBytes = -1;
                break;
            }

        }
    }
    public short bytesToShort(byte[] byteArr)
    {
        short result = (short)((byteArr[0] & 0xff) << 8);
        result += (short)(byteArr[1] & 0xff);
        return result;
    }

    public byte[] shortToBytes(short num)
    {
        byte[] bytesArr = new byte[2];
        bytesArr[0] = (byte)((num >> 8) & 0xFF);
        bytesArr[1] = (byte)(num & 0xFF);
        return bytesArr;
    }
}
