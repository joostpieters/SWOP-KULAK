package scenariotest;

import static org.junit.Assert.assertEquals;

import java.time.LocalDateTime;
import java.util.List;
import org.junit.BeforeClass;
import org.junit.Test;
import controller.CreateProjectHandler;
import controller.FrontController;
import domain.DetailedProject;
import domain.Project;
import domain.ProjectManager;
import static org.junit.Assert.*;
/**
 * This scenario test, tests the create projects use case
 * 
 * @author Frederic, Mathias, Pieter-Jan
 */
public class CreateProjectScenarioTest { // TODO uitgebreider?
	
	private static ProjectManager manager;
	private static CreateProjectHandler handler;
    
    @BeforeClass
    public static void setUpClass() {
        manager = new ProjectManager();
        FrontController controller = new FrontController(manager);
        handler = controller.getCreateProjectHandler();
    }
    
    /**
     * Tests the main success scenario of the "Create Project" use case
     */
    @Test
    public void testMainSuccessScenario()
    {
    	
    	// Step 4
    	String project1Name = "project 1 :)";
    	String project1Description = "This is project 1";
    	String project1StartTime = "2015-03-12 17:30";
    	String project1EndTime = "2015-03-13 14:44";
    	handler.createProject(project1Name, project1Description, project1StartTime, project1EndTime);
    	List<Project> projects = manager.getProjects();
    	boolean foundProject = false;
    	for(DetailedProject p : projects)
    	{
    		if(p.getName().equals(project1Name))
    		{
    			foundProject = true;
    			assertEquals(project1Name, p.getName());
    			assertEquals(project1Description, p.getDescription());
    			assertTrue(p.getCreationTime().equals(LocalDateTime.of(2015, 3, 12, 17, 30)));
    			assertTrue(p.getDueTime().equals(LocalDateTime.of(2015, 3, 13, 14, 44)));
    		}
    	}
    	assertTrue(foundProject);
    }
}
