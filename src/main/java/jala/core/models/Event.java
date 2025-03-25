package jala.core.models;

public class Event {
    public enum Type {
        USER_LOGIN,
        PUBLIC_MESSAGE,
        CREATE_ROOM,
        JOIN_ROOM,
        PRIVATE_MESSAGE,
        USER_LOGOUT
    }
    private Type type;
    private Object payload;
}
