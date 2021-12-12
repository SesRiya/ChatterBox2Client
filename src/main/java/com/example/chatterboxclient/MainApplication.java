package com.example.chatterboxclient;

import com.example.chatterboxclient.client.SocketClient;
import javafx.application.Application;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

import static com.example.chatterboxclient.cipher.CipherAES.encrypt;


public class MainApplication extends Application {
    private static final String SECRET = "Open Sesame";
    public static SocketClient client;
    public static String userName;
    private static Stage stage;
    @FXML
    private TextField tf_username;
    @FXML
    private TextField tf_password;
    @FXML
    private TextField tf_ipaddress;
    @FXML
    private TextField tf_port;

    public static void main(String[] args) {
        launch();
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("login.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 500, 400);
        this.stage = primaryStage;
        primaryStage.setTitle("Login!");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    @FXML
    public void login(Event event) throws Exception {
        String name = tf_username.getText();
        String password = tf_password.getText();
        String ipAddress = tf_ipaddress.getText();

        int portNumber = Integer.parseInt(tf_port.getText());


        client = new SocketClient(ipAddress, portNumber);
        client.start();
        //LOGIN:<username>:<password>
        //TODO common method to build protocol messages
        String login = "LOGIN:" + name + ":" + password;

//        System.out.println(String.format("Password for user [%s]: [%s]", name, encryptMessage(password)));

        client.sendMessage(login, name);

        if (client.login()) {
            System.out.println("success");
            userName = name;
            BoxController boxController = new BoxController();
            boxController.start(this.stage);
        } else {
            System.out.println("Invalid login");
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Credentials are incorrect. Please try again");
            alert.show();
        }

    }

    private String encryptMessage(String message) throws InvalidAlgorithmParameterException, NoSuchPaddingException, UnsupportedEncodingException, IllegalBlockSizeException, InvalidKeySpecException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException {
        String encryptedMessage = encrypt(message, SECRET);
        System.out.println("Encrypted Password:" + encryptedMessage);
        return encryptedMessage;
    }
}