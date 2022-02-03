module java.ru.gb.simplechat {
    requires javafx.controls;
    requires javafx.fxml;


    opens ru.gb.simplechat to javafx.fxml;
    exports ru.gb.simplechat;
}