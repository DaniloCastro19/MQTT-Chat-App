package jala.presentation;

import jala.application.UserMenuHandler;
import jala.domain.Menu;
import jala.domain.UserService;
import jala.infraestructure.UserRepositoryImpl;
import jala.application.UserServiceImpl;
import jala.domain.UserRepository;
import jala.presentation.menus.MainMenu;
import jala.presentation.menus.MenuManager;

public class ChatApp {

    public static void main(String[] args) {
        UserRepository userRepository = new UserRepositoryImpl();
        UserService userService = new UserServiceImpl(userRepository);

        Menu mainMenu = new MainMenu(userService);
        MenuManager manager = new MenuManager(mainMenu);
        manager.run();

    }
}
