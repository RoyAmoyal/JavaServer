package bgu.spl.net.impl.BGRSServer;


import bgu.spl.net.impl.BGRSServer.users.Course;
import bgu.spl.net.impl.BGRSServer.users.User;
import bgu.spl.net.impl.BGRSServer.users.Admin;
import bgu.spl.net.impl.BGRSServer.users.Student;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
     * Passive object representing the Database where all courses and users are stored.
     * <p>
     * This class must be implemented safely as a thread-safe singleton.
     * You must not alter any of the given public methods of this class.
     * <p>
     * You can add private fields and methods to this class as you see fit.
     */

    public class Database {
        private final ConcurrentHashMap<Short,Course> coursesList = new ConcurrentHashMap<>();
        private final ConcurrentHashMap<String, User> usersList = new ConcurrentHashMap<>(); // CHECK IF BETTER TO SPLIT THE ADMINS AND THE STUDENTS SYNCHRONIZED
        private final ConcurrentHashMap<BGRSMessageProtocol,String> clientsLoggedIn = new ConcurrentHashMap<>();
        private final Object registerLock = new Object();




    //to prevent user from creating new Database
    private Database(){
        initialize("Courses.txt");
    }

    private static class DatabaseSingletonHolder {
        private static final Database instance = new Database(); //final or not final????
    }


        /**
         * Retrieves the single instance of this class.
         */
        public static Database getInstance() {
            return Database.DatabaseSingletonHolder.instance;
        }



    /**
         * loades the courses from the file path specified
         * into the Database, returns true if successful.
         */
    boolean initialize(String coursesFilePath) {
        try {
            File myFile = new File(coursesFilePath);
            Scanner myReader = new Scanner(myFile);
            int rowIndex = 0;
            while (myReader.hasNextLine()) {
                String data = myReader.nextLine();
                rowIndex++;
                loadNewCourse(data,rowIndex);
            }
            for(Map.Entry<Short,Course> item: coursesList.entrySet()){ //Sort all the kdamcourses shorts of each course to the order that the courses appear in the Courses.text file
                ArrayList<Short> currKdamCourses = item.getValue().getMyKdamCoursesList();
                Collections.sort(item.getValue().getMyKdamCoursesList(), new Comparator<Short>() { //Anonymous Class can be replaced with lambda that can be replaced to even shorter call.
                    @Override
                    public int compare(Short o1, Short o2) {
                        return coursesList.get(o1).getMyRowInCoursesFile()-coursesList.get(o2).getMyRowInCoursesFile();
                    }
                });
                }

        }catch(FileNotFoundException e){
                e.printStackTrace();
            }
        return true;
        }


        private void loadNewCourse(String data,int rowIndex){
            String[] fullCourseData = data.split("\\|");
            short courseNum = Short.parseShort(fullCourseData[0]);
            String courseName = fullCourseData[1];
            Course currNewCourse;
            if(fullCourseData[2].equals("[]")) {
                int numOfMaxStudents = Integer.parseInt(fullCourseData[3]);
                currNewCourse = new Course(courseNum, courseName, new ArrayList<Short>(), numOfMaxStudents, rowIndex);
            }
            else {
                String[] kdamCoursesString = fullCourseData[2].substring(1, fullCourseData[2].length() - 1).split(","); //ignore the [ ] of the string with substring.
                ArrayList<Short> kdamCourseLists = new ArrayList<>();
                for (int i = 0; i < kdamCoursesString.length; i++)
                    kdamCourseLists.add(Short.parseShort(kdamCoursesString[i]));
                int numOfMaxStudents = Integer.parseInt(fullCourseData[3]);
                currNewCourse = new Course(courseNum, courseName, kdamCourseLists, numOfMaxStudents, rowIndex);
            }
            coursesList.put(courseNum,currNewCourse);
        }



    //synchronized?
    /*
        public synchronized boolean isAdminRegistered(String userName){ // in the future we have to check if its better to seperate this method
            // to 2 methods of isStudentRegistered and isAdminRegistered to prevent damage of the  threads mutability.
            for(Admin currAdmin: adminsList){
                if(currAdmin.getUserName().equals(userName)) // if an admin with this username already exists in the system return true
                    return true;
            }
            return false;
        }
*/

    /* return true if he manage to add new admin
                 or false if there is a user with that username that is already registered */
        public boolean addNewAdmin(String userName, String password,BGRSMessageProtocol client) {
            synchronized (usersList) { // prevent 2 clients with the SAME NAME to register together
                if (!isUserExist(userName) && !isClientLoggedIn(client)) { // if we don't have a user with that userName
                    usersList.put(userName, new Admin(userName, password));
                    return true; // The synchronized aware of that return and will release the key.
                }
                return false; // there is already a registered user with that username.
            }
        }

        public boolean addNewStudent(String userName, String password,BGRSMessageProtocol client) { //synchronized?
             /* return true if he manage to add new admin
                    or false if there is a user with that username that is already registered */
            synchronized (usersList) { // prevent 2 clients with the same name to register together
                if (!isUserExist(userName) && !isClientLoggedIn(client)) { // if we don't have a user with that userName
                    usersList.put(userName, new Student(userName, password));
                    return true; // The synchronized aware of that return and will release the key.
                }
                return false; // there is already a registered user with that username.
            }
        }



         public boolean loginToTheSystem(String userName,String password, BGRSMessageProtocol client) { //this method is called by the protocol assuming isLoggedIn return false to the protocol
             synchronized (clientsLoggedIn) { // To prevent 2 clients to login in the same time, or one of them will logout while the other login.
                 //if the user exists in the system and the password is correct
                 if(!isUserExist(userName) || isClientLoggedIn(client) || isUserLoggedIn(userName))
                     return false;
                 if (((usersList.get(userName)).getPassword()).equals(password)){
                     clientsLoggedIn.put(client,userName);
                     usersList.get(userName).logIn();
                     return true;
                 }
                 return false; // The client didn't manage to login successfully for some reason.
             }
         }

         public void logoutFromTheSystem(BGRSMessageProtocol client){
            synchronized (clientsLoggedIn) {
                String userToDisconnect = clientsLoggedIn.get(client); // gets the username of the user the client is logged in to.
                usersList.get(userToDisconnect).logOut(); //logging out from the system
                clientsLoggedIn.remove(client); // removes this client from the logged in clients list.
            }
         }

         //methods that check login and courses list

         public boolean isClientLoggedIn(BGRSMessageProtocol client){
                if (clientsLoggedIn.containsKey(client))
                    return true;
                else
                    return false;
         }

         public boolean isUserLoggedIn(String userName){
            synchronized (clientsLoggedIn) {
                if (usersList.get(userName).isLoggedIn())
                    return true;
                else
                    return false;
            }
         }

         public boolean isUserExist(String userName){
            synchronized (usersList) {
                return usersList.containsKey(userName);
            }
         }

        public boolean isAdmin(BGRSMessageProtocol client){ //We assume we call this method after we check if the client logged-in. so if he loggedin the User must exist.
            String currUserName = clientsLoggedIn.get(client);
            User currUser = usersList.get(currUserName);
            return (currUser.getClass()).equals(Admin.class);
        }

        public boolean isAdmin(String userName){ //We assume we call this method after we check if the a user with that userName exist. isUserExist()..
                User currUser = usersList.get(userName);
                return (currUser.getClass().equals(Admin.class));
        }


    /*COURSE METHODS*/
    public boolean registerToNewCourse(short courseNum,BGRSMessageProtocol client){ // the method isAdmin should be called before this method to check if the client is a student.
            if(!isCourseExist(courseNum) || !clientsLoggedIn.containsKey(client) || isAdmin(client)) // return false if The course doesn't exist/the client isn't a student/the client isn't logged it
                return false;
            synchronized (registerLock){
            Course currCourse = coursesList.get(courseNum);
            ArrayList<Short> currKdamCoursesRequired = currCourse.getMyKdamCoursesList();
            String currStudentName = clientsLoggedIn.get(client);
            Student currStudent = (Student)(usersList.get(currStudentName));
            ArrayList<Short> currStudentRegisteredCourses = currStudent.getMyRegisteredCourses();
            if(isRegisteredToCourse(client,courseNum)) //if the student is already registered to that course.
                return false;
            for (Short shortItem: currKdamCoursesRequired) {
                if (!currStudentRegisteredCourses.contains(Short.valueOf(shortItem)))
                    return false; //if one of the required kdam courses of that course is missing in the student's registered courses.
            }
            //if the student got all the kdam courses required for that course then he can register
            currCourse.registerToMyCourse(currStudent);
            currStudent.registerToNewCourse(courseNum);
            return true;
        }
    }

      public boolean isCourseExist(short courseNum){
          if(coursesList.containsKey(courseNum))
             return true;
          else
              return false;
     }

    public ArrayList<Short> getKdamCourses(short courseNum){ //This method should be called only after isCourseExist(short courseNum).
            return coursesList.get(courseNum).getMyKdamCoursesList(); // we already sorted the kdamCourses list of each course to the order of the file.
        }


        public String getCourseStatString(short courseNum){ //we have to implement this method here because its thread safe (with synchronized)
            synchronized (coursesList) {
                Course currCourse = coursesList.get(courseNum);
                ArrayList<User> currRegisteredStudents = currCourse.getMyRegisteredStudents();
                String fullStrRegistered;
                if (currRegisteredStudents.size() == 0)
                    fullStrRegistered = "[]";
                else {
                    String[] currRegisteredStrArray = new String[currRegisteredStudents.size()];
                    for (int i = 0; i < currRegisteredStrArray.length; i++) {
                        currRegisteredStrArray[i] = currRegisteredStudents.get(i).getUserName();
                    }
                    Arrays.sort(currRegisteredStrArray); // sort to alphabetical order of names.
                    fullStrRegistered = "[" + String.join(",", currRegisteredStrArray) + "]";
                }
                int currFreeSeats = currCourse.getFreeSeatsNum();
                int maxSeats = currCourse.getMyNumOfMaxStudents();

                String fullStrAvailableSeats = String.valueOf(currFreeSeats) + "/" + String.valueOf(maxSeats);

                return "Course: (" + courseNum + ") " + currCourse.getMyCourseName() + "\nSeats Available: " + fullStrAvailableSeats +
                        "\nStudents Registered: " + fullStrRegistered;
            }
        }

    /*COURSE METHODS*/

    public String getStudentStatString(String userName){
        Student currStudent = (Student)usersList.get(userName); //we assume we check before calling this method if that userName is a student on the system.
        ArrayList<Short> currRegisteredCourses = currStudent.getMyRegisteredCourses();
        String strRegisteredCourses;
        if(currRegisteredCourses.size()==0)
            strRegisteredCourses = "";
        else {
            Collections.sort(currRegisteredCourses, new Comparator<Short>() { //sort
                @Override
                public int compare(Short o1, Short o2) {
                    return coursesList.get(o1).getMyRowInCoursesFile() - coursesList.get(o2).getMyRowInCoursesFile();
                }
            });
            synchronized (registerLock) {
                String[] currRegisteredStrArray = new String[currRegisteredCourses.size()];
                for (int i = 0; i < currRegisteredStrArray.length; i++)
                    currRegisteredStrArray[i] = String.valueOf(currRegisteredCourses.get(i));
                strRegisteredCourses = String.join(",", currRegisteredStrArray);
            }
        }
        return "Student: " + userName + "\nCourses: [" + strRegisteredCourses + "]";
    }

    public boolean isRegisteredToCourse(BGRSMessageProtocol client,short courseNum){
        synchronized (registerLock) {
            String clientUserName = clientsLoggedIn.get(client); // we assume we checked before if the client is logged-in as an student
            Student currStudent = ((Student) usersList.get(clientUserName));
            ArrayList<Short> currRegisteredCourses = ((Student) usersList.get(clientUserName)).getMyRegisteredCourses();
            Course curCourse = coursesList.get(courseNum);
            ArrayList<User> registeredToCurrCourse = curCourse.getMyRegisteredStudents();

            return currRegisteredCourses.contains(courseNum) || registeredToCurrCourse.contains(currStudent);
        }
    }

    public boolean unRegisterToCourse(BGRSMessageProtocol client,short courseNum) {
        synchronized (registerLock) {
            if (!isRegisteredToCourse(client, courseNum)) //if the user is unregistered to that course he cant unRegister again so return false
                return false;

            String clientUserName = clientsLoggedIn.get(client); // we assume we checked before if the client is logged-in as an student
	Student currStudent = (Student)usersList.get(clientUserName);
            currStudent.unRegisterToCourse(courseNum);
		Course currCourse = coursesList.get(courseNum);
		currCourse.removeAstudentFromMyCourse(currStudent);
            return true;
        }
    }

    public String getMyCoursesStr(BGRSMessageProtocol client){
        String clientUserName = clientsLoggedIn.get(client); // we assume we checked before if the client is logged-in as an student
        ArrayList<Short> currRegisteredCourses = ((Student)usersList.get(clientUserName)).getMyRegisteredCourses();
        Collections.sort(currRegisteredCourses, new Comparator<Short>() { //sort
            @Override
            public int compare(Short o1, Short o2) {
                return coursesList.get(o1).getMyRowInCoursesFile()-coursesList.get(o2).getMyRowInCoursesFile();
            }
        });
        String[] registeredCoursesStr = new String[currRegisteredCourses.size()];
        for(int i=0;i< registeredCoursesStr.length;i++){
            registeredCoursesStr[i] = String.valueOf(currRegisteredCourses.get(i));  //not have to be sorted
        }
        return "[" + String.join(",",registeredCoursesStr) + "]";
    }


        }





