package domain;

import domain.time.WorkWeekConfiguration;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
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

    private ResourceType type0, type1, type2;
    private Resource res0, res1, res2;

    @Before
    public void setUp() throws Exception {
        type0 = new ResourceType("very simple");
        type1 = new ResourceType("still simple", Arrays.asList(type0), new ArrayList<>());
        type2 = new ResourceType("limited", new ArrayList<>(), Arrays.asList(type0), available);
        
        res0 = new Resource("tic", type1);
        res1 = new Resource("tac", type2);
        res2 = new Resource("bla", type2);
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
        assertEquals(0, type0.numberOfResources(Arrays.asList(res0,res1,res2)));
        assertEquals(1, type1.numberOfResources(Arrays.asList(res0,res1,res2)));
        assertEquals(0, type1.numberOfResources(Arrays.asList(res1,res2)));
        assertEquals(2, type2.numberOfResources(Arrays.asList(res0, res1,res2)));
        assertEquals(1, type2.numberOfResources(Arrays.asList(res0,res2)));
        assertEquals(0, type2.numberOfResources(Arrays.asList(res0)));

    }

}
