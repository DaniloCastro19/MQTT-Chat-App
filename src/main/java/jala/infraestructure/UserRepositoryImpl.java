package jala.infraestructure;

import jala.domain.User;
import jala.domain.UserRepository;

import java.util.Hashtable;
import java.util.Optional;

public class UserRepositoryImpl implements UserRepository {
    private Hashtable<String, User> users;

    public UserRepositoryImpl() {
        users = new Hashtable<>();
    }

    public void addUser(User user){
        users.put(user.getId(), user);
    }

    public User findByID(String id){
        return  users.get(id);
    }

    @Override
    public Optional<User> findByUsername(String username) {
        return users.values().stream()
                .filter(user -> user.getUsername().equals(username))
                .findFirst();
    }

}
