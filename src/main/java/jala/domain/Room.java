package jala.domain;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;

public class Room {
    private String id;
    private String name;
    private String topicName;
    private String adminUsername;
    public List<String> usersOnRoom;
    private byte[] encryptionKey;

    public Room(String id,String name, String topicName, String adminUsername, List<String> usersOnRoom, byte[] encryptionKey) {
        this.id = id;
        this.name = name;
        this.topicName = topicName;
        this.adminUsername = adminUsername;
        this.usersOnRoom = usersOnRoom;
        this.encryptionKey = encryptionKey;
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

    public byte[] getEncryptionKey() {
        return encryptionKey;
    }

    public String toLine(){
        String users = String.join(";", usersOnRoom);
        String key64 = Base64.getEncoder().encodeToString(encryptionKey);
        return String.join(",",id,name,topicName,adminUsername,users,key64);
    }

    public static Room fromLine(String line){
        String[] parts = line.split(",", 6);
        if(parts.length != 6){
            throw new IllegalArgumentException("Invalid line: " + line);
        }
        List<String> users = new ArrayList<>(Arrays.asList(parts[4].split(";")));
        byte[] key = Base64.getDecoder().decode(parts[5]);
        return new Room(parts[0], parts[1], parts[2],parts[3] ,users, key);
    }
}
