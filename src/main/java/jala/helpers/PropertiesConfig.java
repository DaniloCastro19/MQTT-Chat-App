package jala.helpers;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertiesConfig {
    private static Properties properties;

    static {
        properties = new Properties();
        try(InputStream input = PropertiesConfig.class.getClassLoader().getResourceAsStream("application.properties")){
            properties.load(input);
        }catch (IOException exception){
            exception.printStackTrace();
        }
    }

    public static String get(String key){
        return properties.getProperty(key);
    }
}
