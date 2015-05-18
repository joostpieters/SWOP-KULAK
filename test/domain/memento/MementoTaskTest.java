package domain.memento;

import static org.junit.Assert.*;

import java.util.List;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import org.easymock.EasyMock;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import domain.Planning;
import domain.Project;
import domain.ResourceType;
import domain.task.Available;
import domain.task.Failed;
import domain.task.Finished;
import domain.task.Status;
import domain.task.Task;
import domain.time.Duration;
import domain.time.Timespan;

public class MementoTaskTest {

	private Task t0, t1, t2;
	private Project p0;
	/*
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}*/
	@Before
	public void setUp() throws Exception {
		p0 = new Project("project 0", "this is project 0", LocalDateTime.of(2015, 5, 18, 10, 20), LocalDateTime.of(2015, 5, 30, 13, 0));
		t0 = new Task("task 0", new Duration(30), 54, new HashMap<ResourceType, Integer>(), p0);
		t1 = new Task("task 1", new Duration(31), 55, Arrays.asList(t0), new HashMap<ResourceType, Integer>(), p0);
		t2 = new Task("task 2", new Duration(24), 23, new HashMap<ResourceType, Integer>(), p0);
		
	}
	private void checkEquals(Task task, Timespan timespan, Task altTask, List<Task> prereqs, Status status, Planning planning)
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
		Task.Memento t0memento2 = t0.createMemento();
		TaskProperties t0properties2 = new TaskProperties(t0);
		t0properties2.checkEquals(t0);

		t0.setMemento(t0memento1);
		t0properties1.checkEquals(t0);
		

		t0.setMemento(t0memento0);
		t0properties0.checkEquals(t0);
	}
	
	/**
	 * Checks if the alt task property is rolled back properly when setting the memento
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
	
	private class TaskProperties
	{
		Timespan timespan;
		Task altTask;
		List<Task> prereqs;
		Status status;
		Planning planning;
		public TaskProperties(Task task)
		{
			this.timespan = task.getTimeSpan();
			this.altTask = task.getAlternativeTask();
			this.prereqs = new ArrayList<Task>(task.getPrerequisiteTasks());
			this.status = task.getStatus();
			this.planning = task.getPlanning();
		}
		
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