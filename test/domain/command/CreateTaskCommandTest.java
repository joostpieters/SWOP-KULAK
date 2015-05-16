package domain.command;

import domain.Project;
import domain.ProjectContainer;
import domain.task.Task;
import domain.time.Duration;
import domain.time.Timespan;
import java.time.LocalDateTime;
import java.util.HashMap;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Test;

/**
 * Test class for the create task command.
 * 
 * @author Mathias
 */
public class CreateTaskCommandTest {
    private ProjectContainer pc;
    private Project p;
    private Task t0;
    private Task t1;
    private CreateTaskCommand createTaskCommand;
    
   
    @Before
    public void setUp() {
        pc = new ProjectContainer();
    	p = pc.createProject("Name", "Description", LocalDateTime.MIN, LocalDateTime.of(2072, 10, 9, 8, 0));
        
    	t0 = p.createTask("description!", new Duration(10), 20, Project.NO_ALTERNATIVE, Project.NO_DEPENDENCIES, new HashMap<>());
        t0.fail(new Timespan(LocalDateTime.MIN, LocalDateTime.MAX), LocalDateTime.MAX);
        
    	t1 = p.createTask("t1", new Duration(10), 10, Project.NO_ALTERNATIVE, Project.NO_DEPENDENCIES, new HashMap<>());
    	
        createTaskCommand = new CreateTaskCommand(p, "test", new Duration(10), 10, t0.getId(), Project.NO_DEPENDENCIES, new HashMap<>());
    }
    
     /**
     * Test of getCreatedTask method, of class CreateTaskCommand.
     */
    @Test
    public void testGetCreatedTask() {
       createTaskCommand.execute();
        assertEquals("test", createTaskCommand.getCreatedTask().getDescription());
    }

    /**
     * Test of execute method, of class CreateTaskCommand.
     */
    @Test
    public void testExecute() {
        createTaskCommand.execute();
        assertEquals("test", createTaskCommand.getCreatedTask().getDescription());
        assertTrue(p.hasTask(createTaskCommand.getCreatedTask()));
        assertEquals(t0.getAlternativeTask(), createTaskCommand.getCreatedTask());
    }

    /**
     * Test of revert method, of class CreateTaskCommand.
     */
    @Test
    public void testRevert() {
        int size = p.getTasks().size();
        createTaskCommand.execute();
        createTaskCommand.revert();
        assertEquals(null, createTaskCommand.getCreatedTask());
        assertEquals(size, p.getTasks().size());
        assertFalse(t0.hasAlternativeTask());
    }
    
}
