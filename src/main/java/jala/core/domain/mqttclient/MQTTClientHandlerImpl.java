package jala.core.domain.mqttclient;

import com.hivemq.client.mqtt.mqtt5.Mqtt5Client;
import jala.core.utils.Constants;

import java.nio.charset.StandardCharsets;

public class MQTTClientHandlerImpl implements MQTTClientHandler {
    static final String HOST = "47dd8417ba1643e088d58d823cfd5261.s1.eu.hivemq.cloud";

    final String brokerUsername = "Dan-ADMIN";
    final String brokerPassword = "Danilo123";
    static final String ID = "chat-app-client";

    static final Mqtt5Client mqttClient = Mqtt5Client.builder()
            .identifier(ID)
            .serverHost(HOST)
            .automaticReconnectWithDefaultConfig()
            .serverPort(Constants.BROKER_PORT)
            .sslWithDefaultConfig()
            .build();

    public MQTTClientHandlerImpl(String userInSession) {
        connectWithBroker(userInSession);

    }

    private void connectWithBroker(String userInSession) {
        System.out.println("Connecting with broker...");
        mqttClient.toBlocking().connectWith()
                .simpleAuth()
                .username(brokerUsername)
                .password(brokerPassword.getBytes(StandardCharsets.UTF_8))
                .applySimpleAuth()
                .cleanStart(false)
                .willPublish()
                .topic(Constants.START_SESSION_TOPIC)
                .payload((userInSession + " off.").getBytes())
                .applyWillPublish()
                .send();
        mqttClient.toBlocking().publishWith()
                .topic(Constants.START_SESSION_TOPIC)
                .payload((userInSession + " Connected to server!").getBytes())
                .send();
    }

    @Override
    public void joinRoom() {

    }

    @Override
    public void createRoom() {

    }

    @Override
    public void logout() {

    }
}
