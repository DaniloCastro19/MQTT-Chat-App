package jala.application;

import jala.domain.User;
import jala.domain.UserRepository;
import jala.domain.UserService;

import java.io.IOException;
import java.security.MessageDigest;
import java.util.Optional;
import java.util.UUID;

public class UserServiceImpl implements UserService {
    private UserRepository repository;

    public UserServiceImpl(UserRepository repository) {
        this.repository = repository;
    }

    public boolean registerUser(String username, String password){
        try{
            if(repository.findByUsername(username).isPresent()){
                System.out.println("User "+ username+ " already exists.");
                return false;
            }
            UUID userId = UUID.randomUUID();
            String hashedPassword = RoomSecurityManager.hashPassword(password);
            User user = new User(userId.toString(), username, hashedPassword);
            repository.addUser(user);
            return true;
        } catch (IOException e){
            System.err.println("Error saving user: "+ e.getMessage());
            return false;
        }

    }


    @Override
    public User userLogin(String username, String password) {
        try{
            Optional<User> user = repository.findByUsername(username);
            if(user.isEmpty()){
                System.out.println("User " + username + " doesn't exists");
                return null;
            }
            User existingUser = user.get();
            String hashedPassword = RoomSecurityManager.hashPassword(password);
            if(existingUser.getHashedPassword().equals(hashedPassword)){
                System.out.println("User " + username+ " logged successfully");
                return existingUser;
            }else{
                System.out.println("Incorrect username or password.");
                return null;
            }
        }catch (IOException e){
            System.err.println("Error reading user data: "+ e.getMessage());
            return null;
        }

    }

}
