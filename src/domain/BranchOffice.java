package domain;

import domain.dto.DetailedBranchOffice;
import domain.task.Task;
import domain.user.User;
import java.util.ArrayList;
import java.util.List;

/**
 * This class represents a manager to contain the projects in the system.
 *
 * @author Frederic, Mathias, Pieter-Jan
 */
public class BranchOffice implements DetailedBranchOffice {
	
	private final ProjectContainer projects;
	private final ResourceContainer resources;
	private final List<Task> delegatedTasks;
	
    private String location;
    private List<User> users;
	
        /**
         * Initializes this branchoffice with the given location
         * 
         * @param location The location where this branch office is situated
         */
        public BranchOffice(String location) {
            this();
            this.location = location;            
	}
        
		
	public BranchOffice() {
		this(new ProjectContainer(), new ResourceContainer());
	}
	
	public BranchOffice(ProjectContainer pc, ResourceContainer rc) {
		projects = pc;
		resources = rc;
		delegatedTasks = new ArrayList<>();
		users = new ArrayList<>();
	}
	
	/**
	 * Delegates the given task from this branch office to the given branch office.
	 * @param task The task to delegate to the given branch office.
	 * @param branchOffice The branch office to which the given task should be delegated to.
	 * @throws IllegalArgumentException If the given task is not assigned to this branch office.
	 */
	public void delegateTaskTo(Task task, BranchOffice branchOffice) throws IllegalArgumentException
	{
		if(!taskIsAssigned(task))
			throw new IllegalArgumentException("An attempt has been made to delegate a"
					+ "task from a branch office to which the task is not assigned to.");
		if(containsDelegatedTask(task))
			removeDelegatedTask(task);
		branchOffice.addDelegatedTask(task);
	}
	
	/**
	 * Adds the given task to this branch offices list of delegated tasks and updates 
	 * whether or not the task is delegated.
	 * 
	 * @param task The task to add to this branch offices list of delegated tasks.
	 */
	private void addDelegatedTask(Task task)
	{
		// if this task originally belonged to this branch office
		// we simply set isDelegated to false
		if(getProjectContainer().containsTask(task)) 
			task.setIsDelegated(false);
		else // only add the task as a delegated task if it really is delegated and didn't originally belong to this office
		{
			this.delegatedTasks.add(task);
			task.setIsDelegated(true);
		}
	}
	
	/**
	 * Removes the given task from the list of delegated tasks of this branch office.
	 * 
	 * @param task The delegated task to remove from this branch office.
	 * @throws IllegalArgumentException
	 *         If this branch office does not contain the given task in its list of delegated tasks.
	 * @post This branch office does not contain the given task as one of its delegated tasks.
	 *       | !containsDelegatedTask(task)
	 */
	private void removeDelegatedTask(Task task) throws IllegalArgumentException
	{
		if(!containsDelegatedTask(task))
			throw new IllegalArgumentException(
					"An attempt has been made to remove a delegated task from a branch office"
					+ " which does not contain the given task as one of its delegated tasks.");
		this.delegatedTasks.remove(task);
	}
	
	/**
	 * Checks whether the given task is assigned to this branch office.
	 * @param task The task to check.
	 * @return True if the given task is in the list of delegated tasks of this branch office.
	 *         True if the given task is not delegated and the project container of this branch
	 *                 office contains the given task.
	 *         False otherwise.
	 *         
	 */
	public boolean taskIsAssigned(Task task)
	{
		if(containsDelegatedTask(task))
			return true;
		if(!task.isDelegated() && projects.containsTask(task))
			return true;
		return false;
	}
	
	/**
	 * Checks whether this branch office contains the given ask as a task
	 * which was delegated to this branch office.
	 * 
	 * @param task The task to check.
	 * @return True if and only if the list of delegated tasks belonging to this
	 *         branch office contains the given task.
	 */
	private boolean containsDelegatedTask(Task task)
	{
		return delegatedTasks.contains(task);
	}
	
	/**
	 * @return the project container
	 */
	public ProjectContainer getProjectContainer() {
		return projects;
	}

	/**
	 * @return the resource container
	 */
	public ResourceContainer getResourceContainer() {
		return resources;
	}

	
        /**
         * 
         * @return The location of this branch office 
         */
    @Override
    public String getLocation() {
        return location;
    }
    
    /**
     * 
     * @return The users working in this branchoffice
     */
    public List<User> getUsers(){
        return new ArrayList<>(users);
    }
    
    /**
     * Adds a user to this branchoffice
     * 
     * @param user The user to add
     */
    public void addUser(User user){
        users.add(user);
    }
    
    /**
     * 
     * @return All unplanned tasks in this branchoffice, including the delegated
     * tasks.
     */
    public List<Task> getUnplannedTasks(){
        List<Task> unplannedTasks = projects.getUnplannedTasks();
        unplannedTasks.addAll(getDelegatedUnplannedTasks());
        return unplannedTasks;
    }
    
    /**
     * 
     * @return All unplanned tasks in the list of delegated tasks 
     */
    private List<Task> getDelegatedUnplannedTasks() {
        List<Task> tasks = new ArrayList<>();
        for(Task task : delegatedTasks){
            if(!task.isPlanned()){
                tasks.add(task);
            }
        }
        return tasks;
    }

}
