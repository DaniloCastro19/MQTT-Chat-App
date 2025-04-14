package jala.presentation.menus;

import jala.domain.Menu;
import jala.domain.UserService;

import java.util.Scanner;

public class MainMenu implements Menu {
    private final Scanner scanner;
    private final UserService userService;

    public MainMenu(UserService userService) {
        this.userService = userService;
        this.scanner = new Scanner(System.in);
    }

    @Override
    public Menu run() {
        System.out.println("\n Welcome to ChatApp!");
        System.out.println("1) Register");
        System.out.println("2) Login");
        System.out.println("3) Exit");
        System.out.println("Select an option (1-3): ");
        String choice = scanner.nextLine();
        switch (choice){
            case "1":
                registerFlow();
                return this;
            case "2":
                return loginFlow();
            case "3":
                System.out.println("Thanks for coming, Bye!");
                return null;
            default:
                System.out.println("Invalid option");
                return this;
        }
    }


    private void registerFlow(){
        System.out.println("Enter a username: ");
        String username = scanner.nextLine();
        System.out.println("Enter a password: ");
        String password = scanner.nextLine();
        userService.registerUser(username, password);
    }

    private Menu loginFlow(){
        System.out.println("Enter a username: ");
        String username = scanner.nextLine();
        System.out.println("Enter a password: ");
        String password = scanner.nextLine();

        boolean isLogged = userService.userLogin(username,password);
        if(isLogged){
            return new LoggedMenu(username, userService);
        }else {
            return this;
        }
    }

}
