package bgu.spl.net.bgrs;
import bgu.spl.net.api.*;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;

public class BGRSMessageEncoderDecoder implements MessageEncoderDecoder<String>{

    private byte[] bytes = new byte[1 << 10]; //start with 1k
    private int len = 0;
    private boolean secondZeroByte = false;
    private int endMessageZeroBytes=-2; // -2 as default state.
    String opcode;

   01230 02131


    @Override
    public String decodeNextByte(byte nextByte) {
        //notice that the top 128 ascii characters have the same representation as their utf-8 counterparts
        //this allow us to do the following comparison

        /*if (nextByte == '\0' && counterZeroBytes==endMessageZeroBytes ) { //END MESSAGE
            return popString();
        }*/

        //End message per case without including the zero byte that indicates about the end of the message
        if (nextByte == '\0' && endMessageZeroBytes == 1) {
            return popString();
        }

        if(endMessageZeroBytes == 2 && nextByte == '\0'){
            if(secondZeroByte) //if secondZeroByte=false its mean its the first time we encounter a zero byte so in the next zerobyte encounter we want to popString the message.
                return popString();
            secondZeroByte = true;
        }

        pushByte(nextByte);

        //End Message: for the cases we don't have a terminate byte.
        if(len==2) { //we finished to read the opcode on the previous byte
            opcode = opcodeIdenticator();
            setEndMessageZeroBytesByOpcode(opcode);
            if(endMessageZeroBytes==-1) // if the message doesn't contain '\0' for end message and it contains only the opcode.
                return popString();
        }

        if(len==4 && endMessageZeroBytes==0)
            return popString();




        return null; //not a line yet
    }


    @Override
    public byte[] encode(String message) {
        return (message + "\n").getBytes(); //uses utf8 by default
    }

    private void pushByte(byte nextByte) {
        if (len >= bytes.length) {
            bytes = Arrays.copyOf(bytes, len * 2);
        }
        bytes[len++] = nextByte;
    }

    private String popString() {
        //notice that we explicitly requesting that the string will be decoded from UTF-8
        //this is not actually required as it is the default encoding in java.
        String result = new String(bytes, 0, len, StandardCharsets.UTF_8);
        len = 0;
        return result;
    }


    //The 2 bytes of the opcode are encoding to string
    private String opcodeIdenticator(){
        String opcode = new String(bytes,0,2,StandardCharsets.UTF_8);
        return opcode;
    }

    //This method defines how much zero bytes we should expect for that message to identify the end of the message.
    private void setEndMessageZeroBytesByOpcode(String opcode){
        /* int endMessageZeroBytes=
        -2 as a default state. it should be changed after this method.
        -1 if the message contains only opcode.
         0 if the message contains opcode and string(of 2 bytes) --without-- '\0' char for the end of the message.
         1 if the message contains opcode and string --with a single-- '\0' char for the end of the message.
         2 if the message contains opcode and string (that splits by '\0' char to 2 substrings) --with two-- '\0' chars
           and second appear of the '\0' char represents the end of the message.
        */
        switch(opcode) {
            case "01":
            case "02":
            case "03": { // On this cases the the next bytes should contains '\0' when the second appear of '\0' its the end message char.
                endMessageZeroBytes = 2;
                break;
            }
            case "08": {
                endMessageZeroBytes = 1;
                break;
            }
            case "05":
            case "06":
            case "07":
            case "09":
            case "10": {//message contains only opcode and courseName in the total exactly  4 bytes.
                endMessageZeroBytes = 0;
            }
            case "04": // message contains only opcode
            case "11": {
                endMessageZeroBytes = -1;
                break;
            }

        }


    }
}
