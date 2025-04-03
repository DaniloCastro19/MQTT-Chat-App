package jala.core.domain;

import com.hivemq.client.mqtt.mqtt5.Mqtt5AsyncClient;
import jala.core.utils.Constants;
import jala.core.utils.Hasher;

import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class UserMenuHandler {
    private final Mqtt5AsyncClient mqttClient;
    private final Scanner scanner;
    private String currentUser;
    private final Map<String, Set<String>> rooms = new ConcurrentHashMap<>();

    public UserMenuHandler(Mqtt5AsyncClient mqttClient){
        this.mqttClient = mqttClient;
        this.scanner = new Scanner(System.in);
        setupResponseHandlers();
    }

    private void setupResponseHandlers(){
        //Register response handler
        mqttClient.subscribeWith()
                .topicFilter("response/register" + mqttClient.getConfig().getClientIdentifier())
                .callback(publish -> handleRegistrationResponse(new String(publish.getPayloadAsBytes())))
                .send();

        //handle Login response
        mqttClient.subscribeWith()
                .topicFilter("response/login/" + mqttClient.getConfig().getClientIdentifier())
                .callback(publish -> handleLoginResponse(new String(publish.getPayloadAsBytes())))
                .send();
    }

    private void handleRegistrationResponse(String response){
        if(response.equals("SUCCESS")){
            System.out.println("Registration successful");
        }else {
            System.out.println("Registration failed: " + response);
        }
    }

    private void handleLoginResponse(String response){
        if(response.startsWith("SUCCESS:")){
            currentUser = response.split(":")[1];
            System.out.println("Login successful");
            userLoggedMenu();
        }else {
            System.out.println("Registration failed: " + response);
        }
    }

    private void handleLogin(){
        //From here
        String[] credentials = userLoginMenu();

        String message = String.join("join", credentials[0], Hasher.sha256Hash(credentials[1]));

        mqttClient.publishWith()
                .topic(Constants.LOGIN_TOPIC)
                .payload(message.getBytes())
                .send();

        System.out.println("Authenticating...");


    }

    public void showMainMenu() {
        System.out.println("\n Welcome to ChatApp!");
        System.out.println("1) Register");
        System.out.println("2) Login");
        System.out.println("3) Exit");
        System.out.println("Select an option (1-3): ");

        switch (scanner.nextLine()){
            case "1":
                handleRegistration();
                break;
            case "2":
                handleLogin();
                break;
            case "3":
                logout();
                break;
        }

    }

    private void handleRegistration (){
        //TODO: Try to do a simple Client registration with HiveMQ
        String [] credentials = registerMenu();

        String message = String.join(":", credentials[0], Hasher.sha256Hash(credentials[1]));

        mqttClient.publishWith()
                .topic(Constants.REGISTER_TOPIC)
                .payload(message.getBytes())
                .send();
        System.out.println("Processing registration...");
    }

    // return an array with two String values: [username, password]
    public String[] registerMenu() {
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

    //return true if the user credentials are valid, otherwise return false
    public String[] userLoginMenu() {
        String[] userCredentials = new String[2];

        String username = "";
        String password = "";

        System.out.println("Enter a username: ");
        username = scanner.nextLine();
        System.out.println("Enter a password: ");
        password = scanner.nextLine();

        //TODO: User credential validation

        userCredentials[0] = username;
        userCredentials[1] = password;

        return userCredentials;
    }


    // return the room created name
    private void createRoomMenu() {
        System.out.println("Enter room name: ");
        String roomName = scanner.nextLine();

        mqttClient.publishWith()
                .topic(Constants.ROOM_CREATION_TOPIC)
                .payload(roomName.getBytes())
                .send();
        System.out.println("Room creation request sent");
    }



    public void userLoggedMenu() {
        System.out.println("\n--- Welcome, " + currentUser + "---");
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

    private void logout() {
        currentUser = null;
        mqttClient.unsubscribeWith()
                .topicFilter("chat/rooms/*")
                .send();
        System.out.println("Logged out successfully");
    }

    private void joinRoomMenu() {
        System.out.println("Enter room name to join: ");
        String roomName = scanner.nextLine();

        mqttClient.subscribeWith()
                .topicFilter("chat/rooms"+ roomName)
                .callback(publish ->
                        System.out.println("\n [" + roomName + "]" + new String(publish.getPayloadAsBytes())))
                .send();
        System.out.println("Joined room: " + roomName);
    }
}
