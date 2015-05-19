package domain.command;

import domain.Planning;
import domain.Resource;
import domain.ResourceType;
import domain.task.Task;
import domain.time.Clock;
import domain.time.Timespan;
import exception.ConflictException;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.HashMap;
import org.easymock.Capture;
import org.easymock.EasyMock;
import static org.easymock.EasyMock.capture;
import static org.easymock.EasyMock.createNiceMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author Mathias
 */
public class PlanTaskCommandTest {

    private Task task;
    private ResourceType type;
    private Resource car1, car2, car3;
    private Clock clock;
    private Capture<Planning> capturedArgument;
    private PlanTaskCommand planTaskCommand;
    private LocalDateTime start;
    private LocalDateTime end;
    private ArrayList<Resource> resList;
    private Task task2;
    private Capture<Planning> capturedArgument2;

   

    @Before
    public void setUp() {
        clock = new Clock();
        task = createNiceMock(Task.class);
        capturedArgument = new Capture<>();
        
        task.setPlanning(capture(capturedArgument));
        EasyMock.expectLastCall().times(0, 100);
        expect(task.getPlanning())
        .andAnswer(() -> capturedArgument.getValue());
        
        EasyMock.expectLastCall().times(0, 100);
        HashMap<ResourceType, Integer> requirements = new HashMap<>();

        expect(task.getRequiredResources()).andReturn(requirements).anyTimes();

        replay(task);
        
        //task.setPlanning(new Planning(null, null, task));
        task.setPlanning(null);
        
        task2 = createNiceMock(Task.class);
        capturedArgument2 = new Capture<>();
        
        task2.setPlanning(capture(capturedArgument2));
        EasyMock.expectLastCall().times(0, 100);
        expect(task2.getPlanning())
        .andAnswer(() -> capturedArgument2.getValue());
        
        EasyMock.expectLastCall().times(0, 100);
        
        expect(task2.getRequiredResources()).andReturn(new HashMap<>()).anyTimes();

        replay(task2);
        
        
        task2.setPlanning(null);
        

        type = new ResourceType("car");
        car1 = new Resource("car1", type);
        car2 = new Resource("car2", type);
        car3 = new Resource("car3", type);
        start = LocalDateTime.of(2015, Month.MARCH, 10, 10, 30);
        end = LocalDateTime.of(2015, Month.MARCH, 20, 10, 30);
        resList = new ArrayList<>();
        resList.add(car1);
        resList.add(car2);
        resList.add(car3);
        planTaskCommand = new PlanTaskCommand(new Timespan(start, end), resList, task, clock, new ArrayList<>());

    }

    
    /**
     * Test of execute method, of class PlanTaskCommand.
     */
    @Test
    public void testExecute() {
        planTaskCommand.execute();
        assertEquals(start, car1.getReservation(task).getStartTime());
        assertEquals(end, car1.getReservation(task).getEndTime());
        assertEquals(start, car2.getReservation(task).getStartTime());
        assertEquals(end, car2.getReservation(task).getEndTime());
        assertEquals(start, car3.getReservation(task).getStartTime());
        assertEquals(end, car3.getReservation(task).getEndTime());
        
        assertNotEquals(null, capturedArgument.getValue());

        // check not too much reservations 
        assertEquals(1, car1.getReservations().size());
        assertEquals(1, car2.getReservations().size());
        assertEquals(1, car3.getReservations().size());
    }

    /**
     * Test of revert method, of class PlanTaskCommand.
     */
    @Test
    public void testRevert() {
        planTaskCommand.execute();
        planTaskCommand.revert();
        
        assertEquals(null, capturedArgument.getValue());
        // check all reservations dissapeared
        assertEquals(0, car1.getReservations().size());
        assertEquals(0, car2.getReservations().size());
        assertEquals(0, car3.getReservations().size());
    }
    
       
     /**
     * Test of execute method when a conflict arises with another task, of class PlanTaskCommand.
     */
    @Test
    public void testExecuteConflictOtherTask() {
        planTaskCommand.execute();
        
        Planning oldPlanning = capturedArgument.getValue();
        
        
        LocalDateTime start2 = LocalDateTime.of(2015, Month.MARCH, 8, 10, 30);
        LocalDateTime end2 = LocalDateTime.of(2015, Month.MARCH, 13, 10, 30);
        ArrayList<Resource> resList2 = new ArrayList<>();
        Resource bicycle = new Resource("bicycle", type);
        resList2.add(bicycle);
        resList2.add(car3);
        PlanTaskCommand planTaskCommand2 = new PlanTaskCommand(new Timespan(start2, end2), resList2, task2, clock, new ArrayList<>());
        try{
             planTaskCommand2.execute();
             fail("No exception arised");
        }catch(ConflictException e){
            assertTrue(e.getConflictingTasks().contains(task));
            // check planning not changed
            assertEquals(start, car1.getReservation(task).getStartTime());
            assertEquals(end, car1.getReservation(task).getEndTime());
            assertEquals(start, car2.getReservation(task).getStartTime());
            assertEquals(end, car2.getReservation(task).getEndTime());
            assertEquals(start, car3.getReservation(task).getStartTime());
            assertEquals(end, car3.getReservation(task).getEndTime());
            assertEquals(oldPlanning, capturedArgument.getValue());
            assertEquals(null, capturedArgument2.getValue());

        // check not too much reservations 
        assertEquals(1, car1.getReservations().size());
        assertEquals(1, car2.getReservations().size());
        assertEquals(1, car3.getReservations().size());
        assertEquals(0, bicycle.getReservations().size());
        }
    }
    
    /**
     * Test of execute method when a planning of a task is moved to another time
     * , of class PlanTaskCommand.
     */
    @Test
    public void testExecuteMoveTask() {
        planTaskCommand.execute();
        
        Planning oldPlanning = capturedArgument.getValue();
        
        
        LocalDateTime start2 = LocalDateTime.of(2015, Month.MARCH, 8, 10, 30);
        LocalDateTime end2 = LocalDateTime.of(2015, Month.MARCH, 13, 10, 30);
        ArrayList<Resource> resList2 = new ArrayList<>();
        Resource bicycle = new Resource("bicycle", type);
        resList2.add(bicycle);
        resList2.add(car1);
        resList2.add(car2);
        resList2.add(car3);
        PlanTaskCommand planTaskCommand2 = new PlanTaskCommand(new Timespan(start2, end2), resList2, task, clock, new ArrayList<>());
        
             planTaskCommand2.execute();
         
            
            // check planning changed
            assertEquals(start2, car1.getReservation(task).getStartTime());
            assertEquals(end2, car1.getReservation(task).getEndTime());
            assertEquals(start2, car2.getReservation(task).getStartTime());
            assertEquals(end2, car2.getReservation(task).getEndTime());
            assertEquals(start2, car3.getReservation(task).getStartTime());
            assertEquals(end2, car3.getReservation(task).getEndTime());
            assertNotEquals(oldPlanning, capturedArgument.getValue());

        // check not too much reservations 
        assertEquals(1, car1.getReservations().size());
        assertEquals(1, car2.getReservations().size());
        assertEquals(1, car3.getReservations().size());
        assertEquals(1, bicycle.getReservations().size());
        
    }

}
