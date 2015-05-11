package domain.task;

import domain.time.Duration;
import domain.time.Timespan;

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

    /**
     * The status is finished, so there's nothing to update
     *
     * @param task The task to update the status of
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
     * @return True if and only if this task ended before the given timespan.
     * @see Task#endsBefore
     */
    @Override
    boolean isFulfilledBefore(Task task, Timespan timeSpan) {
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

    /**
     * Calculates the amount of time spent on the given task.
     *
     * @param task The task to check
     * @return The result is equal to the sum
     * of the maximum amount of time spent on a prerequisite task of this task
     * and the duration of the time span of this task.
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

        return temp;

    }
}
