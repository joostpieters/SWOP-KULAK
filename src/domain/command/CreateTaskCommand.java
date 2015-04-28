package domain.command;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import controller.CreateProjectHandler;
import domain.Database;
import domain.Project;
import domain.ProjectContainer;
import domain.ResourceType;
import domain.Task;
import domain.time.Duration;

public class CreateTaskCommand implements Command {
	
	private Project project;
	private String description;
	private int accDev;
	private List<Integer> prereq;
	private int estDurMinutes;
	private int altfor;
	private Map<Integer, Integer> requiredResources;
	private Database db;
	private ProjectContainer manager;
	
	private Project.Memento projectMemento;
	private Task altforTask;
	private Task.Memento altforTaskMemento;
	
	@Override
	public void execute() {
    	Project.Memento pMemento = project.createMemento();
    	if(altforTask != null)
    		altforTaskMemento= project.getTask(altfor).createMemento();
    	if(prereq == null){
            prereq = Project.NO_DEPENDENCIES;
        }
        
        if(altfor < 0){
            altfor = Project.NO_ALTERNATIVE;
        }
        try {
        	Project project = manager.getProject(pId);
            Duration duration = new Duration(estDurMinutes);
            
            HashMap<ResourceType, Integer> resources = new HashMap<>();
            // convert id's to objects
            for(Integer id : requiredResources.keySet()){
                resources.put(db.getResourceTypes().get(id), requiredResources.get(id));
            }
            
            Task createTask = project.createTask(description, duration, accDev, altfor, prereq, resources);
                        
        } catch (IllegalArgumentException | IllegalStateException e) {
            throw e;
        }catch(Exception e){
            // log for further review
            Logger.getLogger(CreateProjectHandler.class.getName()).log(Level.SEVERE, null, e);
            throw new RuntimeException("An unexpected error occured, please contact the system admin.");
            
        }
	}
	/**
     * Create a new task with the given parameters.
     * 
     * @param pId The id of the project to add the task to.
     * @param description The description of the task
     * @param accDev The deviation by which the estimated duration can deviate
     * @param prereq The id's of the task that have to be finished before this task.
     * @param estDurMinutes The extra minutes to the estimated duration in hours.
     * @param altfor The id of the task, the task is an alternative for, is smaller
     * than 0, if no alternative. 
     * @param requiredResources The resources that are required for this task.
     */
    public void createTask(int pid, String description, int accDev, List<Integer> prereq, int estDurMinutes, int altfor, Map<Integer, Integer> requiredResources, Database db, ProjectContainer manager){       
        // project, altfor
    	this.project = project;
    	this.description = description;
    	this.accDev = accDev;
    	this.prereq = new ArrayList<>(prereq);
    	this.estDurMinutes = estDurMinutes;
    	this.altfor = altfor;
    	if(project.hasTask(altfor))
    		this.altforTask = project.getTask(altfor);
    	this.requiredResources = new HashMap<>(requiredResources);
    	this.db = db;
    	this.manager = manager;
    }

	@Override
	public void revert() {
		if(project != null && projectMemento != null)
			project.setMemento(projectMemento);
		if(altforTask != null && altforMemento != null)
			altforTask.setMemento(altforMemento);
	}

}
