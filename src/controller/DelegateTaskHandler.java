package controller;

import domain.BranchOffice;
import domain.Database;
import domain.command.SimulatorCommand;
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

    protected final BranchOffice manager;
    private final Database db;
    
	private SimulatorCommand simulatorCommand;

    /**
     * Initialize a new create task handler with the given projectContainer.
     *
     * @param manager The projectContainer to use in this handler.
     * @param auth The authorization manager to use
     * @param acl The action control list to use
     * @param db The database to use in this handler
     */
    public DelegateTaskHandler(BranchOffice manager, Auth auth, Acl acl, Database db) {
    	this(manager, auth, acl, db, new SimulatorCommand());
    }
    
    /**
     * Initialize a new create task handler with the given projectContainer.
     *
     * @param manager The projectContainer to use in this handler.
     * @param auth The authorization manager to use
     * @param acl The action control list to use
     * @param db The database to use in this handler
     * @param simulatorCommand The simulator command to which commands are added.
     */
    public DelegateTaskHandler(BranchOffice manager, Auth auth, Acl acl, Database db, SimulatorCommand simulatorCommand)
    {
        super(auth, acl);
        this.manager = manager;
        this.db = db;
        this.simulatorCommand = simulatorCommand;
    }
    /**
     * 
     * @return All unplanned tasks in this branchoffice 
     */
	public List<DetailedTask> getUnplannedTasks() { // TODO mss zal dit later onnodige methode zijn
		return new ArrayList<>(manager.getUnplannedTasks());
	}
	
	/**
	 * @return All unplanned tasks which are assigned to this branch office.
	 */
	public List<DetailedTask> getUnplannedAssignedTasks()
	{
		return new ArrayList<>(manager.getAssignedUnplannedTasks());
	}
        
    
    /**
     * 
     * @return All the other branch offices in the system
     */
	public List<DetailedBranchOffice> getOtherBranchOffices() {
		List<DetailedBranchOffice> otherOffices = new ArrayList<>(db.getOffices());
		otherOffices.remove(manager);
		return otherOffices;
	}
        
        /**
         * Delegates the task with the given id to the given branchoffice
         * 
         * @param pId The project id of the task
         * @param tId The id of the task to delegate
         * @param officeId The id of the office to delegate to
         */
        public void delegateTask(int pId, int tId, int officeId){
            Task task = manager.getProjectContainer().getProject(pId).getTask(tId);
            manager.delegateTaskTo(task, db.getOffices().get(officeId));
        }
    
}
