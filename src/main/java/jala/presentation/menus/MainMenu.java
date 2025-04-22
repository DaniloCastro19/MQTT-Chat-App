package jala.presentation.menus;

import jala.application.RoomSecurityManager;
import jala.domain.Menu;
import jala.domain.RoomService;
import jala.domain.User;
import jala.domain.UserService;

import java.util.Scanner;

public class MainMenu implements Menu {
    private final Scanner scanner;
    private final UserService userService;
    private final RoomService roomService;

    public MainMenu(UserService userService , RoomService roomService) {
        this.userService = userService;
        this.roomService = roomService;
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

        if(RoomSecurityManager.isStrongPassword(password)){
            userService.registerUser(username, password);
        }else {
            System.out.println("Contraseña no válida. La contraseña debe ser una combinación de al menos: " + "\n"
            + "9 caractéres" + "\n"
            + "Debe usar un caracter especial (@-_!¡)" + "\n"
            + "Debe usar caracter en minúsculas y en mayúscula" + "\n"
            + "Debe usar un valor numérico" + "\n"
            );
        }
    }

    private Menu loginFlow(){
        System.out.println("Enter a username: ");
        String username = scanner.nextLine();
        System.out.println("Enter a password: ");
        String password = scanner.nextLine();

        User user = userService.userLogin(username,password);
        if(user != null){
            return new LoggedMenu(user, userService, roomService);
        }else {
            return this;
        }
    }

}
