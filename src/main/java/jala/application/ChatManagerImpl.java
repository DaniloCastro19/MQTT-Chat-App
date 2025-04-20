package jala.application;

import com.hivemq.client.mqtt.datatypes.MqttQos;
import com.hivemq.client.mqtt.mqtt5.Mqtt5Client;
import jala.domain.ChatManager;

import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class ChatManagerImpl implements ChatManager {
    private final Mqtt5Client mqtt5Client;
    private final Scanner scanner;
    private final String username;

    public ChatManagerImpl(Mqtt5Client mqtt5Client, Scanner scanner, String username) {
        this.mqtt5Client = mqtt5Client;
        this.scanner = scanner;
        this.username = username;
    }

    @Override
    public void startChat(String topicName) {
        mqtt5Client.toAsync().subscribeWith()
                .topicFilter(topicName)
                .callback(publish -> {
                    String message = new String(publish.getPayloadAsBytes(), StandardCharsets.UTF_8);
                    System.out.println(message);
                }).send();
        String message;
        do {
            System.out.println("Write your message (type 'Bye' to exit):");
            message = scanner.nextLine();
            if(!"Bye".equals(message)){
                mqtt5Client.toBlocking().publishWith()
                        .topic(topicName)
                        .qos(MqttQos.AT_LEAST_ONCE)
                        .payload(("[" + username + "]: " + message).getBytes(StandardCharsets.UTF_8))
                        .send();
            }
        } while (!"Bye".equals(message));

    }
}
