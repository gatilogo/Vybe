package com.example.vybe;

import com.example.vybe.Models.User;

import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class UserTest {

    private User mockEmptyUser() {
        return new User();
    }

    private User mockUser()
    {
        return new User("test", "mock@test.ca", new ArrayList<>(), new ArrayList<>(), new ArrayList<>());
    }

    private ArrayList<String> getUserStringList(){
        ArrayList<String> userList = new ArrayList<>();

        userList.add("user1");
        userList.add("user2");
        userList.add("user3");

        return userList;
    }

    private ArrayList<User> getUserObjectList(){
        ArrayList<User> userList = new ArrayList<>();

        userList.add(new User("user1", "test_user_1@test.ca"));
        userList.add(new User( "user2", "test_user_2@test.ca"));
        userList.add(new User("user3", "test_user_3@test.ca"));

        return userList;
    }


    @Test
    public void UserConstructor_EmptyInit() {
        User testUser = mockEmptyUser();

        assertNull(testUser.getUsername());
        assertNull(testUser.getEmail());
        assertNull(testUser.getFollowers());
        assertNull(testUser.getFollowing());
    }

    @Test
    public void UserConstructor_EmailInit() {
        User testUser = new User("mock@test.ca");

        assertEquals("mock@test.ca", testUser.getEmail());
        assertEquals("", testUser.getUsername());
        assertTrue(testUser.isFollowersEmpty());
        assertTrue(testUser.isFollowingEmpty());
        assertTrue(testUser.isRequestsEmpty());

    }

    @Test
    public void UserConstructor_UserEmailInit() {
        User testUser = new User("test", "mock@test.ca");

        assertEquals("mock@test.ca", testUser.getEmail());
        assertEquals("test", testUser.getUsername());
        assertTrue(testUser.isFollowersEmpty());
        assertTrue(testUser.isFollowingEmpty());
        assertTrue(testUser.isRequestsEmpty());

    }


    @Test
    public void UserConstructor_FullInit_EmptyLists() {
        User testUser = mockUser();
        assertEquals("mock@test.ca", testUser.getEmail());
        assertEquals("test", testUser.getUsername());
        assertTrue(testUser.isFollowersEmpty());
        assertTrue(testUser.isFollowingEmpty());
        assertTrue(testUser.isRequestsEmpty());

    }

    @Test
    public void GetUsername() {
        // Test Method for empty constructor
        User testUser = mockEmptyUser();
        assertNull(testUser.getUsername());

        // Test Method for non-empty constructor
        testUser.setUsername("test");
        assertEquals("test", testUser.getUsername());

    }

    @Test
    public void SetUsername() {
        User testUser = mockUser();
        assertEquals("test", testUser.getUsername());

        testUser.setUsername("vibes");
        assertEquals("vibes", testUser.getUsername());

    }

    @Test
    public void GetEmail() {
        // Test Method for empty constructor
        User testUser = mockEmptyUser();
        assertNull(testUser.getEmail());

        // Test Method for non-empty constructor
        testUser.setUsername("mock@test.ca");
        assertEquals("mock@test.ca", testUser.getUsername());
    }

    @Test
    public void SetEmail() {
        User testUser = new User("test", "mock@test.ca");
        assertEquals("mock@test.ca", testUser.getEmail());

        testUser.setEmail("vybe@test.ca");
        assertEquals("vybe@test.ca", testUser.getEmail());
    }

    @Test
    public void GetFollowerList() {
        User testUser = mockUser();

        testUser.getFollowers().add("test2");
        assertFalse(testUser.isFollowersEmpty());
    }

    @Test
    public void SetFollowerList() {
        User testUser = mockUser();

        assertTrue(mockUser().isFollowersEmpty());

        testUser.setFollowers(getUserStringList());

        ArrayList<String> verificationList = getUserStringList();
        for(int i = 0; i < verificationList.size(); i++){
            assertEquals(verificationList.get(i), testUser.getFollowers().get(i));
        }

    }

    @Test
    public void GetFollowingList() {
    }

    @Test
    public void SetFollowingList() {
    }

}