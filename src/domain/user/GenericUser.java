package domain.user;

import domain.BranchOffice;

/**
 * This class represents a developer in the system
 * 
 * @author Mathias, Frederic, Pieter-Jan
 */
public class GenericUser implements User {
    private String name;
    private final String role;
    private final BranchOffice branchoffice;

    /**
     * Initialize a new user with the given name and role
     * 
     * @param name The name of the manager
     * @param role The role of the user
     * @param branchOffice The office this user belongs to
     */
    public GenericUser(String name, String role, BranchOffice branchOffice) {
        this.name = name;
        this.role = role;
       this.branchoffice = branchOffice;
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
    
    
     /**
     * 
     * @return The branchoffice this user belongs to
     */
    @Override
    public BranchOffice getBranchOffice(){
        return branchoffice;
        
    }
}
