package jala.domain;

public interface MQTTClientHandler {
    void joinRoom();
    void createRoom(String roomName);
    void chatInRoom(String topicName, String message);
    void listUserRooms();
    void showAvailableRooms();
    void deleteRoom(String roomName);
    void logout();
}
