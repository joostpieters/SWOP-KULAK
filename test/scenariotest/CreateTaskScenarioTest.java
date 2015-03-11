package scenariotest;

import static org.junit.Assert.assertEquals;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.BeforeClass;
import org.junit.Test;

import controller.CreateProjectHandler;
import controller.CreateTaskHandler;
import controller.FrontController;
import domain.DetailedProject;
import domain.DetailedTask;
import domain.Project;
import domain.ProjectManager;
import domain.Task;
import static org.junit.Assert.*;
/**
 * This scenario test, tests the create task use case
 * 
 * @author Frederic, Mathias, Pieter-Jan
 */
public class CreateTaskScenarioTest {
	
	private static ProjectManager manager;
	private static CreateTaskHandler handler;
    private static Project p1;
    
    @BeforeClass
    public static void setUpClass() {
        manager = new ProjectManager();
        String project1Name = "project 1 :)";
    	String project1Description = "This is project 1";
        LocalDateTime project1StartTime = LocalDateTime.of(2015,03,12,17,30);
        LocalDateTime project1EndTime = LocalDateTime.of(2015,03,12,17,30);
        p1 = manager.createProject(project1Name, project1Description, project1StartTime, project1EndTime);
        FrontController controller = new FrontController(manager);
        handler = controller.getCreateTaskHandler();
    }
    
    /**
     * Tests the main success scenario of the "Create Task" use case
     */
    @Test
    public void testMainSuccessScenario()
    {
    	
    	// Step 4
    	handler.createTask(p1.getId(), "Fun task", 50, null, 10, 20, -1);
    	
    	    Project project = manager.getProject(p1.getId());
            List<Task> tasks = project.getTasks();
    	boolean foundTask = false;
    	for(Task t : tasks)
    	{
    		if(t.getDescription().equals("Fun task"))
    		{
    			foundTask = true;
    			assertEquals(50, t.getAcceptableDeviation());
    			assertEquals(10, t.getEstimatedDuration().getHours());
                        assertEquals(20, t.getEstimatedDuration().getMinutes());
    			
    			
    		}
    	}
    	assertTrue(foundTask);
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
