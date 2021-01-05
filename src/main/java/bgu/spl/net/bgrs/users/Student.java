package bgu.spl.net.bgrs.users;

import java.util.ArrayList;
import java.util.concurrent.ConcurrentLinkedQueue;

public class Student extends User{
    private ArrayList<Short> myRegisteredCourses;

    public Student(String userName, String password) {
        super(userName, password);
        myRegisteredCourses = new ArrayList<>();
    }

    public void registerToNewCourse(short courseNum){
        myRegisteredCourses.add(courseNum);
    }

    public ArrayList<Short> getMyRegisteredCourses() {
        return myRegisteredCourses;
    }
}
