package jala.core.domain;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class UserMenuHandler {
    private BufferedReader in;
    private PrintWriter out;

    public UserMenuHandler(Socket socket) throws IOException {
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        out = new PrintWriter(socket.getOutputStream(), true);
    }

    public void showMainMenu() {
        out.println("\n Welcome to ChatApp!");
        out.println("1) Register");
        out.println("2) Login");
        out.println("3) Exit");
        out.println("Select an option (1-3): ");
    }

    // return an array with two String values: [username, password]
    public String[] registerMenu() {
        String[] userCredentials = new String[2];
        try{
            out.println("Enter a username: ");
            //TODO: unique username validation
            String username = in.readLine();
            userCredentials[0] = username;
            out.println("Enter a password: ");
            String password = in.readLine();
            userCredentials[1] = password;

            //TODO: DB data sent & pass encryption.

        }catch(IOException ioException) {
            System.out.println(ioException.getMessage());
        }
        return userCredentials;
    }

    //return true if the user credentials are valid, otherwise return false
    public boolean userLoginMenu() {
        String username = "";
        String password = "";

        try{
            out.println("Enter a username: ");
            username = in.readLine();
            out.println("Enter a password: ");
            password = in.readLine();

        }catch(IOException ioException) {
            System.out.println(ioException.getMessage());
        }
        //TODO: User credential validation
        return username.equals("Exists") && password.equals("exists");
    }


    // return the room created name
    public String createRoomMenu() {
        String roomName = "";

        try{
            out.println("Enter a name for the room: ");
            roomName = in.readLine();

        }catch(IOException ioException) {
            System.out.println(ioException.getMessage());
        }
        return roomName;
    }



    public void userLoggedMenu(String username, String IP) {
        out.println("\n--- Welcome, " + username + " [" + IP + "] ---");
        out.println("1) Join a room");
        out.println("2) Create a room");
        out.println("3) Logout");
        out.println("Enter your choice: ");
    }

}
