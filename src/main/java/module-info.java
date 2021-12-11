module com.example.chatterboxclient {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;


    exports com.example.chatterboxclient.client;
    opens com.example.chatterboxclient.client to javafx.fxml;
    exports com.example.chatterboxclient;
    opens com.example.chatterboxclient to javafx.fxml;
}