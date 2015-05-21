package domain.task;

import domain.time.Clock;
import domain.time.Duration;
import domain.time.Timespan;
import exception.ConflictException;

import java.util.ArrayList;


/**
 * This class represents the available status of a task and implements all the actions
 * of a task that are status dependent on available.
 * 
 * @author Mathias
 */
public class Available extends Status {

    /**
     * Initializes this available status.
     *
     */
    public Available() {

    }

    /**
     * Updates the status of this task to UNAVAILABLE if not all the
     * prerequisite tasks are fulfilled.
     *
     */
    @Override
    void update(Task task) {
        boolean unavailable = false;
        for (Task t : task.getPrerequisiteTasks()) {
            if (!t.isFulfilled()) {
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
     * The estimated amount of work time needed for this task.
     *
     * @return The estimated duration of this task.
     */
    @Override
    Duration estimatedWorkTimeNeeded(Task task) {
        return task.getEstimatedDuration();
    }

    /**
     * Checks whether this task was fulfilled before the given time span.
     *
     * @param task The task to check.
     * @param timeSpan The time span to check.
     * @return False, an available task can never be fulfilled
     * @throws IllegalStateException If this task is not fulfilled.
     */
    @Override
    boolean isFulfilledBefore(Task task, Timespan timeSpan) {
        return false;
    }

    /**
     * Transition to the failed state
     *
     * @param timespan The timespan of this failed task
     * @param task The task which is to be set to failed.
     *
     * @throws IllegalStateException This state can't transition to finished.
     * @throws IllegalArgumentException The given task can't have the given time
     * span as its time span
     */
    @Override
    void fail(Task task, Timespan timespan) {
        if (!canHaveAsTimeSpan(task, timespan)) {
            throw new IllegalArgumentException("The given task can't have the given time span as its time span");
        } else {
            task.setTimeSpan(timespan);
        }
        task.setStatus(new Failed());
    }

    /**
     * Moves the given task to the executing state
     *
     * @param task The task to adjust
     *
     */
    @Override
    public void execute(Task task, Clock clock) {
        if (task.hasPlanning()) {
            if (task.getPlanning().isBefore(clock.getTime())) {
                task.setStatus(new Executing());
                
            } else {
                try {
                    // unplanned execution
                    task.plan(clock.getTime(), new ArrayList<>(), clock);
                    task.setStatus(new Executing());
                } catch (ConflictException ex) {
                    throw new IllegalStateException("This task can't move to executing because there are not enough resources available");
                }
            }
        } else {
            throw new IllegalStateException("A task has to be planned before you can execute it!");
        }

    }
}
