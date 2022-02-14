package ru.gb.simplechat;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import ru.gb.simplechat.client.ChatClient;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class ClientСontroller {

    private final ChatClient client;
    @FXML
    private HBox loginBox;
    @FXML
    private TextField loginField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private HBox messageBox;
    @FXML
    private ListView<String> clientList;
    @FXML
    private TextArea messageArea;
    @FXML
    private TextField messageField;


    public ClientСontroller() {
        client = new ChatClient(this);
    }

    public void onClickCheckButton(ActionEvent actionEvent) {
        String message = messageField.getText();
        if (message != null && !message.isEmpty()) {
            client.sendMessage(message);
            messageField.clear();
            messageField.requestFocus();
        }
    }

    public void addMessage(String message) {
        LocalTime time = LocalTime.now();
        DateTimeFormatter format = DateTimeFormatter.ofPattern("HH:mm:ss");
        String intro = format.format(time);
        messageArea.appendText(intro + " " + message + "\n");
    }

    public void btnAuthClick(ActionEvent actionEvent) {
        client.sendMessage("/auth " + loginField.getText() + " " + passwordField.getText());
    }

    public void selectClient(MouseEvent mouseEvent) {
        if (mouseEvent.getClickCount() == 2) {
            String message = messageField.getText();
            String client = clientList.getSelectionModel().getSelectedItem();
            messageField.setText("/w " + client + " " + message);
            messageField.requestFocus();
            messageField.selectEnd();
        }
    }

    public void setAuth(boolean isAuthSuccess) {
        loginBox.setVisible(!isAuthSuccess);
        messageBox.setVisible(isAuthSuccess);
    }

    public void updateClientsList(String[] clients) {
        clientList.getItems().clear();
        clientList.getItems().addAll(clients);
    }
}