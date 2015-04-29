package controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;
import java.util.logging.Level;
import java.util.logging.Logger;

import domain.Acl;
import domain.Auth;
import domain.Database;
import domain.Project;
import domain.ProjectContainer;
import domain.ResourceType;
import domain.Task;
import domain.command.Command;
import domain.command.CreateTaskCommand;
import domain.time.Duration;

public class CreateTaskSimulatorHandler extends CreateTaskHandler {

	private final Stack<Command> commandStack;
	/**TODO
	 * 
	 * @param manager
	 * @param auth
	 * @param acl
	 * @param db
	 * @param commandStack
	 */
	public CreateTaskSimulatorHandler(ProjectContainer manager, Auth auth,
			Acl acl, Database db, Stack<Command> commandStack) {
		super(manager, auth, acl, db);
		this.commandStack = commandStack;
	}
	
	/**
	 * @see controller.CreateTaskHandler#createTask(int, java.lang.String, int, java.util.List, int, int, java.util.Map)
	 */
	@Override
	public void createTask(int pId, String description, int accDev, List<Integer> prereq, int estDurMinutes, int altfor, Map<Integer, Integer> requiredResources)
	{
		// TODO code duplicatie
		// TODO misschien zijn deze simulator handlers niet nodig, alternatieve methode is zorgen dat CreateTaskHandler.CreateTask een Command returned
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
            
            CreateTaskCommand ctm = new CreateTaskCommand(project, description, duration, accDev, altfor, prereq, resources);
            commandStack.push(ctm);
            ctm.execute();
                        
        } catch (IllegalArgumentException | IllegalStateException e) {
            throw e;
        }catch(Exception e){
            // log for further review
            Logger.getLogger(CreateProjectHandler.class.getName()).log(Level.SEVERE, null, e);
            throw new RuntimeException("An unexpected error occured, please contact the system admin.");
            
        }
	}
	
}
