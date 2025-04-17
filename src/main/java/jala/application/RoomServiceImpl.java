package jala.application;

import jala.domain.Room;
import jala.domain.RoomRepository;
import jala.domain.RoomService;
import jala.domain.User;

import java.io.IOException;
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
    public Room createRoom(String name, String adminUsername){
        try{
            String roomId = UUID.randomUUID().toString();
            Room room = new Room(roomId, name, adminUsername, Collections.singletonList(adminUsername));
            return roomRepository.createRoom(room);
        } catch (IOException e){
            System.err.println("Error creating room: "+ e.getMessage());
            return null;
        }

    }

    @Override
    public Optional<Room> getRoomById(String id) {
        try {
            return roomRepository.findById(id);
        } catch (IOException e){
            System.err.println("Error creating room: "+ e.getMessage());
            return null;
        }
    }

    @Override
    public Optional<Room> getRoomByName(String name) {
        try {
            return roomRepository.findByName(name);
        } catch (IOException e){
            System.err.println("Error creating room: "+ e.getMessage());
            return null;
        }

    }

    @Override
    public List<Room> ListRoomsAvailable() {
        try {
            return roomRepository.findAll();
        } catch (IOException e){
            System.err.println("Error creating room: "+ e.getMessage());
            return null;
        }
    }

    @Override
    public boolean deleteRoom(String id) {
        try {
            return roomRepository.deleteRoomById(id);
        } catch (IOException e){
            System.err.println("Error creating room: "+ e.getMessage());
            return false;
        }
    }
}
