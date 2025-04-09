package jala.core.domain.mqttclient;

import com.hivemq.client.mqtt.mqtt5.Mqtt5Client;
import jala.core.domain.room.RoomHandler;
import jala.core.domain.room.RoomHandlerImpl;
import jala.core.utils.Constants;

import java.nio.charset.StandardCharsets;

public class MQTTClientHandlerImpl implements MQTTClientHandler {
    RoomHandler roomHandler;
    static final String HOST = "47dd8417ba1643e088d58d823cfd5261.s1.eu.hivemq.cloud";

    final String brokerUsername = "Dan-ADMIN";
    final String brokerPassword = "Danilo123";
    static final String ID = "chat-app-client";
    String userInSession;

    static final Mqtt5Client mqttClient = Mqtt5Client.builder()
            .identifier(ID)
            .serverHost(HOST)
            .automaticReconnectWithDefaultConfig()
            .serverPort(Constants.BROKER_PORT)
            .sslWithDefaultConfig()
            .build();

    public MQTTClientHandlerImpl(String userInSession, RoomHandler roomHandler) {
        this.userInSession = userInSession;
        connectWithBroker();
        this.roomHandler = roomHandler;

    }

    private void connectWithBroker() {
        System.out.println("Connecting with broker...");
        mqttClient.toBlocking().connectWith()
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
        mqttClient.toBlocking().publishWith()
                .topic(Constants.START_SESSION_TOPIC)
                .payload((this.userInSession + " Connected to server!").getBytes())
                .send();

        mqttClient.toBlocking().unsubscribeWith().topicFilter(Constants.START_SESSION_TOPIC).send();
    }

    @Override
    public void joinRoom(String roomName) {
        System.out.println("Enter room name to join: ");


    }

    @Override
    public void createRoom(String roomName) {
        //FIX: client already subscribe issue
        this.roomHandler.createRoom(roomName);
        mqttClient.toBlocking().publishWith()
                .topic(Constants.ROOM_CREATED)
                .payload((this.userInSession + " create" + roomName + "!").getBytes())
                .send();
    }

    @Override
    public void showAvailableRooms() {

    }

    @Override
    public void logout() {

    }
}
