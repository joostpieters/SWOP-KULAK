package scenariotest;

import static org.junit.Assert.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.junit.BeforeClass;
import org.junit.Test;

import controller.PlanTaskHandler;
import domain.BranchOffice;
import domain.Database;
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
import domain.user.GenericUser;
import domain.user.User;

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
		t1.plan(START, new ArrayList<>(), clock, rc);
		t1.execute(clock, rc);
		//TODO: veranderen naar 90 minuten doet test failen...
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
		office = new BranchOffice("Het hol van de paashaas", pc, rc);
		handler = new PlanTaskHandler(office, clock, auth, acl, db);
	}

	@Test
	public void testMainSuccesScenario() {
		List<DetailedTask> unplannedTasks = handler.getUnplannedTasks();
		assertEquals(2, unplannedTasks.size());
		assertTrue(unplannedTasks.contains(t2));
		assertTrue(unplannedTasks.contains(t3));
		DetailedTask selectedTask = t3;
		int pId = selectedTask.getProject().getId();
		int tId = selectedTask.getId();
		
		Set<LocalDateTime> times = handler.getPossibleStartTimesCurrentTask(pId, tId);
		assertEquals(3, times.size());
		LocalDateTime first = clock.getTime().withMinute(0);
		assertTrue(times.contains(first));
		assertTrue(times.contains(first.plusHours(1)));
		assertTrue(times.contains(first.plusHours(2)));
		LocalDateTime selectedTime = first.plusHours(2);
		
		List<Entry<DetailedResourceType, DetailedResource>> requiredResources = handler.getRequiredResources(pId, tId, selectedTime);
		assertEquals(1, requiredResources.size()); //TODO: wordt hier evenveel resources als required verwacht?
		Set<Resource> expectedResources = rc.getResourcesOfType(type0);
		assertEquals(type0, requiredResources.get(0).getKey());
		assertTrue(expectedResources.contains(requiredResources.get(0).getValue()));
		
		List<Integer> resources = new ArrayList<>();
		for(Entry<DetailedResourceType, DetailedResource> e : requiredResources) 
			resources.add(e.getValue().getId());
		handler.planTask(pId, tId, selectedTime, resources);
		DetailedPlanning planning = selectedTask.getPlanning();
		assertTrue(planning != null);
		assertEquals(new Timespan(selectedTime, selectedTask.getEstimatedDuration()), planning.getTimespan());
		assertEquals(Arrays.asList(requiredResources.get(0).getValue()), planning.getResources());
	}

}
