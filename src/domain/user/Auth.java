package domain.user;

import domain.Database;

/**
 * This class handles the authorization of user and keeps track of the logged
 * in user.
 * 
 * @author Mathias, Frederic, Pieter-Jan
 */
public class Auth {

    
    
    private User user;
    private final Database db;
    
    /**
     * Initializes this authorization class with the given database
     * 
     * @param db The database to use to retrieve the users
     */
    public Auth(Database db){
        this.db = db;
    }
    
    /**
     * Attempts to login a user with the given username
     * 
     * @param username The username of the user to login
     * @return True if and only if the login succeeded.
     */
    public boolean login(String username){
        for(User user : db.getUsers()){
            if(user.getName().equalsIgnoreCase(username)){
                this.user = user;
                return true;
            }
                
        }
        
        return false;
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
     * Logs out the currently logged in user.
     */
    public void logout(){
        user = null;
    }
}
