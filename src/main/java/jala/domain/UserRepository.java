package jala.domain;

import java.util.Optional;

public interface UserRepository {
    void addUser(User user);
    User findByID(String id);
    Optional<User> findByUsername(String username);
}
