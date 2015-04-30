package domain;

import domain.time.Duration;
import domain.time.Timespan;
import java.time.LocalDateTime;

/**
 * This class represents the status of a task, all satuses are mutually
 * exclusive.
 *
 * @author Frederic, Mathias, Pieter-Jan
 */
public abstract class Status {

    public Status() {
    }

    /**
     * Sets the alternative task of the given task to the given alternative
     * task.
     *
     * @param task The task to set the alternative for.
     * @param alternativeTask The alternative task for this task.
     * @throws IllegalStateException If the state of this task is not FAILED.
     * @throws IllegalStateException If this task already has an alternative
     * task.
     * @throws IllegalArgumentException If this task can't have the given task
     * as its alternative task.
     * @see canHaveAsAlternativeTask
     */
    public void setAlternativeTask(Task task, Task alternativeTask) throws IllegalStateException {
        throw new IllegalStateException("Can't set an alternative task for this "
                + "task because the status of this task is not equal to FAILED.");
    }


    /**
     * Update the status of the given task to based on the changes made in this
     * task.
     */
    abstract void update(Task task);

    /**
     * Checks whether the given task has been fulfilled.
     *
     * @param task The task to check if it is fulfilled.
     * @return True if and only if this task is finished or if it is failed and has an
     * alternative task and this alternative task is fulfilled.
     */
    abstract boolean isFulfilled(Task task);

    /** TODO
     * Checks whether the given task was fulfilled before the given time span.
     *
     * @param task The task to check.
     * @param timeSpan The time span to check.
     * @return True if this task is finished and this task ends before the given
     * time span. Otherwise the result is equal to whether or not the
     * alternative task of this task was fulfilled before the given time span.
     * 
     */
    abstract boolean isFulfilledBefore(Task task, Timespan timeSpan);

    /**
     * Checks whether the given task can be fulfilled or already is fulfilled,
     * based on the current status.
     *
     * @return True if this task is finished or available. False if this task is
     * failed and doesn't have an alternative task. True if this task is failed
     * and has an alternative task that can be fulfilled. False if this task is
     * unavailable and any of the prerequisite tasks can't be fulfilled. True if
     * this task is unavailable and all prerequisite tasks can be fulfilled.
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
     * Transition to the failed state
     * 
     * @param timespan The timespan of this failed task
     * @param task The task which is to be set to failed.
     * 
     * @throws IllegalStateException This state can't transition to finished.
    */
    void fail(Task task, Timespan timespan) throws IllegalStateException
    {
        throw new IllegalStateException("This status can't transition to failed.");
    }
    
    /**
     * Transition to the finished state
     * 
     * @param timespan The timespan of this finished task
     * @param task The task which is to be set to finished.
     * 
     * @throws IllegalStateException This state can't transition to failed.
    */
    void finish(Task task, Timespan timespan) throws IllegalStateException
    {
        throw new IllegalStateException("This status ("+getClass().getSimpleName() + ") can't transition to finished.");
    }
    
    /**
     * Checks whether the given time span is a valid time span for this task.
     *
     * @param task The task to check
     * @param timeSpan The time span to check.
     * @return False if any of the prerequisite tasks were fulfilled before the
     * given time span.
     * @see isFulfilledBefore
     */
    protected boolean canHaveAsTimeSpan(Task task, Timespan timeSpan) {
        for (Task t : task.getPrerequisiteTasks()) {
            
            if (t.isFulfilled() && !t.getStatus().isFulfilledBefore(t,timeSpan)) {
                return false;
            }
        }
        return true;
    }
    
    /**
     * Moves the given task to the executing state
     * 
     * @param task The task to adjust
     * @param currenTime The current time of the system
     * @throws IllegalStateException The task can't move to executing from this
     * state.
     */
    public void execute(Task task, LocalDateTime currenTime) throws IllegalStateException {
        throw new IllegalStateException("This task can't execute, because it's not available.");
    }
    
    /**
     * Check whether the given object is the same status as this status
     * 
     * @param o The object to check
     * @return True if and only if the given object is a status and is of the 
     * same class of this status.
     */
    @Override
    public boolean equals(Object o){
        if(o instanceof Status){
            return hashCode() == o.hashCode();
        }
        
        return false;
    }

    /**
     * Generates a hashcode for this status
     * 
     * @return A positive integer that represents this status, statuses of 
     * same classes have the same hashcode.
     */
    @Override
    public int hashCode() {
        int hash = this.getClass().hashCode();
        return hash;
    }
    
    /**
     * 
     * @return The name of this class, which equals the name of the status
     */
    @Override
    public String toString(){
        return getClass().getSimpleName();
    }
    
     /**
     * Calculates the amount of time spent on the given task.
     *
     * @param task The task to check
     * @return If this task has a time span then the result is equal to the sum
     * of the maximum amount of time spent on a prerequisite task of this task
     * and the alternative task if this task has an alternative task and the
     * duration of the time span of this task. Otherwise a duration of 0 minutes
     * is returned.
     */
    public Duration getTimeSpent(Task task) {
        return new Duration(0);
    }
}
