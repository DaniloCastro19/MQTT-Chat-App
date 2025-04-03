package jala.core.domain;

import com.hivemq.client.mqtt.MqttClient;
import com.hivemq.client.mqtt.mqtt3.Mqtt3AsyncClient;
import com.hivemq.client.mqtt.mqtt3.Mqtt3Client;
import com.hivemq.client.mqtt.mqtt5.Mqtt5AsyncClient;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class ClientSubscriber {
    private static final String BROKER_HOST = "47dd8417ba1643e088d58d823cfd5261.s1.eu.hivemq.cloud";
    private static final int BROKER_PORT = 8883;


    public Mqtt5AsyncClient createClient(String clientId){
        return MqttClient.builder()
                .useMqttVersion5()
                .identifier(clientId)
                .serverHost(BROKER_HOST)
                .serverPort(BROKER_PORT)
                .sslWithDefaultConfig()
                .buildAsync();
    }

    public Mqtt5AsyncClient createServerClient(){
        return createClient("server-auth-" + UUID.randomUUID());
    }
}
