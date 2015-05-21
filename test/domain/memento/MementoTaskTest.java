package domain.memento;

import domain.Planning;
import domain.Project;
import domain.Resource;
import domain.ResourceType;
import domain.task.Status;
import domain.task.Task;
import domain.time.Clock;
import domain.time.Duration;
import domain.time.Timespan;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.easymock.EasyMock;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

/**
 * A class testing the Task Memento.
 * @author Mathias, Pieter-Jan, Frederic
 *
 */
public class MementoTaskTest {

	private Clock clock;
	private Task t0, t1, t2;
	private Project p0;
	
	@Before
	public void setUp() throws Exception {
		clock = new Clock();
		p0 = new Project("project 0", "this is project 0", LocalDateTime.of(2015, 5, 18, 10, 20), LocalDateTime.of(2015, 5, 30, 13, 0));
		t0 = new Task("task 0", new Duration(30), 54, Task.getDefaultRequiredResources(), p0);
		t1 = new Task("task 1", new Duration(31), 55, Arrays.asList(t0), Task.getDefaultRequiredResources(), p0);
		t2 = new Task("task 2", new Duration(24), 23, Task.getDefaultRequiredResources(), p0);
		
	}
	
	/**
	 * Tests whether the status and time span of a task are properly stored and loaded when
	 * using Task Mementos.
	 */
	@Test
	public void testStatusTimespan() {
		Task.Memento t0memento0 = t0.createMemento();
		TaskProperties t0properties0 = new TaskProperties(t0);
		TaskProperties t1properties0 = new TaskProperties(t1);
		t0properties0.checkEquals(t0);
		t1properties0.checkEquals(t1);
		
		Timespan t0finishTS = new Timespan(
				LocalDateTime.of(2015, 5, 18, 15, 0),
				LocalDateTime.of(2015, 5, 19, 15, 0));
        
    	t0.plan(clock.getTime(), Arrays.asList(new Resource("name", ResourceType.DEVELOPER)), clock);
    	t0.execute(clock);
		t0.finish(t0finishTS,
				LocalDateTime.of(2015, 5, 20, 10, 0));
		
		TaskProperties t0properties1 = new TaskProperties(t0);
		TaskProperties t1properties1 = new TaskProperties(t1);
		Task.Memento t0memento1 = t0.createMemento();
		t0properties1.checkEquals(t0);
		t1properties1.checkEquals(t1);
		
		t0.setMemento(t0memento0);
		
		t0properties0.checkEquals(t0);
		t1properties0.checkEquals(t1);
		t0.setMemento(t0memento1);

		t0properties1.checkEquals(t0);
		t1properties1.checkEquals(t1);
		
		t0.setMemento(t0memento0);
		
		t0properties0.checkEquals(t0);
		t1properties0.checkEquals(t1);
		
		t0.fail(
				new Timespan(
						LocalDateTime.of(2015, 5, 18, 15, 0),
						LocalDateTime.of(2015, 5, 19, 15, 0)),
				LocalDateTime.of(2015, 5, 20, 10, 0));
		TaskProperties t0properties2 = new TaskProperties(t0);
		t0properties2.checkEquals(t0);

		t0.setMemento(t0memento1);
		t0properties1.checkEquals(t0);
		

		t0.setMemento(t0memento0);
		t0properties0.checkEquals(t0);
	}
	
	/**
	 * Checks if the alternative task property is rolled back properly when setting the Task Memento
	 */
	@Test
	public void testStatusAlttask()
	{
		Task.Memento t0memento0 = t0.createMemento();
		TaskProperties t0properties0 = new TaskProperties(t0);
		t0properties0.checkEquals(t0);
		
		t0.fail(
				new Timespan(
						LocalDateTime.of(2015, 5, 18, 15, 0),
						LocalDateTime.of(2015, 5, 19, 15, 0)),
				LocalDateTime.of(2015, 5, 20, 10, 0));
		t0.setAlternativeTask(t2);
		Task.Memento t0memento1 = t0.createMemento();
		TaskProperties t0properties1 = new TaskProperties(t0);
		
		t0.setMemento(t0memento0);
		t0properties0.checkEquals(t0);
		
		t0.setMemento(t0memento1);
		t0properties1.checkEquals(t0);
	}
	
	/**
	 * Tests whether the planning is stored and restored properly in Task Memento.
	 */
	@Test
	public void testPlanning()
	{
		Task.Memento t0memento0 = t0.createMemento();
		TaskProperties t0properties0 = new TaskProperties(t0);
		t0properties0.checkEquals(t0);
		
		Planning planning1 = EasyMock.createNiceMock(Planning.class);
		t0.setPlanning(planning1);
		
		Task.Memento t0memento1 = t0.createMemento();
		TaskProperties t0properties1 = new TaskProperties(t0);
		t0properties1.checkEquals(t0);
		
		t0.setMemento(t0memento0);
		t0properties0.checkEquals(t0);
		
		t0.setMemento(t0memento1);
		t0properties1.checkEquals(t0);
	}
	
	/**
	 * A class storing the properties which should be kept track of in Task Mementos.
	 * 
	 * @author Mathias, Pieter-Jan, Frederic
	 *
	 */
	private class TaskProperties
	{
		Timespan timespan;
		Task altTask;
		List<Task> prereqs;
		Status status;
		Planning planning;
		
		/**
		 * Initializes this TaskProperties based on the given task.
		 * 
		 * @param task The task on which the Taskproperties should be based on.
		 */
		public TaskProperties(Task task)
		{
			this.timespan = task.getTimeSpan();
			this.altTask = task.getAlternativeTask();
			this.prereqs = new ArrayList<Task>(task.getPrerequisiteTasks());
			this.status = task.getStatus();
			this.planning = task.getPlanning();
		}
		
		/**
		 * Tests whether the given task has the same state as the one stored in this TaskProperties
		 * 
		 * @param task The task to check.
		 */
		public void checkEquals(Task task)
		{
			assertEquals(timespan, task.getTimeSpan());
			assertEquals(altTask, task.getAlternativeTask());
			List<Task> taskprereqs = task.getPrerequisiteTasks();
			
			if(taskprereqs == null || prereqs == null)
			{
				assertEquals(null, taskprereqs);
				assertEquals(null, prereqs);
			}
			else
			{
				assertTrue(taskprereqs.containsAll(prereqs));
				assertTrue(prereqs.containsAll(taskprereqs));
			}
			assertEquals(status, task.getStatus());
			assertEquals(planning, task.getPlanning());
		}
	}

}