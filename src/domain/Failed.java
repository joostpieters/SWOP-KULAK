package domain;

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

        if (!task.canHaveAsAlternativeTask(alternativeTask)) {
            throw new IllegalArgumentException(
                    "This task can't have the given task as its alternative task.");
        }

        if (!(project.hasTask(task.getId()) && project.hasTask(alternativeTask.getId()))) {
            throw new IllegalArgumentException("This task and/or the given alternative task don't belong to the given project");
        }

        task.alternativeTask = alternativeTask;
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
     * Change the status of the given task to the given status.
     * Failed can only be changed to failed
     * 
     * @throws IllegalArgumentException The given status isn't a failed status.
     */
    @Override
    void changeTo(Task task, Status status) {
        if(! (status instanceof Failed) )
            throw new IllegalArgumentException("This task can't be updated to the given status.");
    }

}
