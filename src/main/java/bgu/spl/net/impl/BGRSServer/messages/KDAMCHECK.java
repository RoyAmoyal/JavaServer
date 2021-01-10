package bgu.spl.net.impl.BGRSServer.messages;

import bgu.spl.net.impl.BGRSServer.BGRSMessageProtocol;
import bgu.spl.net.impl.BGRSServer.Database;

import java.util.ArrayList;

public class KDAMCHECK extends Message{
    private final short myCourseNumber;

    public KDAMCHECK(short courseNumber) {
        super.myOpCode = 6;
        myCourseNumber = courseNumber;
    }

    public int getMyCourseNumber() {
        return myCourseNumber;
    }


    @Override
    public Message process(BGRSMessageProtocol myClient) {
        Database dataBase = Database.getInstance();
        if(!dataBase.isCourseExist(myCourseNumber) || !dataBase.isClientLoggedIn(myClient) || dataBase.isAdmin(myClient))
            //if the client isn't logged in or he is an admin or the course doesn't exist in the system return error
            return new ERROR(myOpCode);
        ArrayList<Short> tempKdamCourses = dataBase.getKdamCourses(myCourseNumber); //Kdam check doesn't change so we can do it here.
        String kdamCheckString = kdamCheckCoursesToString(tempKdamCourses);
        System.out.println(kdamCheckString);
        return new ACK(myOpCode,kdamCheckString); //returns the list of the kdamCourses of myCourseNumber. Example: "[43,124,457]"
    }


    private String kdamCheckCoursesToString(ArrayList<Short> kdamCheckCourses){
        String[] tempStr = new String[kdamCheckCourses.size()];
        if(kdamCheckCourses.size()==0) //if there are not kdamcourses for that course we send empty string.
            return "[]";
        for(int i=0;i<kdamCheckCourses.size();i++)
            tempStr[i] = String.valueOf(kdamCheckCourses.get(i));
        String kdamCheckStr = String.join(",",tempStr);
        return "[" + kdamCheckStr + "]";
    }

}
