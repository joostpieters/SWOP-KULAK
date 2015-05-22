package domain;

import domain.user.Acl;
import domain.user.Developer;
import domain.user.GenericUser;
import domain.user.Role;
import domain.user.User;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.easymock.EasyMock;
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
    private BranchOffice branchOffice;
    
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
        branchOffice = EasyMock.createNiceMock(BranchOffice.class);
        acl = new Acl();
        
        acl.addEntry(Role.MANAGER, new ArrayList<>(Arrays.asList("edit", "remove", "view")));
        acl.addEntry(Role.DEVELOPER, new ArrayList<>(Arrays.asList("edit", "view")));
        
        user1 = new GenericUser("John", Role.MANAGER, branchOffice);
        user2 = new Developer("Fred", branchOffice);
        
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
        
        Role role = Role.MANAGER;
        
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
        
        Role role = Role.ADMIN;
        String permission = "hide";
        
        acl.addPermission(role, permission);
       
        List<String> result = acl.getPermissions(role);
        assertTrue(result.contains("hide"));
        
        // add permission to unexisting role
        acl.addPermission(Role.USER, "view");
        List<String> result2 = acl.getPermissions(Role.USER);
        assertTrue(result2.contains("view"));
        assertEquals(1, result2.size());
    }

    /**
     * Test of addEntry method, of class Acl.
     */
    @Test
    public void testAddEntry() {
        
        Role role = Role.ADMIN;
        List<String> permissionList = new ArrayList<>(Arrays.asList("test"));
        
        acl.addEntry(role, permissionList);
        
        List<String> result = acl.getPermissions(role);
        assertTrue(result.contains("test"));
        assertEquals(1, result.size());
    }
    
}
