package jala.core.domain.mqttclient;

public interface MQTTClientHandler {
    void joinRoom(String roomName);
    void createRoom(String roomName);
    void showAvailableRooms();
    void logout();
}
