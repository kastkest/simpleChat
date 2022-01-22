module java.ru.gb.simplechat {
    requires javafx.controls;
    requires javafx.fxml;


    opens java.ru.gb.simplechat to javafx.fxml;
    exports java.ru.gb.simplechat;
}