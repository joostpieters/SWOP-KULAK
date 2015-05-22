package domain;

import domain.user.Auth;
import domain.user.GenericUser;
import domain.user.Role;
import domain.user.User;

import org.junit.After;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

/**
 * This is a test class for the Acl domain class
 * 
 * @author Mathias, Frederic, Pieter-Jan
 */
public class AuthTest {
    private static Auth auth;
    private static User user1;
    private BranchOffice branchOffice;
    private Company company;
    
    @Before
    public void setUp() {
        branchOffice = new BranchOffice("test");
        company = new Company();
        company.addOffice(branchOffice);
        auth = new Auth(company);
        
        user1 = new GenericUser("John", Role.MANAGER, branchOffice);
        
       branchOffice.addUser(user1);
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of login method, of class Auth.
     */
    @Test
    public void testLogin() {
        
        String username = "John";
        
        boolean result = auth.login(username);
        assertEquals(true, result);
        
        // unexisting user
        boolean result2 = auth.login("Kol");
        assertEquals(false, result2);
    }

    /**
     * Test of getUser method, of class Auth.
     */
    @Test
    public void testGetUser() {
        // no user logged in
        assertEquals(null, auth.getUser());
        
        auth.login("John");
        User result = auth.getUser();
        assertEquals(user1, result);
        
        
    }

    /**
     * Test of loggedIn method, of class Auth.
     */
    @Test
    public void testLoggedIn() {
        
        boolean expResult = false;
        boolean result = auth.loggedIn();
        assertEquals(expResult, result);
        
        auth.login("John");
        
        assertEquals(true, auth.loggedIn());
    }

    
    
}
