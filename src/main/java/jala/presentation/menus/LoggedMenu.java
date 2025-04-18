package jala.presentation.menus;

import jala.application.MQTTClientHandlerImpl;
import jala.domain.*;

import java.util.Scanner;

public class LoggedMenu implements Menu {
    private final User userInSession;
    private final Scanner scanner;
    private MQTTClientHandler mqttClientHandler;
    private RoomService roomService;
    private UserService userService;

    public LoggedMenu(User userInSession, UserService userService, RoomService roomService) {
        this.userInSession = userInSession;
        this.userService = userService;
        this.roomService = roomService;
        this.scanner = new Scanner(System.in);
        this.mqttClientHandler = new MQTTClientHandlerImpl(userInSession, roomService);
        System.out.println("Client " + userInSession.getUsername() + " connected with broker!");
    }

    @Override
    public Menu run() {
        System.out.println("\n--- Welcome " +  userInSession.getUsername()  + "!---");
        System.out.println("1) Create a room");
        System.out.println("2) Join a room");
        System.out.println("3) List my rooms");
        System.out.println("4) Delete a room");
        System.out.println("5) Logout");
        System.out.println("Enter your choice: ");
        String choice = scanner.nextLine();
        switch (choice){
            case "1":
                System.out.println("Enter room name to create: ");
                String roomName = scanner.nextLine();
                mqttClientHandler.createRoom(roomName);
                return this;
            case "2":
                mqttClientHandler.joinRoom();
                return this;

            case "3":
                mqttClientHandler.listUserRooms();
                return this;
            case "4":
                mqttClientHandler.listUserRooms();
                System.out.println("Enter room name to delete: ");
                roomName = scanner.nextLine();
                mqttClientHandler.deleteRoom(roomName);
                return this;
            case "5":
                System.out.println("Logging out " +  userInSession.getUsername()  + "...");
                mqttClientHandler.logout();
                return new MainMenu(userService, roomService);
            default:
                System.out.println("Invalid option");
                return this;
            }
        }



}
