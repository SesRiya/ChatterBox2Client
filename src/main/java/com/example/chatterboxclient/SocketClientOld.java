package com.example.chatterboxclient;

import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class SocketClientOld extends Thread{
    private TextArea textArea;
    private String serverAddress;
    private int port;
    private String input;
    private TextField tf_username;
    private static Socket socket;
    private String msg;

    public SocketClientOld(String serverAddress, int port, TextArea textArea) throws IOException {
        System.out.println("Called");
        this.serverAddress = serverAddress;
        this.port = port;
        if (socket == null) {
            socket = new Socket(serverAddress, port);
        }
        this.textArea = textArea;
    }

    public SocketClientOld(TextArea textArea, String input) {
        this.textArea = textArea;
        this.input = input;
        this.serverAddress = serverAddress;
        this.port = port;
    }

    public void startMessagingService(String msg) {
        this.msg = msg;
        //sendMsgService.restart();
        //sendMessageThread.start();
        Thread thread = new Thread(new MessageSender(msg));
        thread.start();
    }

    public void startListenerService() {
        //listenerService.start();
    }

    class MessageSender implements Runnable {
        String message;
        public MessageSender(String message) {
            this.message = message;
        }

        @Override
        public void run() {
            sendMessage(message);
        }
    }

//    private final Service sendMsgService = new Service() {
//        @Override
//        protected Task<Boolean> createTask() {
//            return new Task<>() {
//                @Override
//                protected Boolean call() throws Exception {
//                    sendMessage();
//                    return true;
//                }
//            };
//        }
//    };

    private void sendMessage(String msg1) {
        try {
            System.out.println("MSG: "+msg1);
            PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);
            writer.println("Hello from client");
//            writer.flush();
            //startListenerService();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private  Service listenerService = new Service() {
        @Override
        protected Task<Boolean> createTask() {
            return new Task<>() {
                @Override
                protected Boolean call() throws Exception {
                    messageListener();
                    return true;
                }
            };
        }
    };

    private void messageListener() {
        try {
            InputStreamReader inputStreamReader = new InputStreamReader(socket.getInputStream());
            BufferedReader input = new BufferedReader(inputStreamReader);
            while (socket.isConnected()) {
                String msg = input.readLine();
                if (msg != null) {
                    textArea.appendText("Server response:\n" + msg);
                    //System.out.println(msg);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



//    public void connect() throws IOException {
//        try (Socket clientSocket = new Socket(serverAddress, port)) {
//            //Send request to the server
//            PrintWriter writer = new PrintWriter(clientSocket.getOutputStream(), true);
//            writer.println(this.input);
//            //Receive the response from the server
//            InputStreamReader inputStreamReader = new InputStreamReader(clientSocket.getInputStream());
//            BufferedReader input = new BufferedReader(inputStreamReader);
//            while (clientSocket.isConnected()) {
//                String msg = input.readLine();
//                if (msg != null) {
//                    textArea.appendText("Server response:\n" + msg);
//                }
//            }
//        }
//    }
//
//    @Override
//    public void run() {
//        try {
//            connect();
//        } catch (IOException exception) {
//            exception.printStackTrace();
//        }
//    }


}
