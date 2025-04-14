package jala.application;

import jala.domain.User;
import jala.domain.UserRepository;
import jala.domain.UserService;

import java.security.MessageDigest;

public class UserServiceImpl implements UserService {
    private UserRepository repository;

    public UserServiceImpl(UserRepository repository) {
        this.repository = repository;
    }

    public boolean registerUser(String username, String password){
        if(repository.findByUsername(username) != null){
            System.out.println("User "+ username+ " already exists.");
            return false;
        }
        String hasshedPassword = hashPassword(password);
        User user = new User(username, hasshedPassword);

        if(repository.addUser(user)){
            System.out.println("User "+ username+ " successfully registered.");
            return true;
        }
        return false;
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
    public boolean userLogin(String username, String password) {
        User user = repository.findByUsername(username);
        if(user == null){
            System.out.println("User " + username + " doesn't exists");
            return false;
        }

        String hashedPassword = hashPassword(password);
        if(user.getHashedPassword().equals(hashedPassword)){
            System.out.println("User " + username+ " logged successfully");
            return true;
        }else{
            System.out.println("Incorrect username or password.");
            return false;
        }
    }

}
