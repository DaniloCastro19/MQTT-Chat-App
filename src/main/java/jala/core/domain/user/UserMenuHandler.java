package jala.core.domain.user;

import com.hivemq.client.mqtt.mqtt5.Mqtt5Client;
import jala.core.domain.mqttclient.MQTTClientHandler;
import jala.core.domain.mqttclient.MQTTClientHandlerImpl;

import jala.core.domain.room.RoomHandler;
import jala.core.domain.room.RoomHandlerImpl;
import jala.core.domain.user.model.UserService;

import java.util.Scanner;


public class UserMenuHandler {
    private final Scanner scanner;
    UserService userService;
    public UserMenuHandler(UserService userService){
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

                    while(userlogged){
                        userlogged = userLoggedMenu(credentials[0]);
                    }
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

    public boolean userLoggedMenu(String userInSession) {
        RoomHandler roomHandler = new RoomHandlerImpl();
        MQTTClientHandler mqttClient = new MQTTClientHandlerImpl(userInSession, roomHandler);
        System.out.println("Client" + userInSession + " connected with broker!");
        System.out.println("\n--- Welcome " + userInSession + "!---");
        System.out.println("1) Join a room");
        System.out.println("2) Create a room");
        System.out.println("3) Logout");
        System.out.println("Enter your choice: ");

        switch (scanner.nextLine()){
            case "1":
                joinRoomMenu(mqttClient);
                break;

            case "2":
                createRoomMenu(mqttClient);
                break;

            case "3":
                return false;
            default:
                System.out.println("Invalid option");
        }
        return true;
    }

    private void createRoomMenu(MQTTClientHandler mqttClientHandler) {
        System.out.println("Enter room name to create: ");
        String roomName = scanner.nextLine();
        mqttClientHandler.createRoom(roomName);
    }

    private void joinRoomMenu(MQTTClientHandler mqttClientHandler) {
        //TODO: Client handling with mqttClient
        System.out.println("Enter room name to join: ");
        String roomName = scanner.nextLine();
        mqttClientHandler.joinRoom(roomName);
    }


}
