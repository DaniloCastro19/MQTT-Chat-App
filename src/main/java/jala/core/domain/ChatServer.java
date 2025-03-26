package jala.core.domain;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ChatServer {

    private final int PORT = 8080;
    private final int MAX_THREADS = 10;
    private volatile boolean isRunning = true;
    private final ExecutorService threadPool = Executors.newFixedThreadPool(MAX_THREADS);

    public void startServer(){
        System.out.println("Starting server on port: " + PORT);

        try(ServerSocket serverSocket = new ServerSocket(PORT)){
            Runtime.getRuntime().addShutdownHook(new Thread(this::shutdown));
            while (isRunning){
                Socket clientSocket = serverSocket.accept();
                System.out.println("New Connection: " + clientSocket.getInetAddress());

                threadPool.execute(new ClientHandler(clientSocket));
            }

        }catch (IOException ioException){
            System.err.println("Server Error: "+ ioException.getMessage());
        }finally {
            shutdown();
        }

    }

    public void shutdown(){
        isRunning=false;
        threadPool.shutdown();
        System.out.println("Server stopped");
    }


}
