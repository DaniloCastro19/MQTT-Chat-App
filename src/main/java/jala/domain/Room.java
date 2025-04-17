package jala.domain;

import java.util.List;

public class Room {
    private String id;
    private String name;
    private User administrator;
    public List<User> usersOnRoom;

    public Room(String id,String name, User administrator, List<User> usersOnRoom) {
        this.id = id;
        this.name = name;
        this.administrator = administrator;
        this.usersOnRoom = usersOnRoom;
    }

    public String getName() {
        return name;
    }

    public User getAdministrator() {
        return administrator;
    }

    public List<User> getUsersOnRoom() {
        return usersOnRoom;
    }
    public String getId() {
        return id;
    }
}
