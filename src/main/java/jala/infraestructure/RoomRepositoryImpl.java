package jala.infraestructure;

import jala.domain.Room;
import jala.domain.RoomRepository;
import jala.domain.User;

import java.util.Hashtable;
import java.util.Optional;

public class RoomRepositoryImpl implements RoomRepository {
    private Hashtable<String, Room> rooms;

    public RoomRepositoryImpl() {
        rooms = new Hashtable<>();
    }

    @Override
    public void addRoom(Room room) {
        rooms.put(room.getId(), room);
    }

    @Override
    public Room findRoomByID(String id) {
        return rooms.get(id);
    }

    @Override
    public Optional<Room> findRoomByName(String roomName) {
        return rooms.values().stream()
                .filter(user -> user.getName().equals(roomName))
                .findFirst();
    }

}
