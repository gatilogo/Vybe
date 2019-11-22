package com.example.vybe.Models;

import java.io.Serializable;
import java.util.ArrayList;

public class User implements Serializable {
    private String userID;
    private String username;
    private String email;
    private ArrayList<String> followers;
    private ArrayList<String> following;
    private ArrayList<User> requests;

    /**
     * Default constructor for user with provided parameters
     *
     * @param username
     *      This is the username of the user
     * @param email
     *      This is the email of the user
     * @param followers
     *      This is a list of all the users following this user
     * @param following
     *      This is a list of all the user IDs that this user follows
     * @param requests
     *      This is a list of all user IDs requesting to follow this user
     */
    public User(String username, String email, ArrayList<String> followers, ArrayList<String> following, ArrayList<User> requests) {
        this.username = username;
        this.email = email;
        this.followers = followers;
        this.following = following;
        this.requests = requests;
    }

    /**
     * Constructor for new accounts where they have no followers/following users
     * @param username
     *      This is the username of the user
     * @param email
     *      This is the email of the user
     */
    public User(String username, String email) {
        this(username, email, new ArrayList<>(), new ArrayList<>(), new ArrayList<>());
    }

    /**
     * Empty constructor used for initialization
     */
    public User() {
    }

    /**
     * Constructor for logging in when we only have the email from the login
     * @param email
     *      The email used to login
     */
    public User(String email) {
        this ("", email, new ArrayList<>(), new ArrayList<>(), new ArrayList<>());
    }

    /**
     * This gets the Firebase user ID for the user
     * @return The Firebase user ID for the user
     */
    public String getUserID() {
        return userID;
    }

    /**
     * This sets the Firebase user ID for the user
     * @param userID
     *      The email to user
     */
    public void setUserID(String userID) {
        this.userID = userID;
    }

    /**
     * This gets the username for the user
     * @return The username
     */
    public String getUsername() {
        return username;
    }

    /**
     * This sets the username for the user
     * @param username
     *      The username to use
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * This gets the email for the user
     * @return The email for the user
     */
    public String getEmail() {
        return email;
    }

    /**
     * This sets the email for the user
     * @param email
     *      The email to user
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * This gets the list of users following this user
     * @return List of users following this user
     */
    public ArrayList<String> getFollowers() {
        return followers;
    }

    /**
     * This sets the list of users following this user
     * @param followers
     *      List of users following this user
     */
    public void setFollowers(ArrayList<String> followers) {
        this.followers = followers;
    }

    /**
     * This gets the list of users this user follows
     * @return The list of users this user follows
     */
    public ArrayList<String> getFollowing() {
        return following;
    }

    /**
     * This sets the list of users this user follows
     * @param following
     *      List of users this user will follow
     */
    public void setFollowing(ArrayList<String> following) {
        this.following = following;
    }

    /**
     * This gets the list of users that have requested to follow this user
     * @return The list of users that want to follow this user
     */
    public ArrayList<User> getRequests() {
        return requests;
    }

    /**
     * This sets the list of users that have sent a request to follow this user
     * @param requests
     *      List of users that want to follow this user
     */
    public void setRequests(ArrayList<User> requests) {
        this.requests = requests;
    }

    /**
     * Wrapper Functions - i.e. 'Hide Delegate' refactoring pattern
     * More may be added as we continue development
     */

    /**
     * This gets the number of other users that this user follows
     * @return The number of users this user follows
     */
    public int followingCount() {
        return this.following.size();
    }

    /**
     * This gets the number of users that follow this user
     * @return The number of users that follow this user
     */
    public int followerCount() {
        return this.followers.size();
    }

    /**
     * This gets the number of users that want to follow this user
     * @return The number of users that want to follow this user
     */
    public int requestCount() {
        return this.requests.size();
    }

    /**
     * This returns whether or not this user has followers
     * @return Boolean indicating whether or not this user has followers
     */
    public boolean isFollowersEmpty() {
        return this.followers.isEmpty();
    }

    /**
     * This returns whether or not this user is following any other user
     * @return Boolean indicating whether or not this user follows anyone
     */
    public boolean isFollowingEmpty() {
        return this.following.isEmpty();
    }

    /**
     * This returns whether or not this user has any follow requests
     * @return Boolean indicating whether or not anyone wants to follow this user
     */
    public boolean isRequestsEmpty() {
        return this.requests.isEmpty();
    }

}
