package domain;

/**
 * This class represents a developer in the system
 * 
 * @author Mathias, Frederic, Pieter-Jan
 */
public class GenericUser implements User {
    private String name;
    private String role;

    /**
     * Initialize a new manager with the given name
     * 
     * @param name The name of the manager
     */
    public GenericUser(String name, String role) {
        this.name = name;
        this.role = role;
       
    }
    
    /**
     * 
     * @return The name of this user 
     */
     @Override
    public String getName() {
        return name;
    }
    
    /**
     * Sets the name of this user
     * 
     * @param name The name to set 
     */
     @Override
    public void setName(String name) {
        this.name = name;
    }
    
    /**
     * 
     * @return The role of this user 
     */
     @Override
    public String getRole() {
        return role;
    }  
}
