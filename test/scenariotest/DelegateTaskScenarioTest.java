package scenariotest;

import controller.DelegateTaskHandler;
import controller.HandlerFactory;
import domain.BranchOffice;
import domain.Company;
import domain.Project;
import domain.ProjectContainer;
import domain.ResourceContainer;
import domain.dto.DetailedTask;
import domain.task.Task;
import domain.time.Clock;
import domain.time.Duration;
import domain.user.Acl;
import domain.user.Auth;
import domain.user.GenericUser;
import domain.user.Role;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

/**
 * This scenario test, tests the delegate task use case.
 *
 * @author Frederic, Mathias, Pieter-Jan
 */
public class DelegateTaskScenarioTest {

	private Clock clock;
	private Company db;
    private ProjectContainer pc0, pc1;
    private BranchOffice office0, office1;
    private DelegateTaskHandler handler;
    private Project p1;
    private Acl acl;
    private static Auth auth;

    @Before
    public void setUpClass() {
    	db = new Company();
    	
    	pc0 = new ProjectContainer();
    	pc1 = new ProjectContainer();
        
    	office0 = new BranchOffice("Kortrijk", pc0, new ResourceContainer());
        db.addOffice(office0);
        office1 = new BranchOffice("Lauwe city", pc1, new ResourceContainer());
        db.addOffice(office1);
        
        String project1Name = "Project X";
        String project1Description = "Original project description";
        LocalDateTime project1StartTime = LocalDateTime.of(2015, 03, 12, 17, 30);
        LocalDateTime project1EndTime = LocalDateTime.of(2015, 03, 16, 17, 30);
        p1 = pc0.createProject(project1Name, project1Description, project1StartTime, project1EndTime);
        p1.createTask("Task 0", new Duration(500), 50, Project.NO_ALTERNATIVE, Project.NO_DEPENDENCIES, Task.getDefaultRequiredResources());
        
        clock = new Clock();
        auth = new Auth(db);
        acl = new Acl();
        office1.addUser(new GenericUser("John", Role.MANAGER, office0));
        acl.addEntry(Role.MANAGER, new ArrayList<>(Arrays.asList("DelegateTask")));
        auth.login("John");
        HandlerFactory controller = new HandlerFactory(db, auth, acl, clock);
        handler = controller.getDelegatedTaskHandler();
    }

    /**
     * Tests the main success scenario of the "Delegate Task" use case
     */
    @Test
    public void testMainSuccessScenario() {
        // Step 2 - Show all currently unplanned tasks assigned to the branch office to which the user logged in
        List<DetailedTask> tasks = handler.getUnplannedAssignedTasks();
        
        // Step 3 - The users selects one of the tasks
        DetailedTask chosenTask = tasks.get(0);
        int chosenTaskId = chosenTask.getId();
        int chosenTaskProjectId = chosenTask.getProject().getId();
        
        // Step 4 - Show an overview of the different branch offices
        handler.getBranchOffices();
        
        // Step 5 - The user selects one of the branch offices
        int chosenBranchOffice = 1; // The second branch office is chosen: 'Lauwe city'
        handler.delegateTask(chosenTaskProjectId, chosenTaskId, chosenBranchOffice);
        
        
        // check that the original branch office no longer contains the delegated task as one of its assigned tasks.
        for(Task task : office0.getAssignedTasks())
        	assertTrue(task.getId() != chosenTaskId);
        
        // check that the delegated to branch office now contains the delegated task as one of its assigned tasks.
        boolean foundTask = false;
        for(Task task : office1.getAssignedTasks())
        	if(task.getId() == chosenTaskId)
        		foundTask = true;
        assertTrue(foundTask);
        
    }
}
