package jala.domain;

public interface MQTTClientHandler {
    void joinRoom(String roomName);
    void createRoom(String roomName);
    void chatInRoom(String topicName, String message);
    void showAvailableRooms();
    void logout();
}
