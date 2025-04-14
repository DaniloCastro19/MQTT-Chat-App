package jala.domain;

public interface UserRepository {

    boolean addUser(User user);
    User findByUsername(String username);


}
