package domain;

import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;


/**
 * This class represents a project
 * 
 * @author Frederic, Mathias, Pieter-Jan 
 */
public class Project implements DetailedProject {
	
	public static final int[] NO_DEPENDENCIES = new int[]{};
	public static final int NO_ALTERNATIVE = -1;
	
    private boolean isFinished;									//performance-variable
    
    private final int id;
    private final String name;
    private final String description;
    private final Timespan creationDueTime;
    private final SortedMap<Integer, Task> tasks = new TreeMap<>();
    
    /**
     * Construct a new project, with given id, name, description, 
     * creation time and due time.
     * 
     * @param 	id
     * 			The id for this project.
     * @param 	name
     * 			The name for this project.
     * @param 	descr
     * 			The description for this project.
     * @param 	creation
     * 			The creation time for this project.
     * @param 	due
     * 			The due time for this project.
     * @throws	IllegalArgumentException
     * 			if id < 0
     * 			or if name or descr = null
     * 			or if creation and due form an invalid time pair.
     */
    public Project(int id, String name, String descr, LocalDateTime creation, LocalDateTime due){
    	if(id < 0)
    		throw new IllegalArgumentException("id should be bigger than zero.");
    	if(name == null || descr == null)
    		throw new IllegalArgumentException("Both name and description are expected.");
    	
    	this.id = id;
    	this.name = name;
    	this.description = descr;
    	this.creationDueTime = new Timespan(creation, due);
    }
    
    /****************************************
     * Getters & setters					*
     ****************************************/
    
    /**
	 * @return 	the id of this project.
	 */
    @Override
	public int getId() {
		return id;
	}

	/**
	 * @return 	the name of this project.
	 */
    @Override
	public String getName() {
		return name;
	}

	/**
	 * @return 	the description of this project.
	 */
    @Override
	public String getDescription() {
		return description;
	}

	/**
	 * @return 	the creationTime of this project.
	 */
    @Override
	public LocalDateTime getCreationTime() {
		return creationDueTime.getStartTime();
	}

	/**
	 * @return 	the dueTime of this project.
	 */
    @Override
	public LocalDateTime getDueTime() {
		return creationDueTime.getEndTime();
	}

	/**
	 * 
	 * @return	a list of tasks contained in this project.
	 */
	public List<Task> getTasks(){
        return new LinkedList<Task>(tasks.values());
    }
	
	/**
	 * Get a task with given id contained in this project.
	 * 
	 * @param 	tid
	 * 			The id of the task to be returned.
	 * @return	The task with the given id if it is contained in this,
	 * 			null otherwise.
	 */
	public Task getTask(int tid) {
		Task t = tasks.get(tid);
		if(t == null)
			throw new IllegalArgumentException("Task with tid " + tid + " doesn't exist.");
		return t;
	}
    
    /****************************************
     * Task-management						*
     ****************************************/
	
	/**
	 * Add a task to the list of tasks for this project.
	 * 
	 * @param 	t
	 * 			The task to be added.
	 * @throws	IllegalStateException
	 * 			if this project is already finished.
	 * @throws	NullPointerException
	 * 			if t == null.
	 * @throws	IllegalArgumentException
	 * 			if t.getAlternativeTask() doesn't exist in this project
	 * 			or if there exists a task in t.getPrerequisiteTasks() 
	 * 			which doesn't exist in this project.
	 */
	private void addTask(Task t) {
		if(isFinished())
			throw new IllegalStateException("This project has already been finished.");
		if(t == null)
			throw new NullPointerException("You can't add null-tasks to a project.");
		if(t.getAlternativeTask() != null && !tasks.containsKey(t.getAlternativeTask().getId()))
			throw new IllegalArgumentException(
					"The task this task should be an alternative for, doesn't exist in this project.");
		
		for(Task x : t.getPrerequisiteTasks())
			if(!tasks.containsKey(x.getId()))
				throw new IllegalArgumentException(
						"One or more of the prerequisite tasks doesn't exist in this project.");
		
		this.tasks.put(t.getId(), t);
	}
    
	/**
	 * Create a new task and add it to this project.
	 * 
	 * @param 	descr
	 * 			The description for the new task.
	 * @param 	estDur
	 * 			The estimated duration for the new task in hours.
	 * @param 	accdev
	 * 			The acceptable deviation for the new task in %.
	 * @param 	altFor
	 * 			The id this task is an alternative for.
	 * @param 	prereq
	 * 			An array of id's of tasks that the new task will depend on.
	 * @throws	IllegalStateException
	 * 			if this project is already finished.
	 */
	public void createTask(String descr, int estDur, int accdev, int[] prereq, int altFor) {
		if(isFinished())
			throw new IllegalStateException("This project has already been finished.");
		
		Task[] arr = new Task[prereq.length];
		for(int i = 0; i < prereq.length; i++) {
			arr[i] = getTask(prereq[i]);
		}

		Task t = new Task(descr, estDur, accdev, arr);
		if(altFor != Project.NO_ALTERNATIVE)
			getTask(altFor).setAlternativeTask(t);
		addTask(t);
	}
	
	/**
	 * Return all tasks from this project which are available.
	 * 
	 * @return	all tasks t for which t.isAvailable() is true,
	 * 			an empty list if this project is already finished.
	 */
	public List<Task> getAvailableTasks() {
		List<Task> result = new LinkedList<Task>();
		for(Task t : tasks.values()) {
			if(t.isAvailable())
				result.add(t);
		}
		return result;
	}

	/**
	 * Check whether this project is finished or not.
	 * 
	 * @return	true if this all tasks in this project have been finished
	 * 			and if this project contains at least one task,
	 * 			false otherwise.
	 */
    @Override
	public boolean isFinished() {
		if(isFinished)
			return true;
		if(getTasks().isEmpty())
			return false;
		
		for(Task t : getTasks())
			if(!t.isFinished()) {
				return false;
			}
		
		return (isFinished = true);
	}
	
	/**
	 * Get the time details for this project.
	 * 
	 * @return	true if this project finished on time or
	 * 			if this project is estimated to finish on time,
	 * 			false otherwise.
	 */
    @Override
	public boolean isOnTime() {
		if(isFinished()) {
			for(Task t : getTasks())
				if(t.getTimeSpan().getEndTime().isAfter(this.getDueTime()))
					return false;
			return true;
		} else {
			Duration sum, max = new Duration(0);
			for(Task t : getTasks()) {
				sum = t.getEstimatedDuration();
				for(Task ta : t.getPrerequisiteTasks()) {
					sum = sum.add(ta.getEstimatedDuration());
				}
				if(sum.compareTo(max) > 0)
					max = sum;
			}
			//TODO: implement
			return true;
		}
	}
 
}
