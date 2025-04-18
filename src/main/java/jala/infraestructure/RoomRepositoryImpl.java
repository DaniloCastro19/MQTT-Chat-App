package jala.infraestructure;

import jala.domain.Room;
import jala.domain.RoomRepository;
import jala.domain.User;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.*;
import java.util.stream.Collectors;

public class RoomRepositoryImpl implements RoomRepository {
    private final Path filePath;


    public RoomRepositoryImpl(String fileName) throws IOException {
        this.filePath = Paths.get(fileName);
        if(Files.notExists(filePath)){
            Files.createFile(filePath);
        }
    }

    @Override
    public Room createRoom(Room room) throws IOException {
        String line = room.toLine() + System.lineSeparator();
        Files.writeString(filePath, line, StandardCharsets.UTF_8, StandardOpenOption.APPEND);
        return room;
    }

    @Override
    public Optional<Room> findById(String id) throws IOException {
        return findAll().stream()
                .filter(room -> room.getId().equals(id))
                .findFirst();
    }

    @Override
    public Optional<Room> findByName(String name) throws IOException {
        return findAll().stream()
                .filter(room -> room.getName().equals(name))
                .findFirst();
    }

    @Override
    public List<Room> findAll() throws IOException {
        return Files.readAllLines(filePath, StandardCharsets.UTF_8).stream()
                .filter(line -> !line.isBlank())
                .map(Room::fromLine)
                .collect(Collectors.toList());
    }

    @Override
    public boolean deleteRoomByRoomName(String name) throws IOException {
        List<Room> rooms = findAll();
        boolean removed = rooms.removeIf(room -> room.getName().equals(name));

        if(removed){
            List<String> lines = rooms.stream()
                    .map(Room::toLine)
                    .toList();
            Files.write(filePath, lines, StandardCharsets.UTF_8, StandardOpenOption.TRUNCATE_EXISTING);
        }

        return removed;
    }

}
