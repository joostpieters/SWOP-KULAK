package domain;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.GregorianCalendar;

/**
 * This class represents a task
 * @author Frederic, Mathias, Pieter-Jan
 */
public class Task {

	private int id;

	private String description;

	private float acceptableDeviation;

	private Timespan timeSpan;

	private Duration estimatedDuration;

	private Task alternativeTask;

	private ArrayList<Task> prerequisiteTasks;

	private Status status;

	private static int nextId=0;

	/**
	 * Initializes this task based on the given description, estimated duration, acceptable deviation,
	 * prerequisite tasks and status.
	 * 
	 * @param description The description of this task.
	 * @param estDur The estimated duration in minutes of this task.
	 * @param accDev The acceptable deviation of this task expressed as a float between 0 and 1.
	 * @param prereq The list of prerequisite tasks which need to be finished before this task can be started.
	 * @param status The status of this task.
	 * @throws IllegalArgumentException The given acceptable deviation is not valid.
	 */
	public Task(String description, long estDur, float accDev, ArrayList<Task> prereq, Status status){
		if(!isValidAcceptableDeviation(accDev))
			throw new IllegalArgumentException("The acceptable deviation has to be between 0 and 1.");

		this.id = generateId();
		this.estimatedDuration = new Duration(estDur);
		this.id = Task.generateId();
		this.description = description;
		this.acceptableDeviation = accDev;
		this.prerequisiteTasks = prereq;
		this.status = status;
		if(this.prerequisiteTasks == null) // TODO mag dit null zijn?
			this.prerequisiteTasks = new ArrayList<Task>();
	}

	/**
	 * @return The identification number of this task.
	 */
	public int getId()
	{
		return this.id;
	}

	/**
	 * @return The description of this task.
	 */
	public String getDescription()
	{
		return this.description;
	}

	/**
	 * @return The estimated duration of this task.
	 */
	public Duration getEstimatedDuration()
	{
		return this.estimatedDuration;
	}

	/**
	 * @return The timeSpan indicating the actual start and end time of this task.
	 */
	public Timespan getTimeSpan()
	{
		return this.timeSpan;
	}
	/**
	 * @return The acceptable deviation of this task expressed as a float between 0 and 1.
	 */
	public float getAcceptableDeviation()
	{
		return this.acceptableDeviation;
	}

	/**
	 * @return The alternative task for this task.
	 */
	public Task getAlternativeTask()
	{
		return this.alternativeTask;
	}

	/**
	 * @return The list of prerequisite tasks for this task.
	 */
	public ArrayList<Task> getPrerequisiteTasks()
	{
		return (ArrayList<Task>)this.prerequisiteTasks.clone();
	}

	/**
	 * @return The status of this task.
	 */
	public Status getStatus()
	{
		return this.status;
	}
	
	/**
	 *  Calculates the duration by which this task been delayed.
	 *  
	 * @return The duration by which this task has been delayed if this task has a time span,
	 *         based on the maximum duration of this task.
	 *         If this task doesn't have a time span then the result is a duration of 0 minutes.
	 */
	public Duration getDelay()
	{
		if(getTimeSpan()==null)
			return new Duration(0);
		
		return getTimeSpan().getDelay(calculateMaxDuration());
	}
	
	/** TODO hoort dit wel in task?
	 * Calculates the maximum duration of this task by which this task will still be finished on time.
	 * 
	 * @return The estimated duration of this task multiplied by (1 + (the acceptable deviation of this task))
	 */
	private Duration calculateMaxDuration()
	{
		int maxMinutes = (int)(getEstimatedDuration().toMinutes()*(1+getAcceptableDeviation()));
		return new Duration(maxMinutes);
	}
	/**
	 * Checks whether the given acceptable deviation is valid.
	 * 
	 * @param  accDev The acceptable deviation.
	 * @return True if and only if the given acceptable deviation is between 0 and 1.
	 */
	private boolean isValidAcceptableDeviation(float accDev)
	{
		return (0 <= accDev) && (accDev <= 1);
	}
	
	/**
	 * Updates the status and the time span of this task.
	 * 
	 * @param start The beginning of the time span of this task.
	 * @param end The end of the time span of this task.
	 * @param status The new status of this task.
	 * @throws IllegalArgumentException The given status is neither FAILED nor FINISHED and is therefore
	 *                                  not a valid status that can be assigned to this task.
	 */
	public void update(LocalDateTime start, LocalDateTime end, Status status){
		if(status != Status.FINISHED && status != Status.FAILED)
			throw new IllegalArgumentException(
					"The given status is neither FAILED nor FINISHED and is therefore not a valid status.");

		setTimeSpan(new Timespan(start, end));
		setStatus(status);
		
	}

	/**
	 * Checks whether this task is available.
	 * 
	 * @return True if and only if every prerequisite task of this task has been fulfilled.
	 */
	public boolean isAvailable(){
		for(Task prereqTask : getPrerequisiteTasks())
		{
			if(!prereqTask.isFulfilled())
				return false;
		}

		return true;
	}

	/**
	 * Checks whether this task has been fulfilled.
	 * 
	 * @return True if and only if this task is finished or if it has an alternative task
	 *         and this alternative task is fulfilled.
	 */
	public boolean isFulfilled()
	{
		if(getStatus().equals(Status.FINISHED)) // TODO replace with isFinished()?
			return true;
		if(getAlternativeTask() != null)
			return getAlternativeTask().isFulfilled();
		return false;
	}

	/**
	 * Sets the status of this task to the given status.
	 * 
	 * @param status The new status of this task.
	 * @throws IllegalStateException If the given status is FINISHED while this task is not available.
	 */
	private void setStatus(Status status)
	{
		if(status.equals(Status.FINISHED) && !this.isAvailable())
			throw new IllegalStateException("Attempted to set task status to FINISHED while the task is not available.");
		this.status = status;
	}
	
	
	/**
	 * Sets the time span of this task to the given time span.
	 * 
	 * @param timeSpan The new time span of this task.
	 * @throws IllegalArgumentException If the given time span is not a valid time span for this task.
	 */
	private void setTimeSpan(Timespan timeSpan)
	{
		if(!isValidTimeSpan(timeSpan))
			throw new IllegalArgumentException("The given time span is not a valid time span for this task");
		this.timeSpan = timeSpan;
	}
	
	/**
	 * Checks whether this task ends before the given time.
	 * 
	 * @param startTime The time to check.
	 * @return True if the time span of this task ends before the given time.
	 * @throws IllegalStateException If this task does not have a time span.
	 */
	public boolean endsBefore(LocalDateTime startTime)
	{
		if(getTimeSpan() == null)
			throw new IllegalStateException(
					"Tried to check whether this task ends before the given time while this task doesn't have a time span.");
		return getTimeSpan().getEndTime().isBefore(startTime); // TODO misschien beter in Timespan klasse?
	}
	
	/** TODO unfinished
	 * Checks whether the given time span is a valid time span for this task.
	 * 
	 * @param timeSpan The time span to check.
	 * @return False if any of the prerequisite tasks has a time span that doesn't end before the given time span.
	 *         True in every other case.
	 */
	public boolean isValidTimeSpan(Timespan timeSpan) // TODO nog niet af.
	{
		if(getPrerequisiteTasks() != null)
		{
			for(Task t: getPrerequisiteTasks())
				if(t.hasTimeSpan() && !t.endsBefore(timeSpan.getStartTime()))
					return false;
		}
		/*TODO
		 * check whether every parent starts after the given time span
		 */
		return true;
	}
	/**
	 * Checks whether this task has a time span.
	 * @return True if and only if the time span of this task is not null.
	 */
	public boolean hasTimeSpan()
	{
		return getTimeSpan() != null;
	}
	
	/**
	 * Checks whether the given task is a valid alternative task for this task.
	 * 
	 * @param altTask The alternative task to check.
	 * @return False if the given alternative task is equal to this task.
	 *         False if this task depends on the given alternative task.
	 *         False if the given alternative task depends on this task.
	 *         True otherwise.
	 */
	public boolean isValidAlternativeTask(Task altTask)
	{
		if(altTask.equals(this))
			return false;
		if(this.dependsOn(altTask))
			return false;
		if(altTask.dependsOn(this))
			return false;
		return true;
	}

	/**
	 * Checks whether this task directly or indirectly depends on the given task.
	 * 
	 * @param task The task to check.
	 * @return True if this task has an alternative task and the alternative task is equal to the given task.
	 *         True if a prerequisite task of this task is equal to the given task.
	 *         True if a prerequisite task depends on the given task.
	 *         False otherwise.
	 */
	public boolean dependsOn(Task task)
	{
		if(getAlternativeTask() != null && getAlternativeTask().equals(task))
			return true;
		if(getPrerequisiteTasks() != null)
		{
			for(Task prereqTask : getPrerequisiteTasks())
			{
				if(prereqTask.equals(task))
					return true;

				if(prereqTask.dependsOn(task))
					return true;
			}
		}
		return false;
	}
	
	/**
	 * Sets the alternative task of this task to the given alternative task.
	 * 
	 * @param alternativeTask The new alternative task for this task.
	 */
	private void setAlternativeTask(Task alternativeTask)
	{
		this.alternativeTask = alternativeTask;
	}
	/**
	 * Generates an id for a new task.
	 * 
	 * @return The id to be used for a newly created task.
	 */
	private static int generateId()
	{
		return nextId++;
	}

}
