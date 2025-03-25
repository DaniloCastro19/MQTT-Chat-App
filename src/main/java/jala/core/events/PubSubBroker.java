package jala.core.events;

import jala.core.models.Subscriber;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

public class PubSubBroker{

    private final Map<String, List<Subscriber>> roomSubscribers = new ConcurrentHashMap<>();


    public void subscribe(String room, Subscriber subscriber){
        roomSubscribers.computeIfAbsent(room, k -> new CopyOnWriteArrayList<>()).add(subscriber);
    }

    public void unsubscribe(String room, Subscriber subscriber){
        roomSubscribers.getOrDefault(room, Collections.emptyList()).remove(subscriber);
    }

    public void publish(String room, Message message){
        roomSubscribers.getOrDefault(room, Collections.emptyList())
                .forEach(sub -> sub.onMessage(message));
    }

}
