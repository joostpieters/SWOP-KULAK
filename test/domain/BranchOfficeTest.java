package domain;

import java.time.LocalDateTime;
import java.util.HashMap;

import domain.task.Task;
import domain.time.Duration;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

/**
 * This is a test class for the branch office domain class
 * 
 * @author Mathias, Frederic, Pieter-Jan
 */
public class BranchOfficeTest {
    BranchOffice bo0, bo1;
    ProjectContainer pc0;
    Project p0;
    Task t0;
    
	@Before
    public void setUp() {
    	pc0 = new ProjectContainer();

    	bo0 = new BranchOffice(pc0, new ResourceContainer());
    	
    	p0 = pc0.createProject("project 0", "description for project 0", LocalDateTime.of(2015, 5, 19, 10, 10), LocalDateTime.of(2015, 6, 19, 10, 10));
    	t0 = new Task("task 0!", new Duration(68), 70, new HashMap<ResourceType, Integer>(), p0);
    	
    	
    	bo1 = new BranchOffice();
    }
    
	/**
	 * Tests the delegation of a task to a different branch office and back to its original branch office.
	 */
	@Test
	public void testDelegateTask()
	{
		assertFalse(t0.isDelegated());
		assertTrue(bo0.taskIsAssigned(t0));
		assertFalse(bo1.taskIsAssigned(t0));
		
		bo0.delegateTaskTo(t0, bo1);
		
		assertTrue(t0.isDelegated());
		assertFalse(bo0.taskIsAssigned(t0));
		assertTrue(bo1.taskIsAssigned(t0));
		
		bo1.delegateTaskTo(t0, bo0);
		
		assertFalse(t0.isDelegated());
		assertTrue(bo0.taskIsAssigned(t0));
		assertFalse(bo1.taskIsAssigned(t0));
	}
	
	/**
	 * Tests the illegal attempt of delegating a task from a branch office to which it is not assigned.
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testIllegalDelegation()
	{
		bo1.delegateTaskTo(t0, bo0);
	}
	
	/**
	 * Tests the illegal attempt of delegating a task from a branch office to which it belongs to
	 * but is no longer assigned to.
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testIllegalDelegation2()
	{
		bo0.delegateTaskTo(t0, bo1);
		bo0.delegateTaskTo(t0, bo1);
	}
}
