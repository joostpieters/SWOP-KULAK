package controller;

import domain.user.Auth;
import exception.NoAccessException;

/**
 * This handler, handles the login use case
 *
 * @author Frederic, Mathias, Pieter-Jan
 */
public class LoginHandler{

    private final Auth auth;

    /**
     * Initialize a new create task handler with the given projectContainer.
     *
     * @param auth The authorization manager to use
     */
    public LoginHandler(Auth auth) {        
        this.auth = auth;
    }

    
    /**
     * Logs the user in with the given username
     * 
     * @param username The username of the user to login
     * @throws NoAccessException The login failed
     */
   public void login(String username) throws NoAccessException{
       if(!auth.login(username)){
           throw new NoAccessException("The login failed");
       }
   }
   
    /**
     * Logs out the currently loggedin user
     * 
     */
   public void logout(){
      auth.logout();
   }
   
   /**
     * 
     * 
     * @return True if and only if a user is logged in
     */
   public boolean loggedIn(){
      return auth.loggedIn();
   }

}
