package domain;

import static org.junit.Assert.*;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import domain.time.WorkWeekConfiguration;

public class ResourceTypeTest {
	
	private LocalTime availableFrom = LocalTime.of(12, 0);
	private LocalTime availableTo = LocalTime.of(17, 0);
	private WorkWeekConfiguration available = new WorkWeekConfiguration(availableFrom, availableTo, 
			WorkWeekConfiguration.NO_LUNCHBREAK, WorkWeekConfiguration.NO_LUNCHBREAK);
	
	private ResourceType type0;

	@Before
	public void setUp() throws Exception {
		type0 = new ResourceType("very simple");
	}

	@Test
	public void testResourceTypeValid() {
		String name = "name";
		List<ResourceType> requirements = new ArrayList<>();
		List<ResourceType> conflicts = new ArrayList<>();
		WorkWeekConfiguration availability = WorkWeekConfiguration.ALWAYS;
		ResourceType type = new ResourceType(name, requirements, conflicts, availability);
		assertEquals(name, type.getName());
		assertEquals(requirements, type.getRequirements());
		assertEquals(conflicts, type.getConflicts());
		assertEquals(availability, type.getAvailability());
		
		String name2 = "other";
		List<ResourceType> requirements2 = Arrays.asList(type);
		List<ResourceType> conflicts2 = new ArrayList<>();
		WorkWeekConfiguration availability2 = available;
		ResourceType type2 = new ResourceType(name2, requirements2, conflicts2, availability2);
		assertEquals(name2, type2.getName());
		assertEquals(requirements2, type2.getRequirements());
		assertEquals(conflicts2, type2.getConflicts());
		assertEquals(availability2, type2.getAvailability());
		
		String name3 = "againAnother";
		List<ResourceType> requirements3 = Arrays.asList(type2);
		List<ResourceType> conflicts3 = Arrays.asList(type);
		WorkWeekConfiguration availability3 = WorkWeekConfiguration.ALWAYS;
		ResourceType type3 = new ResourceType(name3, requirements3, conflicts3, availability3);
		assertEquals(name3, type3.getName());
		assertEquals(requirements3, type3.getRequirements());
		assertEquals(conflicts3, type3.getConflicts());
		assertEquals(availability3, type3.getAvailability());
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testResourceTypeNullName() {
		new ResourceType(null);
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testResourceTypeEmptyName() {
		new ResourceType("", new ArrayList<>(), new ArrayList<>(), null);
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testResourceTypeInvalidRequirements() {
		new ResourceType("name", Arrays.asList(type0), Arrays.asList(type0), null);
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testResourceTypeInvalidAvailability() {
		new ResourceType("name", new ArrayList<>(), new ArrayList<>(), null);
	}

	@Test
	public void testCanHaveAsName() {
		fail("Not yet implemented"); // TODO
	}

	@Test
	public void testCanHaveAsRequirements() {
		fail("Not yet implemented"); // TODO
	}

	@Test
	public void testCanHaveAsConflicts() {
		fail("Not yet implemented"); // TODO
	}

	@Test
	public void testCanHaveAsAvailability() {
		fail("Not yet implemented"); // TODO
	}

	@Test
	public void testCanHaveAsResource() {
		fail("Not yet implemented"); // TODO
	}

	@Test
	public void testGetAvailableResources() {
		fail("Not yet implemented"); // TODO
	}

	@Test
	public void testHasAvailableResources() {
		fail("Not yet implemented"); // TODO
	}

	@Test
	public void testFindConflictingTasks() {
		fail("Not yet implemented"); // TODO
	}

	@Test
	public void testMakeReservation() {
		fail("Not yet implemented"); // TODO
	}

	@Test
	public void testNextAvailableTimespans() {
		fail("Not yet implemented"); // TODO
	}

	@Test
	public void testCanHaveAsCombination() {
		fail("Not yet implemented"); // TODO
	}

	@Test
	public void testNumberOfResources() {
		fail("Not yet implemented"); // TODO
	}

}
