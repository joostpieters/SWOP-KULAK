package domain;

import domain.time.Duration;
import domain.time.Timespan;


public class Executing extends Status {

    @Override
    void setAlternativeTask(Task task, Task alternativeTask, Project project) throws IllegalStateException, IllegalArgumentException {
        throw new IllegalStateException("Can't set an alternative task for this "
                + "task because the status of this task is not equal to FAILED.");
    }

    @Override
    void update(Task task) {
        // TODO kan executing automatisch overgaan naar een andere status bvb unavailable?
    }
    
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
}
