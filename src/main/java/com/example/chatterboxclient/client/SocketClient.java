package com.example.chatterboxclient.client;

import com.example.chatterboxclient.cipher.CipherAES;
import com.example.chatterboxclient.utils.DbUtils;
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
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.UUID;

import static com.example.chatterboxclient.cipher.CipherAES.encrypt;

public class SocketClient extends Thread {
    private static final String SECRET = "Open Sesame";
    private static TextArea result;
    private static Socket socket;
    public TextField input;
    private TextArea textArea;
    private BufferedReader bufferedReader;
    private PrintWriter printWriter;

    private Connection connection;
    private String transactionId = null;

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

        if (socket.isClosed()) {
            System.out.println("Socket closed");
        }
    }

    public TextArea getTextArea() {
        return result;
    }

    public void setTextArea(TextArea textArea) {
        result = textArea;
    }

    public void sendMessage(String message, String userName) throws InvalidAlgorithmParameterException, NoSuchPaddingException, UnsupportedEncodingException, IllegalBlockSizeException, InvalidKeySpecException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException, SQLException, ClassNotFoundException {

        if (transactionId == null) {
            transactionId = UUID.randomUUID().toString();
        }

        //encrypt message
        String encryptedMessage = encryptMessage(message + ":" + transactionId);
        System.out.println("Encrypted:" + encryptedMessage);

        connection = DbUtils.connectToDb();
        String clientId = getUserClientId(userName);

        //log to History table transaction and message
        insertHistory(transactionId, clientId, encryptedMessage, "Send");

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
        } else {
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

    private void insertHistory(String guid, String clientId, String message, String action) throws SQLException {
        String timestamp = LocalDateTime.now().toString();
        DbUtils.updateQuery(connection, "INSERT INTO history (Id, ClientId, Timestamp, Message, Action, Source) VALUES ('"
                + guid + "', '"
                + clientId + "', ' "
                + timestamp + "', '"
                + message + "', '"
                + action + "', "
                + "'Client');");
    }

    private String getUserClientId(String userName) throws SQLException {
        ResultSet resultSet = DbUtils.executeQuery(connection, "SELECT * FROM CLIENTINFO WHERE USERNAME = '" + userName +  "'" );
        while(resultSet.next()){
            String clientId = resultSet.getString("ClientId");
            System.out.println("ClientId: " + clientId);
            return clientId;
        }

        return null;
    }

}
