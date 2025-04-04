package jala.core.domain.user.model;

public interface UserRepository {

    boolean addUser(User user);
    User findByUsername(String username);


}
