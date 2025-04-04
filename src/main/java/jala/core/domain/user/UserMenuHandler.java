package jala.core.domain.user;

import jala.core.domain.user.model.MQTTClientHandler;
import jala.core.domain.user.model.UserService;

import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class UserMenuHandler {
    private final Scanner scanner;
    MQTTClientHandler mqttClientHandler;
    private final Map<String, Set<String>> rooms = new ConcurrentHashMap<>();
    UserService userService;
    public UserMenuHandler(UserService userService, MQTTClientHandler mqttClientHandler){
        this.mqttClientHandler = mqttClientHandler;
        this.userService = userService;
        this.scanner = new Scanner(System.in);
    }

    public void showMainMenu() {
        System.out.println("\n Welcome to ChatApp!");
        System.out.println("1) Register");
        System.out.println("2) Login");
        System.out.println("3) Exit");
        System.out.println("Select an option (1-3): ");

        switch (scanner.nextLine()){
            case "1":
                String [] userCredentials = getUserCredentials();
                this.userService.registerUser(userCredentials[0], userCredentials[1]);
                break;
            case "2":
                String[] credentials = getUserCredentials();
                boolean userlogged = this.userService.userLogin(credentials[0], credentials[1]);
                if (userlogged){
                    //TODO: loop logged menu Handling
                    userLoggedMenu();
                }
                break;
            case "3":
                System.out.println("Thanks for coming, Bye!");
                System.exit(0);
                break;
        }

    }

    // return an array with two String values: [username, password]
    public String[] getUserCredentials() {
        String[] userCredentials = new String[2];
        System.out.println("Enter a username: ");
        //TODO: unique username validation
        String username = scanner.nextLine();
        userCredentials[0] = username;
        System.out.println("Enter a password: ");
        String password = scanner.nextLine();
        userCredentials[1] = password;

        return userCredentials;
    }





    //TODO: loop logged menu Handling
    public void userLoggedMenu() {
        System.out.println("\n--- Welcome, rescue client name!" + "---");
        System.out.println("1) Join a room");
        System.out.println("2) Create a room");
        System.out.println("3) Logout");
        System.out.println("Enter your choice: ");

        switch (scanner.nextLine()){
            case "1":
                joinRoomMenu();
                break;

            case "2":
                createRoomMenu();
                break;

            case "3":
                logout();
                break;

            default:
                System.out.println("Invalid option");

        }
    }

    private void createRoomMenu() {
        mqttClientHandler.createRoom();
    }

    private void logout() {
        mqttClientHandler.logout();

    }

    private void joinRoomMenu() {
        System.out.println("Enter room name to join: ");
        String roomName = scanner.nextLine();
        mqttClientHandler.joinRoom();
    }


}
