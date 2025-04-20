package jala.domain;

public interface MQTTClientHandler {
    void joinRoom();
    void createRoom(String roomName);
    void listUserRooms();
    void showAvailableRooms();
    void deleteRoom(String roomName);
    void logout();
    void unsubscribeTopic(String topicName);
}
