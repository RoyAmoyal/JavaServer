package bgu.spl.net.impl.BGRSServer.users;

import java.util.ArrayList;

public class Student extends User{
    private ArrayList<Short> myRegisteredCourses;

    public Student(String userName, String password) {
        super(userName, password);
        myRegisteredCourses = new ArrayList<>();
    }

    public void registerToNewCourse(short courseNum){
        myRegisteredCourses.add(courseNum);
    }

    public void unRegisterToCourse(short courseNum){
        myRegisteredCourses.remove(Short.valueOf(courseNum));
    }

    public ArrayList<Short> getMyRegisteredCourses() {
        return myRegisteredCourses;
    }
}
