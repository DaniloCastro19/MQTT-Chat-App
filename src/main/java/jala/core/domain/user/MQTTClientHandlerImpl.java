package jala.core.domain.user;

import com.hivemq.client.mqtt.mqtt5.Mqtt5AsyncClient;
import com.hivemq.client.mqtt.mqtt5.Mqtt5Client;
import jala.core.domain.user.model.MQTTClientHandler;

import java.util.Scanner;

public class MQTTClientHandlerImpl implements Runnable, MQTTClientHandler {
    static final String host = "47dd8417ba1643e088d58d823cfd5261.s1.eu.hivemq.cloud";
    final String username = "Dan-ADMIN";
    final String password = "Danilo123";
    static final String macAddress = "ec:bb:2a:12";

    static final Mqtt5Client mqttClient = Mqtt5Client.builder()
            .identifier("Client-" + macAddress)
            .serverHost(host)
            .automaticReconnectWithDefaultConfig()
            .serverPort(8883)
            .sslWithDefaultConfig()
            .build();



    public MQTTClientHandlerImpl() {

    }

    @Override
    public void run() {
        //TODO: Client communication with broker
        System.out.println("Handle client session");

    }


    @Override
    public void initializeClient() {

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

    //Add method to connect with broker
}

