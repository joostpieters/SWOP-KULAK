package controller;

import domain.BranchOffice;
import domain.Project;
import domain.ResourceType;
import domain.command.CreateTaskCommand;
import domain.command.SimulatorCommand;
import domain.dto.DetailedProject;
import domain.dto.DetailedResourceType;
import domain.dto.DetailedTask;
import domain.task.Task;
import domain.time.Duration;
import domain.user.Acl;
import domain.user.Auth;
import exception.ResourceTypeConflictException;
import exception.ResourceTypeMissingReqsException;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This handler, handles the create task use case
 * 
 * @author Frederic, Mathias, Pieter-Jan
 */
public class CreateTaskHandler extends Handler{
    
    protected final BranchOffice office;
    protected final List<ResourceType> resourceTypes;
    
    protected final SimulatorCommand simulatorCommand;
    
    /**
     * Initialize a new create task handler with the given projectContainer.
     * 
     * @param office The branch office to use in this handler. 
     * @param resourceTypes The list of all resource types
     * @param auth The authorization manager to use
     * @param acl The action control list to use
     * @param simulatorCommand The command to use, to store all executed commands
     */   
    public CreateTaskHandler(BranchOffice office, List<ResourceType> resourceTypes, Auth auth, Acl acl, SimulatorCommand simulatorCommand){
        super(auth, acl);
        this.office = office;
        this.resourceTypes = resourceTypes;
        this.simulatorCommand = simulatorCommand;
    }
    
    /**
     * Initialize a new create task handler with the given projectContainer.
     * 
     * @param manager The projectContainer to use in this handler. 
     * @param resourceTypes The database to use
     * @param auth The authorization manager to use
     * @param acl The action control list to use
     */   
    public CreateTaskHandler(BranchOffice manager, List<ResourceType> resourceTypes, Auth auth, Acl acl) {
        this(manager, resourceTypes, auth, acl, new SimulatorCommand());
    }
    
    
    /**
     * Get the list of tasks that belongs to the project with the given id.
     * 
     * @param Pid The id of the project to get the tasks from.
     * @return A list containing all the tasks of the project with the given id.
     */   
    public List<DetailedTask>  getTasksByProject(int Pid){
        return new ArrayList<>(office.getProjectContainer().getProject(Pid).getTasks());
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
            Project project = office.getProjectContainer().getProject(pId);
            Duration duration = new Duration(estDurMinutes);
            
            Map<ResourceType, Integer> resources = Task.getDefaultRequiredResources();
            // convert id's to objects
            for(Entry<Integer, Integer> entry : requiredResources.entrySet()){
                resources.put(resourceTypes.get(entry.getKey()), entry.getValue());
            }
            
            simulatorCommand.addAndExecute(new CreateTaskCommand(project, description, duration, accDev, altfor, prereq, resources));
            
        } catch (IllegalArgumentException | IllegalStateException | ResourceTypeConflictException | ResourceTypeMissingReqsException e) {
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
        ArrayList<DetailedProject> projects = new ArrayList<>(office.getProjectContainer().getUnfinishedProjects());
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
        return new ArrayList<>(office.getProjectContainer().getAllTasks());
    }
    
    /**
     * Get the list of different resourcetypes.
     * 
     * @return A list containing all the resourcetypes registered in this database.
     */   
    public List<DetailedResourceType> getResourceTypes(){
        return new ArrayList<>(resourceTypes);
    }
}
