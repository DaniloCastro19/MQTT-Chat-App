package jala;

import jala.core.ChatServer;
import jala.core.events.PubSubBroker;

/**
 * Hello world!
 */
public class App {
    public static void main(String[] args) {
        ChatServer serverSocket = new ChatServer();

        serverSocket.startServer();
    }
}
