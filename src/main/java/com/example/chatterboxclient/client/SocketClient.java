package com.example.chatterboxclient.client;

import com.example.chatterboxclient.cipher.CipherAES;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.*;
import java.net.Socket;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

import static com.example.chatterboxclient.cipher.CipherAES.encrypt;

public class SocketClient extends Thread {
    private static TextArea result;
    private TextArea textArea;
    public TextField input;
    private static Socket socket;
    private BufferedReader bufferedReader;
    private PrintWriter printWriter;

    private static final String SECRET = "Open Sesame";



    public SocketClient(String serverAddress, int port) throws IOException {
        socket = new Socket(serverAddress, port);
        printWriter = new PrintWriter(socket.getOutputStream(), true);
        this.bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));

    }

    public SocketClient(TextArea textArea, TextField input) {
        this.textArea = new TextArea();
        this.input = new TextField();
    }

    @Override
    public void run() {
        messageListener();
    }

    public void messageListener() {
        //Continuously listening for the incoming messages.
        while (socket.isConnected()) {
            try {
                String message = bufferedReader.readLine();
                if (message != null) {
                    System.out.println("Server: " + message);
                    if (result != null) {
                        result.appendText(message);
                    }

                }
            } catch (IOException exception) {
                exception.printStackTrace();
            }
        }

        if(socket.isClosed()){
            System.out.println("Socket closed");
        }
    }


    public void setTextArea(TextArea textArea) {
        result = textArea;
    }

    public TextArea getTextArea() {
        return result;
    }

    public void sendMessage(String message) throws InvalidAlgorithmParameterException, NoSuchPaddingException, UnsupportedEncodingException, IllegalBlockSizeException, InvalidKeySpecException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException {

        //encrypt message
        String encryptedMessage = encryptMessage(message);;
        System.out.println("Encrypted:"+encryptedMessage);

        //This thread is used to send messages to the server
        //Thread is destroyed by the JVM when the run method completes
        new Thread(() -> printWriter.println(encryptedMessage)).start();
    }


    public boolean login() throws IOException {

        String response = bufferedReader.readLine();
        System.out.println("Response Line:" + response);

        if ("valid".equalsIgnoreCase(response)) {
            return true;

        } else if ("invalid".equalsIgnoreCase(response)) {
            return false;
        }else{
            return false;
        }

    }


    private String encryptMessage(String message) throws InvalidAlgorithmParameterException, NoSuchPaddingException, UnsupportedEncodingException, IllegalBlockSizeException, InvalidKeySpecException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException {
        String encryptedMessage = encrypt(message, SECRET);
        System.out.println("Encrypted message:" + encryptedMessage);
        return encryptedMessage;
    }

    private String decryptMessage(String message) throws InvalidAlgorithmParameterException, NoSuchPaddingException, UnsupportedEncodingException, IllegalBlockSizeException, InvalidKeySpecException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException {
        String decryptedMessage = CipherAES.decrpyt(message, SECRET);
        System.out.println("Decrypted message:" + decryptedMessage);
        return decryptedMessage;
    }

}
