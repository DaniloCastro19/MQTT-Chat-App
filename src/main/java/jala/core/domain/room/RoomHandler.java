package jala.core.domain.room;

import java.util.Set;

public interface RoomHandler {
    boolean createRoom(String roomName);
    boolean exists(String roomName);
    Set<String> listRooms();
    boolean deleteRoom(String roomName);
}
