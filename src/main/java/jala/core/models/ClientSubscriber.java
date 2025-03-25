package jala.core.models;

import jala.core.events.Message;

import java.net.Socket;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class ClientSubscriber implements Subscriber{
    private final Socket socket;
    private final Set<String> subscriptions = ConcurrentHashMap.newKeySet();

    public ClientSubscriber(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void onMessage(Message message) {

    }

    @Override
    public String getId() {
        return "";
    }

    public void addSubscription(String room){
        subscriptions.add(room);
    }

    public void removeSubscription(String rooms){
        subscriptions.remove(rooms);
    }
}
