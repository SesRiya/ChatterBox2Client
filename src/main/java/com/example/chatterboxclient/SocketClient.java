package com.example.chatterboxclient;

import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.io.*;
import java.net.Socket;

public class SocketClient extends Thread{
    private TextArea textArea;
    private String serverAddress;
    private int port;
    private String input;
    private TextField tf_username;
    private static Socket socket;
    private BufferedReader bufferedReader;
    private BufferedWriter bufferedWriter;
    private String msg;

    public SocketClient(String serverAddress, int port, TextArea textArea) throws IOException {
        System.out.println("Called");
        this.serverAddress = serverAddress;
        this.port = port;
        socket = new Socket(serverAddress, port);
        this.bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        this.bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        this.textArea = textArea;
       // messageSender();
    }

    public void messageSender() {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    while (socket.isConnected()) {
                        System.out.println(msg);
                        bufferedWriter.write(msg);
                        bufferedWriter.newLine();
                        bufferedWriter.flush();
                    }
                }catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();
    }

    public void sendMessage(String message) {
        msg = message;
    }



}
