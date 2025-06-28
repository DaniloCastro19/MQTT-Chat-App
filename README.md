# Chat App

An MQTT-Protocol console aplication to chat with your friends!


# Getting started

## Prerequisites
* Java (21 or higher). 
* Apache Maven (3.9.9 or higher)

## Instalation

### Clone repository
```
git clone [URL | SSH]
```

### Environment settings

Copy and paste the `application.properties.example` template on your own new `application.properties` file at **src/main/resources**.

This application is using a [HiveMQ cloud client](https://auth.hivemq.cloud/u/login/identifier?state=hKFo2SBwMHlDU0ZsN2VCWE9ubmZzQWUyajdaSUtUY0RHdVd2VqFur3VuaXZlcnNhbC1sb2dpbqN0aWTZIGtmckpBQmtHaDZ1ZVFkSnJyVmZoLV9qS2h6Q3Jsamh6o2NpZNkgSWFqbzRlMzJqeHdVczhBZEZ4Z3hRbjJWUDNZd0laVEs). Once you have your instance, you can connect the application with the broker easily. 


### Run the app
* ```mvn package```
* ```java -cp .\target\ChatApp-1.0-SNAPSHOT.jar main.java.jala.presentation.ChatApp```
