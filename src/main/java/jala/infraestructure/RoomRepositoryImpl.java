package jala.infraestructure;

import jala.domain.Room;
import jala.domain.RoomRepository;

import java.util.*;

public class RoomRepositoryImpl implements RoomRepository {
    private Hashtable<String, Room> rooms;

    public RoomRepositoryImpl() {
        this.rooms = new Hashtable<>();
    }

    @Override
    public Room createRoom(Room room) {
        rooms.put(room.getId(), room);
        return room;
    }

    @Override
    public Optional<Room> findById(String id) {
        return Optional.ofNullable(rooms.get(id));
    }

    @Override
    public Optional<Room> findByName(String name) {
        return rooms.values().stream()
                .filter(room -> room.getName().equals(name))
                .findFirst();
    }

    @Override
    public List<Room> findAll() {
        return new ArrayList<>(rooms.values());
    }

    @Override
    public boolean deleteRoomById(String id) {
        return rooms.remove(id) != null;
    }

}
