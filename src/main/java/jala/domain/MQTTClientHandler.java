package jala.domain;

public interface MQTTClientHandler {
    void joinRoom(String roomName);
    void createRoom(String roomName);
    void showAvailableRooms();
    void logout();
}
