package jala.domain;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Room {
    private String id;
    private String name;
    private String topicName;
    private String adminUsername;
    public List<String> usersOnRoom;

    public Room(String id,String name, String topicName, String adminUsername, List<String> usersOnRoom) {
        this.id = id;
        this.name = name;
        this.topicName = topicName;
        this.adminUsername = adminUsername;
        this.usersOnRoom = usersOnRoom;
    }

    public String getName() {
        return name;
    }

    public String getAdministrator() {
        return adminUsername;
    }

    public String getId() {
        return id;
    }

    public String getTopicName() {
        return topicName;
    }


    public String toLine(){
        return id + "," + name + "," + topicName + "," + adminUsername+ "," + String.join("",usersOnRoom);
    }

    public static Room fromLine(String line){
        String[] parts = line.split(",", 5);
        if(parts.length != 5){
            throw new IllegalArgumentException("Invalid line: " + line);
        }
        List<String> users = new ArrayList<>(Arrays.asList(parts[4].split(";")));
        return new Room(parts[0], parts[1], parts[2],parts[3] ,users);
    }
}
