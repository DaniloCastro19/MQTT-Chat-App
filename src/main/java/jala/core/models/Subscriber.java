package jala.core.models;

import jala.core.events.Message;

public interface Subscriber {
    void onMessage(Message message);
    String getId();

    default void onError(Throwable error){
        System.err.println("Subscriber " + getId() + " error "+ ": "+ error.getMessage());
    }


}
