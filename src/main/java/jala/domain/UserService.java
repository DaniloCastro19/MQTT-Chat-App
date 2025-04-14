package jala.domain;

public interface UserService {

    boolean registerUser(String username, String password);

    boolean userLogin(String username, String password);

}
