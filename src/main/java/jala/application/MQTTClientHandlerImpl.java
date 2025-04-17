package jala.application;

import com.hivemq.client.mqtt.datatypes.MqttQos;
import com.hivemq.client.mqtt.mqtt5.Mqtt5Client;
import jala.domain.MQTTClientHandler;
import jala.domain.RoomService;
import jala.domain.User;
import jala.helpers.Constants;
import jala.domain.RoomRepository;

import java.nio.charset.StandardCharsets;
import java.sql.SQLOutput;
import java.util.Objects;
import java.util.Scanner;

public class MQTTClientHandlerImpl implements MQTTClientHandler {
    private final String clientID;
    private final RoomService roomService;
    private User userInSession;

    static final String HOST = "47dd8417ba1643e088d58d823cfd5261.s1.eu.hivemq.cloud";

    final String brokerUsername = "Dan-ADMIN";
    final String brokerPassword = "Danilo123";
    private Mqtt5Client mqttClient;

    public MQTTClientHandlerImpl(User userInSession, RoomService roomService) {
        this.clientID = userInSession.getId();
        this.userInSession = userInSession;
        this.roomService = roomService;
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

        this.mqttClient.toBlocking().unsubscribeWith().topicFilter(Constants.START_SESSION_TOPIC).send();
    }

    @Override
    public void joinRoom(String roomName) {
        System.out.println("Enter room name to join: ");


    }

    @Override
    public void createRoom(String roomName) {
        this.roomService.createRoom(roomName, userInSession);
        this.mqttClient.toBlocking().publishWith()
                .topic(Constants.ROOM_CREATED)
                .payload((this.userInSession.getUsername() + " create " + roomName + "!").getBytes())
                .send();
        this.mqttClient.toBlocking().unsubscribeWith().topicFilter(Constants.ROOM_CREATED).send();

        String TOPIC_NAME = userInSession.getUsername() + "/room/" + roomName;

        this.mqttClient.toAsync().subscribeWith()
                .topicFilter(TOPIC_NAME)
                .callback(publish -> {
                   String message = new String(publish.getPayloadAsBytes(), StandardCharsets.UTF_8);
                    System.out.println(message);
                })
                .send();
        System.out.println("Chat Topic created: " + TOPIC_NAME);

        Scanner scanner = new Scanner(System.in);
        String message = "";

        while (!Objects.equals(message, "Bye")){
            System.out.println("Write your message: ");
            message = scanner.nextLine();
            chatInRoom(TOPIC_NAME, message);
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
    public void showAvailableRooms() {

    }

    @Override
    public void logout() {
        if (this.mqttClient.getState().isConnected()){
            this.mqttClient.toBlocking().disconnect();
            System.out.println("Disconnected from broker");
        }
    }
}
