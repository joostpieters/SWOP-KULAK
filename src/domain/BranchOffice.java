package domain;

import domain.dto.DetailedBranchOffice;
import domain.task.Task;
import domain.user.User;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * This class represents a branch office with a location, project container, resource container and a list of users.
 *
 * @author Frederic, Mathias, Pieter-Jan
 */
public class BranchOffice implements DetailedBranchOffice {

    private final String location;
	private final ProjectContainer projectContainer;
	private final ResourceContainer resourceContainer;
	private final List<Task> delegatedTasks;
    private final List<User> users;
	
    /**
    * Initializes this branchoffice with the given location
    * 
    * @param location The location where this branch office is situated
    */
    public BranchOffice(String location) {
    	this(location,  new ProjectContainer(), new ResourceContainer());
	}
	
    /**
     * Initializes this branch office with the given location, project container and resource container.
     * 
     * @param location The location where this branch office is situated.
     * @param pc The project container of this branch office.
     * @param rc The resource container of this branch office.
     */
	public BranchOffice(String location, ProjectContainer pc, ResourceContainer rc) {
		this.location = location;
		this.projectContainer = pc;
		this.resourceContainer = rc;
		this.delegatedTasks = new ArrayList<>();
		this.users = new ArrayList<>();
	}
	
	/**
	 * Delegates the given task from this branch office to the given branch office.
	 * 
	 * @param task The task to delegate to the given branch office.
	 * @param branchOffice The branch office to which the given task should be delegated to.
	 * 
	 * @throws IllegalArgumentException If the given task is not assigned to this branch office.
	 * @throws IllegalStateException If the given task has already been planned.
	 */
	public void delegateTaskTo(Task task, BranchOffice branchOffice) throws IllegalArgumentException
	{
		if(!taskIsAssigned(task))
			throw new IllegalArgumentException("An attempt has been made to delegate a"
					+ "task from a branch office to which the task is not assigned to.");
		if(task.hasPlanning())
			throw new IllegalStateException("An attempt has been made to delegate a task"
					+ " which has already been planned.");
		
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
			task.setNotDelegated();
		else // only add the task as a delegated task if it really is delegated and didn't originally belong to this office
		{
			this.delegatedTasks.add(task);
			task.setDelegatedBranchOffice(this);
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
		if(!task.isDelegated() && projectContainer.containsTask(task))
			return true;
		return false;
	}
	
	/**
	 * Checks whether this branch office contains the given ask as a task
	 * which was delegated to this branch office.
	 * 
	 * @param task The task to check.
	 * 
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
		return projectContainer;
	}

	/**
	 * @return the resource container
	 */
	public ResourceContainer getResourceContainer() {
		return resourceContainer;
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
        List<Task> unplannedTasks = projectContainer.getUnplannedTasks();
        unplannedTasks.addAll(getDelegatedUnplannedTasks());
        return unplannedTasks;
    }
    
    /**
     * @return The list of unplanned tasks which are assigned to this branch office.
     * @see taskIsAssigned
     */
    public List<Task> getAssignedUnplannedTasks()
    {
    	List<Task> assignedUnplannedTasks = new ArrayList<>();
    	for(Task task : projectContainer.getUnplannedTasks())
    		if(taskIsAssigned(task))
    			assignedUnplannedTasks.add(task);
    	return assignedUnplannedTasks;
    }
    
    /**
     * @return The list of tasks assigned to this branch office.
     */
    public List<Task> getAssignedTasks()
    {
    	Set<Task> assignedTasks = new HashSet<>();
    	for(Task task : projectContainer.getAllTasks())
    		if(taskIsAssigned(task))
    			assignedTasks.add(task);
    	assignedTasks.addAll(delegatedTasks);
    	return new ArrayList<>(assignedTasks);
    }
    
    /**
     * @return All unplanned tasks in the list of delegated tasks of this branch office.
     */
    private List<Task> getDelegatedUnplannedTasks() {
        List<Task> tasks = new ArrayList<>();
        
        for(Task task : delegatedTasks)
            if(!task.hasPlanning())
                tasks.add(task);
        
        return tasks;
    }

}
