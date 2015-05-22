package domain.user;

import domain.BranchOffice;

/**
 *  This interface represents a user of the system
 * 
 *  @author Mathias, Frederic, Pieter-Jan
 */
public interface User {
      
    /**
     * 
     * @return The name of this user 
     */
    public String getName();
    
    /**
     * 
     * @return The role of this user 
     */
    public Role getRole();
    
     /**
     * 
     * @return The branchoffice this user belongs to
     */
    public BranchOffice getBranchOffice();

}
