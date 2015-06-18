package domain.task;

import domain.time.Duration;
import domain.time.Timespan;


/**
 * This class represents the executing status of a task and implements all the actions
 * of a task that are status dependent on executing.
 * 
 * @author Mathias
 */
public class Executing extends Status {

    /**
     * Updates the status to the according status.
     * 
     * @param task The task to update the status of.
     */
    @Override
    void update(Task task) { }
    
    /**
     * Check whether the given task is fulfilled based on this status.
     * 
     * @param task The task to check
     * @return False, an executing task can never be fulfilled.
     */
    @Override
    boolean isFulfilled(Task task) {
        return false;
    }
    
    /**
     * Check whether the given task is fulfilled before the given timespan
     * based on this status.
     * 
     * @param task The task to check
     * @return False, an executing task can never be fulfilled.
     */
    @Override
    boolean isFulfilledBefore(Task task, Timespan timeSpan) {
        return false;
    }
    
     /**
     *
     * @return True, because this task is executing.
     */
    @Override
    boolean canBeFulfilled(Task task) {
        return true;
    }

    /**
     * The estimated amount of work time needed for this task.
     *
     * @return The estimated duration of the given task.
     */
    @Override
    Duration estimatedWorkTimeNeeded(Task task) {
        return task.getEstimatedDuration();
    } 
    
    /**
    * Transition to the failed state
    * 
    * @param timespan The time span of this failed task
    * @param task The task which is to be set to failed.
    * 
    * @throws IllegalStateException This state can't transition to finished.
    * @throws IllegalArgumentException The given task can't have the given time span as its time span 
   */
   @Override
   void fail(Task task, Timespan timespan) {
       if(!canHaveAsTimeSpan(task, timespan))
           throw new IllegalArgumentException("The given task can't have the given time span as its time span");
       
       task.setTimeSpan(timespan);
       task.setStatus(new Failed());
   }
   
   /**
    * Transition to the finished state
    * 
    * @param timespan The time span of this finished task
    * @param task The task which is to be set to failed.
    * 
    * @throws IllegalStateException This state can't transition to finished.
    * @throws IllegalArgumentException The given task can't have the given time span as its time span 
   */
   @Override
   void finish(Task task, Timespan timespan) throws IllegalStateException {
       if(!canHaveAsTimeSpan(task, timespan))
           throw new IllegalArgumentException("The given task can't have the given time span as its time span");
       
       task.setTimeSpan(timespan);
       task.setStatus(new Finished());
   }   
}
