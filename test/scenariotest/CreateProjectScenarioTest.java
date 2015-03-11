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
public class CreateProjectScenarioTest { // TODO is dit genoeg?
	
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
        //TODO kan niet gwn contains gebruikt worden?
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
    
    /**
     * Tests extension 4a. "The entered data is invalid" with an invalid name
     */
    @Test (expected = IllegalArgumentException.class)
    public void testInvalidDataName()
    {
    	String project5InvalidName = "";
    	String project5Description = "This is not project 4";
    	String project5StartTime = "2015-03-13 11:30";
    	String project5EndTime = "2015-04-13 14:42";
    	handler.createProject(project5InvalidName, project5Description, project5StartTime, project5EndTime);
    }
    /**
     * Tests extension 4a. "The entered data is invalid" with an invalid description
     */
    @Test (expected = IllegalArgumentException.class)
    public void testInvalidDataDescription()
    {
    	String project4Name = "a valid project name";
    	String project4InvalidDescription = "";
    	String project4StartTime = "2015-02-13 10:30";
    	String project4EndTime = "2015-04-15 15:51";
    	handler.createProject(project4Name, project4InvalidDescription, project4StartTime, project4EndTime);
    }
    
    /**
     * Tests extension 4a. "The entered data is invalid" with an invalid start time
     */
    @Test (expected = IllegalArgumentException.class)
    public void testInvalidDataStartTime()
    {
    	String project2Name = "project 2 :)";
    	String project2Description = "This is project 2";
    	String project2InvalidStartTime = "2015-03-12 17-30";
    	String project2EndTime = "2015-03-13 14:44";
    	handler.createProject(project2Name, project2Description, project2InvalidStartTime, project2EndTime);
    }
    
    /**
     * Tests extension 4a. "The entered data is invalid" with an invalid end time
     */
    @Test (expected = IllegalArgumentException.class)
    public void testInvalidDataEndTime()
    {
    	String project3Name = "project 3 :)";
    	String project3Description = "This is project 3!!";
    	String project3StartTime = "2015-03-12 17:30";
    	String project3InvalidEndTime = "2015-03-13 oops 14:44";
    	handler.createProject(project3Name, project3Description, project3StartTime, project3InvalidEndTime);
    }
    
    /**
     * Tests extension 4a. "The entered data is invalid" with an end time that comes before the start time
     */
    @Test (expected = IllegalArgumentException.class)
    public void testInvalidDataStartTimeEndTime()
    {
    	String project6Name = "project 6 :)";
    	String project6Description = "1+2+3=6";
    	String project6StartTime = "2015-03-12 17:30";
    	String project6EndTime = "2015-03-12 14:44";
    	handler.createProject(project6Name, project6Description, project6StartTime, project6EndTime);
    }
    
}