package domain;

import domain.task.Task;
import domain.time.Duration;
import domain.time.Timespan;
import domain.time.WorkWeekConfiguration;
import exception.ConflictException;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Test;

public class ResourceTypeTest {

    private LocalTime availableFrom = LocalTime.of(12, 0);
    private LocalTime availableTo = LocalTime.of(17, 0);
    private WorkWeekConfiguration available = new WorkWeekConfiguration(availableFrom, availableTo,
            WorkWeekConfiguration.NO_LUNCHBREAK, WorkWeekConfiguration.NO_LUNCHBREAK);
    private LocalDateTime startRes = LocalDateTime.of(2015, 1, 1, 14, 0);
    private LocalDateTime endRes = LocalDateTime.of(2015, 1, 3, 15, 0);
    private Timespan reserved = new Timespan(startRes, endRes);

    private ResourceType type0, type1, type2;
    private Resource res0, res1, res2;
    private Task t0, t1;

    @Before
    public void setUp() throws Exception {
        Project p = new Project("name", "description", startRes, endRes);
        t0 = p.createTask("descr", new Duration(60), 10, Project.NO_ALTERNATIVE, Project.NO_DEPENDENCIES, Task.NO_REQUIRED_RESOURCE_TYPES);
        t1 = p.createTask("task1", new Duration(120), 10, -1, new ArrayList<>(), new HashMap<>());
        res0 = new Resource("tic");
        res1 = new Resource("tac");
        res1.makeReservation(t0, reserved);
        res2 = new Resource("bla");

        type0 = new ResourceType("very simple");
        type1 = new ResourceType("still simple", Arrays.asList(type0), new ArrayList<>());
        type1.addResource(res0);
        type2 = new ResourceType("limited", new ArrayList<>(), Arrays.asList(type0), available);
        type2.addResource(res1);
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

    @Test(expected = IllegalArgumentException.class)
    public void testResourceTypeNullName() {
        new ResourceType(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testResourceTypeEmptyName() {
        new ResourceType("");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testResourceTypeInvalidRequirements() {
        new ResourceType("name", Arrays.asList(type0), Arrays.asList(type0));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testResourceTypeInvalidAvailability() {
        new ResourceType("name", new ArrayList<>(), new ArrayList<>(), null);
    }

    @Test
    public void testGetAvailableResources() {
        assertTrue(type0.getAvailableResources(reserved).isEmpty());

        Set<Resource> availableResources = type1.getAvailableResources(reserved);
        assertEquals(1, availableResources.size());
        assertTrue(availableResources.contains(res0));

        assertTrue(type2.getAvailableResources(reserved).isEmpty());
        availableResources = type2.getAvailableResources(new Timespan(endRes));
        assertEquals(1, availableResources.size());
        assertTrue(availableResources.contains(res1));
    }

    @Test
    public void testHasAvailableResources() {
        assertFalse(type0.hasAvailableResources(reserved, -1));
        assertTrue(type0.hasAvailableResources(reserved, 0));
        assertFalse(type0.hasAvailableResources(reserved, 1));
        assertTrue(type1.hasAvailableResources(reserved, 0));
        assertTrue(type1.hasAvailableResources(reserved, 1));
        assertFalse(type1.hasAvailableResources(reserved, 2));
        assertTrue(type2.hasAvailableResources(reserved, 0));
        assertFalse(type2.hasAvailableResources(reserved, 1));
        assertTrue(type2.hasAvailableResources(new Timespan(endRes), 1));
        assertFalse(type2.hasAvailableResources(new Timespan(endRes), 2));
    }

    @Test
    public void testFindConflictingTasks() {
        assertTrue(type0.findConflictingTasks(reserved).isEmpty());
        assertTrue(type1.findConflictingTasks(reserved).isEmpty());
        Set<Task> conflictingTasks = type2.findConflictingTasks(reserved);
        assertEquals(1, conflictingTasks.size());
        assertTrue(conflictingTasks.contains(t0));
    }

    @Test
    public void testMakeReservationValid() throws ConflictException {
        Set<Resource> reservations = type1.makeReservation(t0, reserved, 1);
        assertEquals(1, reservations.size());
        assertTrue(reservations.contains(res0));
        reservations = type2.makeReservation(t1, new Timespan(endRes, endRes.plusDays(1)), 1);
        assertEquals(1, reservations.size());
        assertTrue(reservations.contains(res1));
        type2.addResource(res2);
        reservations = type2.makeReservation(t1, reserved, 1);
        assertEquals(1, reservations.size());
        assertTrue(reservations.contains(res2));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMakeReservationNoResources() throws ConflictException {
        type0.makeReservation(t0, reserved, 1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMakeEmptyReservation() throws ConflictException {
        type1.makeReservation(t0, reserved, 0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMakeReservationSameTask() throws ConflictException {
        type2.makeReservation(t0, new Timespan(endRes, endRes.plusDays(1)), 1);
    }

    @Test(expected = ConflictException.class)
    public void testMakeReservationOverlappingTime() throws ConflictException {
        type2.makeReservation(t1, reserved, 1);
    }

    @Test
    public void testNextAvailableTimespans() {
        assertTrue(type0.nextAvailableTimespans(startRes).isEmpty());
        SortedSet<Timespan> availableTimespans = type1.nextAvailableTimespans(startRes);
        assertEquals(1, availableTimespans.size());
        assertTrue(availableTimespans.contains(new Timespan(startRes)));

        availableTimespans = type2.nextAvailableTimespans(endRes);
        assertEquals(1, availableTimespans.size());
        assertTrue(availableTimespans.contains(new Timespan(endRes)));

        availableTimespans = type2.nextAvailableTimespans(startRes);
        assertEquals(1, availableTimespans.size());
        assertTrue(availableTimespans.contains(new Timespan(endRes)));
        LocalDateTime beforeStart = startRes.minusDays(1);

        type2.addResource(res2);

        availableTimespans = type2.nextAvailableTimespans(beforeStart);
        assertEquals(3, availableTimespans.size());
        assertTrue(availableTimespans.contains(new Timespan(beforeStart)));
        assertTrue(availableTimespans.contains(new Timespan(endRes)));
        assertTrue(availableTimespans.contains(new Timespan(beforeStart, startRes)));

        availableTimespans = type2.nextAvailableTimespans(startRes);
        assertEquals(2, availableTimespans.size());
        assertTrue(availableTimespans.contains(new Timespan(startRes)));
        assertTrue(availableTimespans.contains(new Timespan(endRes)));
        try {
            type2.makeReservation(t1, reserved, 1);
        } catch (ConflictException e) {
        }

        availableTimespans = type2.nextAvailableTimespans(beforeStart);
        assertEquals(2, availableTimespans.size());
        assertTrue(availableTimespans.contains(new Timespan(endRes)));
        assertTrue(availableTimespans.contains(new Timespan(beforeStart, startRes)));
    }

    @Test
    public void testCanHaveAsCombination() {
        HashMap<ResourceType, Integer> combination = new HashMap<>();
        combination.put(type0, 2);
        combination.put(type1, 3);
        assertFalse(type2.canHaveAsCombination(combination));
        assertTrue(type1.canHaveAsCombination(combination));
        combination.remove(type0);
        assertFalse(type1.canHaveAsCombination(combination));
    }

    @Test
    public void testNumberOfResources() {
        type0.addResource(res0);
        type0.addResource(res1);
        assertEquals(2, type0.numberOfResources(Arrays.asList(res0,res1,res2)));
        assertEquals(0, type1.numberOfResources(Arrays.asList(res1,res2)));

    }

}
