package jala.domain;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public interface UserRepository {
    User addUser(User user) throws IOException;
    Optional<User>  findByID(String id) throws IOException;
    Optional<User> findByUsername(String username) throws IOException;
    List<User> findAll() throws IOException;
}
