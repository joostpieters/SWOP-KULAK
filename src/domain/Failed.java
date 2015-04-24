package domain;

import time.Duration;
import time.Timespan;

/**
 * This class represents a failed status of a task.
 *
 * @author Frederic, Mathias, Pieter-Jan
 */
public class Failed extends Status {

    /**
     * Initializes this failed status.
     */
    public Failed() {
       
    }

    @Override
    public void setAlternativeTask(Task task, Task alternativeTask, Project project) throws IllegalStateException, IllegalArgumentException {
        if (task.getAlternativeTask() != null) {
            throw new IllegalStateException(
                    "Can't set an alternative task for this task because "
                    + "this task already has an alternative task.");
        }

        if (!task.canHaveAsAlternativeTask(alternativeTask, project)) {
            throw new IllegalArgumentException(
                    "This task can't have the given task as its alternative task.");
        }
        task.setAlternativeTaskRaw(alternativeTask);
    }

    /**
     * The status is failed, so there's nothing to update
     * 
     * @param task The task the status has to be updated of.
     */
    @Override
    void update(Task task) {
    }
    
    /**
     * Checks whether the given task has been fulfilled.
     *
     * @param task The task to check if it is fulfilled.
     * @return True if and only if the given task has an alternative task and the alternative task is fulfilled.
     */
    boolean isFulfilled(Task task) {
        if (task.getAlternativeTask() != null) {
            return task.getAlternativeTask().isFulfilled();
        }
        return false;
    }
    
    /**
     * Checks whether the given task was fulfilled before the given time span.
     *
     * @param task The task to check.
     * @param timeSpan The time span to check.
     * @return True if this task is finished and this task ends before the given
     * time span. Otherwise the result is equal to whether or not the
     * alternative task of this task was fulfilled before the given time span.
     */
    @Override
    boolean isFulfilledBefore(Task task, Timespan timeSpan) {
        if (!task.isFulfilled() || !task.hasAlternativeTask()) {
           return false;
        }
        
        return task.getAlternativeTask().getStatus().isFulfilledBefore(task.getAlternativeTask(), timeSpan); // TODO misschien isFulfilledBefore aanmaken in Task en delegeren naar Status.isFulfilledBefore
    }
    
    /**
     * 
     * @return False if this task doesn't have an alternative task.
     *         True if this task has an alternative task that can be fulfilled.
     */
    @Override
    boolean canBeFulfilled(Task task) {
        if (!task.hasAlternativeTask()) {
            return false;
        } else {
            return task.getAlternativeTask().canBeFulfilled();
        }
    }
    
    /**
     * The estimated amount of work time needed for this task.
     * 
     * @return A duration of 0
     */
    @Override
    Duration estimatedWorkTimeNeeded(Task task) {
        return Duration.ZERO;
    }
    
    
}
