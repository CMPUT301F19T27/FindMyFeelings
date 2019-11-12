package com.example.findmyfeelings;

import java.util.HashMap;

public class FollowUser {

    private String email;
    private String username;
    private String firstName;
    private String lastName;


    public FollowUser() {

    }

    public FollowUser(String email, String username, String firstName, String lastName) {
        this.email = email;
        this.username = username;
        this.firstName = firstName;
        this.lastName = lastName;
    }


    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public HashMap<String, Object> userToMap() {
        HashMap<String, Object> newMap = new HashMap<>();

        newMap.put("username", username);
        newMap.put("first_name", firstName);
        newMap.put("last_name", lastName);

        return newMap;
    }
}
