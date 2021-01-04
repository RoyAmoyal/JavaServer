package bgu.spl.net.bgrs.users;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

public class Course {
    private final short myCourseNum;
    private final String myCourseName;
    private final ArrayList<Short> myKdamCoursesList;
    private final int myNumOfMaxStudents; // int >= 5
    private ConcurrentLinkedQueue<User> myRegisteredStudents;

    public Course(short courseNum, String courseName, ArrayList<Short> kdamCoursesList, int numOfMaxStudents){
        myCourseNum = courseNum;
        myCourseName = courseName;
        myKdamCoursesList = kdamCoursesList;
        myNumOfMaxStudents = numOfMaxStudents;
        myRegisteredStudents = new ConcurrentLinkedQueue<>();
    }

    public short getMyCourseNum() {
        return myCourseNum;
    }

    public String getMyCourseName() {
        return myCourseName;
    }

    public ArrayList<Short> getMyKdamCoursesList() {
        return myKdamCoursesList;
    }

    public int getMyNumOfMaxStudents() {
        return myNumOfMaxStudents;
    }

    public int getFreeSeatsNum(){
        return myNumOfMaxStudents-myRegisteredStudents.size();
    };

    public boolean registerToMyCourse(User e){
        if(myRegisteredStudents.size()<=myNumOfMaxStudents){
            myRegisteredStudents.add(e);
            return true;
        }
        return false;
    }
}
