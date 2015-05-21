package domain;

import domain.task.Task;
import domain.time.Timespan;
import exception.ConflictException;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.SortedSet;
import static org.easymock.EasyMock.createNiceMock;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Test;

public class ResourceTest {
	
	private LocalDateTime startTime = LocalDateTime.of(2015, 1, 1, 14, 30);
	private LocalDateTime dueTime = LocalDateTime.of(2015, 1, 15, 10, 0);
	private Timespan reservedSpan = new Timespan(startTime, startTime.plusHours(2));
	private Timespan justBefore = new Timespan(reservedSpan.getStartTime().minusDays(1), reservedSpan.getStartTime());
	private Timespan justAfter = new Timespan(reservedSpan.getEndTime(), reservedSpan.getEndTime().plusDays(1));
	
	private Task t0, t1, t2;
	private Resource r0, r1;
	private ResourceType type0, type1;
	private Reservation reservation;
   

	@Before
	public void setUp() throws Exception {
		t0 = createNiceMock(Task.class);
		t1 = createNiceMock(Task.class);
		t2 = createNiceMock(Task.class);
		
		type0 = new ResourceType("type2");
		type1 = new ResourceType("type3");
		
		r0 = new Resource("not reserved", type0);
		r1 = new Resource("reserved", type1);
		reservation = r1.makeReservation(t0, reservedSpan);
	}

	@Test
	public void testConstructorValid() {
		String name = "r";
		ResourceType type = new ResourceType("test");
		Resource r = new Resource(name, type);
		
		assertEquals(name, r.getName());
		assertEquals(type, r.getType());
		assertTrue(r.getReservations().isEmpty());
		assertTrue(r.getPreviousReservations().isEmpty());
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testConstructorNullName() {
		new Resource(null, type0);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testConstructorShortName() {
		new Resource("", type0);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testConstructorNullType() {
		new Resource("name", null);
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
	public void testMakeReservationValid() throws ConflictException {
		Reservation res = r0.makeReservation(t0, reservedSpan);
		assertEquals(1, r0.getReservations().size());
		assertTrue(r0.getReservations().contains(res));
		
		res = r1.makeReservation(t1, justAfter);
		assertEquals(2, r1.getReservations().size());
		assertTrue(r1.getReservations().contains(reservation));
		assertTrue(r1.getReservations().contains(res));
		Reservation res2 = r1.makeReservation(t2, justBefore);
		assertEquals(3, r1.getReservations().size());
		assertTrue(r1.getReservations().contains(res2));
	}
	
	@Test(expected = ConflictException.class)
	public void testMakeReservationConflict() throws ConflictException {
		r1.makeReservation(t1, reservedSpan);
	}
	
	@Test(expected = IllegalArgumentException.class) 
	public void testMakeReservationSameTask() throws ConflictException {
		r1.makeReservation(t0, justAfter);
	}
        
        @Test
	public void testClearFutureReservations() {
        // just overlapping on edge
        r0.makeReservation(t0, new Timespan(startTime, startTime.plusHours(2)));
        r0.clearFutureReservations(startTime, t0);
        assertEquals(null, r0.getReservation(t0));
        
        // partly consumed
        r0.makeReservation(t0, new Timespan(startTime, startTime.plusHours(2)));
        r0.clearFutureReservations(startTime.plusHours(1), t0);
        assertEquals(startTime.plusHours(1), r0.getPreviousReservations().get(0).getEndTime());
        
        // no reservations in future
        r0.makeReservation(t0, new Timespan(startTime, startTime.plusHours(2)));
        r0.clearFutureReservations(startTime.plusHours(8), t0);
        assertEquals(1, r0.getReservations().size());
	}
}
