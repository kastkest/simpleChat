module java.ru.gb.simplechat {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires org.apache.logging.log4j;


    opens ru.gb.simplechat to javafx.fxml;
    exports ru.gb.simplechat;
}