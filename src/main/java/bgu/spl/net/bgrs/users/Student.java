package bgu.spl.net.bgrs.users;

import java.util.concurrent.ConcurrentLinkedQueue;

public class Student extends User{
    private ConcurrentLinkedQueue<Short> myRegisteredCourses;

    public Student(String userName, String password) {
        super(userName, password);
        myRegisteredCourses = new ConcurrentLinkedQueue();
    }

    public void registerToNewCourse(short courseNum){
        myRegisteredCourses.add(courseNum);
    }

    public ConcurrentLinkedQueue<Short> getMyRegisteredCourses() {
        return myRegisteredCourses;
    }
}
