package domain;

import java.time.LocalDateTime;

/**
 * This class represents a task
 * @author Frederic, Mathias, Pieter-Jan
 */
public class Task {

	private int id;

	private String description;

	private int acceptableDeviation;

	private Timespan timeSpan;

	private Duration estimatedDuration;

	private Task alternativeTask;

	private Task[] prerequisiteTasks;

	private Status status;

	private static int nextId=0;

	/**
	 * Initializes this task based on the given description, estimated duration, acceptable deviation,
	 * prerequisite tasks, status and time span.
	 * 
	 * @param description
	 *        The description of this task.
	 * @param estDur
	 *        The estimated duration in minutes of this task.
	 * @param accDev
	 *        The acceptable deviation of this task expressed as an integer between 0 and 100.
	 * @param prereq
	 *        The list of prerequisite tasks which need to be finished before this task can be started.
	 * @param status
	 *        The status of this task.
	 * @param timeSpan
	 *        The time span of this task.
	 */
	public Task(String description, long estDur, int accDev, Task[] prereq, Status status,
			LocalDateTime startTime, LocalDateTime endTime){
		this.id = generateId();
		setDescription(description);
		this.estimatedDuration = new Duration(estDur);
		setAcceptableDeviation(accDev);
		if(prereq == null)
			setPrerequisiteTasks(new Task[] {});
		else
			setPrerequisiteTasks(prereq);
		
		// first we set the status of this task to UNAVAILABLE or AVAILABLE
		// to make sure the state of this task is valid.
		updateStatus(); 
		
		if(status!=null)
			update(startTime, endTime, status);
	}
	/**
	 * Initializes this task based on the given description, estimated duration,
	 * acceptable deviation, status and time span.
	 * 
	 * @param description
	 *        The description of this task.
	 * @param estDur
	 *        The estimated duration in minutes of this task.
	 * @param accDev
	 *        The acceptable deviation of this task expressed as an integer between 0 and 100.
	 * @param status
	 *        The status of this task.
	 * @param timeSpan
	 *        The time span of this task.
	 */
	public Task(String description, long estDur, int accDev, Status status,
			LocalDateTime startTime, LocalDateTime endTime)
	{
		this(description, estDur, accDev, null, status, startTime, endTime);
	}
	
	/**
	 * Initializes this task based on the given description, estimated duration, acceptable deviation
	 *  and prerequisite tasks.
	 *  
	 * @param description
	 *        The description of this task.
	 * @param estDur
	 *        The estimated duration in minutes of this task.
	 * @param accDev
	 *        The acceptable deviation of this task expressed as an integer between 0 and 100.
	 * @param prereq
	 *        The list of prerequisite tasks for this task.
	 */
	public Task(String description, long estDur, int accDev, Task[] prereq)
	{
		this(description, estDur, accDev, prereq, null, null, null);
	}

	/**
	 * Initializes this task based on the given description, estimated duration
	 * and acceptable deviation.
	 *  
	 * @param description
	 *        The description of this task.
	 * @param estDur
	 *        The estimated duration in minutes of this task.
	 * @param accDev
	 *        The acceptable deviation of this task expressed as an integer between 0 and 100.
	 */
	public Task(String description, long estDur, int accDev)
	{
		this(description, estDur, accDev, null, null, null);
	}
	
	/**
	 * Sets the description of this task to the given description.
	 * 
	 * @param description
	 *        The new description of this task.
	 * @throws IllegalArgumentException
	 *         If this task can't have the given description as its description.
	 */
	private void setDescription(String description)
	{
		if(!canHaveAsDescription(description))
			throw new IllegalArgumentException("The given description is uninitialized.");
		else
			this.description = description;
	}

	/**
	 * Checks whether this task can have the given description as its description.
	 * 
	 * @param description The description to check.
	 * @return True if and only if the given description is not equal to null.
	 */
	public boolean canHaveAsDescription(String description)
	{
		return description != null;
	}
	/**
	 * Sets the list of prerequisite tasks to the given list of prerequisite tasks.
	 * 
	 * @param prereq
	 *        The new list of prerequisite tasks for this task.
	 * @throws IllegalArgumentException
	 *         If this task can't have the given list of prerequisite tasks as its list of prerequisite tasks.
	 */
	private void setPrerequisiteTasks(Task[] prereq)
	{
		if(!canHaveAsPrerequisiteTasks(prereq))
			throw new IllegalArgumentException(
					"This task can't have the given list of prerequisite tasks as its prerequisite tasks.");
		this.prerequisiteTasks = prereq;
	}

	/**
	 * Checks whether this task can have the given list of prerequisite tasks as its prerequisite tasks.
	 * 
	 * @param  prereq
	 *         The list of prerequisite tasks to check.
	 * @return False if the list is null.
	 *         False if any of the tasks in the given list is null.
	 *         False if any of the tasks in the given list is equal to this task.
	 *         True otherwise.
	 */
	public boolean canHaveAsPrerequisiteTasks(Task[] prereq)
	{
		if(prereq==null)
			return false;
		for(Task t: prereq)
			if(t==null || t==this)
				return false;
		/**
		 * TODO lus detectie
		 */
		return true;
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
	 * @return The acceptable deviation of this task expressed as an int between 0 and 100.
	 */
	public int getAcceptableDeviation()
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
	public Task[] getPrerequisiteTasks()
	{
		return (Task[])this.prerequisiteTasks.clone();
	}

	/**
	 * @return The status of this task.
	 */
	public Status getStatus()
	{
		if(this.status == Status.UNAVAILABLE)
			updateStatus();
		return this.status;
	}


	/**
	 * Updates the status of this task to AVAILABLE if all the prerequisite tasks are
	 * fulfilled and if the status of this task is currently equal to UNAVAILABLE or not initialized.
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

		return getTimeSpan().getExcess( calculateMaxDuration() );
	}

	/**
	 * Calculates the maximum duration of this task by which this task will still be finished on time.
	 * 
	 * @return The estimated duration of this task multiplied by (100 + (the acceptable deviation of this task))/100
	 */
	private Duration calculateMaxDuration()
	{
		return getEstimatedDuration().multiplyBy( (100d + getAcceptableDeviation())/100d );
	}

	/**
	 * Sets this task his acceptable deviation to the given acceptable deviation.
	 * 
	 * @param accDev
	 *        
	 * @throws IllegalArgumentException
	 *         If this task can't have the given acceptable deviation as its acceptable deviation.
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
	 * @param  accDev
	 *         The acceptable deviation.
	 * @return True if and only if the given acceptable deviation is between 0 and 100.
	 */
	public boolean canHaveAsAcceptableDeviation(int accDev)
	{
		return (0 <= accDev) && (accDev <= 100);
	}
	
	/**
	 * Updates the status and the time span of this task.
	 * 
	 * @param timeSpan
	 *        The new time span of this task.
	 * @param status
	 *        The new status of this task.
	 * @throws IllegalArgumentException
	 *         The given status is neither FAILED nor FINISHED and is therefore
	 *         not a valid status that can be assigned to this task.
	 */
	/*public void update(Timespan timeSpan, Status status){
		if( (!status.equals(Status.FINISHED)) && !status.equals(Status.FAILED) )
			throw new IllegalArgumentException(
					"The given status is neither FAILED nor FINISHED and is therefore not a valid status "
							+ "that can be assigned to this task.");

		setTimeSpan(timeSpan);
		setStatus(status);

	}*/
	/**
	 * Updates the status and the time span of this task.
	 * 
	 * @param start
	 *        The beginning of the time span of this task.
	 * @param end
	 *        The end of the time span of this task.
	 * @param status
	 *        The new status of this task.
	 * @throws IllegalArgumentException
	 *         The given status is neither FAILED nor FINISHED and is therefore
	 *         not a valid status that can be assigned to this task.
	 */
	public void update(LocalDateTime start, LocalDateTime end, Status status){
		if( (!status.equals(Status.FINISHED)) && !status.equals(Status.FAILED) )
			throw new IllegalArgumentException(
					"The given status is neither FAILED nor FINISHED and is therefore not a valid status "
							+ "that can be assigned to this task.");
		//TODO check of start, end == null?
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
		if(getStatus().equals(Status.FINISHED))
			return true;
		if(getAlternativeTask() != null)
			return getAlternativeTask().isFulfilled();
		return false;
	}

	/**
	 * Sets the status of this task to the given status.
	 * 
	 * @param status
	 *        The new status of this task.
	 * @throws IllegalStateException
	 *         If the given status is FINISHED while this task is not available or
	 *         if the given status is FAILED while this task is already finished.
	 */
	private void setStatus(Status status)
	{
		if(status.equals(Status.FINISHED) && getStatus() != Status.AVAILABLE)
			throw new IllegalStateException("Attempted to set task status to FINISHED "
					+ " while the task is not available.");
		if(status.equals(Status.FAILED) && getStatus() == Status.FINISHED)
			throw new IllegalStateException("Attempted to set task status to FAILED "
						+ " while the task is already finished.");
		this.status = status;
	}


	/**
	 * Sets the time span of this task to the given time span.
	 * 
	 * @param  timeSpan
	 *         The new time span of this task.
	 * @throws IllegalArgumentException
	 *         If this task can't have the given time span as its time span.
	 * @see    canHaveAsTimeSpan
	 */
	private void setTimeSpan(Timespan timeSpan)
	{
		if(!canHaveAsTimeSpan(timeSpan))
			throw new IllegalArgumentException("The given time span is not a valid time span for this task");
		this.timeSpan = timeSpan;
	}

	/**
	 * Checks whether this task ends before the given time span.
	 * 
	 * @param timeSpan
	 *        The time span to check.
	 * @return True if the time span of this task ends before the start of the given time span.
	 * @throws IllegalStateException
	 *         If this task does not have a time span.
	 */
	public boolean endsBefore(Timespan timeSpan)
	{
		if(getTimeSpan() == null)
			throw new IllegalStateException(
					"Tried to check whether this task ends before the given time while this task doesn't have a time span.");
		return getTimeSpan().endsBefore(timeSpan);
	}

	/** TODO unfinished
	 * Checks whether the given time span is a valid time span for this task.
	 * 
	 * @param timeSpan
	 *        The time span to check.
	 * @return False if any of the prerequisite tasks has a time span that doesn't end before the given time span.
	 *         True in every other case.
	 */
	public boolean canHaveAsTimeSpan(Timespan timeSpan) // TODO nog niet af.
	{
		if(getPrerequisiteTasks() != null)
		{
			for(Task t: getPrerequisiteTasks())
				if(t.hasTimeSpan() && !t.endsBefore(timeSpan))
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
	 * @param alternativeTask
	 *        The alternative task for this task.
	 * @throws IllegalStateException
	 *         If the state of this task is not FAILED.
	 * @throws IllegalStateException
	 *         If this task already has an alternative task.
	 * @throws IllegalArgumentException
	 *         If this task can't have the given task as its alternative task.
	 * @see    canHaveAsAlternativeTask
	 */
	public void setAlternativeTask(Task alternativeTask)
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
	 * @param altTask
	 *        The alternative task to check.
	 * @return False if the given alternative task is equal to null.
	 *         False if the given alternative task is equal to this task.
	 *         False if the given alternative task depends on this task.
	 *         True otherwise.
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
	 * Generates an id for a new task.
	 * 
	 * @return The id to be used for a newly created task.
	 */
	private static int generateId()
	{
		return nextId++;
	}

	/**
	 * Checks whether the status of this task is equal to finished.
	 * 
	 * @return True if and only if the status of this task is equal to finished.
	 */
	public boolean isFinished() // TODO is dit nodig?
	{
		return getStatus()==Status.FINISHED;
	}

}
