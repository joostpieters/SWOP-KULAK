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
public class Project {
    
    private final int id;
    private final String name;
    private final String description;
    private final LocalDateTime creationTime;
    private final LocalDateTime dueTime;
    private final SortedMap<Integer, Task> tasks = new TreeMap<>();
    
    /**
     * 
     * @param 	id
     * @param 	name
     * @param 	descr
     * @param 	creation
     * @param 	due
     */
    public Project(int id, String name, String descr, LocalDateTime creation, LocalDateTime due){
    	if(!checkTimeOrder(creation, due))
    		throw new IllegalArgumentException("The creation time was after the due time.");
    	if(id < 0)
    		throw new IllegalArgumentException("id should be bigger than zero.");
    	if(name == null || descr == null)
    		throw new IllegalArgumentException("Both name and description are expected.");
    	
    	this.id = id;
    	this.name = name;
    	this.description = descr;
    	this.creationTime = creation;
    	this.dueTime = due;
    }
    
    /****************************************
     * Getters & setters					*
     ****************************************/
    
    /**
	 * @return 	the id
	 */
	public int getId() {
		return id;
	}

	/**
	 * @return 	the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @return 	the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @return 	the creationTime
	 */
	public LocalDateTime getCreationTime() {
		return creationTime;
	}

	/**
	 * @return 	the dueTime
	 */
	public LocalDateTime getDueTime() {
		return dueTime;
	}

	/**
	 * 
	 * @return	the tasks
	 */
	public SortedMap<Integer, Task> getTasks(){
        return new TreeMap<>(tasks);
    }
	
	/**
	 * 
	 * @param 	tid
	 * @return	
	 */
	public Task getTask(int tid) {
		Task t = tasks.get(id);
		if(t == null)
			throw new IllegalArgumentException("Task with tid " + tid + " doesn't exist.");
		return t;
	}
	
	/**
	 * 
	 * @param 	t
	 */
	public void addTask(Task t) {
		if(t == null)
			throw new NullPointerException("You can't add null-tasks to a project.");
		this.tasks.put(t.getId(), t);
	}

    /****************************************
     * Validation							*
     ****************************************/
	
	/**
	 * 
	 * @param start
	 * @param end
	 * @return
	 */
	//TODO: Information expert?
	public boolean checkTimeOrder(LocalDateTime start, LocalDateTime end) {
		return start.isBefore(end);
	}
    
    /****************************************
     * Task-management						*
     ****************************************/
    
	/**
	 * 
	 * @param 	descr
	 * @param 	estDur
	 * @param 	accdev
	 * @param 	altFor
	 * @param 	prereq
	 * @param 	status
	 */
	public void createTask(String descr, int estDur, int accdev, int altFor, int[] prereq, Status status) {
		Task[] arr = new Task[prereq.length];
		for(int i : prereq) {
			arr[i] = getTask(prereq[i]);
		}
		//TODO: Constructor for Task!!!!
		//Task t = new Task(descr, estDur, accdev, getTask(altFor), arr, status);
		//addTask(t);
		throw new IllegalStateException("waiting for new task constructor.");
	}
	
	/**
	 * 
	 * @param 	tid
	 * @param 	start
	 * @param 	end
	 * @param 	status
	 */
	public void updateTask(int tid, LocalDateTime start, LocalDateTime end, Status status) {
		if(!checkTimeOrder(start, end))
    		throw new IllegalArgumentException("The creation time was after the due time.");
		getTask(tid).update(start, end, status);
	}
	
	/**
	 * 
	 * @return	
	 */
	public List<Task> getAvailableTasks() {
		List<Task> result = new LinkedList<Task>();
		for(Task t : tasks.values()) {
			if(t.isAvailable())
				result.add(t);
		}
		return result;
	}
	
	
 
}
