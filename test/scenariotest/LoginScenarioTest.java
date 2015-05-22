package scenariotest;

import controller.HandlerFactory;
import controller.LoginHandler;
import domain.BranchOffice;
import domain.Company;
import domain.time.Clock;
import domain.user.Acl;
import domain.user.Auth;
import domain.user.Developer;
import domain.user.GenericUser;
import domain.user.User;
import java.time.LocalTime;
import java.util.List;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * This scenario test, tests the create projects use case
 *
 * @author Frederic, Mathias, Pieter-Jan
 */
public class LoginScenarioTest {

    private static Company db;
    private static BranchOffice manager;
    private static LoginHandler handler;
    private static Clock clock;
    private static Auth auth;
    private static Acl acl;
    private static BranchOffice office1;
    private static BranchOffice office2;
    private static BranchOffice office3;
    private static GenericUser managerOffice2;
    private static Developer devOffice2;

    @BeforeClass
    public static void setUpClass() {
        db = new Company();
        clock = new Clock();
        auth = new Auth(db);
        acl = new Acl();

        manager = new BranchOffice("Beijing");
        managerOffice2 = new GenericUser("John", "manager", manager);
        devOffice2 = new Developer("Layla", manager);
        manager.addUser(managerOffice2);
        manager.addUser(devOffice2);

        office1 = new BranchOffice("Brazil");
        office2 = new BranchOffice("Cuba");
        office3 = new BranchOffice("Panama");
        // add to db
        db.addOffice(office1);
        db.addOffice(office2);
        db.addOffice(office3);
        
        // add users to office
        office2.addUser(devOffice2);
        office2.addUser(managerOffice2);
        

        HandlerFactory controller = new HandlerFactory(db, auth, acl, clock);
        handler = controller.getLoginHandler();
    }

    /**
     * Tests the main success scenario of the "Create Project" use case
     */
    @Test
    public void testMainSuccessScenario() {
        // step 2
        assertTrue(handler.getOffices().contains(office1));
        assertTrue(handler.getOffices().contains(office2));
        assertTrue(handler.getOffices().contains(office3));
        assertEquals(3, handler.getOffices().size());

        // Step 3
        // Select office 2
        List<User> users = handler.getUsers(1);
        
        // Step 4
        // Show all users of branchoffice 2
        assertTrue(users.contains(devOffice2));
        assertTrue(users.contains(managerOffice2));
        assertEquals(2, handler.getUsers(1).size());
        
        // Step 5
        // Login as John
       handler.login(managerOffice2.getName());
       
        assertTrue(handler.loggedIn());
        assertEquals(managerOffice2, auth.getUser());
        
        // logout
        handler.logout();
        
        assertFalse(handler.loggedIn());
        assertEquals(null, auth.getUser());
    }

    /**
     * Tests to set a new lunchbreak for a developer
     */
    @Test
    public void testLoginDeveloperAndChangeLunchbreak() {
         // step 2
        assertTrue(handler.getOffices().contains(office1));
        assertTrue(handler.getOffices().contains(office2));
        assertTrue(handler.getOffices().contains(office3));
        assertEquals(3, handler.getOffices().size());

        // Step 3
        // Select office 2
        List<User> users = handler.getUsers(1);
        
        // Step 4
        // Show all users of branchoffice 2
        assertTrue(users.contains(devOffice2));
        assertTrue(users.contains(managerOffice2));
        assertEquals(2, handler.getUsers(1).size());
        
        // Step 5
        // Login as Layla
       handler.login(devOffice2.getName());
       
        assertTrue(handler.loggedIn());
        assertEquals(devOffice2, auth.getUser());
        
        // lunchbreak
        assertTrue(handler.askLunchbreak());
        
        handler.setLunchbreak("12:00");
        
        assertEquals(devOffice2.getAvailability().getBeginLunch(), LocalTime.of(12,00));
         assertEquals(devOffice2.getAvailability().getEndLunch(), LocalTime.of(13,00));
        // logout
        handler.logout();
        
        assertFalse(handler.loggedIn());
        assertEquals(null, auth.getUser());
    }    
}
