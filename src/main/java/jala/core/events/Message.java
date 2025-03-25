package jala.core.events;

import java.io.Serializable;

public class Message implements Serializable {
    private String payload;
    private String signature;
    private boolean isAPrivateMessage;
}
