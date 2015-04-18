package domain;

import java.util.Map.Entry;

/**
 * This class represents an available status of a task.
 *
 * @author Frederic, Mathias, Pieter-Jan
 */
public class Available extends Status {

    /**
     * Initializes this available status.
     *
     */
    public Available() {

    }

    @Override
    public void setAlternativeTask(Task task, Task alternativeTask, Project project) throws IllegalStateException {
        throw new IllegalStateException("Can't set an alternative task for this "
                + "task because the status of this task is not equal to FAILED.");
    }

    /**
     * Updates the status of this task to UNAVAILABLE if not all the
     * prerequisite tasks are fulfilled. Or if not  all resources are available
     * at current system time //TODO
     *
     */
    @Override
    void update(Task task) {
        boolean unavailable = false;
        for (Task t : task.getPrerequisiteTasks()) {
            if (!t.isFulfilled()) {
                unavailable = true;
            }
        }
        
        for (Entry<ResourceType, Integer>  t : task.getRequiredResources().entrySet()) {
            //TODO
           t.getKey().getAvailability();
        }
        

        if (unavailable) {
            task.setStatus(new Unavailable());
        }
    }

    /**
     *
     * @return True, because this task is available.
     */
    @Override
    boolean canBeFulfilled(Task task) {
        return true;
    }
    
    /**
     * Checks whether the given task has been fulfilled.
     *
     * @param task The task to check if it is fulfilled.
     * @return False an available task can never be fulfilled
     */
    @Override
    boolean isFulfilled(Task task) {
        return false;
    }


    /**
     * The estimated amount of work time needed for this task.
     *
     * @return The estimated duration of this task.
     */
    @Override
    Duration estimatedWorkTimeNeeded(Task task) {
        return task.getEstimatedDuration();
    }

    /**
     * Checks whether this task was fulfilled before the given time span.
     *
     * @param task The task to check.
     * @param timeSpan The time span to check.
     * @return False, an available task can never be fulfilled
     * @throws IllegalStateException If this task is not fulfilled.
     */
    @Override
    boolean isFulfilledBefore(Task task, Timespan timeSpan) {
        return false;
    }
    
     /**
     * Transition to the failed state
     * 
     * @param timespan The timespan of this failed task
     * @param task The task which is to be set to failed.
     * 
     * @throws IllegalStateException This state can't transition to finished.
     * @throws IllegalArgumentException The given task can't have the given time span as its time span 
    */
    @Override
    void fail(Task task, Timespan timespan)
    {
        if(!canHaveAsTimeSpan(task, timespan))
            throw new IllegalArgumentException("The given task can't have the given time span as its time span");
        else
            task.setTimeSpan(timespan);
        task.setStatus(new Failed());
    }
    
    /**
     * Transition to the finished state
     * 
     * @param timespan The timespan of this finished task
     * @param task The task which is to be set to failed.
     * 
     * @throws IllegalStateException This state can't transition to finished.
     * @throws IllegalArgumentException The given task can't have the given time span as its time span 
    */
    @Override
    void finish(Task task, Timespan timespan) throws IllegalStateException
    {
        if(!canHaveAsTimeSpan(task, timespan))
            throw new IllegalArgumentException("The given task can't have the given time span as its time span");
        else
            task.setTimeSpan(timespan);
        task.setStatus(new Finished());
    }
    
     /**
     * Moves the given task to the executing state
     * 
     * @param task The task to adjust
     */
    @Override
    public void execute(Task task){
        task.setStatus(new Executing());
    }
}
