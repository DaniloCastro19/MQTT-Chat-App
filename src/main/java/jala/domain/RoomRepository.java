package jala.domain;

import java.util.Optional;

public interface RoomRepository {
    void addRoom(Room room);
    Room findRoomByID(String id);
    Optional<Room> findRoomByName(String roomName);
}
