package domain;

import static org.junit.Assert.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Set;

import org.easymock.EasyMock;
import org.junit.Before;
import org.junit.Test;

import domain.task.Task;
import domain.time.Timespan;
import exception.ConflictException;

public class ResourceContainerTest {

    private LocalDateTime startRes = LocalDateTime.of(2015, 1, 1, 14, 0);
    private LocalDateTime endRes = LocalDateTime.of(2015, 1, 3, 15, 0);
    private Timespan reserved = new Timespan(startRes, endRes);
    
	private Resource res0, res1, res2;
	private ResourceType type0, type1;
	private ResourceContainer rc0, rc3;
	private Task t0, t1;

	@Before
	public void setUp() throws Exception {
		t0 = EasyMock.createNiceMock(Task.class);
		t1 = EasyMock.createNiceMock(Task.class);
	    
		rc0 = new ResourceContainer();
		rc3 = new ResourceContainer();
		
		type0 = new ResourceType("very simple");
	    type1 = new ResourceType("still simple", Arrays.asList(type0), new ArrayList<>());
	    
	    res0 = rc3.createResource("tic", type0);
	    res1 = rc3.createResource("tac", type1);
	    res1.makeReservation(t0, reserved);
	    res2 = rc3.createResource("bla", type1);
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
		r = rc3.createResource(name, type1);
		assertEquals(4, rc3.getResources().size());
		assertTrue(rc3.getResources().contains(res0));
		assertTrue(rc3.getResources().contains(res1));
		assertTrue(rc3.getResources().contains(res2));
		assertTrue(rc3.getResources().contains(r));
	}

	@Test
	public void testGetResourcesOfType() {
		assertTrue(rc0.getResourcesOfType(type0).isEmpty());
		assertTrue(rc0.getResourcesOfType(type1).isEmpty());
		
		Set<Resource> resources = rc3.getResourcesOfType(type0);
		assertEquals(1, resources.size());
		assertTrue(resources.contains(res0));
		resources = rc3.getResourcesOfType(type1);
		assertEquals(2, resources.size());
		assertTrue(resources.contains(res1));
		assertTrue(resources.contains(res2));
	}

	@Test
	public void testGetAvailableResourcesTimespan() {
		assertTrue(rc0.getAvailableResources(reserved).isEmpty());
		Set<Resource> availableResources = rc3.getAvailableResources(reserved);
		assertEquals(2, availableResources.size());
		assertTrue(availableResources.contains(res0));
		assertTrue(availableResources.contains(res2));
		availableResources = rc3.getAvailableResources(new Timespan(endRes));
		assertEquals(3, availableResources.size());
		assertTrue(availableResources.contains(res0));
		assertTrue(availableResources.contains(res1));
		assertTrue(availableResources.contains(res2));
		
	}

	@Test
	public void testGetAvailableResourcesResourceTypeTimespan() {
		assertTrue(rc0.getAvailableResources(type0, reserved).isEmpty());
		assertTrue(rc0.getAvailableResources(type1, reserved).isEmpty());

        Set<Resource> availableResources = rc3.getAvailableResources(type1, reserved);
        assertEquals(1, availableResources.size());
        assertTrue(availableResources.contains(res2));

        availableResources = rc3.getAvailableResources(type0, reserved);
		assertEquals(1, availableResources.size());
        assertTrue(availableResources.contains(res0));
        availableResources = rc3.getAvailableResources(type1, new Timespan(endRes));
        assertEquals(2, availableResources.size());
        assertTrue(availableResources.contains(res1));
        assertTrue(availableResources.contains(res2));
	}

	@Test
	public void testHasAvailableOfType() {
		assertFalse(rc0.hasAvailableOfType(type0, reserved, -1));
		assertTrue(rc0.hasAvailableOfType(type1, reserved, 0));
        assertFalse(rc3.hasAvailableOfType(type0, reserved, -1));
        assertTrue(rc3.hasAvailableOfType(type0, reserved, 0));
        assertTrue(rc3.hasAvailableOfType(type0, reserved, 1));
        assertFalse(rc3.hasAvailableOfType(type0, reserved, 2));
        assertTrue(rc3.hasAvailableOfType(type1, reserved, 0));
        assertTrue(rc3.hasAvailableOfType(type1, reserved, 1));
        assertFalse(rc3.hasAvailableOfType(type1, reserved, 2));
        assertTrue(rc3.hasAvailableOfType(type1, new Timespan(endRes), 1));
        assertTrue(rc3.hasAvailableOfType(type1, new Timespan(endRes), 2));
        assertFalse(rc3.hasAvailableOfType(type1, new Timespan(endRes), 3));
	}

//    @Test
//    public void testMakeReservationValid() throws ConflictException {
//        Set<Resource> reservations = rc3.makeReservation(type0, t0, reserved, 1);
//        assertEquals(1, reservations.size());
//        assertTrue(reservations.contains(res0));
//        reservations = rc3.makeReservation(type1, t0, reserved, 1);
//        assertEquals(1, reservations.size());
//        assertTrue(reservations.contains(res2));
//        reservations = rc3.makeReservation(type1, t1, new Timespan(endRes, endRes.plusDays(1)), 2);
//        assertEquals(2, reservations.size());
//        assertTrue(reservations.contains(res1));
//        assertTrue(reservations.contains(res2));
//        Resource extra = rc3.createResource("extra", type1);
//        reservations = rc3.makeReservation(type1, t1, reserved, 1);
//        assertEquals(1, reservations.size());
//        assertTrue(reservations.contains(extra));
//    }
//
//    @Test(expected = IllegalArgumentException.class)
//    public void testMakeReservationNoResources() throws ConflictException {
//        rc0.makeReservation(type0, t0, reserved, 1);
//    }
//
//    @Test(expected = IllegalArgumentException.class)
//    public void testMakeEmptyReservation() throws ConflictException {
//        rc3.makeReservation(type0, t0, reserved, 0);
//    }
//
//    @Test(expected = IllegalArgumentException.class)
//    public void testMakeReservationSameTask() throws ConflictException {
//    	rc3.makeReservation(type0, t0, reserved, 1);
//        rc3.makeReservation(type0, t0, new Timespan(endRes, endRes.plusDays(1)), 1);
//    }
//
//    @Test(expected = ConflictException.class)
//    public void testMakeReservationOverlappingTime() throws ConflictException {
//    	res2.makeReservation(t0, reserved);
//        rc3.makeReservation(type1, t1, reserved, 1);
//    }

}
