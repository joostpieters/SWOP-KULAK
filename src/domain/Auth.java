package domain;

import java.util.HashMap;
import java.util.Map;

/**
 * This class handles the authorization of user and keeps track of the logged
 * in user.
 * 
 * @author Mathias, Frederic, Pieter-Jan
 */
public class Auth {

    
    
    private User user;
    private static Map<String, User> users;
    
    public Auth(){
       users = new HashMap<>();
    }
    
    /**
     * Attempts to login a user with the given username
     * 
     * @param username The username of the user to login
     * @return True if and only if the login succeeded.
     */
    public boolean login(String username){
        if(!users.containsKey(username)){
            return false;
        }
        user = users.get(username);
        return true;
    }
    
    /**
     * 
     * @return The currently logged in user 
     */
    public User getUser(){
        return user;
    }
    
    /**
     * Checks whether a user is logged in
     * 
     * @return True if and only if a user is currently logged in.
     */
    public boolean loggedIn(){
        return user != null;
    }
    
    /**
     * Registers the given user to the list of users
     * 
     * @param user The user to register
     */
    public static void registerUser(User user) {
        users.put(user.getName(), user);
    }
}
