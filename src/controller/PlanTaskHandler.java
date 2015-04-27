package controller;

import domain.Acl;
import domain.Auth;
import domain.DetailedProject;
import domain.DetailedResourceType;
import domain.DetailedTask;
import domain.Project;
import domain.ProjectContainer;
import domain.Status;
import domain.Task;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This handler, handles the update task use case
 *
 * @author Frederic, Mathias, Pieter-Jan
 */
public class PlanTaskHandler extends Handler{

    private final ProjectContainer manager;

    private Task currentTask;
    private Project currentProject;

    /**
     * Initialize a new create task handler with the given projectContainer.
     *
     * @param manager The projectContainer to use in this handler.
     * @param auth The authorization manager to use
     * @param acl The action control list to use
     */
    public PlanTaskHandler(ProjectContainer manager, Auth auth, Acl acl) {
        super(auth, acl);
        this.manager = manager;
    }

    /**
     * Returns a map with all available tasks in this projectContainer ascociated
     * with their project.
     *
     * @return All available tasks in the projectContainer of this handler.
     */
    public Map<DetailedTask, DetailedProject> getAvailableTasks() {

        return new HashMap<>(manager.getAllAvailableTasks());
    }
    
    /**
     * Returns a map with all available tasks in this projectContainer ascociated
     * with their project.
     *
     * @return All available tasks in the projectContainer of this handler.
     */
    public Map<DetailedResourceType, Integer> getRequiredResourcesCurrenTask() {
        if (currentTask == null || currentProject == null) {
            throw new IllegalStateException("No task currently selected.");
        }
        return new HashMap<>(currentTask.getRequiredResources());
    }

    /**
     * Sets the task with given id in the project with the given id as this
     * handlers current task.
     *
     * @param pId The id of the project, this task belongs to.
     * @param tId The id of the task to select.
     */
    public void selectTask(int pId, int tId) {
        currentTask = manager.getProject(pId).getTask(tId);
        currentProject = manager.getProject(pId);
    }

    /**
     * Update the start and end time and status of this current task.
     *
     * @param startTime The start time of the task (yyyy-MM-dd HH:mm)
     * @param endTime The end time of the task (yyyy-MM-dd HH:mm)
     * @param status The status of the task @see domain.Status
     * @throws RuntimeException An error occured whilte updating the currently
     * selected task.
     */
    public void updateCurrentTask(LocalDateTime startTime, LocalDateTime endTime, String status) throws RuntimeException {
        if (currentTask == null || currentProject == null) {
            throw new IllegalStateException("No task currently selected.");
        }

        Status taskStatus;

        try {
            Class<?> statusClass = Class.forName("domain." + status);
            taskStatus = (Status) statusClass.newInstance();
            
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException ex) {
            throw new IllegalArgumentException("The given status doesn't exist.");
        }

        try {
//            currentProject.updateTask(currentTask.getId(), startTime, endTime, taskStatus);
        } catch (IllegalArgumentException | IllegalStateException e) {
            throw e;
        } catch (Exception e) {
            // log for further review
            Logger.getLogger(CreateProjectHandler.class.getName()).log(Level.SEVERE, null, e);
            throw new RuntimeException("An unexpected error occured, please contact the system admin.");

        }

    }

}
