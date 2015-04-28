package domain;

import static org.junit.Assert.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;

import org.junit.Before;
import org.junit.Test;

import domain.time.Duration;
import domain.time.Timespan;

public class ResourceTest {
	
	private Resource r0, r1;
	LocalDateTime startTime = LocalDateTime.of(2015, 1, 1, 14, 30);
	LocalDateTime dueTime = LocalDateTime.of(2015, 1, 15, 10, 0);
	Timespan reservedSpan = new Timespan(startTime, startTime.plusHours(2));
	Timespan justBefore = new Timespan(reservedSpan.getStartTime().minusDays(1), reservedSpan.getStartTime());
	Timespan justAfter = new Timespan(reservedSpan.getEndTime(), reservedSpan.getEndTime().plusDays(1));
	Reservation reservation;

	@Before
	public void setUp() throws Exception {
		
		ResourceType type = new ResourceType("type", new ArrayList<>(), new ArrayList<>());
		Map<ResourceType, Integer> requirements = new HashMap<>();
		requirements.put(type, 2);
		
		ProjectContainer pc = new ProjectContainer();
		Project p = pc.createProject("name", "description", startTime, dueTime);
		Task t = p.createTask("descr", new Duration(120), 10, -1, new ArrayList<>(), requirements);
		r0 = new Resource("not reserved");
		r1 = new Resource("reserved");
		reservation = r1.makeReservation(t, reservedSpan);
	}

	@Test
	public void testConstructorValid() {
		String name = "r";
		Resource r = new Resource(name);
		
		assertEquals(name, r.getName());
		assertTrue(r.getReservations().isEmpty());
		assertTrue(r.getPreviousReservations().isEmpty());
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testConstructorNullName() {
		new Resource(null);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testConstructorShortName() {
		new Resource("");
	}

	@Test
	public void testIsAvailable() {
		assertTrue(r0.isAvailable(new Timespan(startTime, dueTime)));
		assertTrue(r1.isAvailable(justBefore));
		assertTrue(r1.isAvailable(justAfter));
		assertFalse(r1.isAvailable(reservedSpan));
		assertFalse(r1.isAvailable(new Timespan(reservedSpan.getStartTime().plusMinutes(30), reservedSpan.getEndTime())));
		assertFalse(r1.isAvailable(new Timespan(reservedSpan.getStartTime(), reservedSpan.getEndTime().minusMinutes(30))));
	}

	@Test
	public void testGetConflictingReservations() {
		assertTrue(r0.getConflictingReservations(reservedSpan).isEmpty());
		assertTrue(r1.getConflictingReservations(justBefore).isEmpty());
		assertTrue(r1.getConflictingReservations(justAfter).isEmpty());
		
		Set<Reservation> conflictingReservations = r1.getConflictingReservations(reservedSpan);
		assertEquals(1, conflictingReservations.size());
		assertTrue(conflictingReservations.contains(reservation));
	}

	@Test
	public void testFindConflictingTasks() {
		assertTrue(r0.findConflictingTasks(reservedSpan).isEmpty());
		assertTrue(r1.findConflictingTasks(justAfter).isEmpty());
		assertTrue(r1.findConflictingTasks(justBefore).isEmpty());
		
		Set<Task> conflictingTasks = r1.findConflictingTasks(reservedSpan);
		assertEquals(1, conflictingTasks.size());
		assertTrue(conflictingTasks.contains(reservation.getTask()));
	}

	@Test
	public void testGetReservationTask() {
		assertEquals(null, r0.getReservation(reservation.getTask()));
		assertEquals(reservation, r1.getReservation(reservation.getTask()));
	}

	@Test
	public void testGetReservationsLocalDateTime() {
		assertTrue(r0.getReservations(startTime).isEmpty());
		assertTrue(r1.getReservations(dueTime).isEmpty());
		
		SortedSet<Reservation> reservations = r1.getReservations(startTime);
		assertEquals(1, reservations.size());
		assertTrue(reservations.contains(reservation));
	}

	@Test
	public void testNextAvailableTimespans() {
		Set<Timespan> available0 = r0.nextAvailableTimespans(startTime);
		assertEquals(1, available0.size());
		assertTrue(available0.contains(new Timespan(startTime)));
		
		Set<Timespan> available1 = r1.nextAvailableTimespans(dueTime);
		assertEquals(1, available1.size());
		assertTrue(available1.contains(new Timespan(dueTime)));
		
		LocalDateTime afterStartTime = reservedSpan.getStartTime().plusMinutes(30);
		Set<Timespan> available2 = r1.nextAvailableTimespans(afterStartTime);
		assertEquals(1, available2.size());
		assertTrue(available2.contains(new Timespan(reservedSpan.getEndTime())));
		
		LocalDateTime beforeStartTime = reservedSpan.getStartTime().minusDays(2);
		Set<Timespan> available3 = r1.nextAvailableTimespans(beforeStartTime);
		assertEquals(2, available3.size());
		assertTrue(available3.contains(new Timespan(reservedSpan.getEndTime())));
		assertTrue(available3.contains(new Timespan(beforeStartTime, reservedSpan.getStartTime())));
	}

	@Test
	public void testMakeReservationTaskTimespan() {
		fail("Not yet implemented"); // TODO
	}

}
