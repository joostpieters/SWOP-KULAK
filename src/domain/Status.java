package domain;

/**
 * This class represents the status of a task, all satuses are mutually exclusive.
 * 
 * @author Frederic, Mathias, Pieter-Jan 
 */
public abstract class Status {

   
    public Status(){
    }
   
    
    /**
	 * Sets the alternative task of the given task to the given alternative task.
         * 
	 * @param 	task
	 *        	The task to set the alternative for.
	 * @param 	alternativeTask
	 *        	The alternative task for this task.
	 * @param   project
	 *          The project to which this task and the given alternative task belong to.
	 * @throws 	IllegalStateException
	 *         	If the state of this task is not FAILED.
	 * @throws 	IllegalStateException
	 *         	If this task already has an alternative task.
	 * @throws 	IllegalArgumentException
	 *         	If this task can't have the given task as its alternative task.
	 * @throws  IllegalArgumentException
	 *          If this task and the given alternative task don't both belong to the given project
	 * @see    	canHaveAsAlternativeTask
	 */
	abstract void setAlternativeTask(Task task, Task alternativeTask, Project project) throws IllegalStateException, IllegalArgumentException;
        
        /**
	 * Update the status of the given task to based on the changes made in this task.
	 */
	abstract void update(Task task);
        
        /**
	 * Checks whether the given task has been fulfilled.
	 * 
         * @param       task
         *              The task to check if it is fulfilled.
	 * @return 	True if and only if this task is finished or if it has an alternative task
	 *         	and this alternative task is fulfilled.
	 */
	boolean isFulfilled(Task task)
	{
		if(task.getAlternativeTask() != null)
			return task.getAlternativeTask().isFulfilled();
		return false;
	}
        
        /**
	 * Checks whether this task was fulfilled before the given time span.
	 * 
         * @param 	task
	 *        	The task to check.
	 * @param 	timeSpan
	 *        	The time span to check.
	 * @return 	True if this task is finished and this task ends before the given time span.
	 *         	Otherwise the result is equal to whether or not the alternative task of this task was fulfilled
	 *         	before the given time span.
	 * @throws 	IllegalStateException
	 *         	If this task is not fulfilled.
	 */
	boolean isFulfilledBefore(Task task, Timespan timeSpan) throws IllegalStateException
	{
		if(!isFulfilled(task))
			throw new IllegalStateException("Tried to check whether this task was "
					+ "fulfilled before the given time span while this task is not fulfilled.");
		
		return task.getAlternativeTask().isFulfilledBefore(timeSpan);
	}
        
        /**
	 * Checks whether the given task can be fulfilled or already is fulfilled,
         * based on the current status.
	 * 
	 * @return True if this task is finished or available.
	 *         False if this task is failed and doesn't have an alternative task.
	 *         True if this task is failed and has an alternative task that can be fulfilled.
	 *         False if this task is unavailable and any of the prerequisite tasks can't be fulfilled.
	 *         True if this task is unavailable and all prerequisite tasks can be fulfilled.
	 */
	abstract boolean canBeFulfilled(Task task);   
        
        /**
	 * The estimated amount of work time needed for this task.
	 * 
	 * @return The estimated worktime needed based on the implementing status.
	 *         
	 */
	abstract Duration estimatedWorkTimeNeeded(Task task);
        
        /**
	 * Change the status of the given task to the given status.
	 * 
	 */
	abstract void changeTo(Task task, Status status);
    
}
