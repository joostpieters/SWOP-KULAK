package controller;

import domain.user.Acl;
import domain.user.Auth;
import domain.Database;
import domain.command.CreateTaskCommand;
import domain.command.SimulatorCommand;
import domain.dto.DetailedProject;
import domain.dto.DetailedResourceType;
import domain.dto.DetailedTask;
import domain.Project;
import domain.ProjectContainer;
import domain.ResourceType;
import domain.task.Task;
import domain.time.Duration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This handler, handles the create task use case
 * 
 * @author Frederic, Mathias, Pieter-Jan
 */
public class CreateTaskHandler extends Handler{
    
    protected final ProjectContainer manager;
    protected final Database db;
    
    protected final SimulatorCommand simulatorCommand;
    
    /**
     * Initialize a new create task handler with the given projectContainer.
     * 
     * @param manager The projectContainer to use in this handler. 
     * @param auth The authorization manager to use
     * @param acl The action control list to use
     * @param db The database to use
     */   
    public CreateTaskHandler(ProjectContainer manager, Auth auth, Acl acl, Database db, SimulatorCommand simulatorCommand){
        super(auth, acl);
        this.manager = manager;
        this.db = db;
        this.simulatorCommand = simulatorCommand;
    }
    
    /**
     * Initialize a new create task handler with the given projectContainer.
     * 
     * @param manager The projectContainer to use in this handler. 
     * @param auth The authorization manager to use
     * @param acl The action control list to use
     * @param db The database to use
     */   
    public CreateTaskHandler(ProjectContainer manager, Auth auth, Acl acl, Database db) {
        this(manager, auth, acl, db, new SimulatorCommand());
    }
    
    
    /**
     * Get the list of tasks that belongs to the project with the given id.
     * 
     * @param Pid The id of the project to get the tasks from.
     * @return A list containing all the tasks of the project with the given id.
     */   
    public List<DetailedTask>  getTasksByProject(int Pid){
        return new ArrayList<>(manager.getProject(Pid).getTasks());
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
    public void createTask(int pId, String description, int accDev, List<Integer> prereq, int estDurMinutes, int altfor, Map<Integer, Integer> requiredResources){       
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
            
            simulatorCommand.addAndExecute(new CreateTaskCommand(project, description, duration, accDev, altfor, prereq, resources));
            
        } catch (IllegalArgumentException | IllegalStateException e) {
            throw e;
        }catch(Exception e){
            // log for further review
            Logger.getLogger(CreateProjectHandler.class.getName()).log(Level.SEVERE, null, e);
            throw new RuntimeException("An unexpected error occured, please contact the system admin.");
            
        }
        
        
    } 
    
    /**
     * 
     * @return A list of all projects in this projectContainer.
     * @throws IllegalStateException No unfinished projects are available.
     */
    public List<DetailedProject> getUnfinishedProjects() throws IllegalStateException{
        ArrayList<DetailedProject> projects = new ArrayList<>(manager.getUnfinishedProjects());
        if(projects.isEmpty()){
            throw new IllegalStateException("No unfinished projects are available.");
        }
        return projects;
    }
    
    /**
     * 
     * @return A list of all the tasks available in this manager. 
     */
    public List<DetailedTask> getAllTasks(){
        return new ArrayList<>(manager.getAllTasks());
    }
    
    /**
     * Get the list of different resourcetypes.
     * 
     * @return A list containing all the resourcetypes registered in this database.
     */   
    public List<DetailedResourceType>  getResourceTypes(){
        return new ArrayList<>(db.getResourceTypes());
    }
}
