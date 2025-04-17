package jala.presentation;

import jala.application.RoomServiceImpl;
import jala.domain.*;
import jala.infraestructure.RoomRepositoryImpl;
import jala.infraestructure.UserRepositoryImpl;
import jala.application.UserServiceImpl;
import jala.presentation.menus.MainMenu;
import jala.presentation.menus.MenuManager;

import java.io.IOException;

public class ChatApp {

    public static void main(String[] args) throws IOException {
        UserRepository userRepository = new UserRepositoryImpl("users-persistence.txt");
        RoomRepository roomRepository = new RoomRepositoryImpl();
        UserService userService = new UserServiceImpl(userRepository);
        RoomService roomService = new RoomServiceImpl(roomRepository);

        Menu mainMenu = new MainMenu(userService, roomService);
        MenuManager manager = new MenuManager(mainMenu);
        manager.run();

    }
}
