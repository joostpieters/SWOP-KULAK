package domain.memento;

import static org.junit.Assert.*;

import java.time.LocalDateTime;
import java.util.List;

import org.easymock.EasyMock;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import domain.Project;
import domain.task.Task;

/**
 * A class testing Project.Memento
 * @author Pieter-Jan, Mathias, Frederic
 */
public class MementoProjectTest {
	Project p0;
	Task t0, t1;
	
	@Before
	public void setUp() {
		p0 = new Project("project 0", "this is project 0", LocalDateTime.of(2015, 5, 18, 10, 20), LocalDateTime.of(2015, 5, 30, 13, 0));
		
		t0 = EasyMock.createNiceMock(Task.class);
		EasyMock.expect(t0.getProject()).andReturn(p0);
		EasyMock.expect(t0.isFulfilled()).andReturn(false);
		EasyMock.expect(t0.getId()).andReturn(0);
		EasyMock.replay(t0);
		
		t1 = EasyMock.createNiceMock(Task.class);
		EasyMock.expect(t1.getProject()).andReturn(p0);
		EasyMock.expect(t1.isFulfilled()).andReturn(false);
		EasyMock.expect(t1.getId()).andReturn(1);
		EasyMock.replay(t1);
	}
	
	/**
	 * Tests if project mementos properly save and restore the list of tasks of a project.
	 */
	@Test
	public void testTasks() {
		Project.Memento p0memento0 = p0.createMemento();
		ProjectProperties p0properties0 = new ProjectProperties(p0);
		p0properties0.checkEquals(p0);
		
		p0.addTask(t0);
		
		Project.Memento p0memento1 = p0.createMemento();
		ProjectProperties p0properties1 = new ProjectProperties(p0);
		p0properties1.checkEquals(p0);
		
		p0.addTask(t1);
		
		Project.Memento p0memento2 = p0.createMemento();
		ProjectProperties p0properties2 = new ProjectProperties(p0);
		p0properties2.checkEquals(p0);
		
		p0.setMemento(p0memento0);
		p0properties0.checkEquals(p0);
		
		p0.setMemento(p0memento1);
		p0properties1.checkEquals(p0);

		p0.setMemento(p0memento2);
		p0properties2.checkEquals(p0);

		p0.setMemento(p0memento0);
		p0properties0.checkEquals(p0);
		}
	
	/**
	 * A class storing the properties of a project that are stored
	 * in a project memento
	 * 
	 * @author Mathias, Pieter-Jan, Frederic
	 *
	 */
	private class ProjectProperties
	{
		private List<Task> tasks;
		
		/**
		 * Initializes this ProjectProperties based on the given project.
		 * @param project The project on which this ProjectProperties should be based
		 */
		public ProjectProperties(Project project)
		{
			tasks = project.getTasks();
		}
		
		/**
		 * Tests if the state stored in this ProjectProperties is equal to the state
		 * of the given project.
		 * 
		 * @param project The project to check.
		 */
		public void checkEquals(Project project)
		{
			if(tasks == null)
				assertEquals(null, project.getTasks());
			else
			{
				assertTrue(tasks.containsAll(project.getTasks()));
				assertTrue(project.getTasks().containsAll(tasks));
			}
		}
	}

}
