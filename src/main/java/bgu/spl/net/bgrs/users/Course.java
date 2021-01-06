package bgu.spl.net.bgrs.users;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

public class Course {
    private final short myCourseNum;
    private final String myCourseName;
    private final ArrayList<Short> myKdamCoursesList;
    private final int myNumOfMaxStudents; // int >= 5
    private final ArrayList<User> myRegisteredStudents; // WE HAVE TO SYNCHRONIZE THE DATA BASE METHODS THAT USE THAT FIELD!!!!
    private final int myRowInCoursesFile;




    public Course(short courseNum, String courseName, ArrayList<Short> kdamCoursesList, int numOfMaxStudents, int rowIndex){
        myCourseNum = courseNum;
        myCourseName = courseName;
        myKdamCoursesList = kdamCoursesList;
        myNumOfMaxStudents = numOfMaxStudents;
        myRegisteredStudents = new ArrayList<>();
        myRowInCoursesFile = rowIndex;
        };


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

    public boolean registerToMyCourse(User e){ //the database check before if that user is already registered to the course so we don't have to worry about it.
        if(myRegisteredStudents.size()<=myNumOfMaxStudents){
            myRegisteredStudents.add(e);
            return true;
        }
        return false;
    }

    public ArrayList<User> getMyRegisteredStudents(){
        return myRegisteredStudents;
    }


    public int getMyRowInCoursesFile() {
        return myRowInCoursesFile;
    }


}
