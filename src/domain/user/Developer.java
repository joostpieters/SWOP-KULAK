package domain.user;

import domain.BranchOffice;
import domain.Resource;
import domain.ResourceType;
import domain.time.Clock;

/**
 * This class represents a developer in the system
 * 
 * @author Mathias, Frederic, Pieter-Jan
 */
public class Developer extends Resource implements User {
    
    private String name;
    private final String role;
    private final BranchOffice branchoffice;
    
    /**
     * Initializes a new developer with the given name and role
     * 
     * @param name The name of the developer
     * @param clock The clock this developer should observe 
     * @param branchOffice The office this developer belongs to
     */
    public Developer(String name, Clock clock, BranchOffice branchOffice) {
        super(name, ResourceType.DEVELOPER);
        this.name = name;
        this.role = "developer";
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
