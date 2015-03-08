package domain;

import java.time.LocalDateTime;
import java.util.List;

/**
 *
 * @author Mathias
 */
public interface DetailedProject {

    /**
     * @return 	the creationTime of this project.
     */
    LocalDateTime getCreationTime();

    /**
     * @return 	the description of this project.
     */
    String getDescription();

    /**
     * @return 	the dueTime of this project.
     */
    LocalDateTime getDueTime();

    /****************************************
     * Getters & setters					*
     ****************************************/
    /**
     * @return 	the id of this project.
     */
    int getId();

    /**
     * @return 	the name of this project.
     */
    String getName();

    /**
     * Check whether this project is finished or not.
     *
     * @return	true if this all tasks in this project have been finished,
     * 			false otherwise.
     * @throws 	IllegalArgumentException
     * 			if this project doesn't have any tasks yet.
     */
    boolean isFinished();

    /**
     * Get the time details for this project.
     *
     * @return	true if this project finished on time or
     * 			if this project is estimated to finish on time,
     * 			false otherwise.
     */
    boolean isOnTime();

    public List<Task> getTasks();
    
}
