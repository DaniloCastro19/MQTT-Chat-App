package jala.core.domain;

import com.hivemq.client.mqtt.mqtt5.Mqtt5AsyncClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.UUID;

public class ClientHandler implements Runnable{
    private final UserMenuHandler userMenuHandler;
    private final Mqtt5AsyncClient mqttClient;
    private final String clientID;
    public ClientHandler(Mqtt5AsyncClient mqttClient, String clientID) {
        this.mqttClient = mqttClient;
        this.clientID = UUID.randomUUID().toString();
        userMenuHandler = new UserMenuHandler(mqttClient);
    }

    @Override
    public void run() {
        try{
            System.out.println("Managing client: " + clientID);
            userMenuHandler.showMainMenu();
        }catch (Exception exception){
            System.err.println("Client error: " + exception.getMessage());
        }

    }
}
