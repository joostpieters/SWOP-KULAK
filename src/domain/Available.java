package domain;

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
    public void setAlternativeTask(Task task, Task alternativeTask, Project project) throws IllegalStateException, IllegalArgumentException {
        throw new IllegalStateException("Can't set an alternative task for this "
					+ "task because the status of this task is not equal to FAILED.");
    }
    
    /**
     * Updates the status of this task to UNAVAILABLE if not all the prerequisite
     * tasks are fulfilled.
     * 
     */
    @Override
    void update(Task task) {
        boolean unavailable = false;
        for (Task t : task.getPrerequisiteTasks()) {
            if (t.isFulfilled()) {
                unavailable = true;
            }
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
     * The estimated amount of work time needed for this task.
     * 
     * @return The estimated duration of this task. 
     */
    @Override
    Duration estimatedWorkTimeNeeded(Task task) {
        return task.getEstimatedDuration();
    }
    
    /**
     * Change the status of the given task to the given status.
     * Available can only be changed to finished, available or failed.
     * 
     * @throws IllegalArgumentException The given status isn't a failed,
     * finished or available status.
     */
    @Override
    void changeTo(Task task, Status status) {
        if(!(status instanceof Failed) && !(status instanceof Finished) && !(status instanceof Available))
            throw new IllegalArgumentException("This task can't be updated to the given status.");
        task.setStatus(status);
        
    }
    

}
