package jala.domain;

import java.util.UUID;

public class User {
    private String id;
    private String username;
    private String hashedPassword;

    public User(String id, String username, String hashedPassword) {
        this.id = id;
        this.username = username;
        this.hashedPassword = hashedPassword;
    }

    public String getUsername() {
        return username;
    }

    public String getHashedPassword() {
        return hashedPassword;
    }

    public String getId() {
        return id;
    }
}
