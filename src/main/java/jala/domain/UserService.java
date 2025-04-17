package jala.domain;

public interface UserService {

    boolean registerUser(String username, String password);

    User userLogin(String username, String password);

}
