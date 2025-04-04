package jala;

import com.hivemq.client.mqtt.mqtt5.Mqtt5AsyncClient;
import jala.core.domain.user.MQTTClientHandlerImpl;
import jala.core.domain.user.UserMenuHandler;
import jala.core.domain.user.UserRepositoryImpl;
import jala.core.domain.user.UserServiceImpl;
import jala.core.domain.user.model.MQTTClientHandler;
import jala.core.domain.user.model.UserRepository;

public class App {

    public static void main(String[] args) {
        UserRepository userRepository = new UserRepositoryImpl();
        MQTTClientHandler mqttClientHandler = new MQTTClientHandlerImpl();
        UserServiceImpl userService = new UserServiceImpl(userRepository);

        UserMenuHandler menuHandler = new UserMenuHandler(userService,mqttClientHandler );

        while (true){
            menuHandler.showMainMenu();
        }
    }
}
