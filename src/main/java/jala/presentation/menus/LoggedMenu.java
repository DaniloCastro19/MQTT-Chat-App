package jala.presentation.menus;

import jala.application.MQTTClientHandlerImpl;
import jala.application.RoomHandlerImpl;
import jala.domain.*;

import java.util.Scanner;

public class LoggedMenu implements Menu {
    private final User userInSession;
    private final Scanner scanner;
    private MQTTClientHandler mqttClientHandler;
    private RoomHandler roomHandler;
    private UserService userService;

    public LoggedMenu(User userInSession, UserService userService) {
        this.userInSession = userInSession;
        this.scanner = new Scanner(System.in);
        this.roomHandler = new RoomHandlerImpl();
        this.mqttClientHandler = new MQTTClientHandlerImpl(userInSession.getId(),userInSession.getUsername(),roomHandler);
        this.userService = userService;
        System.out.println("Client " + userInSession.getUsername() + " connected with broker!");
    }

    @Override
    public Menu run() {
        System.out.println("\n--- Welcome " +  userInSession.getUsername()  + "!---");
        System.out.println("1) Create a room");
        System.out.println("2) Join a room");
        System.out.println("3) Logout");
        System.out.println("Enter your choice: ");
        String choice = scanner.nextLine();
        switch (choice){
            case "1":
                System.out.println("Enter room name to create: ");
                String roomName = scanner.nextLine();
                mqttClientHandler.createRoom(roomName);
                return this;
            case "2":
                System.out.println("Enter room name to join: ");
                roomName = scanner.nextLine();
                mqttClientHandler.joinRoom(roomName);
                return this;
            case "3":
                System.out.println("Logging out " +  userInSession.getUsername()  + "...");
                mqttClientHandler.logout();
                return new MainMenu(userService);
            default:
                System.out.println("Invalid option");
                return this;
            }
        }



}
