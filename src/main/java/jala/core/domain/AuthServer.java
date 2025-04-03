package jala.core.domain;

import com.hivemq.client.internal.mqtt.message.publish.MqttPublish;
import com.hivemq.client.mqtt.mqtt5.Mqtt5AsyncClient;
import com.hivemq.client.mqtt.mqtt5.message.publish.Mqtt5Publish;
import jala.core.utils.Constants;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class AuthServer {
    private final Mqtt5AsyncClient mqttClient;
    private final Map<String, String> userPersistence = new ConcurrentHashMap<>();


    public AuthServer(){
        this.mqttClient = new ClientSubscriber().createServerClient();

        mqttClient.subscribeWith()
                .topicFilter(Constants.REGISTER_TOPIC)
                .callback(this::handleRegistration)
                .send();

        mqttClient.subscribeWith()
                .topicFilter(Constants.LOGIN_TOPIC)
                .callback(this::handleLogin)
                .send();
    }


    private void handleRegistration(Mqtt5Publish publish){
        String [] parts = new String(publish.getPayloadAsBytes()).split(":");
        String username = parts[0];
        String hashedPassword = parts[1];

        if (userPersistence.containsKey(username)){
            sendResponse(publish, "Username already exists");
        }else {
            userPersistence.put(username,hashedPassword);
            sendResponse(publish, "SUCCESS");
        }
    }

    private void handleLogin(Mqtt5Publish pusblish){
        String[] parts = new String(pusblish.getPayloadAsBytes()).split(":");
        String username = parts[0];
        String hashedPassword = parts[1];

        if(userPersistence.containsKey(username) && userPersistence.get(username).equals(hashedPassword)){
            sendResponse(pusblish, "SUCESS" + username);
        }else {
            sendResponse(pusblish, "Invalid Credentials");
        }

    }

    private void sendResponse(Mqtt5Publish originalPublish, String response) {
        String clientId = originalPublish.getTopic().toString().split("/")[2];

        mqttClient.publishWith()
                .topic("response/" + originalPublish.getTopic().toString().split("/")[1]+ "/" + clientId)
                .payload(response.getBytes())
                .send();
    }
}
