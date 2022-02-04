package ru.gb.simplechat;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class SimpleChatСontroller {

    @FXML
    private TextArea conversationArea;
    @FXML
    private TextField answerField;


    public void onClickCheckButton(ActionEvent actionEvent) {
        String text = answerField.getText();
        DateTimeFormatter format = DateTimeFormatter.ofPattern("HH:mm:ss");
        if (text !=  null && !text.isEmpty()){
            LocalTime time = LocalTime.now();
            String intro = format.format(time);
            conversationArea.appendText( intro + text + "\n");
            answerField.clear();
        }
    }
}