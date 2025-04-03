package jala.core.domain;

public class ChatServer {
    //TODO: Server logic handling
        private static final String BROKER_HOST = "47dd8417ba1643e088d58d823cfd5261.s1.eu.hivemq.cloud";
        private static final int BROKER_PORT = 8883;

        public void startServer() throws InterruptedException {
            System.out.println("Starting Auth Server...");

            try {
                AuthServer authServer = new AuthServer();
                Thread.currentThread().join();
            }catch (Exception e){
                System.err.println("Server Startup Failed: " + e.getMessage());
            }
        }



}
