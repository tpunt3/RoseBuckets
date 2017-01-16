package edu.rosehulman.punttj.rosebuckets.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by alangavr on 1/15/2017.
 */


/**
 * User class, really just holds the bucketList
 * also checks username and password
 */

public class User {

    private List<BucketList> bucketLists;
    private String username;
    private String password;

    public User() {
        this.bucketLists = new ArrayList<>();
    }

    public void addList(BucketList b) {
        this.bucketLists.add(b);
    }

    public boolean checkPassword (String attempt) {
        return password.equals(attempt);
    }

    public boolean checkUsername(String attempt) {
        return username.equals(attempt);
    }

    public boolean checkLogin(String username, String password) {
        return checkUsername(username) && checkPassword(password);
    }






}
