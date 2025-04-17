package jala.domain;

import java.util.List;
import java.util.Optional;

public interface RoomService {
    Room createRoom(String name, String adminUsername);
    Optional<Room> getRoomById(String id);
    Optional<Room> getRoomByName(String name);
    List<Room> ListRoomsAvailable();
    boolean deleteRoom(String id);
}
