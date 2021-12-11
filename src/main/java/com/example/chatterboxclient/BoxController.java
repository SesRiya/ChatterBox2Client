package com.example.chatterboxclient;

import javafx.application.Application;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.ResourceBundle;

import static com.example.chatterboxclient.MainApplication.*;

public class BoxController extends Application implements Initializable {
    @FXML
    public TextField input;
    @FXML
    public TextArea result;
    @FXML
    TextField tf_receiver;

    public BoxController() {

    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        MainApplication.client.setTextArea(result);
    }


    @Override
    public void start(Stage stage) throws Exception {
        Parent parent = FXMLLoader.load(BoxController.class.getResource("box.fxml"));
        stage.setTitle("ChatterBox");
        stage.setScene(new Scene(parent, 600, 400));
        stage.show();
    }


    @FXML
    public void sendMessage(Event event) {
        String rcvName = tf_receiver.getText();
        String msg = input.getText();
        //SEND:<sender>:<receiver>:message


        String command = "SEND:" + userName + ":" + rcvName + ":" + msg;
        try {
            MainApplication.client.sendMessage(command);
        } catch (InvalidAlgorithmParameterException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        }
    }



}