package org.gb.SimpleCloudStorage.client;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;



public class Controller implements Initializable {
    private NetworkHandler networkHandler;

    @FXML
    TextField msgField;

    @FXML
    TextArea mainArea;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        networkHandler = new NetworkHandler();
    }

    public void sendMsgAction(ActionEvent actionEvent) throws IOException {
        networkHandler.sendMessage(msgField.getText());
        msgField.clear();
        msgField.requestFocus();
    }
}
