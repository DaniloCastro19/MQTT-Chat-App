package jala.core.domain.mqttclient;

public interface MQTTClientHandler {
    void joinRoom();
    void createRoom();
    void logout();
}
