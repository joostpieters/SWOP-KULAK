package domain.command;

import domain.Resource;
import domain.task.Task;
import domain.time.Timespan;
import exception.ConflictException;
import java.time.LocalDateTime;
import java.time.Month;
import static org.easymock.EasyMock.createNiceMock;
import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.Test;

/**
 * A test class for the create reservation command
 * @author Mathias
 */
public class CreateReservationCommandTest {
    private Task t0;
    private CreateReservationCommand createReservationCommand;
    private LocalDateTime start;
    private LocalDateTime end;
    private Resource res0;
    
   
    @Before
    public void setUp() {
        t0 = createNiceMock(Task.class);
        
        start = LocalDateTime.of(2015, Month.MARCH, 2, 10, 30);
        end = LocalDateTime.of(2015, Month.MARCH, 15, 10, 30);
        res0 = new Resource("car");
        createReservationCommand = new CreateReservationCommand(new Timespan(start, end), res0, t0);
    }
    

    /**
     * Test of execute method, of class CreateReservationCommand.
     */
    @Test
    public void testExecute() {
        createReservationCommand.execute();
        assertEquals(new Timespan(start,end), res0.getReservation(t0).getTimespan());
        assertEquals(1, res0.getReservations().size());
    }

    /**
     * Test of revert method, of class CreateReservationCommand.
     */
    @Test
    public void testRevert() {
       createReservationCommand.execute();
        createReservationCommand.revert();
        assertEquals(0, res0.getReservations().size());
    }
    
    /**
     * Test of execute method when a there is a conflict, of class CreateReservationCommand.
     */
    @Test (expected=ConflictException.class)
    public void testExecuteConflict() {
        Task t1 = createNiceMock(Task.class);
        createReservationCommand.execute();
        CreateReservationCommand createReservationCommand2 = new CreateReservationCommand(new Timespan(start, end), res0, t1);
        createReservationCommand2.execute();
    }
    
}
