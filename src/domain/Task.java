package domain;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * This class represents a task
 * 
 * @author Frederic, Mathias, Pieter-Jan
 */
public class Task implements DetailedTask {

	private static int nextId=0;
	
	private final int id;
	private String description;
	private int acceptableDeviation;
	private Timespan timeSpan;
	private Duration estimatedDuration;
	private Task alternativeTask;
	private List<Task> prerequisiteTasks;
	private Status status;

    
    /****************************************
     * Constructors							*
     ****************************************/
	
	/**
	 * Initializes this task based on the given description, estimated duration,
	 * acceptable deviation, prerequisite tasks and alternative task for.
	 *  
	 * @param 	description
	 *        	The description of this task.
     * @param   duration
     *          The estimated duration of this task
	 * @param 	accDev
	 *        	The acceptable deviation of this task expressed as an integer between 0 and 100.
	 * @param 	prereq
	 *        	The list of prerequisite tasks for this task.
	 * @param   alternativeFor
	 *          The task for which this task is an alternative for.
	 */
	public Task(String description, Duration duration, int accDev, List<Task> prereq, Task alternativeFor)
	{
		this.id = generateId();
		setDescription(description);
		this.estimatedDuration = duration;
		setAcceptableDeviation(accDev);
		if(prereq == null)
			setPrerequisiteTasks(new ArrayList<Task>());
		else
			setPrerequisiteTasks(prereq);
		updateStatus();
		if(alternativeFor != null)
			alternativeFor.setAlternativeTask(this);
	}
	/**
	 * Initializes this task based on the given description, estimated duration,
	 * acceptable deviation and alternative task for.
	 *  
	 * @param 	description
	 *        	The description of this task.
     * @param   duration
     *          The estimated duration of this task
	 * @param 	accDev
	 *        	The acceptable deviation of this task expressed as an integer between 0 and 100.
	 * @param   alternativeFor
	 *          The task for which this task is an alternative for.
	 */
	public Task(String description, Duration duration, int accDev, Task alternativeFor)
	{
		this(description, duration, accDev, null, alternativeFor);
	}
	
	/**
	 * Initializes this task based on the given description, estimated duration, acceptable deviation
	 *  and prerequisite tasks.
	 *  
	 * @param 	description
	 *        	The description of this task.
	  * @param       duration
         *              The estimated duration of this task
	 * @param 	accDev
	 *        	The acceptable deviation of this task expressed as an integer between 0 and 100.
	 * @param 	prereq
	 *        	The list of prerequisite tasks for this task.
	 */
	public Task(String description, Duration duration, int accDev, List<Task> prereq)
	{
		this(description, duration, accDev, prereq, null);
	}

	/**
	 * Initializes this task based on the given description, estimated duration
	 * and acceptable deviation.
	 *  
	 * @param 	description
	 *        	The description of this task.
	  * @param       duration
         *              The estimated duration of this task
	 * @param 	accDev
	 *        	The acceptable deviation of this task expressed as an integer between 0 and 100.
	 */
	public Task(String description, Duration duration, int accDev)
	{
		this(description, duration, accDev, null, null);
	}

    /****************************************
     * Getters & Setters & Checkers			*
     ****************************************/
	
	/**
	 * Generates an id for a new task.
	 * 
	 * @return 	The id to be used for a newly created task.
	 */
	private static int generateId()
	{
		return nextId++;
	}

	/**
	 * @return 	The identification number of this task.
	 */
    @Override
	public int getId()
	{
		return this.id;
	}

	/**
	 * @return 	The description of this task.
	 */
    @Override
	public String getDescription()
	{
		return this.description;
	}
    
	/**
	 * Sets the description of this task to the given description.
	 * 
	 * @param 	description
	 *        	The new description of this task.
	 * @throws 	IllegalArgumentException
	 *         	If this task can't have the given description as its description.
	 */
	private void setDescription(String description) throws IllegalArgumentException
	{
		if(!canHaveAsDescription(description))
			throw new IllegalArgumentException("The given description is uninitialized.");
		else
			this.description = description;
	}
	
	/**
	 * Checks whether this task can have the given description as its description.
	 * 
	 * @param 	description
	 *        	The description to check.
	 * @return 	True if and only if the given description is not equal to null and the given 
	 *         	is not empty.
	 */
	public boolean canHaveAsDescription(String description)
	{
		return description != null && !description.isEmpty();
	}

	/**
	 * @return 	The estimated duration of this task.
	 */
    @Override
	public Duration getEstimatedDuration()
	{
		return this.estimatedDuration;
	}

	/**
	 * @return 	The timeSpan indicating the actual start and end time of this task.
	 */
    @Override
	public Timespan getTimeSpan()
	{
		return this.timeSpan;
	}

	/**
	 * Sets the time span of this task to the given time span.
	 * 
	 * @param  	timeSpan
	 *         	The new time span of this task.
	 * @throws 	IllegalArgumentException
	 *         	If this task can't have the given time span as its time span.
	 * @see    	canHaveAsTimeSpan
	 */
	private void setTimeSpan(Timespan timeSpan) throws IllegalArgumentException
	{
		if(!canHaveAsTimeSpan(timeSpan))
			throw new IllegalArgumentException("The given time span is not a valid time span for this task");
		this.timeSpan = timeSpan;
	}

	/**
	 * Checks whether the given time span is a valid time span for this task.
	 * 
	 * @param  	timeSpan
	 *         	The time span to check.
	 * @return 	False if any of the prerequisite tasks were fulfilled before the given time span.
	 * @see    	isFulfilledBefore
	 */
	public boolean canHaveAsTimeSpan(Timespan timeSpan)
	{
		for(Task t: getPrerequisiteTasks())
			if(t.isFulfilled() && !t.isFulfilledBefore(timeSpan))
				return false;
		return true;
	}
	
	/**
	 * @return 	The acceptable deviation of this task expressed as an integer between 0 and 100.
	 */
    @Override
	public int getAcceptableDeviation()
	{
		return this.acceptableDeviation;
	}

	/**
	 * Sets this task his acceptable deviation to the given acceptable deviation.
	 * 
	 * @param 	accDev
	 *        	The new acceptable deviation of this task.
	 * @throws 	IllegalArgumentException
	 *         	If this task can't have the given acceptable deviation as its acceptable deviation.
	 */
	private void setAcceptableDeviation(int accDev) throws IllegalArgumentException
	{
		if(!canHaveAsAcceptableDeviation(accDev))
			throw new IllegalArgumentException(
					"This task can't have the given acceptable deviation as its acceptable deviation.");
		this.acceptableDeviation = accDev;
	}

	/**
	 * Checks whether this task can have the given acceptable deviation as its acceptable deviation.
	 * 
	 * @param  	accDev
	 *         	The acceptable deviation.
	 * @return 	True if and only if the given acceptable deviation is between 0 and 100.
	 */
	public boolean canHaveAsAcceptableDeviation(int accDev)
	{
		return (0 <= accDev) && (accDev <= 100);
	}

	/**
	 * @return 	The alternative task for this task.
	 */
        @Override
	public Task getAlternativeTask()
	{
		return this.alternativeTask;
	}

	/**
	 * Sets the alternative task of this task to the given alternative task.
	 * 
	 * @param 	alternativeTask
	 *        	The alternative task for this task.
	 * @throws 	IllegalStateException
	 *         	If the state of this task is not FAILED.
	 * @throws 	IllegalStateException
	 *         	If this task already has an alternative task.
	 * @throws 	IllegalArgumentException
	 *         	If this task can't have the given task as its alternative task.
	 * @see    	canHaveAsAlternativeTask
	 */
	public void setAlternativeTask(Task alternativeTask) throws IllegalStateException, IllegalArgumentException
	{
		if(getStatus() != Status.FAILED)
			throw new IllegalStateException(
					"Can't set an alternative task for this "
					+ "task because the status of this task is not equal to FAILED.");
		
		if(getAlternativeTask() != null)
			throw new IllegalStateException(
					"Can't set an alternative task for this task because "
					+ "this task already has an alternative task.");
		
		if(!canHaveAsAlternativeTask(alternativeTask))
			throw new IllegalArgumentException(
					"This task can't have the given task as its alternative task.");
		this.alternativeTask = alternativeTask;
	}
	
	/**
	 * Checks whether this task can have the given alternative task as its alternative task.
	 * 
	 * @param 	altTask
	 *        	The alternative task to check.
	 * @return 	False if the given alternative task is equal to null.
	 *         	False if the given alternative task is equal to this task.
	 *         	False if the given alternative task depends on this task.
	 *         	True otherwise.
	 * @see    	dependsOn
	 */
	public boolean canHaveAsAlternativeTask(Task altTask)
	{
		if(altTask == null)
			return false;
		if(altTask.equals(this))
			return false;
		if(altTask.dependsOn(this))
			return false;
		return true;
	}

	/**
	 * @return 	The list of prerequisite tasks for this task.
	 */
    @Override
	public List<Task> getPrerequisiteTasks()
	{
		return new ArrayList<Task>(this.prerequisiteTasks);
	}

	/**
	 * Sets the list of prerequisite tasks to the given list of prerequisite tasks.
	 * 
	 * @param 	prereq
	 *        	The new list of prerequisite tasks for this task.
	 * @throws 	IllegalArgumentException
	 *         	If this task can't have the given list of prerequisite tasks as its list of prerequisite tasks.
	 */
	private void setPrerequisiteTasks(List<Task> prereq) throws IllegalArgumentException
	{
		if(!canHaveAsPrerequisiteTasks(prereq))
			throw new IllegalArgumentException(
					"This task can't have the given list of prerequisite tasks as its prerequisite tasks.");
		this.prerequisiteTasks = prereq;
	}

	/**
	 * Checks whether this task can have the given list of prerequisite tasks as its prerequisite tasks.
	 * 
	 * @param  	prereq
	 *         	The list of prerequisite tasks to check.
	 * @return 	False if the list is null.
	 *         	False if any of the tasks in the given list is null.
	 *         	False if any of the tasks in the given list is equal to this task.
	 *         	False if any of the tasks in the given list depends on this task.
	 *         	True otherwise.
	 * @see    	dependsOn
	 */
	public boolean canHaveAsPrerequisiteTasks(List<Task> prereq)
	{
		if(prereq==null)
			return false;
		for(Task t: prereq)
			if(t==null || t==this || t.dependsOn(this)) //TODO is t.dependsOn wel nodig? kan nooit voorkomen?
				return false;
		
		return true;
	}

	/**
	 * @return 	The status of this task.
	 */
    @Override
	public Status getStatus()
	{
		if(this.status == Status.UNAVAILABLE)
			updateStatus();
		return this.status;
	}


	/**
	 * Updates the status of this task to AVAILABLE if all the prerequisite tasks are
	 * fulfilled and to UNAVAILABLE if the current status is not initialized.
	 */
	private void updateStatus()
	{
		if(this.status == Status.UNAVAILABLE || this.status == null)
		{
			Status newStatus = Status.AVAILABLE;
			for(Task t : getPrerequisiteTasks())
				if(!t.isFulfilled())
					newStatus = Status.UNAVAILABLE;
			setStatus(newStatus);
		}
	}

	/**
	 * Sets the status of this task to the given status.
	 * 
	 * @param 	status
	 *        	The new status of this task.
	 */
	private void setStatus(Status status)
	{
		this.status = status;
	}

    /****************************************
     * Other methods						*
     ****************************************/
	
	/**
	 *  Calculates the duration by which this task been delayed.
	 *  
	 * @return 	The duration by which this task has been delayed if this task has a time span,
	 *         	based on the maximum duration of this task.
	 *         	If this task doesn't have a time span then the result is a duration of 0 minutes.
	 */
    @Override
	public Duration getDelay()
	{
		if(getTimeSpan()==null)
			return new Duration(0);

		return getTimeSpan().getExcess( calculateMaxDuration() );
	}

	/**
	 * Calculates the maximum duration of this task by which this task will still be finished on time.
	 * 
	 * @return 	The estimated duration of this task multiplied by (100 + (the acceptable deviation of this task))/100
	 */
	private Duration calculateMaxDuration()
	{
		return getEstimatedDuration().multiplyBy( (100d + getAcceptableDeviation())/100d );
	}
	
	/**
	 * Updates the status and the time span of this task.
	 * 
	 * @param 	start
	 *        	The beginning of the time span of this task.
	 * @param 	end
	 *        	The end of the time span of this task.
	 * @param 	status
	 *        	The new status of this task.
	 * @throws 	IllegalArgumentException
	 *         	The given status is neither FAILED nor FINISHED and is therefore
	 *         	not a valid status that can be assigned to this task.
	 * @throws 	IllegalArgumentException
	 *         	If the start and/or end time are not initialized.
	 */
	public final void update(LocalDateTime start, LocalDateTime end, Status status) throws IllegalArgumentException
	{
		if(!canUpdateStatus(status))
			throw new IllegalArgumentException("This task can't be updated to the given status.");
		if(start == null || end == null)
			throw new IllegalArgumentException("The given start and/or end time are not initialized.");
		setTimeSpan(new Timespan(start, end));
		setStatus(status);

	}

	/**
	 * Checks whether this task is available.
	 * 
	 * @return 	True if and only if the status of this task is available.
	 */
	public boolean isAvailable()
	{
		return getStatus() == Status.AVAILABLE;
	}

	/**
	 * Checks whether this task has been fulfilled.
	 * 
	 * @return 	True if and only if this task is finished or if it has an alternative task
	 *         	and this alternative task is fulfilled.
	 */
	public boolean isFulfilled()
	{
		if(getStatus().equals(Status.FINISHED))
			return true;
		if(getAlternativeTask() != null)
			return getAlternativeTask().isFulfilled();
		return false;
	}

	/**
	 * Checks whether this task ends before the given time span.
	 * 
	 * @param 	timeSpan
	 *        	The time span to check.
	 * @return 	True if the time span of this task ends before the start of the given time span.
	 * @throws 	IllegalStateException
	 *         	If this task does not have a time span.
	 */
	public boolean endsBefore(Timespan timeSpan) throws IllegalStateException
	{
		if(getTimeSpan() == null)
			throw new IllegalStateException(
					"Tried to check whether this task ends before the given time while this task doesn't have a time span.");
		return getTimeSpan().endsBefore(timeSpan);
	}
	
	/**
	 * Checks whether this task was fulfilled before the given time span.
	 * 
	 * @param 	timeSpan
	 *        	The time span to check.
	 * @return 	True if this task is finished and this task ends before the given time span.
	 *         	Otherwise the result is equal to whether or not the alternative task of this task was fulfilled
	 *         	before the given time span.
	 * @throws 	IllegalStateException
	 *         	If this task is not fulfilled.
	 */
	private boolean isFulfilledBefore(Timespan timeSpan) throws IllegalStateException
	{
		if(!isFulfilled())
			throw new IllegalStateException("Tried to check whether this task was "
					+ "fulfilled before the given time span while this task is not fulfilled.");
		if(getStatus() == Status.FINISHED)
			return endsBefore(timeSpan);
		else
			return getAlternativeTask().isFulfilledBefore(timeSpan);
	}
	/**
	 * Checks whether this task has a time span.
	 * 
	 * @return 	True if and only if the time span of this task is not null.
	 */
	public boolean hasTimeSpan()
	{
		return getTimeSpan() != null;
	}

	/**
	 * Checks whether this task directly or indirectly depends on the given task.
	 * 
	 * @param 	task
	 *        	The task to check.
	 * @return 	True if this task has an alternative task and the alternative task is equal to the given task.
	 *         	True if a prerequisite task of this task is equal to the given task.
	 *         	True if a prerequisite task depends on the given task.
	 *         	False otherwise.
	 */
	public boolean dependsOn(Task task)
	{
		if(getAlternativeTask() != null && getAlternativeTask().equals(task))
			return true;
		for(Task prereqTask : getPrerequisiteTasks())
		{
			if(prereqTask.equals(task))
				return true;
			
			if(prereqTask.dependsOn(task))
				return true;
		}
		return false;
	}
	
	/**
	 * Checks whether the given status can be assigned to this task.
	 * 
	 * @param 	status
	 *        	The status to check.
	 * @return 	False if the status of this task is not equal to available.
	 *         	False if the given status is equal to unavailable.
	 *         	False if the given status is equal to available.
	 *         	True otherwise.
	 */
	public boolean canUpdateStatus(Status status)
	{
		if((getStatus() == Status.AVAILABLE || getStatus() == Status.UNAVAILABLE) && status == Status.FAILED)
			return true;
		if(getStatus() != Status.AVAILABLE)
			return false;
		if(status == Status.UNAVAILABLE)
			return false;
		if(status == Status.AVAILABLE)
			return false;
		return true;
	}

	/**
	 * Checks whether the status of this task is equal to finished.
	 * 
	 * @return 	True if and only if the status of this task is equal to finished.
	 */
	public boolean isFinished() // TODO daarom is dit nodig. TODO duplicate code see isFulfilled
	{
		if(getStatus() == Status.FAILED)
			return getAlternativeTask() != null && getAlternativeTask().isFinished();
		return getStatus() == Status.FINISHED;
	}
	
	/**
	 * The estimated amount of time needed until this task can be set to finished.
	 * 
	 * @return If this task is already finished, a duration of 0 is returned.
	 *         
	 */
	public Duration estimatedWorkTimeNeeded()
	{
		if(!canBeFulfilled())
			throw new IllegalStateException("This task doesn't have an estimated work time needed because this task can't be fulfilled");
		if(getStatus() == Status.AVAILABLE)
			return getEstimatedDuration();
		if(getStatus() == Status.FAILED)
			return getEstimatedDuration().add(getAlternativeTask().estimatedWorkTimeNeeded());
		if(getStatus() == Status.UNAVAILABLE)
		{
			Duration retDuration = getEstimatedDuration();
			for(Task prereq : getPrerequisiteTasks())
				retDuration = retDuration.add(prereq.estimatedWorkTimeNeeded());
			return retDuration;
		}
		//if(getStatus() == Status.FINISHED)
		return new Duration(0);
		
	}
	
	/**
	 * Checks whether this task can be fulfilled or already is fulfilled.
	 * 
	 * @return True if this task is finished or available.
	 *         False if this task is failed and doesn't have an alternative task.
	 *         True if this task is failed and has an alternative task that can be fulfilled.
	 *         False if this task is unavailable and any of the prerequisite tasks can't be fulfilled.
	 *         True if this task is unavailable and all prerequisite tasks can be fulfilled.
	 */
	public boolean canBeFulfilled()
	{
		if(getStatus() == Status.FINISHED)
			return true;
		else if(getStatus() == Status.AVAILABLE )
			return true;
		else if(getStatus() == Status.UNAVAILABLE)
		{
			for(Task t : getPrerequisiteTasks())
				if(!t.canBeFulfilled())
					return false;
			return true;
		}
		else //if(getStatus() == Status.FAILED)
			if(!hasAlternativeTask())
				return false;
			else
				return getAlternativeTask().canBeFulfilled();
	}
	
	/**
	 * Calculates the amount of time spent on this task.
	 * 
	 * @return If this task has a duration then the duration of the time span of this task is returned.
	 *         Otherwise a duration of 0 minutes is returned.
	 */
	public Duration getTimeSpent()
	{
		if(hasTimeSpan())
			return getTimeSpan().getDuration(); // TODO vervangen door werkelijke time spent ipv duration
		else 
			return new Duration(0);
	}
	/**
	 * Checks whether this task has an alternative task.
	 * 
	 * @return True if and only if this task has an alternative task.
	 */
	public boolean hasAlternativeTask()
	{
		return getAlternativeTask() != null;
	}

}
