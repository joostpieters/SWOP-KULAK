package domain;

import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

import exception.ObjectNotFoundException;

/**
 * This class represents a project
 * 
 * @author Frederic, Mathias, Pieter-Jan 
 */
public class Project implements DetailedProject {
	
	public static final int[] NO_DEPENDENCIES = new int[]{};
	public static final int NO_ALTERNATIVE = -1;
	
	private static int nextId = 0;
	
    private boolean isFinished;									//performance-variable
    
    private final int id;
    private final String name;
    private final String description;
    private final Timespan creationDueTime;
    private final SortedMap<Integer, Task> tasks = new TreeMap<>();
    private final Clock clock;
    
    /**
     * Construct a new project, with given id, name, description, 
     * creation time and due time.
     * @param 	name
     * 			The name for this project.
     * @param 	descr
     * 			The description for this project.
     * @param 	creation
     * 			The creation time for this project.
     * @param 	due
     * 			The due time for this project.
     * @param	clock
     * 			The system clock this project depends on.
     * 
     * @throws	IllegalArgumentException
     * 			if id < 0
     * 			or if name or descr = null
     * 			or if creation and due form an invalid time pair.
     */
    public Project(String name, String descr, LocalDateTime creation, LocalDateTime due, Clock clock){
    	if(!canHaveAsName(name) || !canHaveAsDescription(descr))
    		throw new IllegalArgumentException("Both name (at least one character) and description "
    				+ "(at least one character) are expected.");
    	
    	this.id = Project.generateId();
    	this.name = name;
    	this.description = descr;
    	this.creationDueTime = new Timespan(creation, due);
    	this.clock = clock;
    }
    
    /****************************************
     * Getters & setters					*
     ****************************************/
    
    private static int generateId() {
		return Project.nextId++;
	}

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
     * Check whether this project can have a given name.
     * 
     * @param 	name
     * 			The name to be checked.
     * @return	name != null && name.length() > 0
     */
    public boolean canHaveAsName(String name) {
    	return name != null && name.length() > 0;
    }

	/**
	 * @return 	the description of this project.
	 */
    @Override
	public String getDescription() {
		return description;
	}
    
    /**
     * Check whether this project can have a given description.
     * 
     * @param 	descr
     * 			The description to be checked.
     * @return	descr != null && descr.length() > 0
     */
    public boolean canHaveAsDescription(String descr) {
    	return descr != null && descr.length() > 0;
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
	 * @return the clock used in this project.
	 */
	public Clock getClock() {
		return new Clock(clock);
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
			throw new ObjectNotFoundException("Task with tid " + tid + " doesn't exist in this project (" + id + ").", tid);
		return t;
	}
	
	/**
	 * Check whether this project can have a given task.
	 * 
	 * @param 	t
	 * 			The task to be checked.
	 * @return	true if t is not null and is not in this project already,
				and if the alternative tasks for t are in this project,
				and if the prerequisite tasks for t are in this project,
				false otherwise.
	 */
	public boolean canHaveAsTask(Task t) {
		boolean taskCheck = t != null && !tasks.containsKey(t.getId());
		boolean altTaskCheck = t.getAlternativeTask() == null || tasks.containsKey(t.getAlternativeTask().getId());
		boolean prereqTaskCheck = true;
		for(Task x : t.getPrerequisiteTasks())
			if(!tasks.containsKey(x.getId())) {
				prereqTaskCheck = false;
				break;
			}
		
		return taskCheck && altTaskCheck && prereqTaskCheck;
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
		if(!canHaveAsTask(t))
			throw new IllegalArgumentException("The given task can't be a part of this project.");
		
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
	 * @return	The task that has been created.
	 * @throws	IllegalStateException
	 * 			if this project is already finished.
	 */
	public Task createTask(String descr, int estDurHours, int estDurMinutes, int accdev, int altFor, int[] prereq) {
		if(isFinished())
			throw new IllegalStateException("This project has already been finished.");
		
		Task t;
		if(prereq.equals(Project.NO_DEPENDENCIES)) {
			t = new Task(descr, estDurHours, estDurMinutes, accdev);
		} else {
			Task[] arr = new Task[prereq.length];
			for(int i = 0; i < prereq.length; i++) {
				arr[i] = getTask(prereq[i]);
			}
			t = new Task(descr, estDurHours, estDurMinutes, accdev, arr);
		}
		
		if(altFor != Project.NO_ALTERNATIVE)
			getTask(altFor).setAlternativeTask(t);
		addTask(t);
		return t;
	}
	
	/**
	 * Return all tasks from this project which are available.
	 * 
	 * @return	all tasks t for which t.isAvailable() is true,
	 * 			an empty list if this project is already finished.
	 */
	public List<Task> getAvailableTasks() {
		List<Task> result = new LinkedList<Task>();
		for(Task t : getTasks()) {
			if(t.getStatus() == Status.AVAILABLE)
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
    
    /****************************************
     * Time-management						*
     ****************************************/
	
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
			LocalDateTime end = max.getEndTimeFrom(getCreationTime());
			if(end.isBefore(clock.getTime()))
				end = clock.getTime();
			return end.isBefore(getDueTime());
		}
	}
    
    /**
     * Gets the total delay of this project.
     * 
     * @return 	the sum of the delays of the tasks within this project if isOnTime(),
     * 			null otherwise.
     */
    @Override
    public Duration getDelay() {
    	if(isOnTime())
    		return null;
    	Duration totalDuration = new Duration(0);
    	for(Task t : getTasks())
    		totalDuration = totalDuration.add(t.getDelay());
    	return totalDuration;
    }
}
