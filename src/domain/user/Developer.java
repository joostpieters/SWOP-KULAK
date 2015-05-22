package domain.user;

import domain.BranchOffice;
import domain.Resource;
import domain.ResourceType;

/**
 * This class represents a developer in the system
 * 
 * @author Mathias, Frederic, Pieter-Jan
 */
public class Developer extends Resource implements User {
    
    private final Role role;
    private final BranchOffice branchoffice;
    
    /**
     * Initializes a new developer with the given name for a given office
     * 
     * @param name The name of the developer
     * @param branchOffice The office this developer belongs to
     */
    public Developer(String name, BranchOffice branchOffice) {
        super(name, ResourceType.DEVELOPER);
        this.role = Role.DEVELOPER;
        this.branchoffice = branchOffice;
    }
    
    /**
     * 
     * @return The role of this user 
     */
     @Override
    public Role getRole() {
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
