package domain;

/**
 * This class represents a unavailable status of a task.
 *
 * @author Frederic, Mathias, Pieter-Jan
 */
public class Unavailable extends Status {

    /**
     * Initializes this unavailable status.
     */
    public Unavailable() {
       
    }

    @Override
    public void setAlternativeTask(Task task, Task alternativeTask, Project project) throws IllegalStateException {
        throw new IllegalStateException("Can't set an alternative task for this "
                + "task because the status of this task is not equal to FAILED.");
    }

    /**
     * Updates the status of this task to AVAILABLE if all the prerequisite
     * tasks are fulfilled.
     *
     * @param task The task to update the status of
     */
    @Override
    public void update(Task task) {
        boolean available = true;
        for (Task t : task.getPrerequisiteTasks()) {
            if (!t.isFulfilled()) {
                available = false;
            }
        }

        if (available) {
            task.setStatus(new Available());
        }
    }

    /**
     *
     * @return True if and only if all prerequisite tasks can be fullfilled.
     */
    @Override
    boolean canBeFulfilled(Task task) {
        for (Task t : task.getPrerequisiteTasks()) {
            if (!t.canBeFulfilled()) {
                return false;
            }
        }
        return true;
    }

    /**
     * The estimated amount of work time needed for this task.
     * 
     * @return The sum of the amounts of estimated work time needed of the
     * prerequisite tasks of this task + the estimated duration of this task +
     * the estimated durations of the alternatives of the prerequisites of this
     * task.
     */
    @Override
    Duration estimatedWorkTimeNeeded(Task task) {
        Duration retDuration = task.getEstimatedDuration();
        for (Task prereq : task.getPrerequisiteTasks()) {
            retDuration = retDuration.add(prereq.estimatedWorkTimeNeeded());
            if (prereq.hasAlternativeTask()) {
                retDuration = retDuration.add(prereq.getAlternativeTask().getEstimatedDuration());
            }
        }
        return retDuration;
    }
    
     /**
     * Checks whether this task was fulfilled before the given time span.
     *
     * @param task The task to check.
     * @param timeSpan The time span to check.
     * @return False, an unavailable task can never be fulfilled
     */
    @Override
    boolean isFulfilledBefore(Task task, Timespan timeSpan) {
        return false;
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
}
