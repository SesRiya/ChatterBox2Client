module com.example.chatterboxclient {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.chatterboxclient to javafx.fxml;
    exports com.example.chatterboxclient;
}