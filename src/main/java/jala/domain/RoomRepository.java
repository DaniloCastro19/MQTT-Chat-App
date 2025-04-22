package jala.domain;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface RoomRepository {
    Room createRoom(Room room) throws IOException;
    Optional<Room> findById(String id) throws IOException;
    Optional<Room> findByName(String name) throws IOException;
    List<Room> findAll() throws IOException;
    boolean deleteRoomByRoomName(String id) throws IOException;
}
