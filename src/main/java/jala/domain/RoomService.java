package jala.domain;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public interface RoomService {
    Room createRoom(String name, String adminUsername, String topicName);
    Optional<Room> getRoomById(String id);
    Optional<Room> getRoomByName(String name);
    List<Room> ListRoomsAvailable();
    boolean deleteRoom(String id);

    Room addUserToRoom(String roomName, String username) throws IOException;
    Room removeUserFromRoom(String roomName, String username) throws IOException;
    List<Room> getRoomsForUser(String username) throws IOException;
}
