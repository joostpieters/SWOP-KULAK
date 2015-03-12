package domain;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

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
     * @return The sum of the delays of the tasks within this project.
     */
    public Duration getDelay();

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
     * @return	true if this project finished on time or if this project is
     * estimated to finish on time, false otherwise.
     */
    boolean isOnTime();

    /**
     *
     * @return A list containing all projects ascociated with this project.
     */
    List<? extends DetailedTask> getTasks();

    /**
     * Return all tasks which can cause this project to get overdue and the
     * percentage the project will be late because of the task.
     *
     * @return	a map of tasks for which the work time needed is greater than 
     * the due time of this project minus the system time  to their corresponding
     * percentage by which they are over time.
     *
     */
    Map<? extends DetailedTask, Double> getUnacceptablyOverdueTasks(); 
    
}
