package domain;

import domain.command.PlanTaskCommand;
import domain.datainterface.DetailedTask;
import domain.time.Clock;
import domain.time.Duration;
import domain.time.Timespan;
import exception.ConflictException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;

/**
 * This class represents a task
 *
 * @author Frederic, Mathias, Pieter-Jan
 */
public class Task implements DetailedTask {

    private static int nextId = 0;

    private final int id;
    private String description;
    private int acceptableDeviation;
    private Timespan timespan;
    private final Duration estimatedDuration;
    private Task alternativeTask;
    private List<Task> prerequisiteTasks;
    private Status status;
    private LocalDateTime plannedStartTime;
    private final Map<ResourceType, Integer> requiredResources;
    private final Project project;
    /**
     * A constant to indicate that a task requires no resources
     */
    public static Map<ResourceType, Integer> NO_REQUIRED_RESOURCE_TYPES = new HashMap<>();

    /****************************************
     * Constructors							*
     ****************************************/
    
    /**
     * Initializes this task based on the given description, estimated duration,
     * acceptable deviation and prerequisite tasks.
     *
     * @param description The description of this task.
     * @param duration The estimated duration of this task
     * @param accDev The acceptable deviation of this task expressed as an
     * integer between 0 and 100.
     * @param prereq The list of prerequisite tasks for this task.
     * @param resources The resources this task requires to be performed
     * @param The project this task belongsto
     */
    Task(String description, Duration duration, int accDev, List<Task> prereq, Map<ResourceType, Integer> resources, Project project) {
        this.id = generateId();
        setDescription(description);
        this.estimatedDuration = duration;
        setAcceptableDeviation(accDev);
        if (prereq == null) {
            setPrerequisiteTasks(new ArrayList<>());
        } else {
            setPrerequisiteTasks(prereq);
        }
        if(!canHaveAsResourceTypes(resources))
            throw new IllegalArgumentException("This combination of resourcetypes is not valid.");
        this.requiredResources = new HashMap<>(resources);
        
        Status initStatus = new Available();
        setStatus(initStatus);
        initStatus.update(this);
        this.project = project;
        
        if(project == null)
            throw new IllegalArgumentException("A task cannot exist without a project");
        project.addTask(this);
    }

    /**
     * Initializes this task based on the given description, estimated duration
     * and acceptable deviation.
     *
     * @param description The description of this task.
     * @param duration The estimated duration of this task
     * @param accDev The acceptable deviation of this task expressed as an
     * integer between 0 and 100.
     * @param resources The resources this task requires to be performed
     * @param The project this task belongs to
     */
    Task(String description, Duration duration, int accDev, Map<ResourceType, Integer> resources, Project project) {
        this(description, duration, accDev, null, resources, project);
    }

    /****************************************
     * Getters & Setters & Checkers	        *
     ****************************************/
    
    /**
     * Generates an id for a new task.
     *
     * @return The id to be used for a newly created task.
     */
    private static int generateId() {
        return nextId++;
    }

    /**
     * @return The identification number of this task.
     */
    @Override
    public int getId() {
        return this.id;
    }

    /**
     * @return The description of this task.
     */
    @Override
    public String getDescription() {
        return this.description;
    }

    /**
     * Sets the description of this task to the given description.
     *
     * @param description The new description of this task.
     * @throws IllegalArgumentException If this task can't have the given
     * description as its description.
     */
    private void setDescription(String description) throws IllegalArgumentException {
        if (!canHaveAsDescription(description)) {
            throw new IllegalArgumentException("The given description is uninitialized.");
        } else {
            this.description = description;
        }
    }

    /**
     * Checks whether this task can have the given description as its
     * description.
     *
     * @param description The description to check.
     * @return True if and only if the given description is not equal to null
     * and the given is not empty.
     */
    public boolean canHaveAsDescription(String description) {
        return description != null && !description.isEmpty();
    }

    /**
     * @return The estimated duration of this task.
     */
    @Override
    public Duration getEstimatedDuration() {
        return this.estimatedDuration;
    }

    /**
     * @return The timeSpan indicating the actual start and end time if this
     * task has one, null otherwise.
     */
    @Override
    public Timespan getTimeSpan() {
        return this.timespan;
    }

    /**
     * Sets the time span of this task to the given time span.
     * (this method must only be used by a subclass of status.)
     * 
     * @param timeSpan The new time span of this task.
     */
    void setTimeSpan(Timespan timeSpan) {
        this.timespan = timeSpan;
    }
    
    /**
     * @return The acceptable deviation of this task expressed as an integer
     * between 0 and 100.
     */
    @Override
    public int getAcceptableDeviation() {
        return this.acceptableDeviation;
    }
    
    /**
     * Sets this task his acceptable deviation to the given acceptable
     * deviation.
     *
     * @param accDev The new acceptable deviation of this task.
     * @throws IllegalArgumentException If this task can't have the given
     * acceptable deviation as its acceptable deviation.
     */
    private void setAcceptableDeviation(int accDev) throws IllegalArgumentException {
        if (!canHaveAsAcceptableDeviation(accDev)) {
            throw new IllegalArgumentException(
                    "This task can't have the given acceptable deviation as its acceptable deviation.");
        }
        this.acceptableDeviation = accDev;
    }
    
    /**
     * Checks whether this task can have the given acceptable deviation as its
     * acceptable deviation.
     *
     * @param accDev The acceptable deviation.
     * @return True if and only if the given acceptable deviation is between 0
     * and 100.
     */
    public boolean canHaveAsAcceptableDeviation(int accDev) {
        return (0 <= accDev) && (accDev <= 100);
    }

    /**
     * @return The alternative task for this task.
     */
    @Override
    public Task getAlternativeTask() {
        return this.alternativeTask;
    }

    /**
     * Sets the alternative task of this task to the given alternative task.
     *
     * @param alternativeTask The alternative task for this task.
     * @throws IllegalStateException
     * @see Status#setAlternativeTask(domain.Task, domain.Project)
     * @see canHaveAsAlternativeTask
     */
    public void setAlternativeTask(Task alternativeTask) throws IllegalStateException, IllegalArgumentException {
        status.setAlternativeTask(this, alternativeTask, project);
    }
    
    /**
     * This method sets the alternative of this task without any checks
     * (Must only be used by subclasses of status.)
     * 
     * @param task The alternative task.
     */
    void setAlternativeTaskRaw(Task task){
        alternativeTask = task;
    }

    /**
     * Checks whether this task can have the given alternative task as its
     * alternative task.
     *
     * @param altTask The alternative task to check.
     * @return False if the given alternative task is equal to null.
     *         False if the given alternative task is equal to this task.
     *         False if the given alternative task depends on this task.
     *         False if this task doesn't belong to the given project.
     *         False if the given alternative task doesn't belong to the given project.
     *         True otherwise.
     * @see dependsOn
     */
    public boolean canHaveAsAlternativeTask(Task altTask, Project project) {
        if (altTask == null) {
            return false;
        }
        if (altTask.equals(this)) {
            return false;
        }
        if (altTask.dependsOn(this)) {
            return false;
        }
        if(!project.hasTask(getId()))
        	return false;
        if(!project.hasTask(altTask.getId()))
        	return false;
        return true;
    }

    /**
     * @return The list of prerequisite tasks for this task.
     */
    @Override
    public List<Task> getPrerequisiteTasks() {
        return new ArrayList<>(this.prerequisiteTasks);
    }

    /**
     * Sets the list of prerequisite tasks to the given list of prerequisite
     * tasks.
     *
     * @param prereq The new list of prerequisite tasks for this task.
     * @throws IllegalArgumentException If this task can't have the given list
     * of prerequisite tasks as its list of prerequisite tasks.
     */
    private void setPrerequisiteTasks(List<Task> prereq) throws IllegalArgumentException {
        if (!canHaveAsPrerequisiteTasks(prereq)) {
            throw new IllegalArgumentException(
                    "This task can't have the given list of prerequisite tasks as its prerequisite tasks.");
        }
        this.prerequisiteTasks = prereq;
    }

    /**
     * Checks whether this task can have the given list of prerequisite tasks as
     * its prerequisite tasks.
     *
     * @param prereq The list of prerequisite tasks to check.
     * @return False if the list is null. False if any of the tasks in the given
     * list is null. False if any of the tasks in the given list is equal to
     * this task. False if any of the tasks in the given list depends on this
     * task. True otherwise.
     * @see dependsOn
     */
    public boolean canHaveAsPrerequisiteTasks(List<Task> prereq) {
        if (prereq == null) {
            return false;
        }
        for (Task t : prereq) {
            if (t == null || t == this || t.dependsOn(this)) {
                return false;
            }
        }

        return true;
    }
    
    /**
     * Check whether the given list of resource types is mutually compatible
     * 
     * @param 	resources 
     *       	The resource to check
     * @return 	{@code true} if and only if all the given resource types don't mutually 
     *        	conflict and for each resource type, its requirements are included in
     *        	the given list. 
     */
    public static boolean canHaveAsResourceTypes(Map<ResourceType, Integer> resources) {
        if(resources == null){
            return false;
        }
        for (ResourceType type : resources.keySet()) {
            if(!type.canHaveAsCombination(resources))
                return false;
        }
        return true;
    }

    /**
     * @return The status of this task.
     */
    @Override
    public final Status getStatus() {
        status.update(this);
        return this.status;
    }

    /**
     * Sets the status of this task to the given status.
     * Must only be accessed by objects that are subclasses of status
     *
     * @param status The new status of this task.
     */
    final void setStatus(Status status) {
        this.status = status;
    }

    /****************************************
     * Other methods	                    *
     ****************************************/
    
    /**
     * Calculates the duration by which this task been delayed.
     *
     * @return The duration by which this task has been delayed if this task has
     * a time span, based on the maximum duration of this task. If this task
     * doesn't have a time span then the result is a duration of 0 minutes.
     */
    @Override
    public Duration getDelay() {
        if (getTimeSpan() == null) {
            return new Duration(0);
        }

        return getTimeSpan().getExcess(calculateMaxDuration());
    }

    /**
	 * @return the requiredResources
	 */
	public Map<ResourceType, Integer> getRequiredResources() {
            return requiredResources;
	}

	/**
     * Calculates the maximum duration of this task by which this task will
     * still be finished on time.
     *
     * @return The estimated duration of this task multiplied by (100 + (the
     * acceptable deviation of this task))/100
     */
    Duration calculateMaxDuration() {
        return getEstimatedDuration().multiplyBy((100d + getAcceptableDeviation()) / 100d);
    }

    /** TODO: clear future reservations
     * Fail this task
     * 
     * @param timespan The timespan of this failed task
     * @param currentTime The current time when this task is changed to finished
     * 
     */
    public void fail(Timespan timespan, LocalDateTime currentTime)
    {
        if (timespan == null) {
            throw new IllegalArgumentException("The given timespan are not initialized.");
        }
    	getStatus().fail(this, timespan);
    	clearFutureReservations(currentTime);
    }
    
    /** TODO: clear future reservations
     * Finish this task
     * 
     * @param timespan The timespan of this finished task
     * @param currentTime The current time when this task is changed to finished
     * 
    */
    public void finish(Timespan timespan, LocalDateTime currentTime)
    {
        if (timespan == null) {
            throw new IllegalArgumentException("The given timespan are not initialized.");
        }
        
        if(timespan.endsAfter(currentTime)){
            throw new IllegalArgumentException("The given timespan is after the current time.");
        }
    	getStatus().finish(this, timespan);
    	clearFutureReservations(timespan.getEndTime());
    }
    
     /**
     * Move this task to the executing state
     * @param currentTime The current time when this task is changed to executing
     */
    public void execute(LocalDateTime currentTime){
        getStatus().execute(this);
    }
    // TODO verplaatsen
    public void clearFutureReservations(LocalDateTime currentTime)
    {
    	for(ResourceType resourceType : getRequiredResources().keySet())
    		resourceType.clearFutureReservations(currentTime, this);
    }
    
    /**
     * Checks whether this task is available.
     *
     * @return True if and only if the status of this task is available.
     */
    public boolean isAvailable() {
        return (getStatus() instanceof Available);
    }

    /**
     * Checks whether this task has been fulfilled.
     *
     * @return True if and only if this task is finished or if it has an
     * alternative task and this alternative task is fulfilled.
     */
    public boolean isFulfilled() {
        return getStatus().isFulfilled(this);
    }

    /**
     * Checks whether this task ends before the given time span.
     *
     * @param timeSpan The time span to check.
     * @return True if the time span of this task ends before the start of the
     * given time span.
     * @throws IllegalStateException If this task does not have a time span.
     */
    public boolean endsBefore(Timespan timeSpan) throws IllegalStateException {
        if (getTimeSpan() == null) {
            throw new IllegalStateException(
                    "Tried to check whether this task ends before the given time while this task doesn't have a time span.");
        }
        return getTimeSpan().endsBefore(timeSpan);
    }
    
    /**
     * Checks whether this task has a time span.
     *
     * @return True if and only if the time span of this task is not null.
     */
    public boolean hasTimeSpan() {
        return getTimeSpan() != null;
    }

    /**
     * Checks whether this task directly or indirectly depends on the given
     * task.
     *
     * @param task The task to check.
     * @return True if this task has an alternative task and the alternative
     * task is equal to the given task. True if a prerequisite task of this task
     * is equal to the given task. True if a prerequisite task depends on the
     * given task. False otherwise.
     */
    public boolean dependsOn(Task task) {
        if (getAlternativeTask() != null && getAlternativeTask().equals(task)) {
            return true;
        }
        for (Task prereqTask : getPrerequisiteTasks()) {
            if (prereqTask.equals(task)) {
                return true;
            }

            if (prereqTask.dependsOn(task)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Checks whether the status of this task is equal to finished.
     *
     * @return True if and only if the status of this task is equal to finished.
     */
    public boolean isFinished() {
        return getStatus() instanceof Finished;
    }

    /**
     * The estimated amount of work time needed for this task.
     *
     * @return If this task is finished or failed, a duration of 0. If this task
     * is unavailable, the sum of the amounts of estimated work time needed of
     * the prerequisite tasks of this task + the estimated duration of this task
     * + the estimated durations of the alternatives of the prerequisites of
     * this task. If this task is available, the estimated duration of this
     * task.
     *
     */
    public Duration estimatedWorkTimeNeeded() {
        return getStatus().estimatedWorkTimeNeeded(this);
    }

    /**
     * Checks whether this task can be fulfilled or already is fulfilled.
     *
     * @return True if this task is finished or available. False if this task is
     * failed and doesn't have an alternative task. True if this task is failed
     * and has an alternative task that can be fulfilled. False if this task is
     * unavailable and any of the prerequisite tasks can't be fulfilled. True if
     * this task is unavailable and all prerequisite tasks can be fulfilled.
     */
    public boolean canBeFulfilled() {
        return getStatus().canBeFulfilled(this);
    }

    /**
     * Calculates the amount of time spent on this task.
     *
     * @return If this task has a time span then the result is equal to the sum
     * of the maximum amount of time spent on a prerequisite task of this task
     * and the alternative task if this task has an alternative task and the
     * duration of the time span of this task. Otherwise a duration of 0 minutes
     * is returned.
     */
    public Duration getTimeSpent() {
        if (hasTimeSpan()) {
            Duration temp, max = Duration.ZERO;
            for (Task t : getPrerequisiteTasks()) {
                temp = t.getTimeSpent();
                if (temp.compareTo(max) > 0) {
                    max = temp;
                }
            }
            temp = getTimeSpan().getDuration().add(max);
            if (hasAlternativeTask()) {
                return temp.add(getAlternativeTask().getTimeSpent());
            }
            return temp;
        }
        return new Duration(0);
    }

    /**
     * Checks whether this task has an alternative task.
     *
     * @return True if and only if this task has an alternative task.
     */
    public boolean hasAlternativeTask() {
        return getAlternativeTask() != null;
    }
    
    /**
     * Returns the etimated end time of this task
     * 
     * @param clock The clock to use to determine the start time of this task
     * @return The time of the given clock + the estimated work time needed
     * by this task.
     */
    public LocalDateTime getEstimatedEndTime(Clock clock)
    {
        return estimatedWorkTimeNeeded().getEndTimeFrom(clock.getTime());
    }
    
   
    
    /**
     * Check whether this task is planned
     * 
     * @return True if and only if this task has a planned start time.
     */
    public boolean isPlanned(){
        return plannedStartTime != null;
    }
    
     /**
     * Plan this task at the given start time
     *
     * @param startTime The time this task is planned to start
     * @param resources The resources to assign to this task
     * @throws exception.ConflictException The task's reservations conflict with
     * another task
     */
    public PlanTaskCommand plan(LocalDateTime startTime, List<Resource> resources) throws ConflictException {
    	Map<ResourceType, Integer> required = getRequiredResources();
    	for(ResourceType type : required.keySet())
    		if(type.numberOfResources(resources) < required.get(type))
    			throw new IllegalArgumentException("The given set of resources does not match the requirements.");
    		
        PlanTaskCommand planCommand = new PlanTaskCommand(timespan, resources, this);
        planCommand.execute();
        plannedStartTime = startTime;
        return planCommand;
    }
    
    /**
     * Get a set of times this task could possibly be started 
     * from a certain point in time.
     * 
     * @param 	from
     *       	The time after which the task should be started.
     * @return	a sorted set of possible points in time this task may be started.
     *        	The task is completely available after the last time in the result.
     */
    public SortedSet<LocalDateTime> nextAvailableStartingTimes(LocalDateTime from) {
    	//TODO: bad smell?
    	//TODO: developers zijn nog niet in rekening gebracht!!!
    	SortedSet<LocalDateTime> result = new TreeSet<>();
    	Map<ResourceType, Integer> required = getRequiredResources();
    	SortedSet<Timespan> freeMoments;
    	SortedSet<LocalDateTime> temp;
    	LocalDateTime last = LocalDateTime.MIN;
    	
    	for(ResourceType type : required.keySet()) {
    		temp = new TreeSet<>();
    		freeMoments = type.nextAvailableTimespans(from);
    		for(Timespan span : freeMoments) {
    			Timespan rounded = span.roundStartingTime();
    			
    			//oneindige lus vermijden
    			if(rounded.isInfinite()) {
    				
    				//vanaf de laatste tijd zijn alle resources altijd beschikbaar.
    				if(rounded.startsAfter(last)) {
    					last = rounded.getStartTime();
    					temp.add(rounded.getStartTime());
    				}
    			} else {
    				while(rounded != null && type.hasAvailableResources(rounded, required.get(type))) {
    					temp.add(rounded.getStartTime());
    					rounded = rounded.postponeHours(1);
    				}
    			}
    		}

    		if(result.last().isBefore(temp.last())) {
    			result.retainAll(temp);
    			result.addAll(temp.subSet(result.last(), temp.last()));
    		} else {
    			temp.retainAll(result);
    			temp.addAll(result.subSet(temp.last(), result.last()));
    			result = temp;
    		}
    	}
    	
    	return result;
    }
    
    /**
     * Creates a memento for this task.
     * 
     * @return A memento which stores the the state of this task.
     */
    public Memento createMemento()
    {
    	return new Memento(timespan, alternativeTask, prerequisiteTasks, status, plannedStartTime);
    }
    
    /**
     * Sets the state of this task to the state stored inside the given memento.
     * 
     * @param memento The memento containing the new state of this task.
     */
    public void setMemento(Memento memento)
    {
    	this.timespan = memento.getTimespan();
    	this.alternativeTask = memento.getAlternativeTask();
		this.prerequisiteTasks = memento.getPrerequisiteTasks();
		this.status = memento.getStatus();
		this.plannedStartTime = memento.GetPlannedStartTime();
    }
    
    /**
     * 
     * @return The project this task belongs to 
     */
    @Override
    public Project getProject() {
        return project;
    }

    public class Memento {
	
	private final Timespan timespan;
	private final Task alternativeTask;
	private final List<Task> prerequisiteTasks;
	private final Status status;
	private final LocalDateTime plannedStartTime;
	
	private Timespan getTimespan()
	{
		return this.timespan;
	}
	
	private Task getAlternativeTask()
	{
		return this.alternativeTask;
	}
	
	private List<Task> getPrerequisiteTasks()
	{
		return new ArrayList<>(this.prerequisiteTasks);
	}
	
	private Status getStatus()
	{
		return this.status;
	}
	
	private LocalDateTime GetPlannedStartTime()
	{
		return this.plannedStartTime;
	}
	
	/**
	 * Initializes this memento based on the given state.
	 * 
	 * @param timespan The time span of the originator task.
	 * @param alternativeTask The alternative task of the originator task.
	 * @param prerequisiteTasks The list of prerequisite tasks of the originator task.
	 * @param status The status of the originator task.
	 * @param plannedStartTime The planned start time of the originator task.
	 */
	private Memento(Timespan timespan, Task alternativeTask, List<Task> prerequisiteTasks, Status status, LocalDateTime plannedStartTime)
	{
		this.timespan = timespan;
		this.alternativeTask = alternativeTask;
		this.prerequisiteTasks = new ArrayList<>(prerequisiteTasks);
		this.status = status;
		this.plannedStartTime = plannedStartTime;
	}
    }
}
