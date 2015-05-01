package domain.task;

import domain.Project;
import domain.ProjectContainer;
import domain.task.Finished;
import domain.task.Unavailable;
import domain.task.Failed;
import domain.task.Available;
import domain.task.Task;
import domain.time.Clock;
import domain.time.Duration;
import domain.time.Timespan;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashMap;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Test;

/**
 * 
 * @author Frederic
 *
 */
public class StatusTest {
	
	private Clock clock;
    private Project p;
    private ProjectContainer pm;
	private Task t0, t1, t2, t3, t4, t5, t6, t7, t7alternative, t8;
	private Timespan ts0, ts1, ts2;
    @Before
    public void setUp() {
    	clock = new Clock();
    	pm = new ProjectContainer();
    	p = pm.createProject("Name", "Description", LocalDateTime.of(2001, 1, 9, 8, 0), LocalDateTime.of(2072, 10, 9, 8, 0));
    	t0 = p.createTask("description!", new Duration(10), 20, Project.NO_ALTERNATIVE, Project.NO_DEPENDENCIES, new HashMap<>());
    	t1 = p.createTask("t1", new Duration(10), 10, Project.NO_ALTERNATIVE, Project.NO_DEPENDENCIES, new HashMap<>());
    	t2 = p.createTask("t2", new Duration(20), 10, Project.NO_ALTERNATIVE, Arrays.asList(t0.getId(), t1.getId()), new HashMap<>());
    	
    	ts0= new Timespan(
    			LocalDateTime.of(2015, 3, 4, 11, 48), 
    			LocalDateTime.of(2015, 3, 4, 15, 33)
    			);
    	ts1 = new Timespan(
    			LocalDateTime.of(2015, 3, 4, 11, 48), 
    			LocalDateTime.of(2015, 3, 4, 15, 33)
    			);
    	ts2 = new Timespan(
    			LocalDateTime.of(2015,  3, 5, 10, 20),
    			LocalDateTime.of(2015, 3, 5, 14, 20));
    	//pm.advanceSystemTime(t3ts.getEndTime());
    	t3 = p.createTask("t3 finished", new Duration(30), 40, Project.NO_ALTERNATIVE, Project.NO_DEPENDENCIES, new HashMap<>());
    	t3.fail(new Timespan(ts0.getStartTime(), ts0.getEndTime()), clock.getTime());
    	t4 = p.createTask("t4", new Duration(30), 10, Project.NO_ALTERNATIVE, Arrays.asList(t3.getId()), new HashMap<>());
    	t5 = p.createTask("t5", new Duration(20), 5, Project.NO_ALTERNATIVE, Arrays.asList(t3.getId(), t2.getId()), new HashMap<>());

    	//pm.advanceSystemTime(t6ts.getEndTime());
    	t6 = p.createTask("t6", new Duration(10), 3, Project.NO_ALTERNATIVE, Project.NO_DEPENDENCIES, new HashMap<>());
    	t6.finish(new Timespan(ts1.getStartTime(), ts1.getEndTime()), clock.getTime());
    	t7 = p.createTask("t7", new Duration(15), 4, Project.NO_ALTERNATIVE, Project.NO_DEPENDENCIES, new HashMap<>());
    	t7.fail(new Timespan(ts1.getStartTime(), ts1.getEndTime()), clock.getTime());
    	t7alternative = p.createTask("alternative for t7", new Duration(10), 2, t7.getId(), Project.NO_DEPENDENCIES, new HashMap<>());
    	t8 = p.createTask("depends on t7", new Duration(33), 3, Project.NO_ALTERNATIVE, Arrays.asList(t7.getId()), new HashMap<>());
    }
    
    @Test
    public void testChangeStatus()
    {
    	// Available -> Finished
    	assertTrue(t0.getStatus() instanceof Available);
    	t0.finish(ts0, clock.getTime());
    	assertTrue(t0.getStatus() instanceof Finished);
    	// Available -> Failed
    	assertTrue(t1.getStatus() instanceof Available);
    	t1.fail(ts0, clock.getTime());
    	assertTrue(t1.getStatus() instanceof Failed);
    	// Unavailable -> Failed
    	assertTrue(t4.getStatus() instanceof Unavailable);
    	t4.fail(ts2, clock.getTime());
    	assertTrue(t4.getStatus() instanceof Failed);
    }
}
