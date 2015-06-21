package controller;

import domain.BranchOffice;
import domain.Company;
import domain.dto.DetailedBranchOffice;
import domain.dto.DetailedTask;
import domain.task.Task;
import domain.user.Acl;
import domain.user.Auth;
import java.util.ArrayList;
import java.util.List;

/**
 * This handler, handles the delegate task use case
 *
 * @author Frederic, Mathias, Pieter-Jan
 */
public class DelegateTaskHandler extends Handler {

    protected final BranchOffice office;
    private final Company company;
    
    /**
     * Initialize a new create task handler with the given projectContainer.
     * @param company The company to use in this handler
     * @param office The branch office to use in this handler.
     * @param auth The authorization manager to use
     * @param acl The action control list to use
     */
    public DelegateTaskHandler(Company company, BranchOffice office, Auth auth, Acl acl) {
        super(auth, acl);
        this.office = office;
        this.company = company;
       
    }

	/**
	 * @return All unplanned tasks which are assigned to this branch office.
	 */
	public List<DetailedTask> getUnplannedAssignedTasks() {
		return new ArrayList<>(office.getAssignedUnplannedTasks());
	}
        
    
    /**
     * 
     * @return All the branch offices in the system
     */
	public List<DetailedBranchOffice> getBranchOffices() {
		List<DetailedBranchOffice> otherOffices = new ArrayList<>(company.getOffices());
		return otherOffices;
	}
        
    /**
     * Delegates the task with the given id to the given branch office
     * 
     * @param pId The project id of the task
     * @param tId The id of the task to delegate
     * @param officeId The id of the office to delegate to
     */
    public void delegateTask(int pId, int tId, int officeId) {
        for(Task task : office.getUnplannedTasks())
    		if(task.getId() == tId)
    			office.delegateTaskTo(task, company.getOffices().get(officeId));
    }
}
