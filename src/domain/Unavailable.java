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
    public void setAlternativeTask(Task task, Task alternativeTask, Project project) throws IllegalStateException, IllegalArgumentException {
        throw new IllegalStateException("Can't set an alternative task for this "
                + "task because the status of this task is not equal to FAILED.");
    }

    /**
     * Updates the status of this task to AVAILABLE if all the prerequisite
     * tasks are fulfilled.
     *
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
     * Change the status of the given task to the given status.
     * Unvailable can only be changed to unavailable
     * 
     * @throws IllegalArgumentException The given status isn't an unavailable status.
     */
    @Override
    void changeTo(Task task, Status status) {
        if(!(status instanceof Unavailable))
            throw new IllegalArgumentException("This task can't be updated to the given status.");
    }

}
