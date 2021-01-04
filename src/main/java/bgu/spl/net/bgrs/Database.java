package bgu.spl.net.bgrs;


import bgu.spl.net.bgrs.users.Course;
import bgu.spl.net.bgrs.users.User;
import bgu.spl.net.bgrs.users.Admin;
import bgu.spl.net.bgrs.users.Student;
import com.google.gson.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Scanner;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

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
        private final ConcurrentHashMap<String,User> usersList = new ConcurrentHashMap<>(); // CHECK IF BETTER TO SPLIT THE ADMINS AND THE STUDENTS SYNCHRONIZED
        private final ConcurrentHashMap<BGRSMessageProtocol,String> clientsLoggedIn = new ConcurrentHashMap<>();
        private final Object registerLock = new Object();
        private final Object logInOutLock = new Object();

        /*admin3.getclass();
        MAYBE WE NEED TO ADD A LIST OF LOGGED IN BECAUSE IF 2 CLIENTS ARE TRYING TO LOG0
         */


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
            int indexRow = 1;
            while (myReader.hasNextLine()) {
                String data = myReader.nextLine();
                loadNewCourse(indexRow, data);
                indexRow++;
            }
        }catch(FileNotFoundException e){
                e.printStackTrace();
            }
        return true;
        }

        private void loadNewCourse(int index, String data){
            String[] fullCourseData = data.split("|");
            short courseNum = Short.parseShort(fullCourseData[0]);
            String courseName = fullCourseData[1];

          //  while()
         //   coursesList.add(new Course(),);
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
        public boolean addNewAdmin(String userName, String password) {
            synchronized (registerLock) { // prevent 2 clients with the SAME NAME to register together
                if (!usersList.containsKey(userName)) { // if we don't have a user with that userName
                    usersList.put(userName, new Admin(userName, password));
                    return true; // The synchronized aware of that return and will release the key.
                }
                return false; // there is already a registered user with that username.
            }
        }

        public boolean addNewStudent(String userName, String password) { //synchronized?
             /* return true if he manage to add new admin
                    or false if there is a user with that username that is already registered */
            synchronized (registerLock) { // prevent 2 clients with the same name to register together
                if (!usersList.containsKey(userName)) { // if we don't have a user with that userName
                    usersList.put(userName, new Student(userName, password));
                    return true; // The synchronized aware of that return and will release the key.
                }
                return false; // there is already a registered user with that username.
            }
        }


         public boolean loginToTheSystem(String userName,String password, BGRSMessageProtocol client) { //this method is called by the protocol assuming isLoggedIn return false to the protocol
             synchronized (logInOutLock) { // To prevent 2 clients to login in the same time, or one of them will logout while the other login.
                 //if the user exists in the system and the password is correct
                 if (usersList.containsKey(userName) && usersList.get(userName).getPassword().equals(password)){
                     clientsLoggedIn.put(client,userName);
                     usersList.get(userName).logIn();
                     return true;
                 }
                 return false; // The client didn't manage to login successfully for some reason.
             }
         }

         public void logoutFromTheSystem(BGRSMessageProtocol client){
            String userToDisconnect = clientsLoggedIn.get(client); // gets the username of the user the client is logged in to.
             usersList.get(userToDisconnect).logOut(); //logging out from the system
             clientsLoggedIn.remove(client); // removes this client from the logged in clients list.
         }

         //methods that check login and courses list

         public boolean isClientLoggedIn(BGRSMessageProtocol client){
            if(clientsLoggedIn.containsKey(client))
                return true;
            else
                return false;
         }

         public boolean isUserLoggedIn(String userName){
             if(usersList.get(userName).isLoggedIn())
                 return true;
            else
                return false;
         }

        public boolean isCourseExist(short courseNum){
            if(coursesList.contains(courseNum))
                return true;
            else
                return false;
        }




        }





