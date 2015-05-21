package scenariotest;

import controller.PlanTaskHandler;
import domain.BranchOffice;
import domain.Database;
import domain.Planning;
import domain.Project;
import domain.ProjectContainer;
import domain.Resource;
import domain.ResourceContainer;
import domain.ResourceType;
import domain.dto.DetailedPlanning;
import domain.dto.DetailedResource;
import domain.dto.DetailedResourceType;
import domain.dto.DetailedTask;
import domain.task.Task;
import domain.time.Clock;
import domain.time.Duration;
import domain.time.Timespan;
import domain.user.Acl;
import domain.user.Auth;
import domain.user.Developer;
import domain.user.GenericUser;
import domain.user.User;
import exception.ConflictException;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

public class PlanTaskScenarioTest {

	private static final LocalDateTime START = LocalDateTime.of(2014, 2, 4, 10, 0);
	private static final LocalDateTime DUE = START.plusDays(7);

	private PlanTaskHandler handler;
	private Clock clock;
	private User user;
	private BranchOffice office;
	private ProjectContainer pc;
	private ResourceContainer rc;
	private Task t1, t2, t3;
	private ResourceType type0, type1;
	private Resource res0, res00, res000, res1;
	private Developer dev0, dev1;

	@Before
	public void setUp() throws Exception {
		clock = new Clock(START);
		pc = new ProjectContainer();
		Project p = pc.createProject("project x", "description", START, DUE);
		rc = new ResourceContainer();
		type0 = new ResourceType("very simple");
		type1 = new ResourceType("rather simple", Arrays.asList(type0), Arrays.asList());
		res0 = rc.createResource("resource a", type0);
		res00 = rc.createResource("resource b", type0);
		res000 = rc.createResource("resource c", type0);
		res1 = rc.createResource("resource d", type1);
		dev0 = new Developer("janssen", clock, office);
		dev1 = new Developer("janssens", clock, office);
		
		Map<ResourceType, Integer> requiredResources0 = new HashMap<>();
		requiredResources0.put(type0, 2);
		requiredResources0.put(type1, 1);
		Map<ResourceType, Integer> requiredResources1 = new HashMap<>();
		requiredResources1.put(type0, 2);
		t1 = p.createTask("this is task 1", new Duration(60), 10, 
				Project.NO_ALTERNATIVE, Project.NO_DEPENDENCIES, Task.getDefaultRequiredResources());
		t2 = p.createTask("this is task 2 with task 1 as prerequisite", new Duration(120), 100, 
				Project.NO_ALTERNATIVE, Arrays.asList(t1.getId()), requiredResources0);
		t1.plan(START, Arrays.asList(dev0), clock);
		t1.execute(clock);
		clock.advanceTime(START.plusMinutes(30));
		t1.fail(new Timespan(START, clock.getTime()), clock.getTime());
		t3 = p.createTask("this is task 3 as alternative for task 1", new Duration(180), 0, 
				t1.getId(), Project.NO_DEPENDENCIES, requiredResources1);
		
		user = new GenericUser("name", "manager", office);
		Acl acl = Acl.DEFAULT;
		Database db = new Database();
		db.addUser(user);
		Auth auth = new Auth(db);
		auth.login("name");
		office = new BranchOffice("Het peperkoeken huis van de paashaas", pc, rc);
		office.addUser(dev0);
		office.addUser(dev1);
		handler = new PlanTaskHandler(office, clock, auth, acl);
	}

	@Test
	public void testMainSuccesScenario() {
		//Step 2 - show a list of all currently unplanned tasks of the branch office into which he is logged in
		List<DetailedTask> unplannedTasks = handler.getUnplannedTasks();
		assertEquals(2, unplannedTasks.size());
		assertTrue(unplannedTasks.contains(t2));
		assertTrue(unplannedTasks.contains(t3));
		
		//Step 3 - the user selects the task he wants to plan
		DetailedTask selectedTask = t3;
		int pId = selectedTask.getProject().getId();
		int tId = selectedTask.getId();
		
		//Step 4 - show the first three possible starting times (only consid- ering exact hours) that a task can be planned
		Set<LocalDateTime> times = handler.getPossibleStartTimesCurrentTask(pId, tId);
		assertEquals(3, times.size());
		LocalDateTime first = clock.getTime().plusMinutes(60 - clock.getTime().getMinute());
		assertTrue(times.contains(first));
		assertTrue(times.contains(first.plusHours(1)));
		assertTrue(times.contains(first.plusHours(2)));
		
		//Step 5 - the user selects a proposed time.
		LocalDateTime selectedTime = first.plusHours(2);
		
		//Step 6 - get required resource types and their necessary quantity as assigned by the project manager when creating the task
		//         for each required resource type instance to perform the task, propose a specific resource to make a reservation for
		List<DetailedResource> requiredResources = handler.getRequiredResources(pId, tId, selectedTime);
		assertEquals(2, requiredResources.size());
		Set<Resource> possibleResources = rc.getResourcesOfType(type0);
		assertEquals(type0, requiredResources.get(0).getType());
		assertTrue(possibleResources.contains(requiredResources.get(0)));
		assertEquals(type0, requiredResources.get(1).getType());
		assertTrue(possibleResources.contains(requiredResources.get(1)));
		assertFalse(requiredResources.get(0).equals(requiredResources.get(1)));
		
		//Step 7 - select the required resources
		List<Integer> resources = new ArrayList<>();
		for(DetailedResource e : requiredResources) 
			resources.add(e.getId());
		
		//Step 8 - get list of developers
		List<DetailedResource> developers = handler.getDevelopers();
		assertEquals(2, developers.size());
		assertTrue(developers.contains(dev0));
		assertTrue(developers.contains(dev1));
		
		//Step 9 - user selects the developers to perform the task.
		List<Integer> selectedDev = Arrays.asList(dev1.getId());
		
		//Step 10 - make required reservations and assign the selected developers
		resources.addAll(selectedDev);
		handler.planTask(pId, tId, selectedTime, resources);
		DetailedPlanning planning = selectedTask.getPlanning();
		assertTrue(planning != null);
		assertEquals(new Timespan(selectedTime, selectedTask.getEstimatedDuration()), planning.getTimespan());
		assertEquals(Arrays.asList(requiredResources.get(0), requiredResources.get(1), dev1), planning.getResources());
		
		assertFalse(handler.getUnplannedTasks().contains(t3));
	}
	
	@Test
	public void testCancelScenario() {
		handler.getUnplannedTasks();
		int pId = t2.getProject().getId();
		int tId = t2.getId();
		handler.getPossibleStartTimesCurrentTask(pId, tId);
		handler.getRequiredResources(pId, tId, clock.getTime());
		handler.getDevelopers();
		//last possible moment to cancel...
		assertTrue(handler.getUnplannedTasks().contains(t2));
		assertEquals(3, rc.getAvailableResources(type0, t2.getSpan(clock.getTime())).size());
		assertEquals(1, rc.getAvailableResources(type1, t2.getSpan(clock.getTime())).size());
		assertEquals(2, rc.getAvailableResources(ResourceType.DEVELOPER, t2.getSpan(clock.getTime())).size());
	}
	
	@Test
	public void testRandomTimeScenario() {
		t3.plan(clock.getTime().plusDays(1), Arrays.asList(res0, res00, dev1), clock);
		List<DetailedTask> unplannedTasks = handler.getUnplannedTasks();
		assertEquals(1, unplannedTasks.size());
		assertTrue(unplannedTasks.contains(t2));
		Task selectedTask = t2;
		int pId = selectedTask.getProject().getId();
		int tId = selectedTask.getId();
		
		//Step 5a - the user indicates he wants to select another time
		LocalDateTime selectedTime = clock.getTime().plusMinutes(73);
		
		List<DetailedResource> requiredResources = handler.getRequiredResources(pId, tId, selectedTime);;
		assertEquals(3, requiredResources.size());
		assertTrue(requiredResources.contains(res0) && requiredResources.contains(res00) || 
				requiredResources.contains(res0) && requiredResources.contains(res000) ||
				requiredResources.contains(res00) && requiredResources.contains(res000));
		assertTrue(requiredResources.contains(res1));
		
		List<Integer> resources = new ArrayList<>();
		for(DetailedResource e : requiredResources) 
			resources.add(e.getId());
		
		resources.add(dev0.getId());
		handler.planTask(pId, tId, selectedTime, resources);
		DetailedPlanning planning = selectedTask.getPlanning();
		assertTrue(planning != null);
		assertEquals(new Timespan(selectedTime, selectedTask.getEstimatedDuration()), planning.getTimespan());
		assertEquals(Arrays.asList(requiredResources.get(0), requiredResources.get(1), requiredResources.get(2), dev0), planning.getResources());
		
		assertFalse(handler.getUnplannedTasks().contains(selectedTask));
	}
	
	@Test(expected=ConflictException.class)
	public void testReservationConflictScenario() {
		LocalDateTime selectedTime = clock.getTime().plusDays(1);
		t3.plan(selectedTime, Arrays.asList(res0, res00, dev1), clock);
		List<DetailedTask> unplannedTasks = handler.getUnplannedTasks();
		assertEquals(1, unplannedTasks.size());
		assertTrue(unplannedTasks.contains(t2));
		Task selectedTask = t2;
		int pId = selectedTask.getProject().getId();
		int tId = selectedTask.getId();
		
		//Step 6a - the task’s reservations conflict with another task
		handler.getRequiredResources(pId, tId, selectedTime);
	}
	
	@Test
	public void testSpecificResourceScenario() {
		List<DetailedTask> unplannedTasks = handler.getUnplannedTasks();
		assertEquals(2, unplannedTasks.size());
		assertTrue(unplannedTasks.contains(t2));
		assertTrue(unplannedTasks.contains(t3));
		
		Task selectedTask = t2;
		int pId = selectedTask.getProject().getId();
		int tId = selectedTask.getId();
		LocalDateTime selectedTime = clock.getTime().withMinute(0).plusHours(1);
		
		List<DetailedResource> requiredResources = handler.getRequiredResources(pId, tId, selectedTime);
		assertEquals(3, requiredResources.size());
		assertTrue(requiredResources.contains(res0) && requiredResources.contains(res00) || 
				requiredResources.contains(res0) && requiredResources.contains(res000) ||
				requiredResources.contains(res00) && requiredResources.contains(res000));
		assertTrue(requiredResources.contains(res1));
		
		//Step 8a - The user wants to allocate a specific resource instances for this task
		DetailedResourceType selectedType = type0;
		handler.getResources(selectedType);
		List<Integer> selection = Arrays.asList(res000.getId());
		requiredResources = handler.getRequiredResources(pId, tId, selectedTime, selection);
		assertEquals(3, requiredResources.size());
		assertTrue(requiredResources.contains(res000));
		
		List<Integer> resources = new ArrayList<>();
		for(DetailedResource r : requiredResources)
			resources.add(r.getId());
		resources.add(dev1.getId());
		handler.planTask(pId, tId, selectedTime, resources);
		Planning planning = selectedTask.getPlanning();
		assertTrue(planning != null);
		assertEquals(new Timespan(selectedTime, selectedTask.getEstimatedDuration()), planning.getTimespan());
		assertEquals(Arrays.asList(requiredResources.get(0), requiredResources.get(1), requiredResources.get(2), dev1), planning.getResources());
	}
	
	@Test(expected=ConflictException.class)
	public void testDeveloperConflictScenario() {
		
		List<DetailedTask> unplannedTasks = handler.getUnplannedTasks();
		assertEquals(2, unplannedTasks.size());
		assertTrue(unplannedTasks.contains(t2));
		assertTrue(unplannedTasks.contains(t3));
		
		DetailedTask selectedTask = t2;
		int pId = selectedTask.getProject().getId();
		int tId = selectedTask.getId();
		LocalDateTime selectedTime = clock.getTime().plusHours(1);
		
		List<DetailedResource> requiredResources = handler.getRequiredResources(pId, tId, selectedTime);
		assertEquals(3, requiredResources.size());
		
		List<Integer> resources = new ArrayList<>();
		for(DetailedResource e : requiredResources) 
			resources.add(e.getId());
		
		dev1.makeReservation(t3, new Timespan(selectedTime, DUE));
		List<Integer> selectedDev = Arrays.asList(dev1.getId());
		
		//Step 10a the task’s assigned developers conflict with another task
		resources.addAll(selectedDev);
		handler.planTask(pId, tId, selectedTime, resources);
	}

}
