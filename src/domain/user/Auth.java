package domain.user;

import domain.Company;

/**
 * This class handles the authorization of user and keeps track of the logged
 * in user.
 * 
 * @author Mathias, Frederic, Pieter-Jan
 */
public class Auth {

    
    
    private User user;
    private final Company company;
    
    /**
     * Initializes this authorization class with the given database
     * 
     * @param company The company to use to retrieve the users
     */
    public Auth(Company company){
        this.company = company;
    }
    
    /**
     * Attempts to login a user with the given username
     * 
     * @param username The username of the user to login
     * @return True if and only if the login succeeded.
     */
    public boolean login(String username){
        for(User user : company.getUsers()){
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
