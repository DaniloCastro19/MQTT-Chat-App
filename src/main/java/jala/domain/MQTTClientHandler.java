package jala.domain;

import java.io.IOException;

public interface MQTTClientHandler {
    void joinRoom() throws Exception;
    void createRoom(String roomName) throws Exception;
    void listUserRooms();
    void showAvailableRooms();
    void deleteRoom(String roomName) throws IOException;
    void logout();
    void unsubscribeTopic(String topicName);
}
