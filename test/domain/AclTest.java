package domain;

import domain.time.Clock;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * This is a test class for the Acl domain class
 * 
 * @author Mathias, Frederic, Pieter-Jan
 */
public class AclTest {
    private static Acl acl;
    private static User user1;
    private static User user2;
    private Clock clock;
    
    public AclTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
       
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
        clock = new Clock();
        acl = new Acl();
        
        acl.addEntry("manager", new ArrayList<>(Arrays.asList("edit", "remove", "view")));
        acl.addEntry("developer", new ArrayList<>(Arrays.asList("edit", "view")));
        
        user1 = new Manager("John");
        user2 = new Developer("Fred", clock);
        
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of hasPermission method, of class Acl.
     */
    @Test
    public void testHasPermission() {
              
        assertEquals(true,  acl.hasPermission(user1, "edit"));
        assertEquals(true,  acl.hasPermission(user1, "remove"));
        assertEquals(true,  acl.hasPermission(user1, "view"));
        
        // non existing permission
        assertEquals(false,  acl.hasPermission(user1, "gsdgqsdg"));
        
        // non assigned permission
        assertEquals(false,  acl.hasPermission(user2, "remove"));
        
    }

    /**
     * Test of getPermissions method, of class Acl.
     */
    @Test
    public void testGetPermissions_User() {
        
        
        List<String> result = acl.getPermissions(user2);
        assertTrue(result.contains("view"));
        assertTrue(result.contains("edit"));
        assertEquals(2, result.size());
        
    }

    /**
     * Test of getPermissions method, of class Acl.
     */
    @Test
    public void testGetPermissions_String() {
        
        String role = "manager";
        
        List<String> result = acl.getPermissions(role);
        
        assertTrue(result.contains("view"));
        assertTrue(result.contains("edit"));
        assertTrue(result.contains("remove"));
        assertEquals(3, result.size());
    }

    /**
     * Test of addPermission method, of class Acl.
     */
    @Test
    public void testAddPermission() {
        
        String role = "admin";
        String permission = "hide";
        
        acl.addPermission(role, permission);
       
        List<String> result = acl.getPermissions(role);
        assertTrue(result.contains("hide"));
        
        // add permission to unexisting role
        acl.addPermission("user", "view");
        List<String> result2 = acl.getPermissions("user");
        assertTrue(result2.contains("view"));
        assertEquals(1, result2.size());
    }

    /**
     * Test of addEntry method, of class Acl.
     */
    @Test
    public void testAddEntry() {
        
        String role = "tester";
        List<String> permissionList = new ArrayList<>(Arrays.asList("test"));
        
        acl.addEntry(role, permissionList);
        
        List<String> result = acl.getPermissions("tester");
        assertTrue(result.contains("test"));
        assertEquals(1, result.size());
    }
    
}
