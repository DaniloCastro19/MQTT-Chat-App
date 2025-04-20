package jala.application;

import com.hivemq.client.mqtt.datatypes.MqttQos;
import com.hivemq.client.mqtt.mqtt5.Mqtt5Client;
import jala.domain.*;
import jala.helpers.Constants;

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
    public void joinRoom() {
        System.out.println("Rooms available:");
        System.out.println("---------------------------------------------");
        showAvailableRooms();
        System.out.println("Enter room name to join: ");
        String roomToJoin = scanner.nextLine();
        Optional<Room> room = roomService.getRoomByName(roomToJoin);
        if(room.isPresent()){
            Room existingRoom = room.get();
            existingRoom.usersOnRoom.add(userInSession.getUsername());
            this.mqttClient.toBlocking().publishWith()
                    .topic(existingRoom.getTopicName())
                    .payload((this.userInSession.getUsername() + " Joined to chat!").getBytes())
                    .send();
            String message = "";
            while (!Objects.equals(message, "Bye")){
                System.out.println("Write your message: ");
                message = scanner.nextLine();
                chatInRoom(existingRoom.getTopicName(), message);
            }
            unsubscribeTopic(existingRoom.getTopicName());
        }else {
            System.out.println("Room doesn't exist.");
        }

    }

    @Override
    public void createRoom(String roomName) {
        String TOPIC_NAME = userInSession.getUsername() + "/room/" + roomName;
        //TODO: Set room topic & users online
        Room userRoom = this.roomService.createRoom(roomName, userInSession.getUsername(), TOPIC_NAME);

        if (userRoom != null){

            this.mqttClient.toBlocking().publishWith()
                    .topic(Constants.ROOM_CREATED)
                    .payload((this.userInSession.getUsername() + " create " + roomName + "!").getBytes())
                    .send();
            unsubscribeTopic(Constants.ROOM_CREATED);

            this.mqttClient.toAsync().subscribeWith()
                    .topicFilter(TOPIC_NAME)
                    .callback(publish -> {
                        String message = new String(publish.getPayloadAsBytes(), StandardCharsets.UTF_8);
                        System.out.println(message);
                    })
                    .send();

            userInSession.getUserRooms().add(userRoom);
            System.out.println("Chat Topic created: " + TOPIC_NAME);

            String message = "";

            while (!Objects.equals(message, "Bye")){
                System.out.println("Write your message: ");
                message = scanner.nextLine();
                chatInRoom(TOPIC_NAME, message);
            }
        }

    }

    @Override
    public void chatInRoom(String topicName, String message) {
        this.mqttClient.toBlocking().publishWith()
                .topic(topicName)
                .qos(MqttQos.AT_LEAST_ONCE)
                .payload(("[" + userInSession.getUsername() + "]: " + message).getBytes(StandardCharsets.UTF_8))
                .send();
    }

    @Override
    public void listUserRooms() {
        List<Room> rooms = userInSession.getUserRooms();

        System.out.println("---------------------------------------------");
        for (Room room: rooms){
            System.out.println("Room: " + room.getName());
            System.out.println("Users online: " + room.usersOnRoom.size());
            System.out.println("---------------------------------------------");
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
    public void deleteRoom(String roomName) {
        Optional<Room> room = roomService.getRoomByName(roomName);
        if (room.isPresent()){
            Room existingRoom = room.get();
            if(existingRoom.getAdministrator().equals(userInSession.getUsername())){
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
