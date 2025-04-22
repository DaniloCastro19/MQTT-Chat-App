package jala.application;

import com.hivemq.client.mqtt.datatypes.MqttQos;
import com.hivemq.client.mqtt.mqtt5.Mqtt5Client;
import jala.domain.ChatManager;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.Scanner;

public class ChatManagerImpl implements ChatManager {
    private final Mqtt5Client mqtt5Client;
    private final Scanner scanner;
    private final String username;
    private final SecretKey roomKey; //AES key for this room



    public ChatManagerImpl(Mqtt5Client mqtt5Client, Scanner scanner, String username, SecretKey roomKey) {
        this.mqtt5Client = mqtt5Client;
        this.scanner = scanner;
        this.username = username;
        this.roomKey = roomKey;
    }

    @Override
    public void startChat(String topicName) throws Exception{
        mqtt5Client.toAsync().subscribeWith()
                .topicFilter(topicName)
                .callback(publish -> {
                    try {
                        String messageDecoded = RoomSecurityManager.decrypt(new String(publish.getPayloadAsBytes(), StandardCharsets.UTF_8), roomKey);
                        System.out.println(messageDecoded);
                    } catch (Exception e) {
                        System.err.println("Decrypt error: " + e.getMessage());
                    }
                }).send();
        String message;
        System.out.println("Write your message (type 'Bye' to exit):");
        do {
            message = scanner.nextLine();
            if(!"Bye".equals(message)){
                String messageEncrypted = RoomSecurityManager.encrypt(("[" + username + "]: " + message + "\n"), roomKey);
                mqtt5Client.toBlocking().publishWith()
                        .topic(topicName)
                        .qos(MqttQos.AT_LEAST_ONCE)
                        .payload((messageEncrypted).getBytes(StandardCharsets.UTF_8))
                        .send();
            }

        } while (!"Bye".equals(message));

    }

}
