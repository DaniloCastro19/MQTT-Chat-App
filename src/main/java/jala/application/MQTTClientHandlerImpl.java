package jala.application;

import com.hivemq.client.mqtt.datatypes.MqttQos;
import com.hivemq.client.mqtt.mqtt5.Mqtt5Client;
import jala.domain.*;
import jala.helpers.Constants;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.sql.SQLOutput;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Scanner;

public class MQTTClientHandlerImpl implements MQTTClientHandler {
    private final String clientID;
    private final RoomService roomService;
    private final Scanner scanner;
    private User userInSession;

    static final String HOST = "47dd8417ba1643e088d58d823cfd5261.s1.eu.hivemq.cloud";

    final String brokerUsername = "Dan-ADMIN";
    final String brokerPassword = "Danilo123";
    private Mqtt5Client mqttClient;
    public MQTTClientHandlerImpl(User userInSession, RoomService roomService) {
        this.clientID = userInSession.getId();
        this.userInSession = userInSession;
        this.roomService = roomService;
        this.scanner = new Scanner(System.in);
        this.mqttClient = Mqtt5Client.builder()
                .identifier(clientID)
                .serverHost(HOST)
                .automaticReconnectWithDefaultConfig()
                .serverPort(Constants.BROKER_PORT)
                .sslWithDefaultConfig()
                .build();
        connectWithBroker();


    }

    private void connectWithBroker() {
        System.out.println("Connecting with broker...");
        this.mqttClient.toBlocking().connectWith()
                .simpleAuth()
                .username(brokerUsername)
                .password(brokerPassword.getBytes(StandardCharsets.UTF_8))
                .applySimpleAuth()
                .cleanStart(false)
                .willPublish()
                .topic(Constants.START_SESSION_TOPIC)
                .payload((this.userInSession.getUsername() + " off.").getBytes())
                .applyWillPublish()
                .send();
        this.mqttClient.toBlocking().publishWith()
                .topic(Constants.START_SESSION_TOPIC)
                .payload((this.userInSession.getUsername() + " Connected to server!").getBytes())
                .send();

        unsubscribeTopic(Constants.START_SESSION_TOPIC);
    }

    @Override
    public void joinRoom() throws Exception {
        System.out.println("Rooms available:");
        System.out.println("---------------------------------------------");
        showAvailableRooms();
        System.out.println("Enter room name to join: ");
        String roomToJoin = scanner.nextLine();
        Optional<Room> room = roomService.getRoomByName(roomToJoin);
        if(room.isPresent()){
            byte[] keyBytes = room.get().getEncryptionKey();
            SecretKey roomKey = new SecretKeySpec(keyBytes, 0, keyBytes.length, "AES");
            Room existingRoom = room.get();
            existingRoom.usersOnRoom.add(userInSession.getUsername());
            String welcome = userInSession.getUsername() + " Joined to chat!" + "\n";
            String encodeWelcome = RoomSecurityManager.encrypt(welcome, roomKey);
            this.mqttClient.toBlocking().publishWith()
                    .topic(existingRoom.getTopicName())
                    .payload(encodeWelcome.getBytes(StandardCharsets.UTF_8))
                    .send();

            ChatManager chatManager = new ChatManagerImpl( mqttClient,scanner,userInSession.getUsername(),roomKey);
            chatManager.startChat(existingRoom.getTopicName());
        }else {
            System.out.println("Room doesn't exist.");
        }

    }

    @Override
    public void createRoom(String roomName) throws Exception {
        String TOPIC_NAME = userInSession.getUsername() + "/room/" + roomName;
        Room userRoom = this.roomService.createRoom(roomName, userInSession.getUsername(), TOPIC_NAME);

        if (userRoom != null){
            this.mqttClient.toBlocking().publishWith()
                    .topic(Constants.ROOM_CREATED)
                    .payload((this.userInSession.getUsername() + " create " + roomName + "!").getBytes())
                    .send();
            unsubscribeTopic(Constants.ROOM_CREATED);

            byte[] keyBytes = userRoom.getEncryptionKey();
            SecretKey roomKey = new SecretKeySpec(keyBytes, 0, keyBytes.length, "AES");
            ChatManager chatManager = new ChatManagerImpl( mqttClient,scanner,userInSession.getUsername(),roomKey);
            userRoom.usersOnRoom.add(userInSession.getUsername());
            roomService.addUserToRoom(userRoom.getName(), userInSession.getUsername());
            chatManager.startChat(userRoom.getTopicName());
        }

    }

    @Override
    public void listUserRooms() {
        try {
            List<Room> rooms = roomService.getRoomsForUser(userInSession.getUsername());
            System.out.println("---------------------------------------------");
            for (Room room: rooms){
                System.out.println("Room: " + room.getName());
                System.out.println("Users online: " + room.usersOnRoom.size());
                System.out.println("---------------------------------------------");
            }
        } catch (IOException e) {
            System.err.println("Error listing rooms: " + e.getMessage());
        }
    }

    @Override
    public void showAvailableRooms() {
        List<Room> room = roomService.ListRoomsAvailable();
        for (Room r: room){
            System.out.println("Room: " + r.getName());
            System.out.println("Administrator: " + r.getAdministrator());
            System.out.println("Users online: " + r.usersOnRoom.size());
            System.out.println("---------------------------------------------");

        }

    }


    @Override
    public void deleteRoom(String roomName) throws IOException {
        Optional<Room> room = roomService.getRoomByName(roomName);
        if (room.isPresent()){
            Room existingRoom = room.get();
            if(existingRoom.getAdministrator().equals(userInSession.getUsername())){
                roomService.removeUserFromRoom(existingRoom.getName(), userInSession.getUsername());
                roomService.deleteRoom(roomName);
                System.out.println("Room Deleted successfully");
            }else {
                System.out.println("Your not the administrator of this room.");
            }
        }else {
            System.out.println("Room doesn't exist");
        }
    }

    @Override
    public void logout() {
        if (this.mqttClient.getState().isConnected()){
            this.mqttClient.toBlocking().disconnect();
            System.out.println("Disconnected from broker");
        }
    }

    @Override
    public void unsubscribeTopic(String topicName) {
        this.mqttClient.toBlocking().unsubscribeWith().topicFilter(topicName).send();
    }
}
