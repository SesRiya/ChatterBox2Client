package com.example.chatterboxclient;

import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.io.IOException;

public class LoginController {

    @FXML
    private TextField tf_username;
    @FXML
    private TextField tf_password;
    @FXML
    private TextField tf_ipaddress;
    @FXML
    private TextField tf_port;
    @FXML
    private TextField input;
    @FXML
    private TextArea textArea;

    private SocketClient client;

    @FXML
    public void doLogin(Event event){
        String username = tf_username.getText();
        String password = tf_password.getText();
        String ipAddress = tf_ipaddress.getText();
        int portNumber = Integer.parseInt(tf_port.getText());
        try {
            client = new SocketClient(ipAddress, portNumber, textArea);
            //LOGIN:<username>:<password>
            //TODO common method to build protocol messages
            String login= "LOGIN:"+username+":"+password;
            client.sendMessage(login);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void sendMsg(Event event) {
        String msg = input.getText();
//        System.out.println(msg);
        client.sendMessage("Hello from client.....");
    }
}
