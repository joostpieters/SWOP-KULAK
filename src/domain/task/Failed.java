package domain.task;

import domain.time.Duration;
import domain.time.Timespan;

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
    
    /**
     * {@inheritDoc}
     * 
     */
    @Override
    public void setAlternativeTask(Task task, Task alternativeTask) throws IllegalStateException, IllegalArgumentException {
        if (task.getAlternativeTask() != null) {
            throw new IllegalStateException(
                    "Can't set an alternative task for this task because "
                    + "this task already has an alternative task.");
        }
        
        if (alternativeTask == task) {
            throw new IllegalArgumentException(
                    "Can't set an alternative task for this task because "
                    + "this task is the same alternative task.");
        }
        if (alternativeTask.dependsOn(task)) {
            throw new IllegalArgumentException(
                    "Can't set an alternative task for this task because "
                    + "the alternative depends on this task.");
        }
        if (!task.getProject().hasTask(alternativeTask)) {
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
    @Override
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
        
        return task.getAlternativeTask().isFulfilledBefore(task.getAlternativeTask(), timeSpan); 
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
    
     /**
     * Calculates the amount of time spent on this task.
     *
     * @return If this task has a time span then the result is equal to the sum
     * of the maximum amount of time spent on a prerequisite task of this task
     * and the alternative task if this task has an alternative task and the
     * duration of the time span of this task. 
     */
    @Override
    public Duration getTimeSpent(Task task) {
        
            Duration temp, max = Duration.ZERO;
            for (Task t : task.getPrerequisiteTasks()) {
                temp = t.getTimeSpent();
                if (temp.compareTo(max) > 0) {
                    max = temp;
                }
            }
            temp = task.getTimeSpan().getDuration().add(max);
            if (task.hasAlternativeTask()) {
                return temp.add(task.getAlternativeTask().getTimeSpent());
            }
            return temp;
        
        
    }
}
