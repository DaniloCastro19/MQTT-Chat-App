package jala.core.domain.room;

import java.util.HashSet;
import java.util.Set;

public class RoomHandlerImpl implements RoomHandler{
    private Set<String> rooms;

    public RoomHandlerImpl() {
        this.rooms = new HashSet<>();
    }

    @Override
    public boolean createRoom(String roomName) {
        if(rooms.contains(roomName)){
            System.out.println("Room: " + roomName + " already exists.");
            return false;
        }

        rooms.add(roomName);
        System.out.println("Room " + roomName + " created!");
        return true;
    }

    @Override
    public boolean exists(String roomName) {
        return rooms.contains(roomName);
    }

    @Override
    public Set<String> listRooms() {
        return rooms;
    }

    @Override
    public boolean deleteRoom(String roomName) {
        if(!rooms.contains(roomName)) {
            System.out.println("Room " + roomName + " doesn't exist");
            return false;
        }
        rooms.remove(roomName);
        System.out.println("Room "+ roomName + " deleted.");

        return true;
    }
}
