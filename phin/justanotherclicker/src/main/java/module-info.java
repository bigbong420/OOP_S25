module com.example.justanotherclicker {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.justanotherclicker to javafx.fxml;
    exports com.example.justanotherclicker;
}