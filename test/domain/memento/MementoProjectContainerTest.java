package domain.memento;

import domain.Project;
import domain.ProjectContainer;
import domain.ProjectContainer.Memento;
import java.time.LocalDateTime;
import java.util.Arrays;
import static org.junit.Assert.assertTrue;
import org.junit.BeforeClass;
import org.junit.Test;

public class MementoProjectContainerTest {

    private static Project p1, p2, p3;
    
    private static ProjectContainer container;
    
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
        container = new ProjectContainer();
        p1 = container.createProject("Mobile Steps", "A description.", LocalDateTime.of(2015, 3, 12, 17, 30), LocalDateTime.of(2015, 3, 22, 17, 50));
        p2 = container.createProject("Test 2", "A description.", LocalDateTime.of(2015, 3, 12, 17, 30), LocalDateTime.of(2015, 3, 22, 17, 50));
        p3 = container.createProject("Test 3", "A description.", LocalDateTime.of(2015, 3, 12, 17, 30), LocalDateTime.of(2015, 3, 22, 17, 50));
	}
	
	/**
	 * Tests whether project container mementos save and restore the state of a project container correctly.
	 */
	@Test
	public void testRevertCreateProject() {
		ProjectContainer.Memento memento123 = container.createMemento();
		Project p4 = container.createProject(
				"Test 4",
				"Description of project Test 4",
				LocalDateTime.of(2015, 3, 12, 17, 30),
				LocalDateTime.of(2015, 3, 22, 17, 50));
		ProjectContainer.Memento memento1234 = container.createMemento();
		Project p5 = container.createProject(
				"Test 5",
				"A description",
				LocalDateTime.of(2015, 3, 12, 17, 30),
				LocalDateTime.of(2015, 3, 22, 17, 50));
		Memento memento12345 = container.createMemento();
		assertTrue(container.getProjects().containsAll(Arrays.asList(p1, p2, p3, p4, p5)));
		assertTrue(Arrays.asList(p1, p2, p3, p4, p5).containsAll(container.getProjects()));
		container.setMemento(memento1234);
		assertTrue(container.getProjects().containsAll(Arrays.asList(p1, p2, p3, p4)));
		assertTrue(Arrays.asList(p1, p2, p3, p4).containsAll(container.getProjects()));
		container.setMemento(memento12345);
		assertTrue(container.getProjects().containsAll(Arrays.asList(p1, p2, p3, p4, p5)));
		assertTrue(Arrays.asList(p1, p2, p3, p4, p5).containsAll(container.getProjects()));
		container.setMemento(memento123);
		assertTrue(container.getProjects().containsAll(Arrays.asList(p1, p2, p3)));
		assertTrue(Arrays.asList(p1, p2, p3).containsAll(container.getProjects()));
	}

}
