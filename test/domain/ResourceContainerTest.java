package domain;

import static org.junit.Assert.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.easymock.Capture;
import org.easymock.EasyMock;
import org.junit.Before;
import org.junit.Test;

import domain.task.Task;
import domain.time.Duration;
import domain.time.Timespan;
import exception.ConflictException;
import exception.ObjectNotFoundException;

public class ResourceContainerTest {

    private LocalDateTime startRes = LocalDateTime.of(2015, 1, 1, 14, 0);
    private LocalDateTime endRes = LocalDateTime.of(2015, 1, 3, 15, 0);
    private Timespan reserved = new Timespan(startRes, endRes);
    
	private Resource res0, res1, res2, dev;
	private ResourceType type0, type1;
	private ResourceContainer rc0, rc4;
	private Task t0, t1, t3;

	@Before
	public void setUp() throws Exception {
		rc0 = new ResourceContainer();
		rc4 = new ResourceContainer();
		
		type0 = new ResourceType("very simple");
	    type1 = new ResourceType("still simple", Arrays.asList(type0), new ArrayList<>());
	    
	    res0 = rc4.createResource("tic", type0);
	    res1 = rc4.createResource("tac", type1);
	    res1.makeReservation(t0, reserved);
	    res2 = rc4.createResource("bla", type1);
	    dev = rc4.createResource("Jef", ResourceType.DEVELOPER);
	    
		t0 = EasyMock.createNiceMock(Task.class);
		Project p = EasyMock.createNiceMock(Project.class);
		p.addTask(EasyMock.capture(new Capture<Task>()));
		EasyMock.expectLastCall().times(2);
		
		Map<ResourceType, Integer> requiredResources = Task.getDefaultRequiredResources();
		Map<ResourceType, Integer> requiredResources2 = Task.getDefaultRequiredResources();
		requiredResources2.put(type0, 1);
		requiredResources2.put(type1, 1);
		t1 = new Task("descr", new Duration(15), 0, requiredResources, p);
		t3 = new Task("other", new Duration(120), 10, requiredResources2, p);
	}

	@Test
	public void testResourceContainer() {
		ResourceContainer container = new ResourceContainer();
		assertTrue(container.getResources().isEmpty());
	}

	@Test
	public void testCreateResource() {
		String name = "test";
		Resource r = rc0.createResource(name, type1);
		assertEquals(1, rc0.getResources().size());
		assertTrue(rc0.getResources().contains(r));
		r = rc4.createResource(name, type1);
		assertEquals(5, rc4.getResources().size());
		assertTrue(rc4.getResources().contains(res0));
		assertTrue(rc4.getResources().contains(res1));
		assertTrue(rc4.getResources().contains(res2));
		assertTrue(rc4.getResources().contains(dev));
		assertTrue(rc4.getResources().contains(r));
	}
	
	@Test(expected=ObjectNotFoundException.class)
	public void testGetResourceInt() {
		assertEquals(res0, rc4.getResource(res0.getId()));
		assertEquals(res1, rc4.getResource(res1.getId()));
		assertEquals(res2, rc4.getResource(res2.getId()));
		rc4.getResource(new Resource("something", type0).getId());
	}

	@Test
	public void testGetResourcesOfType() {
		assertTrue(rc0.getResourcesOfType(type0).isEmpty());
		assertTrue(rc0.getResourcesOfType(type1).isEmpty());
		
		Set<Resource> resources = rc4.getResourcesOfType(type0);
		assertEquals(1, resources.size());
		assertTrue(resources.contains(res0));
		resources = rc4.getResourcesOfType(type1);
		assertEquals(2, resources.size());
		assertTrue(resources.contains(res1));
		assertTrue(resources.contains(res2));
	}

	@Test
	public void testGetAvailableResourcesTimespan() {
		assertTrue(rc0.getAvailableResources(reserved).isEmpty());
		Set<Resource> availableResources = rc4.getAvailableResources(reserved);
		assertEquals(3, availableResources.size());
		assertTrue(availableResources.contains(res0));
		assertTrue(availableResources.contains(res2));
		assertTrue(availableResources.contains(dev));
		availableResources = rc4.getAvailableResources(new Timespan(endRes));
		assertEquals(4, availableResources.size());
		assertTrue(availableResources.contains(res0));
		assertTrue(availableResources.contains(res1));
		assertTrue(availableResources.contains(res2));
		assertTrue(availableResources.contains(dev));
		
	}

	@Test
	public void testGetAvailableResourcesResourceTypeTimespan() {
		assertTrue(rc0.getAvailableResources(type0, reserved).isEmpty());
		assertTrue(rc0.getAvailableResources(type1, reserved).isEmpty());

        Set<Resource> availableResources = rc4.getAvailableResources(type1, reserved);
        assertEquals(1, availableResources.size());
        assertTrue(availableResources.contains(res2));

        availableResources = rc4.getAvailableResources(type0, reserved);
		assertEquals(1, availableResources.size());
        assertTrue(availableResources.contains(res0));
        availableResources = rc4.getAvailableResources(type1, reserved);
		assertEquals(1, availableResources.size());
        assertTrue(availableResources.contains(res2));
        availableResources = rc4.getAvailableResources(type1, new Timespan(endRes));
        assertEquals(2, availableResources.size());
        assertTrue(availableResources.contains(res1));
        assertTrue(availableResources.contains(res2));
	}

	@Test
	public void testHasAvailableOfType() {
		assertFalse(rc0.hasAvailableOfType(type0, reserved, -1));
		assertTrue(rc0.hasAvailableOfType(type1, reserved, 0));
        assertFalse(rc4.hasAvailableOfType(type0, reserved, -1));
        assertTrue(rc4.hasAvailableOfType(type0, reserved, 0));
        assertTrue(rc4.hasAvailableOfType(type0, reserved, 1));
        assertFalse(rc4.hasAvailableOfType(type0, reserved, 2));
        assertTrue(rc4.hasAvailableOfType(type1, reserved, 0));
        assertTrue(rc4.hasAvailableOfType(type1, reserved, 1));
        assertFalse(rc4.hasAvailableOfType(type1, reserved, 2));
        assertTrue(rc4.hasAvailableOfType(type1, new Timespan(endRes), 1));
        assertTrue(rc4.hasAvailableOfType(type1, new Timespan(endRes), 2));
        assertFalse(rc4.hasAvailableOfType(type1, new Timespan(endRes), 3));
	}
	
	@Test
	public void testMeetRequirementsNormal() {
		List<Resource> resources = rc4.meetRequirements(t1, reserved, new ArrayList<>());
		assertEquals(1, resources.size());
		assertTrue(resources.contains(dev));
		
		resources = rc4.meetRequirements(t1, reserved, Arrays.asList(res0.getId()));
		
		resources = rc4.meetRequirements(t3, reserved, new ArrayList<>());
		assertEquals(3, resources.size());
		assertTrue(resources.contains(dev));
		assertTrue(resources.contains(res0));
		assertTrue(resources.contains(res2));
	}
	
	@Test(expected=IllegalStateException.class)
	public void testMeetRequirementsNoResources() {
		rc0.meetRequirements(t1, reserved, new ArrayList<>());
	}
	
	@Test(expected=ObjectNotFoundException.class)
	public void testMeetRequirementsUnexistingId() {
		rc4.meetRequirements(t3, reserved, Arrays.asList(100));
	}
	
	@Test(expected=ConflictException.class)
	public void testMeetRequirementsResourceAlreadyReserved() {
		rc4.meetRequirements(t3, reserved, Arrays.asList(res1.getId()));
	}

}
