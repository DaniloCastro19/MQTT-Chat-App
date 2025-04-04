package jala.core.domain.user;

import jala.core.domain.user.model.User;
import jala.core.domain.user.model.UserRepository;

import java.util.Hashtable;

public class UserRepositoryImpl implements UserRepository {
    private Hashtable<String, User> users;

    public UserRepositoryImpl() {
        users = new Hashtable<>();
    }

    public boolean addUser(User user){
        if(users.containsKey(user.getUsername())){
            return false;
        }
        users.put(user.getUsername(), user);
        return true;
    }

    public User findByUsername(String username){
        return  users.get(username);
    }

}
