package jala.infraestructure;

import jala.domain.User;
import jala.domain.UserRepository;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Hashtable;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class UserRepositoryImpl implements UserRepository {
    private final Path filePath;

    public UserRepositoryImpl(String fileName) throws IOException {
        this.filePath = Paths.get(fileName);
        if(Files.notExists(filePath)){
            Files.createFile(filePath);
        }
    }

    public User addUser(User user) throws IOException{
        String line = user.toLine() + System.lineSeparator();
        Files.writeString(filePath, line, StandardCharsets.UTF_8, StandardOpenOption.APPEND);
        return user;
    }

    public Optional<User> findByID(String id) throws IOException{
        return findAll().stream()
                .filter(user -> user.getId().equals(id))
                .findFirst();
    }

    @Override
    public Optional<User> findByUsername(String username) throws IOException {
        return findAll().stream()
                .filter(user -> user.getUsername().equals(username))
                .findFirst();
    }

    @Override
    public List<User> findAll() throws IOException {
        return Files.readAllLines(filePath, StandardCharsets.UTF_8).stream()
                .filter(line -> !line.isBlank())
                .map(User::fromLine)
                .collect(Collectors.toList());
    }

}
