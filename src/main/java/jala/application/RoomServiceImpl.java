package jala.application;

import jala.domain.Room;
import jala.domain.RoomRepository;
import jala.domain.RoomService;

import javax.crypto.SecretKey;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class RoomServiceImpl implements RoomService {
    private final RoomRepository roomRepository;

    public RoomServiceImpl(RoomRepository roomRepository) {
        this.roomRepository = roomRepository;
    }

    @Override
    public Room createRoom(String name, String adminUsername, String topicName){
        try{
            if(roomRepository.findByName(name).isPresent()){
                System.out.println(name + " room name is already in use! Please, try another.");
                return null;
            }
            String roomId = UUID.randomUUID().toString();
            List<String> usersOnRoom = new ArrayList<>();
            SecretKey secretKey = RoomSecurityManager.generateRoomKey();
            byte[] keyBytes =secretKey.getEncoded();
            Room room = new Room(roomId, name,topicName, adminUsername, usersOnRoom, keyBytes);
            return roomRepository.createRoom(room);
        } catch (IOException e){
            System.err.println("Error creating room: "+ e.getMessage());
            return null;
        }catch (Exception e){
            System.err.println("Error generating room key: "+ e.getMessage());
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
    public boolean deleteRoom(String roomName) {
        try {
            return roomRepository.deleteRoomByRoomName(roomName);
        } catch (IOException e){
            System.err.println("Error creating room: "+ e.getMessage());
            return false;
        }
    }

    @Override
    public Room addUserToRoom(String roomName, String username) throws IOException {
        Room room = roomRepository.findByName(roomName)
                .orElseThrow(() -> new IllegalArgumentException("Room " + roomName + " doesn't exist."));
        if(!room.usersOnRoom.contains(username)){
            room.usersOnRoom.add(username);
            roomRepository.updateRoom(room);
        }
        return room;
    }

    @Override
    public Room removeUserFromRoom(String roomName, String username) throws IOException {
        Room room = roomRepository.findByName(roomName)
                .orElseThrow(() -> new IllegalArgumentException("Room " + roomName + " doesn't exist."));
        if(room.usersOnRoom.remove(username)){
            roomRepository.updateRoom(room);
        }
        return room;
    }

    @Override
    public List<Room> getRoomsForUser(String username) throws IOException {
        return roomRepository.findAll().stream()
                .filter(room -> room.usersOnRoom.contains(username))
                .toList();
    }
}
