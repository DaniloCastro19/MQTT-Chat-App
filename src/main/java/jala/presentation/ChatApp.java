package jala.presentation;

import jala.application.UserMenuHandler;
import jala.infraestructure.UserRepositoryImpl;
import jala.application.UserServiceImpl;
import jala.domain.UserRepository;

public class ChatApp {

    public static void main(String[] args) {
        UserRepository userRepository = new UserRepositoryImpl();
        UserServiceImpl userService = new UserServiceImpl(userRepository);
        UserMenuHandler menuHandler = new UserMenuHandler(userService );

        while (true){
            menuHandler.showMainMenu();
        }
    }
}
