package domain;

/**
 * This class represents a finished status of a task.
 *
 * @author Frederic, Mathias, Pieter-Jan
 */
public class Finished extends Status {

    /**
     * Initializes this finished status.
     * 
     */
    public Finished() {
       
    }

    @Override
    public void setAlternativeTask(Task task, Task alternativeTask, Project project) throws IllegalStateException, IllegalArgumentException {
        throw new IllegalStateException("Can't set an alternative task for this "
                + "task because the status of this task is not equal to FAILED.");
    }

    /**
     * The status is finished, so there's nothing to update
     */
    @Override
    public void update(Task task) {
    }

    /**
     * Checks whether this task has been fulfilled.
     *
     * @return True because this status represents a finished status.
     */
    @Override
    public boolean isFulfilled(Task task) {

        return true;

    }

    /**
     * Checks whether this task was fulfilled before the given time span.
     *
     * @param timeSpan The time span to check.
     * @return True if this task is finished and this task ends before the given
     * time span. Otherwise the result is equal to whether or not the
     * alternative task of this task was fulfilled before the given time span.
     * @throws IllegalStateException If this task is not fulfilled.
     */
    @Override
    boolean isFulfilledBefore(Task task, Timespan timeSpan) throws IllegalStateException {
        return task.endsBefore(timeSpan);
    }

    /**
     *
     * @return True, because this task is finished.
     */
    @Override
    boolean canBeFulfilled(Task task) {
        return true;
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
