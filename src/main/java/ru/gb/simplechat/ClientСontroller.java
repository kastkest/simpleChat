package ru.gb.simplechat;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import ru.gb.simplechat.client.ChatClient;
import ru.gb.simplechat.server.AuthService;
import ru.gb.simplechat.server.ClientHandler;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class ClientСontroller {

    private final ChatClient client;
    @FXML
    private TextArea conversationArea;
    @FXML
    private TextField answerField;


    public ClientСontroller() {
        client = new ChatClient(this);
    }

    public void onClickCheckButton(ActionEvent actionEvent) {
        String text = answerField.getText();
        if (text != null && !text.isEmpty()) {
            client.sendMessage(text);
            answerField.clear();
            answerField.requestFocus();
        }
    }

    public void addMessage(String message) {
        LocalTime time = LocalTime.now();
        DateTimeFormatter format = DateTimeFormatter.ofPattern("HH:mm:ss");
        String intro = format.format(time);
        conversationArea.appendText(intro + " " + message + "\n");
    }
}