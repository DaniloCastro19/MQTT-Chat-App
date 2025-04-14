package jala.application;

import jala.domain.Room;
import jala.domain.RoomRepository;
import jala.domain.RoomService;
import jala.domain.User;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class RoomServiceImpl implements RoomService {
    private final RoomRepository roomRepository;

    public RoomServiceImpl(RoomRepository roomRepository) {
        this.roomRepository = roomRepository;
    }

    @Override
    public Room createRoom(String name, User admin){
        String roomId = UUID.randomUUID().toString();
        Room room = new Room(roomId, name, admin, Collections.singletonList(admin));
        return roomRepository.createRoom(room);
    }

    @Override
    public Optional<Room> getRoomById(String id) {
        return roomRepository.findById(id);
    }

    @Override
    public Optional<Room> getRoomByName(String name) {
        return roomRepository.findByName(name);

    }

    @Override
    public List<Room> ListRoomsAvailable() {
        return roomRepository.findAll();
    }

    @Override
    public boolean deleteRoom(String id) {
        return roomRepository.deleteRoomById(id);
    }
}
