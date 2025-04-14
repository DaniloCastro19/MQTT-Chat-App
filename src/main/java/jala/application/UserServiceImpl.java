package jala.application;

import jala.domain.User;
import jala.domain.UserRepository;
import jala.domain.UserService;

import java.security.MessageDigest;
import java.util.Optional;
import java.util.UUID;

public class UserServiceImpl implements UserService {
    private UserRepository repository;

    public UserServiceImpl(UserRepository repository) {
        this.repository = repository;
    }

    public boolean registerUser(String username, String password){
        if(repository.findByUsername(username).isPresent()){
            System.out.println("User "+ username+ " already exists.");
            return false;
        }
        UUID userId = UUID.randomUUID();
        String hashedPassword = hashPassword(password);
        User user = new User(userId.toString(), username, hashedPassword);
        repository.addUser(user);
        return true;

    }

    private String hashPassword(String password) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hashBytes = digest.digest(password.getBytes("UTF-8"));
            StringBuilder hexString = new StringBuilder();
            for (byte byt : hashBytes){
                String hex = Integer.toHexString(0xff & byt);
                if(hex.length() == 1){
                    hexString.append("0");
                }
                hexString.append(hex);
            }
            return hexString.toString();
        }  catch (Exception e) {
            throw new RuntimeException("Password hashing error: " + e);
        }
    }

    @Override
    public User userLogin(String username, String password) {
        Optional<User> user = repository.findByUsername(username);
        if(user.isEmpty()){
            System.out.println("User " + username + " doesn't exists");
            return null;
        }
        User existingUser = user.get();
        String hashedPassword = hashPassword(password);
        if(existingUser.getHashedPassword().equals(hashedPassword)){
            System.out.println("User " + username+ " logged successfully");
            return existingUser;
        }else{
            System.out.println("Incorrect username or password.");
            return null;
        }
    }

}
