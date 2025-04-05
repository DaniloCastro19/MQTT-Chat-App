package jala;

import jala.core.domain.user.UserMenuHandler;
import jala.core.domain.user.UserRepositoryImpl;
import jala.core.domain.user.UserServiceImpl;
import jala.core.domain.user.model.UserRepository;

public class App {

    public static void main(String[] args) {
        UserRepository userRepository = new UserRepositoryImpl();
        UserServiceImpl userService = new UserServiceImpl(userRepository);
        UserMenuHandler menuHandler = new UserMenuHandler(userService );

        while (true){
            menuHandler.showMainMenu();
        }
    }
}
