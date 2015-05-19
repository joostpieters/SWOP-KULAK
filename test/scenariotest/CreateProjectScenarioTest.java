package scenariotest;


import controller.CreateProjectHandler;
import controller.HandlerFactory;
import domain.user.Acl;
import domain.user.Auth;
import domain.Database;
import domain.user.GenericUser;
import domain.Project;
import domain.BranchOffice;
import domain.dto.DetailedProject;
import domain.time.Clock;
import java.time.DateTimeException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import org.junit.BeforeClass;
import org.junit.Test;
/**
 * This scenario test, tests the create projects use case
 * 
 * @author Frederic, Mathias, Pieter-Jan
 */
public class CreateProjectScenarioTest {
	
	private static Database db;
	private static BranchOffice manager;
	private static CreateProjectHandler handler;
    private static Clock clock;
    private static Auth auth;
    private static Acl acl;
    
    @BeforeClass
    public static void setUpClass() {
    	db = new Database();
        clock = new Clock();
         auth = new Auth(db);
        acl = new Acl();

        manager = new BranchOffice("Berlin");
        db.addUser(new GenericUser("John", "manager", manager));
        acl.addEntry("manager", new ArrayList<>(Arrays.asList("CreateProject")));
        auth.login("John");
        
        HandlerFactory controller = new HandlerFactory(manager, clock, auth, acl, db);
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
    	LocalDateTime project1StartTime = LocalDateTime.of(2015,03,12, 17,30);
    	LocalDateTime project1EndTime = LocalDateTime.of(2015,03,13, 14,44);
    	handler.createProject(project1Name, project1Description, project1StartTime, project1EndTime);
    	List<Project> projects = manager.getProjectContainer().getProjects();
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
    	LocalDateTime project5StartTime = LocalDateTime.of(2015,03,12, 17,30);
    	LocalDateTime project5EndTime = LocalDateTime.of(2015,03,13, 14,44);
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
    	LocalDateTime project4StartTime = LocalDateTime.of(2015,03,12, 17,30);
    	LocalDateTime project4EndTime = LocalDateTime.of(2015,03,13, 14,44);
    	handler.createProject(project4Name, project4InvalidDescription, project4StartTime, project4EndTime);
    }
    
    /**
     * Tests extension 4a. "The entered data is invalid" with an invalid start time
     */
    @Test (expected = DateTimeException.class)
    public void testInvalidDataStartTime()
    {
    	String project2Name = "project 2 :)";
    	String project2Description = "This is project 2";
    	LocalDateTime project2InvalidStartTime = LocalDateTime.of(2015, 3, 33, 17, 30);
    	LocalDateTime project2EndTime = LocalDateTime.of(2015, 3, 12, 17, 30);
    	handler.createProject(project2Name, project2Description, project2InvalidStartTime, project2EndTime);
    }
    
    /**
     * Tests extension 4a. "The entered data is invalid" with an invalid end time
     */
    @Test (expected = DateTimeException.class)
    public void testInvalidDataEndTime()
    {
    	String project3Name = "project 3 :)";
    	String project3Description = "This is project 3!!";
    	LocalDateTime project3StartTime = LocalDateTime.of(2015, 3, 12, 17, 30);
    	LocalDateTime project3InvalidEndTime = LocalDateTime.of(2015, 3, 33, 17, 30);
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
    	LocalDateTime project6EndTime = LocalDateTime.of(2015, 3, 12, 17, 30);
    	LocalDateTime project6StartTime = LocalDateTime.of(2015, 3, 12, 17, 30);
    	handler.createProject(project6Name, project6Description, project6StartTime, project6EndTime);
    }
    
}
