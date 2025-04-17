package jala.domain;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface RoomRepository {
    Room createRoom(Room room);
    Optional<Room> findById(String id);
    Optional<Room> findByName(String name);
    List<Room> findAll();
    boolean deleteRoomById(String id);
}
