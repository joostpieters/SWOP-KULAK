package domain;

/**
 *  This class represents a user of the system
 * 
 *  @author Mathias, Frederic, Pieter-Jan
 */
public class User {
    private String name;
    private String role;

    /**
     * Initialize a new user with the given name and role
     * 
     * @param name The name of the user
     * @param role The role of the user
     */    
    public User(String name, String role){
        this.name = name;
        this.role = role;
    }
    
    /**
     * 
     * @return The name of this user 
     */
    public String getName() {
        return name;
    }
    
    /**
     * Sets the name of this user
     * 
     * @param name The name to set 
     */
    public void setName(String name) {
        this.name = name;
    }
    
    /**
     * 
     * @return The role of this user 
     */
    public String getRole() {
        return role;
    }
    
    /**
     * Sets the role of this user
     * 
     * @param role The role to set.
     */
    public void setRole(String role) {
        this.role = role;
    }
    
}
