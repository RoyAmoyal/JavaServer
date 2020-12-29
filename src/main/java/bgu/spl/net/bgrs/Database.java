package bgu.spl.net.bgrs;

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
        private ConcurrentHashMap<String,String> adminsList = new ConcurrentHashMap<>();
        private ConcurrentHashMap<String,String> studentsList = new ConcurrentHashMap<>();


    //to prevent user from creating new Database
        private Database() {
            // TODO: implement
        }

        /**
         * Retrieves the single instance of this class.
         */
        public static Database getInstance() {
            return singleton;
        }

        /**
         * loades the courses from the file path specified
         * into the Database, returns true if successful.
         */

        public boolean isUserRegistered(String userName){ // in the future we have to check if its better to seperate this method
            // to 2 methods of isStudentRegistered and isAdminRegistered to prevent damage of the  threads mutability.
            if(adminsList.containsKey(userName) || studentsList.containsKey(userName)) // DONT FORGET TO CHECK ABOUT THE CONTAINS
                return true;
            else
                return false;
        }

        public void addNewAdmin(String userName, String password){
        adminsList.put(userName,password);

        }
        public void addNewStudent(String userName, String password){
        studentsList.put(userName,password);
    }


        boolean initialize(String coursesFilePath) {
          // READ FROM THE JSON AND PUT THE COURSES LIST ON OUR LIST FUN FUN FUN
            return false;
        }


    }

