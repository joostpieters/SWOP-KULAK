package domain.dto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import domain.time.Clock;
import domain.time.Duration;

/**
 * This interface provides methods to retrieve all data from a project object.
 * 
 * @author Frederic, Mathias, Pieter-Jan
 */
public interface DetailedProject {

    /**
     * @return the creationTime of this project.
     */
    LocalDateTime getCreationTime();

    /**
     * @return the description of this project.
     */
    String getDescription();

    /**
     * @return the dueTime of this project.
     */
    LocalDateTime getDueTime();

    /**
     * Gets the total delay of this project.
     *
     * @param clock The clock to reltively check the delay to.
     * @return The sum of the delays of the tasks within this project.
     */
    public Duration getDelay(Clock clock);

    /**
     * **************************************
     * Getters & setters	*
     ***************************************
     */
    /**
     * @return the id of this project.
     */
    int getId();

    /**
     * @return the name of this project.
     */
    String getName();

    /**
     * Check whether this project is finished or not.
     *
     * @return	true if this all tasks in this project have been finished, false
     * otherwise.
     * @throws IllegalArgumentException if this project doesn't have any tasks
     * yet.
     */
    boolean isFinished();

    /**
     * Get the time details for this project.
     *
     * @param clock The clock to check to relatively check this project is on time.
     * @return	true if this project finished on time or if this project is
     * estimated to finish on time, false otherwise.
     */
    boolean isOnTime(Clock clock);

    /**
     *
     * @return A list containing all projects ascociated with this project.
     */
    List<? extends DetailedTask> getTasks();

    /**
     * Return all tasks which can cause this project to get overdue and the
     * percentage the project will be late because of the task.
     *
     * @param clock Clock to check overdue
     * @return	a map of tasks for which the work time needed is greater than 
     * the due time of this project minus the system time  to their corresponding
     * percentage by which they are over time.
     *
     */
    Map<? extends DetailedTask, Double> getUnacceptablyOverdueTasks(Clock clock); 
    
    /**
     * Get the amount of working hours that have been put into this project thus far.
     * 
     * @return	the sum of durations of the time spans of all tasks in this project.
     * 
     * @see		Task#getTimeSpan()
     */
    public Duration getTotalExecutionTime();
    
}
