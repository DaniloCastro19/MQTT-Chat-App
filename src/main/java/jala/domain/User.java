package jala.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class User {
    private String id;
    private String username;
    private String hashedPassword;
    public List<Room> userRooms;

    public User(String id, String username, String hashedPassword) {
        this.id = id;
        this.username = username;
        this.hashedPassword = hashedPassword;
        this.userRooms = new ArrayList<>();
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

    public String toLine(){
        return id + "," + username + "," + hashedPassword;
    }

    public List<Room> getUserRooms() {
        return userRooms;
    }

    public static User fromLine(String line){
        String[] parts = line.split(",", 3);
        if(parts.length != 3){
            throw new IllegalArgumentException("Invalid line: " + line);
        }
        return new User(parts[0], parts[1], parts[2]);
    }
}
