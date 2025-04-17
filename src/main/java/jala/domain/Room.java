package jala.domain;

import java.util.Collections;
import java.util.List;

public class Room {
    private String id;
    private String name;
    //TODO: Implement topicName attribute
    //private String topicName;
    private String adminUsername;
    //TODO: USER ROOMS attribute
    public List<String> usersOnRoom;

    public Room(String id,String name, String adminUsername, List<String> usersOnRoom) {
        this.id = id;
        this.name = name;
        this.adminUsername = adminUsername;
        this.usersOnRoom = usersOnRoom;
    }

    public String getName() {
        return name;
    }

    public String getAdministrator() {
        return adminUsername;
    }

    public List<String> getUsersOnRoom() {
        return usersOnRoom;
    }
    public String getId() {
        return id;
    }

    public String toLine(){
        return id + "," + name + "," + adminUsername+ "," + usersOnRoom;
    }

    public static Room fromLine(String line){
        String[] parts = line.split(",", 4);
        if(parts.length != 4){
            throw new IllegalArgumentException("Invalid line: " + line);
        }
        return new Room(parts[0], parts[1], parts[2], Collections.singletonList(parts[3]));
    }
}
