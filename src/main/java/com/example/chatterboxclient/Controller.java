package com.example.chatterboxclient;

import javafx.application.Application;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class Controller extends Application {
    @FXML
    public TextField input;
    @FXML
    public TextArea result;
    @FXML
    TextField tf_username;


    private SocketClient client;

    public void sendMessage(Event event) {
        //client = new SocketClient(result, input.getText());
        //client.start();
    }


    @Override
    public void start(Stage primaryStage) throws Exception {
        
    }
}
