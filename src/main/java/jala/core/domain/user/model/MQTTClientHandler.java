package jala.core.domain.user.model;

public interface MQTTClientHandler {
    void initializeClient();
    void joinRoom();
    void createRoom();
    void logout();
}
