package controller;

import domain.Resource;
import domain.time.WorkWeekConfiguration;
import domain.user.Auth;
import exception.NoAccessException;
import java.awt.HeadlessException;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

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
    * Set the start of the one hour lunchbreak for the currently loggedin developer.
    * 
    * @param begintime The start time of the lunchbreak.
    */
   public void setLunchbreak(String begintime){
       if (auth.loggedIn() && auth.getUser().getRole().equals("developer")) {

            if (begintime != null) {
                try {

                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
                    LocalTime start = LocalTime.parse(begintime, formatter);
                    LocalTime end = start.plusHours(1);
                    ((Resource) auth.getUser()).setAvailability(new WorkWeekConfiguration(LocalTime.of(8, 00), LocalTime.of(17, 00), start, end));
                } catch (HeadlessException | IllegalArgumentException exception) {
                    throw new IllegalArgumentException("This lunchbreak is not allowed.");
                }
            }

        }
   }
   
   
   public boolean askLunchbreak(){
       return auth.loggedIn() && auth.getUser().getRole().equals("developer");
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
