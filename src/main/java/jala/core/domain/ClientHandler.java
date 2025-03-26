package jala.core.domain;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Arrays;

public class ClientHandler implements Runnable{
    private UserMenuHandler userMenuHandler;
    private final Socket clientSocket;
    private PrintWriter out;
    private BufferedReader in;
    public ClientHandler(Socket clientSocket) throws IOException {
        this.clientSocket = clientSocket;
        userMenuHandler = new UserMenuHandler(clientSocket);
    }

    @Override
    public void run() {
        try{
            System.out.println("Managing client: " + Arrays.toString(clientSocket.getInetAddress().getAddress()));
            userMenuHandler.showMainMenu();
        }catch (Exception exception){
            System.err.println("Client error: " + exception.getMessage());
        }finally {
            try{
                clientSocket.close();
            }catch (IOException exception){
                System.err.println("Socket closing error: " + exception.getMessage());
            }
        }

    }
}
