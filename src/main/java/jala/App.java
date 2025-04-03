package jala;

import com.hivemq.client.mqtt.mqtt5.Mqtt5AsyncClient;
import jala.core.domain.ClientHandler;
import jala.core.domain.ClientSubscriber;
import jala.core.domain.UserMenuHandler;

import java.io.IOException;
import java.util.UUID;

public class App {

    public static void main(String[] args) {
        ClientSubscriber clientSubscriber = new ClientSubscriber();
        String userId = String.valueOf(UUID.randomUUID());
        Mqtt5AsyncClient client = clientSubscriber.createClient(userId);
        ClientHandler clientHandler = new ClientHandler(client, userId);
        clientHandler.run();
    }
}
