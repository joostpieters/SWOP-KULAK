package domain.dto;

import domain.task.Status;
import domain.time.Duration;
import domain.time.Timespan;
import java.util.List;
import java.util.Map;

/**
 * This interface provides methods to retrieve all data from a task object.
 * 
 * @author Frederic, Mathias, Pieter-Jan
 */
public interface DetailedTask {

    /**
     * @return 	The acceptable deviation of this task expressed as an integer between 0 and 100.
     */
    int getAcceptableDeviation();

    /**
     *  Calculates the duration by which this task been delayed.
     *
     * @return 	The duration by which this task has been delayed if this task has a time span,
     *         	based on the maximum duration of this task.
     *         	If this task doesn't have a time span then the result is a duration of 0 minutes.
     */
    Duration getDelay();

    /**
     * @return 	The description of this task.
     */
    String getDescription();

    /**
     * @return 	The estimated duration of this task.
     */
    Duration getEstimatedDuration();

    /**
     * @return 	The identification number of this task.
     */
    int getId();

    /**
     * @return 	The status of this task.
     */
    Status getStatus();

    /**
     * @return 	The timeSpan indicating the actual start and end time of this task.
     */
    Timespan getTimeSpan();
    
    
	/**
	 * @return 	The list of prerequisite tasks for this task.
	 */
    List<? extends DetailedTask> getPrerequisiteTasks();
    
    /**
	 * @return 	The alternative task for this task.
	 */
	DetailedTask getAlternativeTask();
        
    /**
     * 
     * @return The project this task belongs to 
     */
    public DetailedProject getProject();
    
    /**
     *
     * @return The planning of this task
     */
    public DetailedPlanning getPlanning();
    
    /**
	 * Returns the branch office to which this task has been delegated to.
	 * @return The branch office to which this task has been delegated to.
	 *         null if the task is assigned to the same branch office to which its project belongs to.
	 */
	public DetailedBranchOffice getDelegatedBranchOffice();
         /**
     
     * @return the requiredResources of this specific task, combined with the
     * standard required resources that are equal for all classes.
     */
    public Map<? extends DetailedResourceType, Integer> getRequiredResources();
    
}
