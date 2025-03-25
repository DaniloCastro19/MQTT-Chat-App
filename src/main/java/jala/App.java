package jala;

import jala.core.domain.ChatServer;

public class App {
    public static void main(String[] args) {
        ChatServer serverSocket = new ChatServer();

        serverSocket.startServer();
    }
}
