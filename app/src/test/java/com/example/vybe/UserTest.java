package com.example.vybe;

import com.example.vybe.Models.User;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class UserTest {

    private User mockUser() {
        return new User();
    }

    @Test
    public void testEmptyUserConstructor() {
        User emptyUser = mockUser();
        assertNull(emptyUser.getUsername());
        assertNull(emptyUser.getEmail());
        assertNull(emptyUser.getFollowers());
        assertNull(emptyUser.getFollowing());
    }

    @Test
    public void testEmailOnlyUserConstructor() {
        User emailUser = new User("mock@test.ca");

        assertEquals("mock@test.ca", emailUser.getEmail());
        assertEquals("", emailUser.getUsername());
        assertTrue(emailUser.getFollowers().isEmpty());
        assertTrue(emailUser.getFollowing().isEmpty());
    }

    @Test
    public void testUsernameEmailUserConstructor() {
        User ueUser = new User("test", "mock@test.ca");

        assertEquals("mock@test.ca", ueUser.getEmail());
        assertEquals("test", ueUser.getUsername());
        assertTrue(ueUser.getFollowers().isEmpty());
        assertTrue(ueUser.getFollowing().isEmpty());
    }

    /**
     * Placeholder test. May not be necessary
     */
    @Test
    public void testNonEmptyUserConstructor() {

    }

    @Test
    public void testGetUsername() {
        User testUser = mockUser();
        assertNull(testUser.getUsername());

        testUser.setUsername("test");
        assertEquals("test", testUser.getUsername());

    }

    @Test
    public void testSetUserName() {
        User testUser = new User("test", "mock@test.ca");
        assertEquals("test", testUser.getUsername());

        testUser.setUsername("vibes");
        assertEquals("vibes", testUser.getUsername());

    }

    @Test
    public void testGetEmail() {
        User testUser = mockUser();
        assertNull(testUser.getEmail());

        testUser.setUsername("mock@test.ca");
        assertEquals("mock@test.ca", testUser.getUsername());
    }

    @Test
    public void testSetEmail() {
        User testUser = new User("test", "mock@test.ca");
        assertEquals("mock@test.ca", testUser.getEmail());

        testUser.setEmail("vybe@test.ca");
        assertEquals("vybe@test.ca", testUser.getEmail());
    }

    /**
     * The following tests will be written when the appropriate
     * user stories/issues relating to them are completed so we
     * have a better understanding of what exactly we will be
     * wanting to test
     */
    @Test
    public void testGetFollowers() {
    }

    @Test
    public void testSetFollowers() {
    }

    @Test
    public void testGetFollowing() {
    }

    @Test
    public void testSetFollowing() {
    }

}