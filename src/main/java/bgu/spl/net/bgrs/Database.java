package bgu.spl.net.bgrs;


import bgu.spl.net.bgrs.users.User;
import bgu.spl.net.bgrs.users.Admin;
import bgu.spl.net.bgrs.users.Student;
import com.google.gson.*;

import java.util.HashMap;
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
        private final ConcurrentLinkedQueue<String> coursesList = new ConcurrentLinkedQueue<String>();
        private ConcurrentHashMap<String,User> usersList = new ConcurrentHashMap<>();
        private final Object registerLock = new Object();
        private final Object logInOutLock = new Object();
        /*
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
        Gson g = new Gson();// we need to understand how to read from the gson
        return false;
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
            synchronized (registerLock) { // prevent 2 clients with the same name to register together
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

         public User loginToTheSystem(String userName,String password) {
             synchronized (logInOutLock) { // To prevent 2 clients to login in the same time, or one of them will logout while the other login.
                 //Checks if The user exists in the system and the password is correct and he doesn't already logged in.
                 if (usersList.containsKey(userName) && usersList.get(userName).getPassword().equals(password)
                         && !usersList.get(userName).isLoggedIn()){
                     usersList.get(userName).logIn();
                     return usersList.get(userName); // The client manage to login successfully with that username and password
                 }
                 return null; // The client didn't manage to login successfully for some reason.
             }
         }

        public boolean logoutFromTheSystem(User user){
            if(usersList.get(user.getUserName()).isLoggedIn()){ //if the user is logged in then you can logout
                usersList.get(user.getUserName()).logOut();
                return true;
            }
            return false;
        }


        }


