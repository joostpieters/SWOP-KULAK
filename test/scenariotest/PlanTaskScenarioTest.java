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

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.BeforeClass;
import org.junit.Test;

public class PlanTaskScenarioTest {

	private static final LocalDateTime START = LocalDateTime.of(2014, 2, 4, 10, 0);
	private static final LocalDateTime DUE = START.plusDays(7);

	private static PlanTaskHandler handler;
	private static Clock clock;
	private static User user;
	private static BranchOffice office;
	private static ProjectContainer pc;
	private static ResourceContainer rc;
	private static Task t1, t2, t3;
	private static ResourceType type0, type1;
	private static Developer dev0, dev1;

	@BeforeClass
	public static void setUpClass() throws Exception {
		clock = new Clock(START);
		pc = new ProjectContainer();
		Project p = pc.createProject("project x", "description", START, DUE);
		rc = new ResourceContainer();
		type0 = new ResourceType("very simple");
		type1 = new ResourceType("rather simple", Arrays.asList(type0), Arrays.asList());
		rc.createResource("resource a", type0);
		rc.createResource("resource b", type0);
		rc.createResource("resource c", type0);
		rc.createResource("resource d", type1);
		
		Map<ResourceType, Integer> requiredResources0 = new HashMap<>();
		requiredResources0.put(type0, 2);
		requiredResources0.put(type1, 1);
		Map<ResourceType, Integer> requiredResources1 = new HashMap<>();
		requiredResources1.put(type0, 1);
		t1 = p.createTask("this is task 1", new Duration(60), 10, 
				Project.NO_ALTERNATIVE, Project.NO_DEPENDENCIES, Task.NO_REQUIRED_RESOURCE_TYPES);
		t2 = p.createTask("this is task 2 with task 1 as prerequisite", new Duration(120), 100, 
				Project.NO_ALTERNATIVE, Arrays.asList(t1.getId()), requiredResources0);
		t1.plan(START, new ArrayList<>(), clock);
		t1.execute(clock);
		clock.advanceTime(START.plusMinutes(30));
		t1.fail(new Timespan(START, clock.getTime()), clock.getTime());
		t3 = p.createTask("this is task 3 as alternative for task 1", new Duration(180), 0, 
				t1.getId(), Project.NO_DEPENDENCIES, requiredResources1);

		dev0 = new Developer("janssen", clock, office);
		dev1 = new Developer("janssens", clock, office);
		user = new GenericUser("name", "manager", office);
		Acl acl = Acl.DEFAULT;
		Database db = new Database();
		db.addUser(user);
		Auth auth = new Auth(db);
		auth.login("name");
		office = new BranchOffice("Het hol van de paashaas", pc, rc);
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
		List<Entry<DetailedResourceType, DetailedResource>> requiredResources = handler.getRequiredResources(pId, tId, selectedTime);
		assertEquals(1, requiredResources.size()); //TODO: wordt hier evenveel resources als required verwacht?
		Set<Resource> possibleResources = rc.getResourcesOfType(type0);
		assertEquals(type0, requiredResources.get(0).getKey());
		assertTrue(possibleResources.contains(requiredResources.get(0).getValue()));
		
		//Step 7 - select the required resources
		List<Integer> resources = new ArrayList<>();
		for(Entry<DetailedResourceType, DetailedResource> e : requiredResources) 
			resources.add(e.getValue().getId());
		//Step 8 - get list of developers
		//Step 9 - user selects the developers to perform the task.
		//Step 10 - make required reservations and assign the selected developers
		handler.planTask(pId, tId, selectedTime, resources);
		DetailedPlanning planning = selectedTask.getPlanning();
		assertTrue(planning != null);
		assertEquals(new Timespan(selectedTime, selectedTask.getEstimatedDuration()), planning.getTimespan());
		assertEquals(Arrays.asList(requiredResources.get(0).getValue()), planning.getResources());
		
		
	}
	
	@Test
	public void testCancelScenario() {
		
	}
	
	@Test
	public void testRandomTimeScenario() {
		
	}
	
	@Test
	public void testReservationConflictScenario() {
		
	}
	
	@Test
	public void testSpecificResourceScenario() {
		clock.advanceTime(clock.getTime().plusMinutes(40));
		List<DetailedTask> unplannedTasks = handler.getUnplannedTasks();
		assertEquals(1, unplannedTasks.size());
		assertTrue(unplannedTasks.contains(t2));
		Task selectedTask = t2;
		int pId = selectedTask.getProject().getId();
		int tId = selectedTask.getId();
		
		Set<LocalDateTime> times = handler.getPossibleStartTimesCurrentTask(pId, tId);
		assertEquals(3, times.size());
		LocalDateTime first = clock.getTime().withMinute(0).plusHours(1);
		assertTrue(times.contains(first));
		assertTrue(times.contains(first.plusHours(1)));
		assertTrue(times.contains(first.plusHours(2)));
		LocalDateTime selectedTime = first.plusHours(1);
		
		List<Entry<DetailedResourceType, DetailedResource>> requiredResources = handler.getRequiredResources(pId, tId, selectedTime);
		assertEquals(3, requiredResources.size());
		//TODO: andere volgorde zorgt voor falen test!
		Set<Resource> possibleResources = rc.getResourcesOfType(type0);
		assertEquals(type0, requiredResources.get(0).getKey());
		assertTrue(possibleResources.contains(requiredResources.get(0).getValue()));
		assertEquals(type0, requiredResources.get(1).getKey());
		assertTrue(possibleResources.contains(requiredResources.get(1).getValue()));
		possibleResources = rc.getResourcesOfType(type1);
		assertEquals(type0, requiredResources.get(0).getKey());
		assertTrue(possibleResources.contains(requiredResources.get(2).getValue()));
		
		ArrayList<Integer> resources = new ArrayList<>();
		for(Entry<DetailedResourceType, DetailedResource> e : requiredResources)
			if(e.getKey().equals(type0)) {
				DetailedResource temp = e.getValue();
				resources.add(temp.getId());
				break;
			}
		handler.planTask(pId, tId, selectedTime, resources);
		Planning planning = selectedTask.getPlanning();
		assertTrue(planning != null);
		assertEquals(new Timespan(selectedTime, selectedTask.getEstimatedDuration()), planning.getTimespan());
		assertEquals(Arrays.asList(requiredResources.get(0).getValue()), planning.getResources());
	}
	
	@Test
	public void testDeveloperConflictScenario() {
		
	}

}
