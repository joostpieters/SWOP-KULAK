package domain.user;

import domain.BranchOffice;

/**
 * This class represents a developer in the system
 * 
 * @author Mathias, Frederic, Pieter-Jan
 */
public class GenericUser implements User {
    private String name;
    private final Role role;
    private final BranchOffice branchoffice;

    /**
     * Initialize a new user with the given name and role
     * 
     * @param name The name of the manager
     * @param role The role of the user
     * @param branchOffice The office this user belongs to
     */
    public GenericUser(String name, Role role, BranchOffice branchOffice) {
    	//TODO: kijk eens welke mooie illustratie van hoe vervelend het kan zijn dat developer = resource...
    	//if(role.equals(null) || role.equals(Role.DEVELOPER))
    	//	throw new IllegalArgumentException("The given role is invalid.");
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
