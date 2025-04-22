package jala.domain;

public interface MQTTClientHandler {
    void joinRoom() throws Exception;
    void createRoom(String roomName) throws Exception;
    void listUserRooms();
    void showAvailableRooms();
    void deleteRoom(String roomName);
    void logout();
    void unsubscribeTopic(String topicName);
}
