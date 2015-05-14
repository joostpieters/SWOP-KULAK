package domain.command;

import domain.Project;
import domain.ResourceType;
import domain.task.Task;
import domain.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This class represents the action of creating a new task
 * 
 * @author Mathias, Frederic, Pieter-Jan
 */
public class CreateTaskCommand implements ICommand {

    private final Project project;
    private final String description;
    private final int accDev;
    private final List<Integer> prereq;
    private final Duration estdur;
    private final int altfor;
    private final Map<ResourceType, Integer> requiredResources;

    private Project.Memento projectMemento;
    private Task altforTask;
    private Task.Memento altforTaskMemento;

    private Task createdTask;
    

    /**
     * @return The task which was created upon execution of this command, null if
     * the command was reverted or ir the task isn't created.
     */
    public Task getCreatedTask() {
        return createdTask;
    }
    
    /**
     * Creates a new task based on the paramaters stored in this command
     */
    @Override
    public void execute() {

        // Save state
        projectMemento = project.createMemento();
        if (project.hasTask(altfor)) {
            altforTask = project.getTask(altfor);
            altforTaskMemento = project.getTask(altfor).createMemento();
        } else {
            altforTask = null;
            altforTaskMemento = null;
        }

        // Actual execution
        createdTask = project.createTask(description, estdur, accDev, altfor, prereq, requiredResources);
    }

    /**
     * Initializes this create task command with the given parameters associated
     * with creating a task in a project.
     *
     * @param project The project to which the task will belong.
     * @param descr The description for the task to be created.
     * @param estdur The estimated duration of the task to be created.
     * @param accdev The acceptable deviation for the task to be created in %.
     * @param altFor The id the task to be created is an alternative for.
     * @param prereqs An array of id's of tasks that the to be created task will
     * depend on.
     * @param requiredResources The required resourcetypes for to be created
     * task
     *
     */
    public CreateTaskCommand(Project project, String descr, Duration estdur, int accdev, int altFor, List<Integer> prereqs, Map<ResourceType, Integer> requiredResources) {
        this.project = project;
        this.description = descr;
        this.estdur = estdur;
        this.altfor = altFor;
        this.prereq = new ArrayList<>(prereqs);
        this.requiredResources = new HashMap<>(requiredResources);
        this.accDev = accdev;
    }
    
    /**
     * Revert the creation of this task
     */
    @Override
    public void revert() {
        if (project != null && projectMemento != null) {
            project.setMemento(projectMemento);
        }
        if (altforTask != null && altforTaskMemento != null) {
            altforTask.setMemento(altforTaskMemento);
        }
        createdTask = null;
    }

}
