package jala.core.domain;

import java.io.IOException;
import java.net.Socket;
import java.util.Arrays;

public class ClientHandler implements Runnable{
    private final Socket clientSocket;

    public ClientHandler(Socket clientSocket) {
        this.clientSocket = clientSocket;
    }

    @Override
    public void run() {
        try{
            System.out.println("Managing client: " + Arrays.toString(clientSocket.getInetAddress().getAddress()));

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
