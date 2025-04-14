package jala.application;

import com.hivemq.client.mqtt.mqtt5.Mqtt5Client;
import jala.domain.MQTTClientHandler;
import jala.helpers.Constants;
import jala.domain.RoomHandler;

import java.nio.charset.StandardCharsets;

public class MQTTClientHandlerImpl implements MQTTClientHandler {
    RoomHandler roomHandler;
    static final String HOST = "47dd8417ba1643e088d58d823cfd5261.s1.eu.hivemq.cloud";

    final String brokerUsername = "Dan-ADMIN";
    final String brokerPassword = "Danilo123";
    private String clientID;
    String userInSession;
    private Mqtt5Client mqttClient;

    public MQTTClientHandlerImpl(String clientID, String userInSession, RoomHandler roomHandler) {
        this.clientID = clientID;
        this.userInSession = userInSession;
        this.roomHandler = roomHandler;
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
                .payload((this.userInSession + " off.").getBytes())
                .applyWillPublish()
                .send();
        this.mqttClient.toBlocking().publishWith()
                .topic(Constants.START_SESSION_TOPIC)
                .payload((this.userInSession + " Connected to server!").getBytes())
                .send();

        this.mqttClient.toBlocking().unsubscribeWith().topicFilter(Constants.START_SESSION_TOPIC).send();
    }

    @Override
    public void joinRoom(String roomName) {
        System.out.println("Enter room name to join: ");


    }

    @Override
    public void createRoom(String roomName) {
        this.roomHandler.createRoom(roomName);
        this.mqttClient.toBlocking().publishWith()
                .topic(Constants.ROOM_CREATED)
                .payload((this.userInSession + " create" + roomName + "!").getBytes())
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
